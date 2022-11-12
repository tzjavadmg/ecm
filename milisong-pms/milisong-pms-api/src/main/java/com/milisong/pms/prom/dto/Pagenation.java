package com.milisong.pms.prom.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pagenation<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    // 传参或指定
    private int pageNum; // 当前页号, 采用自然数计数 1,2,3,...
    private int pageSize; // 页面大小:一个页面显示多少个数据

    // 需要从数据库中查找出
    private long rowCount;// 数据总数：一共有多少个数据
    private List<T> list;

    // 可以根据上面属性：num,size,rowCount计算出
    private int pageCount; // 页面总数
    private int startRow;// 当前页面开始行, 第一行是0行
    private int first = 1;// 第一页 页号
    private int last;// 最后页 页号
    private int next;// 下一页 页号
    private int prev;// 前页 页号
    private int start;// 页号式导航, 起始页号
    private int end;// 页号式导航, 结束页号
    private int numCount = 10;// 页号式导航, 最多显示页号数量为numCount+1;这里显示11页。
    public List<String> showPages = new ArrayList<String>(); //要显示的页号
    private Object queryClass;//保存查询条件
    private String queryUrl;

    public Object getQueryClass() {
        return queryClass;
    }

    public void setQueryClass(Object queryClass) {
        this.queryClass = queryClass;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        if (pageNum <= 0) {
            this.pageNum = 1;
        } else {
            this.pageNum = pageNum;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getRowCount() {
        return rowCount;
    }

    public void setRowCount(long rowCount) {
        this.rowCount = rowCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getPrev() {
        return prev;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getNumCount() {
        return numCount;
    }

    public void setNumCount(int numCount) {
        this.numCount = numCount;
    }


    public List<String> getShowPages() {
        return showPages;
    }

    public void setShowPages(List<String> showPages) {
        this.showPages = showPages;
    }

    public String getQueryUrl() {
        return queryUrl;
    }

    public void setQueryUrl(String queryUrl) {
        this.queryUrl = queryUrl;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Pagenation(int pageSize, String str_num, long rowCount) {
        int pageNum = 1;
        if (str_num != null) {
            pageNum = Integer.parseInt(str_num);
        }
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.rowCount = rowCount;

        this.pageCount = (int) Math.ceil((double) rowCount / pageSize);
        this.pageNum = Math.min(this.pageNum, pageCount);
        this.pageNum = Math.max(1, this.pageNum);

        this.startRow = (this.pageNum - 1) * pageSize;
        this.last = this.pageCount;
        this.next = Math.min(this.pageCount, this.pageNum + 1);
        this.prev = Math.max(1, this.pageNum - 1);

        // 计算page 控制
        start = Math.max(this.pageNum - numCount / 2, first);
        end = Math.min(start + numCount, last);
        if (end - start < numCount) {
            start = Math.max(end - numCount, 1);
        }
        for(int i = start; i <= end; i++){
            showPages.add(String.valueOf(i));
        }


    }

    public Pagenation(int pageSize, int pageNum, long rowCount) {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.rowCount = rowCount;

        this.pageCount = (int) Math.ceil((double) rowCount / pageSize);
        this.pageNum = Math.min(this.pageNum, pageCount);
        this.pageNum = Math.max(1, this.pageNum);

        this.startRow = (this.pageNum - 1) * pageSize;
        this.last = this.pageCount;
        this.next = Math.min(this.pageCount, this.pageNum + 1);
        this.prev = Math.max(1, this.pageNum - 1);

        // 计算page 控制
        start = Math.max(this.pageNum - numCount / 2, first);
        end = Math.min(start + numCount, last);
        if (end - start < numCount) {
            start = Math.max(end - numCount, 1);
        }
        for(int i = start; i <= end; i++){
            showPages.add(String.valueOf(i));
        }

    }

    public Pagenation() {
    }
//    public static void main(String[] args) {
//        Pagenation pagenation = new Pagenation(10, 1, 102);
//        System.out.println(111);
//    }

}