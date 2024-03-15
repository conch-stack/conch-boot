package com.nabob.conch.web.response.page;

import com.nabob.conch.tools.code.BasicServiceCode;
import com.nabob.conch.web.response.AbstractJsonResponse;
import com.nabob.conch.web.response.JsonResponse;

import java.util.List;

public class PageResponse<T> extends AbstractJsonResponse {
    protected PageResponse() {
    }

    private long index;
    private long size;
    private long count;//总记录数
    private long pageCount;//总页数
    private List<T> data;

    public static <T> PageResponse<T> create(PageResult<T> pageResult) {
        PageResponse<T> response = new PageResponse<>();
        response.data = pageResult.getData();
        response.index = pageResult.getIndex();
        response.size = pageResult.getSize();
        response.count = pageResult.getCount();
        response.pageCount = pageResult.getPageCount();

        response.message = BasicServiceCode.SUCCESS.getMesg();
        response.codeDescription = BasicServiceCode.SUCCESS.getDesc();
        response.code = BasicServiceCode.SUCCESS.getCode();

        return response;
    }

    public static <T> PageResponse<T> createEnhance(com.mybatisflex.core.paginate.Page<T> page) {
        PageResponse<T> response = new PageResponse<>();
        response.data = page.getRecords();
        response.index = page.getPageNumber();
        response.size = page.getPageSize();
        response.count = page.getTotalRow();
        response.pageCount = page.getTotalPage();

        response.message = BasicServiceCode.SUCCESS.getMesg();
        response.codeDescription = BasicServiceCode.SUCCESS.getDesc();
        response.code = BasicServiceCode.SUCCESS.getCode();

        return response;
    }

    public JsonResponse error() {
        this.message = BasicServiceCode.FAILED.getMesg();
        this.codeDescription = BasicServiceCode.FAILED.getDesc();
        this.code = BasicServiceCode.FAILED.getCode();
        return this;
    }

    public JsonResponse error(String msg) {
        this.message = msg;
        this.codeDescription = BasicServiceCode.FAILED.getDesc();
        this.code = BasicServiceCode.FAILED.getCode();
        return this;
    }

    public JsonResponse success(String msg) {
        this.message = msg;
        return this;
    }

    public long getIndex() {
        return index;
    }

    public PageResponse setIndex(long index) {
        this.index = index;
        return this;
    }

    public long getSize() {
        return size;
    }

    public PageResponse setSize(long size) {
        this.size = size;
        return this;
    }

    public List<T> getData() {
        return data;
    }

    public PageResponse setData(List<T> data) {
        this.data = data;
        return this;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }
}
