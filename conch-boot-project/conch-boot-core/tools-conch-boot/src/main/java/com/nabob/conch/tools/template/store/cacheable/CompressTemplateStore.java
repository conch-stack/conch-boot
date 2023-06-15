package com.nabob.conch.tools.template.store.cacheable;

/**
 *  [可压缩模板库]
 */
public abstract class CompressTemplateStore extends AbstractCacheTemplateStore {

    protected boolean compress = false;

    public void setCompress(boolean compress) {
        this.compress = compress;
    }
}
