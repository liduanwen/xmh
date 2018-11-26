package com.xuyufengyy.xmh.dto.in;

import java.io.Serializable;

public class PageIn implements Serializable {

    /**
     * 页码
     */
    private Integer pageNumber;
    /**
     * 每页条数
     */
    private Integer pageSize;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
