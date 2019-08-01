package ltd.beihu.core.web.boot.response.page;

import ltd.beihu.core.tools.utils.CollectionUtils;

import java.util.List;

/**
 * @Project: [ops]
 * @Package: [com.pengshu.crawler.utils]
 * @Description: [翻页结果]
 * @Author: [toming]
 * @CreateDate: [10/20/16 1:52 PM]
 * @Version: [v1.0]
 */
public class PageResult<T> {
    private List<T> data;//核心数据
    private Integer index;//当前页
    private Integer size;//页大小
    private Long count;//总记录数
    private Long pageCount;//总页数

    public PageResult() {
    }

    public PageResult(List<T> data) {
        boolean empty = CollectionUtils.isEmpty(data);
        this.data = data;
        this.index = 1;
        this.size = 10;
        this.count = empty ? 0 : (long) data.size();
        pageCount = empty ? 0 : (this.count - 1) / size + 1;
    }

    public PageResult(List<T> data, Number count) {

        if (count.intValue() < 0) {
            throw new IllegalArgumentException("count should be positive!");
        }
        this.data = data;
        this.index = 1;
        this.size = 10;
        this.count = count.longValue();
        pageCount = (this.count - 1) / size + 1;
    }

    public PageResult(List<T> data, Pageable pageable, Long count) {
        this(data, pageable.getIndex(), pageable.getSize(), count);
    }

    public PageResult(List<T> data, Integer index, Integer size, Long count) {

        if (index < 1) {
            throw new IllegalArgumentException("Page index should be positive!");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Page size should be positive!");
        }
        if (count < 0) {
            throw new IllegalArgumentException("count shouldn't be negative!");
        }
        this.data = data;
        this.index = index;
        this.size = size;
        this.count = count;
        pageCount = (count - 1) / size + 1;
    }

    public List<T> getData() {
        return data;
    }

    public Integer getIndex() {
        return index;
    }

    public Integer getSize() {
        return size;
    }

    public Long getCount() {
        return count;
    }

    public Long getPageCount() {
        return pageCount;
    }
}
