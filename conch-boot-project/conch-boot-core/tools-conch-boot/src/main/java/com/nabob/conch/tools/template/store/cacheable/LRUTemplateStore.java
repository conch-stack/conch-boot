package com.nabob.conch.tools.template.store.cacheable;

import com.nabob.conch.tools.mapext.LRULinkedHashMap;
import com.nabob.conch.tools.utils.ZipUtils;

import java.util.Map;

/**
 * @Project: [ops]
 * @Description: [本地LRU模板库]
 * @Author: [toming]
 * @CreateDate: [9/20/16 7:14 PM]
 * @Version: [v1.0]
 */
public class LRUTemplateStore extends CompressTemplateStore {

    public LRUTemplateStore() {
        cacheStore = new LRULinkedHashMap<>(10);
    }

    public LRUTemplateStore(int cacheCapacity) {
        cacheStore = new LRULinkedHashMap<>(cacheCapacity);
    }

    /**
     * 核心采用一个LRULinkedHashMap存储
     */
    private Map<String, String> cacheStore;

    @Override
    protected String getInSelf(String name) {
        return compress ? ZipUtils.gunzip(cacheStore.get(name)) : cacheStore.get(name);
    }

    @Override
    protected boolean containInSelf(String name) {
        return cacheStore.containsKey(name);
    }

    @Override
    public void cache(String name, String content) {
        cacheStore.put(name, compress ? ZipUtils.gunzip(content) : content);
    }
}
