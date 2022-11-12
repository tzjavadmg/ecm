package com.milisong.scm.shop.dto.config;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/7/5   14:16
 *    desc    : 基础dto，用于传递分页属性
 *    version : v1.0
 * </pre>
 */

public class BaseParam {

    private int pageNo;
    private int pageSize;

    private int startRow;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartRow() {
        if(pageNo <=0){
            pageNo = 1;
        }

        if(pageSize <=0){
            pageSize = 10;
        }
        startRow = (pageNo -1)*pageSize;
        return startRow;
    }

}
