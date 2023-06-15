package com.nabob.conch.tools.template.format;

import java.util.Map;

/**
 * @Project: [ops]
 * @Description: [模板格式化工具]
 * @Author: [toming]
 * @CreateDate: [9/22/16 4:11 PM]
 * @Version: [v1.0]
 */
public interface TemplateFormat {

    String replace(String content, Map<String, Object> params);
}
