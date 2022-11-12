package com.milisong.breakfast.scm.common.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * @author benny
 *
 */
@Component
@ConfigurationProperties(
        prefix = "bfscm.sys"
)


@Getter
@Setter
@RefreshScope
public class SystemProperties {

	private FileOss fileOss = new FileOss();
	
	private EcmUrl ecmUrl = new EcmUrl();
	
	private LbsUrl lbsUrl = new LbsUrl();
	
	private OmsOrder omsOrder = new OmsOrder();
	
	@Getter
    @Setter
	public static class FileOss{
		private String viewUrl;
		private String exportPath;
	}
	
	@Getter
    @Setter
	public static class EcmUrl{
		private Integer goodsDefaultCount;
		private String goodsStockUrl;
		private String goodsStockListUrl;
		private String orderListUrl;
		private String buildingListUrl;
		private List<String> specialGoods;
		private Integer schedulerPushMiute;
	}
	
	@Getter
    @Setter
	public static class LbsUrl{
		private String pointServiceUrl;
		private String convertServiceUrl;
	}
	
	@Getter
	@Setter
	public static class OmsOrder{
		private String checkUrl;
		private String notifyUrl;
	}
}
