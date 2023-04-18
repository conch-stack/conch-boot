package ltd.beihu.core.tools.template.store;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project: [ops]
 * @Description: [最简单的使用使用map存储，非线程安全]
 * @Author: [toming]
 * @CreateDate: [9/20/16 7:06 PM]
 * @Version: [v1.0]
 */
public class SimpleTemplateStore implements TemplateStore {
    private Map<String, String> store = new HashMap<>();

    @Override
    public String get(String name) {
        return store.get(name);
    }

    @Override
    public boolean contain(String name) {
        return store.containsKey(name);
    }

    /**
     * 注意，这个应该在初始化的时候调用，否则的话，在外围需要保证线程是安全的
     */
    public void add(String name, String content) {
        this.store.put(name, content);
    }
}
