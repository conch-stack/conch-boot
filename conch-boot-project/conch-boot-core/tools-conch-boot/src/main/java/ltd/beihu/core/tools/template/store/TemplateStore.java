package ltd.beihu.core.tools.template.store;

/**
 * @Project: [ops]
 * @Description: [description]
 * @Author: [toming]
 * @CreateDate: [9/20/16 7:04 PM]
 * @Version: [v1.0]
 */
public interface TemplateStore {
    String get(String name);

    boolean contain(String name);
}
