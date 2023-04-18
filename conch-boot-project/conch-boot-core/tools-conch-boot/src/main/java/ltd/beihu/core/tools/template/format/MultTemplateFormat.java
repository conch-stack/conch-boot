package ltd.beihu.core.tools.template.format;

import ltd.beihu.core.tools.json.BeanExtendUtils;
import ltd.beihu.core.tools.json.GsonUtils;
import ltd.beihu.core.tools.utils.CollectionUtils;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project: [ops]
 * @Description: [模板格式化的一个实现]
 * @Author: [toming]
 * @CreateDate: [9/22/16 4:46 PM]
 * @Version: [v1.0]
 */
public class MultTemplateFormat extends SimpleTemplateFormat {
    public MultTemplateFormat() {
    }

    public MultTemplateFormat(String prefix) {
        super(prefix);
    }

    public MultTemplateFormat(String prefix, String suffix) {
        super(prefix, suffix);
    }

    /**
     * 做了点简单的解析～
     */
    @Override
    protected Object getFromParam(Map<String, Object> params, String key) {

        //双引号中间不转义,直接当做字符串输出
        if (key.startsWith("\"") && key.endsWith("\"")) {
            return key.substring(1, key.length() - 1);
        }
        //去括号
        if (key.startsWith("(") && key.endsWith(")")) {
            key = key.substring(1, key.length() - 2);
        }
        Object value = params.get(key);
        if (value != null) {
            return value;
        }

        //解析'|',语义为或
        if (key.contains("|")) {
            String[] keyArr = key.split("\\|");
            for (String k : keyArr) {
                value = getFromParam(params, k);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }

        //解析'.',语义为取子项
        if (key.contains(".")) {
            try {
                String first = key.substring(0, key.indexOf("."));
                Map<String, Object> target = getSubFromMap(params, first);
                if (target == null)
                    return null;
                String rest = key.substring(key.indexOf(".") + 1);
                return getFromParam(target, rest);
            } catch (Exception e) {
                return null;
            }
        }


        //解析'[]',语义为取子项或数组取指定项
        if (key.endsWith("]")) {
            if (key.contains("[")) {
                String fKey = key.substring(0, key.indexOf("[") - 1);
                value = params.get(fKey);
                if (value == null) {
                    return null;
                }
                String kIk = key.substring(key.indexOf("[") + 1, key.indexOf("]") - 1);
                try {
                    Integer index = Integer.parseInt(kIk);
                    return getElementAt(value, index);
                } catch (NumberFormatException ne) {
                    Map<String, Object> target = getSubFromMap(params, fKey);
                    return target == null ? null : target.get(kIk);
                }
            }
        }
        return null;
    }

    private Object getElementAt(Object o, int index) {
        if (o instanceof List) {
            return GsonUtils.toJson(((List) o).get(index));
        }
        if (o.getClass().isArray()) {
            return GsonUtils.toJson((Array.get(o, index)));
        }
        return null;
    }

    private Map<String, Object> getSubFromMap(Map<String, Object> params, String key) {
        try {
            Object target = params.get(key);
            if (target == null) {
                return null;
            }
            if (!(target instanceof Map)) {
                Map<String, Object> parTarget = BeanExtendUtils.bean2map(target);
                params.put(key, parTarget);//替换目标为解析后的map
                return parTarget;
            } else {
                //检查key是否为String
                Map mapTarget = (Map) target;
                if (CollectionUtils.exist(mapTarget.keySet(), k -> !(k instanceof String))) {
                    Map<String, Object> newMap = new HashMap<>();
                    //替换掉键值非String的
                    for (Object k : mapTarget.keySet()) {
                        newMap.put(String.valueOf(k), mapTarget.get(k));
                    }
                    return newMap;
                } else {
                    return mapTarget;
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        TemplateFormat d_templateFormat = new MultTemplateFormat("${", "}").setUnassigned("$^_^");
        TemplateFormat a_templateFormat = new MultTemplateFormat("@").setUnassigned("$^_^");
        TemplateFormat n_templateFormat = new MultTemplateFormat();
        //被替换关键字的的数据源
        Map<String, Object> params = new HashMap<>();
        params.put("cat", "Garfield");
        //拿邮箱配置来试一下～
        params.put("beverage", "$coffee");

        //匹配类似velocity规则的字符串
        String d_template = "${unknown},${config.name} ${cat} really needs some ${beverage}.${config.host|\"default host\"}";
        System.out.println(d_templateFormat.replace(d_template, params));


        String a_template = "@cat @unknown really needs some @beverage.";
        System.out.println(a_templateFormat.replace(a_template, params));

        String n_template = "cat really needs some beverage.";
        System.out.println(n_templateFormat.replace(n_template, params));
    }
}
