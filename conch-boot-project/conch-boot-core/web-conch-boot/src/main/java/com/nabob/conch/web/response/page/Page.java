package com.nabob.conch.web.response.page;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;

public abstract class Page implements Pageable {

    @Min(value = 1, message = "index should be positive!")
    @JsonProperty(value = "pageIndex")
    private int index = 1;

    @Min(value = 1, message = "size should be positive!")
    @JsonProperty(value = "pageSize")
    private int size = 10;

    @Override
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
