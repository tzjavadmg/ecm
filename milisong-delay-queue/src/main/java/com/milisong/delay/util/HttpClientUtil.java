package com.milisong.delay.util;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * HttpClient工具类
 */
@Slf4j
public class HttpClientUtil {

	// 连接池超时时间
	static final int CONNECT_TIMEOUT = 3 * 1000;

	// 请求超时时间
	static final int REQUEST_TIMEOUT = 5 * 1000;

	// 响应超时时间
	static final int SOCKET_TIMEOUT = 25 * 1000;

	private static CloseableHttpClient httpClient = null;

	private final static Object syncLock = new Object();

	private static void config(HttpRequestBase httpRequestBase) {
		// 配置请求的超时设置
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(REQUEST_TIMEOUT)
				.setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
		httpRequestBase.setConfig(requestConfig);
	}

	/**
	 * 获取HttpClient对象
	 */
	public static CloseableHttpClient getHttpClient(String url) {
		String hostname = url.split("/")[2];
		int port = 80;
		if (hostname.contains(":")) {
			String[] arr = hostname.split(":");
			hostname = arr[0];
			port = Integer.parseInt(arr[1]);
		}
		if (httpClient == null) {
			synchronized (syncLock) {
				if (httpClient == null) {
					httpClient = createHttpClient(200, 40, 100, hostname, port);
				}
			}
		}
		return httpClient;
	}

	/**
	 * 创建HttpClient对象
	 */
	public static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute, int maxRoute, String hostname,
			int port) {
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", plainsf).register("https", sslsf).build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		// 将最大连接数增加
		cm.setMaxTotal(maxTotal);
		// 将每个路由基础的连接增加
		cm.setDefaultMaxPerRoute(maxPerRoute);
		HttpHost httpHost = new HttpHost(hostname, port);
		// 将目标主机的最大连接数增加
		cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

		// 请求重试处理
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				// 如果已经重试了5次，就放弃
				if (executionCount >= 5) {
					return false;
				}
				// 如果服务器丢掉了连接，那么就重试
				if (exception instanceof NoHttpResponseException) {
					return true;
				}
				// 不要重试SSL握手异常
				if (exception instanceof SSLHandshakeException) {
					return false;
				}
				// 超时
				if (exception instanceof InterruptedIOException) {
					return false;
				}
				// 目标服务器不可达
				if (exception instanceof UnknownHostException) {
					return false;
				}
				// 连接被拒绝
				if (exception instanceof ConnectTimeoutException) {
					return false;
				}
				// SSL握手异常
				if (exception instanceof SSLException) {
					return false;
				}

				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				// 如果请求是幂等的，就再次尝试
				if (!(request instanceof HttpEntityEnclosingRequest)) {
					return true;
				}
				return false;
			}
		};

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
				.setRetryHandler(httpRequestRetryHandler).build();

		return httpClient;
	}

	private static void setPostParams(HttpPost httpost, Map<String, Object> params) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key).toString()));
		}
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private static void setPostParams(HttpPost httpost, String params) {
		try {
			StringEntity entity = new StringEntity(params, "utf-8");
			// entity.setContentType("application/json");
			httpost.setEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * post请求URL获取内容
	 */
	public static String post(String url, Map<String, Object> params) throws IOException {
		HttpPost httppost = new HttpPost(url);
		// httppost.setHeader(arg0, arg1);
		httppost.setHeader("Content-Type", "application/json");
		config(httppost);
		setPostParams(httppost, params);
		CloseableHttpResponse response = null;
		try {
			response = getHttpClient(url).execute(httppost, HttpClientContext.create());
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			return result;
		} catch (Exception e) {
			// e.printStackTrace();
			throw e;
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * GET请求URL获取内容
	 */
	public static String post(String url, String params) throws IOException {
		HttpPost httppost = new HttpPost(url);
		// httppost.setHeader(arg0, arg1);
		httppost.setHeader("Content-Type", "application/json");
		config(httppost);
		setPostParams(httppost, params);
		CloseableHttpResponse response = null;
		try {
			response = getHttpClient(url).execute(httppost, HttpClientContext.create());
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("====================HttpClientUtil post请求失败：====================");
			log.error(e.getMessage());
			throw e;
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * GET请求URL获取内容
	 */
	public static String get(String url) throws IOException {
		HttpGet httpget = new HttpGet(url);
		config(httpget);
		CloseableHttpResponse response = null;
		try {
			response = getHttpClient(url).execute(httpget, HttpClientContext.create());
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			log.error("====================HttpClientUtil get请求失败：====================");
			log.error(e.getMessage());
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String postForOss(String url, Map<String, Object> map, String encoding) throws IOException {
		String result = "";
		// 创建post方式请求对象
		HttpPost httpPost = new HttpPost(url);
		config(httpPost);

		// 装填参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (map != null) {
			for (Entry<String, Object> entry : map.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
		}
		CloseableHttpResponse response = null;
		try {
			// 设置参数到请求对象中
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));

			// 设置header信息
			// 指定报文头【Content-type】、【User-Agent】
			httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
			httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

			// 执行请求操作，并拿到结果（同步阻塞）
			response = getHttpClient(url).execute(httpPost);
			// 获取结果实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// 按指定编码转换结果实体为String类型
				result = EntityUtils.toString(entity, encoding);
			}
			EntityUtils.consume(entity);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("====================HttpClientUtil oss请求失败：====================");
			log.error(e.getMessage());
			throw e;
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public static void main(String[] args) {
		// URL列表数组
		String[] urisToGet = { "http://blog.csdn.net/catoop/article/details/38849497",
				"http://blog.csdn.net/catoop/article/details/38849497",
				"http://blog.csdn.net/catoop/article/details/38849497",
				"http://blog.csdn.net/catoop/article/details/38849497",

				"http://blog.csdn.net/catoop/article/details/38849497",
				"http://blog.csdn.net/catoop/article/details/38849497",
				"http://blog.csdn.net/catoop/article/details/38849497",
				"http://blog.csdn.net/catoop/article/details/38849497" };

		long start = System.currentTimeMillis();
		try {
			int pagecount = urisToGet.length;
			ExecutorService executors = Executors.newFixedThreadPool(pagecount);
			CountDownLatch countDownLatch = new CountDownLatch(pagecount);
			for (int i = 0; i < pagecount; i++) {
				HttpGet httpget = new HttpGet(urisToGet[i]);
				config(httpget);
				// 启动线程抓取
				executors.execute(new GetRunnable(urisToGet[i], countDownLatch));
			}
			countDownLatch.await();
			executors.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println(
					"线程" + Thread.currentThread().getName() + "," + System.currentTimeMillis() + ", 所有线程已完成，开始进入下一步！");
		}

		long end = System.currentTimeMillis();
		System.out.println("consume -> " + (end - start));
	}

	static class GetRunnable implements Runnable {
		private CountDownLatch countDownLatch;
		private String url;

		public GetRunnable(String url, CountDownLatch countDownLatch) {
			this.url = url;
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run() {
			try {
				System.out.println(HttpClientUtil.get(url));
			} catch (Exception e) {
			} finally {
				countDownLatch.countDown();
			}
		}
	}
}
