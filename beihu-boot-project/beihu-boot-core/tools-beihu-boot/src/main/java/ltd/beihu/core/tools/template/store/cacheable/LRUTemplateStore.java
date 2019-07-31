package ltd.beihu.core.tools.template.store.cacheable;

import ltd.beihu.core.tools.mapext.LRULinkedHashMap;
import ltd.beihu.core.tools.utils.ZipUtils;

import java.util.Map;

/**
 * @Project: [ops]
 * @Package: [com.pengshu.common.template.store]
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
