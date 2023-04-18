package com.nabob.conch.web.response.page;

import com.nabob.conch.tools.code.BasicServiceCode;
import com.nabob.conch.web.response.AbstractJsonResponse;
import com.nabob.conch.web.response.JsonResponse;

import java.util.List;


public class PageResponse<T> extends AbstractJsonResponse {
    protected PageResponse() {
    }

    private Integer index;
    private Integer size;
    private Long count;//总记录数
    private Long pageCount;//总页数
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

    public static <T> PageResponse<T> createEnhance(com.gitee.hengboy.mybatis.pageable.Page<T> page) {
        PageResponse<T> response = new PageResponse<>();
        response.data = page.getData();
        response.index = page.getPageIndex();
        response.size = page.getPageSize();
        response.count = page.getTotalElements();
        response.pageCount = page.getTotalPages();

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

    public Integer getIndex() {
        return index;
    }

    public PageResponse setIndex(Integer index) {
        this.index = index;
        return this;
    }

    public Integer getSize() {
        return size;
    }

    public PageResponse setSize(Integer size) {
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getPageCount() {
        return pageCount;
    }

    public void setPageCount(Long pageCount) {
        this.pageCount = pageCount;
    }
}
