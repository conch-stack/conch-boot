package ltd.beihu.core.tools.template;

import ltd.beihu.core.tools.template.format.SimpleTemplateFormat;
import ltd.beihu.core.tools.template.format.TemplateFormat;
import ltd.beihu.core.tools.template.store.TemplateStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project: [ops]
 * @Package: [com.pengshu.common.template]
 * @Description: [模板工厂类]
 * @Author: [toming]
 * @CreateDate: [9/19/16 9:56 AM]
 * @Version: [v1.0]
 */
public class TemplateFactory {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private TemplateStore store;//模板库

    private TemplateFormat templateFormat = new SimpleTemplateFormat();//模板格式化工具

    public void setTemplateFormat(TemplateFormat templateFormat) {
        this.templateFormat = templateFormat;
    }

    public void setStore(TemplateStore store) {
        this.store = store;
    }

//    /**
//     * just be called in init
//     *
//     * @param name    -name of template
//     * @param content -content of template
//     * @return -current factory
//     */
//    public TemplateFactory add(String name, String content) {
//        this.store.put(name, content);
//        return this;
//    }

    public TemplateBuilder builder(String name) {
        String tlContent = store.get(name);
        if (tlContent == null) {
            throw new IllegalArgumentException("There's none template named [" + name + "] in this TemplateFactory");
        }
        return new TemplateBuilder(tlContent);
    }

    public TemplateBuilder builderFromContent(String content) {
        return new TemplateBuilder(content);
    }

    public class TemplateBuilder {
        String content;
        private Map<String, Object> params = new HashMap<>();

        private TemplateBuilder(String content) {
            if (content == null) {
                throw new IllegalArgumentException("content of TemplateBuilder couldn't be null");
            }
            this.content = content;
        }

        public TemplateBuilder setParam(String name, Object value) {
            params.put(name, value);
            return this;
        }

        public TemplateBuilder setParam(String name, String value, Object... params) {
            this.params.put(name, String.format(value, params));
            return this;
        }

        @Override
        public String toString() {
            try {
                return templateFormat.replace(content, params);
            } catch (Exception e) {
                return "try format tl failed";
            }
        }
    }

}
