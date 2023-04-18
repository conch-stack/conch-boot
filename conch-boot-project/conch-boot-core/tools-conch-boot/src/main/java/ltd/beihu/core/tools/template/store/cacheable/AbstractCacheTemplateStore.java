package ltd.beihu.core.tools.template.store.cacheable;

import ltd.beihu.core.tools.template.store.TemplateStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * [缓存型模板库]
 */
public abstract class AbstractCacheTemplateStore implements TemplateStore {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private TemplateStore inferior;//下级模板库

    public void setInferior(TemplateStore inferior) {
        this.inferior = inferior;
    }

    /**
     * 获取模板
     *
     * @param name -模板名称
     * @return -模板内容
     */
    @Override
    public String get(String name) {
        String content = null;
        try {
            content = getInSelf(name);
        } catch (Exception e) {
            logger.error("{} may be broken", getClass().getSimpleName());
        }
        if (content == null) {
            content = tryGetFromInferior(name);
        }
        return content;
    }

    @Override
    public boolean contain(String name) {
        try {
            if (containInSelf(name))
                return true;
        } catch (Exception e) {
            logger.error("{} may be broken", getClass().getSimpleName());
        }
        return tryGetFromInferior(name) != null;
    }

    /**
     * 从当前模板库中获取模板
     *
     * @param name -模板名称
     * @return -模板内容
     */
    protected abstract String getInSelf(String name);

    /**
     * 判断当前模板库中是否含有该模板
     *
     * @param name -模板名称
     */
    protected abstract boolean containInSelf(String name);

    /**
     * 缓存模板
     *
     * @param name
     * @param content
     */
    protected abstract void cache(String name, String content);

    /**
     * 尝试从下级缓存获取模板
     */
    private String tryGetFromInferior(String name) {
        if (inferior == null || name == null)
            return null;
        String newTl = inferior.get(name);
        if (newTl != null) {
            //todo:这里只是简单的处理一下～要是真的有需要再改，理论上来说这里的同步块不会对性能产生多大影响
            synchronized (this) {
                if (!containInSelf(name)) {
                    cache(name, newTl);
                }
            }
        }
        return newTl;
    }

}
