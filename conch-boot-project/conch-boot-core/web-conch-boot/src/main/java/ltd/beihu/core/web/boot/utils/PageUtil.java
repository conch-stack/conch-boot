package ltd.beihu.core.web.boot.utils;

import ltd.beihu.core.tools.code.BasicServiceCode;
import ltd.beihu.core.web.boot.exception.ServiceException;
import ltd.beihu.core.web.boot.response.page.PageResult;
import org.apache.commons.collections.ListUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by layker on 2018-12-10
 */
public class PageUtil {
    /**
     * 生成 pageRequest
     *
     * @param index
     * @param size
     * @return
     */
    public static PageRequest of(int index, int size) {
        if (index < 1) {
            throw new ServiceException(BasicServiceCode.PAGE_INDEX_ERROR);
        }
        return PageRequest.of(index - 1, size);
    }

    public static PageRequest of(int limit) {
        if (limit < 1) {
            throw new ServiceException(BasicServiceCode.PAGE_INDEX_ERROR);
        }
        return PageRequest.of(0, limit);
    }

    public static PageRequest of(int index, int size, Sort sort) {
        if (index < 1) {
            throw new ServiceException(BasicServiceCode.PAGE_INDEX_ERROR);
        }
        return PageRequest.of(index - 1, size, sort);
    }


    public static PageRequest of(ltd.beihu.core.web.boot.response.page.Page page) {
        return PageRequest.of(page.getIndex() - 1, page.getSize());
    }


    public static PageRequest of(ltd.beihu.core.web.boot.response.page.Page  page, Sort sort) {
        return PageRequest.of(page.getIndex() - 1, page.getSize(), sort);
    }

    /**
     * 构建分页返回结果
     *
     * @param data
     * @param page
     * @param <T>
     * @return
     */
    public static <T> PageResult<T> pageResult(List<T> data, Page page) {
        return new PageResult<>(data, page.getNumber() + 1, page.getSize(), page.getTotalElements());
    }

    /**
     * @param page
     * @param index
     * @param size
     * @param <T>
     * @return
     */
    public static <T> PageResult<T> pageResult(Page<T> page, int index, int size) {
        return new PageResult<>(page.getContent(), index, size, page.getTotalElements());
    }

    /**
     * @param data
     * @param index
     * @param size
     * @param count
     * @return
     */
    public static <T> PageResult<T> pageResult(List<T> data, int index, int size, long count) {
        return new PageResult<>(data, index, size, count);
    }

    public static <T> PageResult empty(Pageable pageable) {
        return new PageResult<T>(ListUtils.EMPTY_LIST, pageable.getPageNumber() + 1, pageable.getPageSize(), 0L);
    }

    public static <T> PageResult empty(int index, int size) {
        return new PageResult<T>(ListUtils.EMPTY_LIST, index, size, 0L);
    }


    /**
     * 分页返回
     *
     * @param page
     * @param pageable
     * @param <T>
     * @return
     */
    public static <T> PageResult<T> pageResult(Page<T> page, Pageable pageable) {
        return new PageResult<T>(page.getContent(), pageable.getPageNumber() + 1, pageable.getPageSize(), page.getTotalElements());
    }

    /**
     * 分页返回
     *
     * @param page
     * @param <T>
     * @return
     */
    public static <T> PageResult<T> pageResult(Page<T> page) {
        return new PageResult<T>(page.getContent(), page.getNumber() + 1, page.getSize(), page.getTotalElements());
    }
}
