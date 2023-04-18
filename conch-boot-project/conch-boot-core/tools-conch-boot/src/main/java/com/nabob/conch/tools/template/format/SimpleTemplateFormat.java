package com.nabob.conch.tools.template.format;

import com.nabob.conch.tools.json.GsonUtils;
import com.nabob.conch.tools.utils.ClassHelper;
import com.nabob.conch.tools.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [模板格式化类的简单实现]
 */
public class SimpleTemplateFormat implements TemplateFormat {
    private String prefix;//匹配前缀
    private String suffix;//匹配后缀
    private String unassigned = "";//未匹配时的默认值
    private String nullAs = "null";
    private Map<Class, Function<Object, String>> converts = new HashMap<>();
    private boolean useJson = false;

    public SimpleTemplateFormat() {
    }

    public SimpleTemplateFormat(String prefix) {
        this.prefix = StringUtil.escape(prefix);
        this.suffix = "";
    }

    public SimpleTemplateFormat(String prefix, String suffix) {
        this.prefix = StringUtil.escape(prefix);
        this.suffix = StringUtil.escape(suffix);
    }

    /**
     * 设置参数前缀
     * `
     *
     * @param prefix -前缀,如"${","<-","@",也可以为空
     */
    public SimpleTemplateFormat setPrefix(String prefix) {
        this.prefix = StringUtil.escape(prefix);
        return this;
    }

    /**
     * 设置参数后缀
     *
     * @param suffix-后缀,如"}","->",也可以为空
     */
    public SimpleTemplateFormat setSuffix(String suffix) {
        this.suffix = StringUtil.escape(suffix);
        return this;
    }

    /**
     * Set the default value for the key which is not contained in params,
     * It will not work if both of the prefix and suffix are blank
     *
     * @param unassigned - the default value for unassigned key
     * @return -current format
     */
    public SimpleTemplateFormat setUnassigned(String unassigned) {
        this.unassigned = encodeDollar(unassigned);
        return this;
    }

    public <T> SimpleTemplateFormat registerConverter(Class<T> cls, Function<T, String> converter) {
        this.converts.put(cls, t -> converter.apply((T) t));
        return this;
    }

    public SimpleTemplateFormat nullAs(String nullAs) {
        this.nullAs = nullAs;
        return this;
    }

    public SimpleTemplateFormat useJson(boolean useJson) {
        this.useJson = useJson;
        return this;
    }

    /**
     * it's the real method to format the content with a map;
     */
    @Override
    public String replace(String content, Map<String, Object> params) {
        String patternString = getPattern(params);
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(content);

        //替换参数
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, encodeReplacement(resolve(params, matcher.group(1))));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 生成匹配模式的正则表达式
     */
    private String getPattern(Map<String, Object> params) {
        if (StringUtils.isBlank(prefix) && StringUtils.isBlank(suffix)) {
            //literally
            return "(" + StringUtils.join(params.keySet(), "|") + ")";
        } else if (StringUtils.isBlank(prefix)) {
            return "\\b(.*?)" + suffix;
        } else if (StringUtils.isBlank(suffix)) {
            return prefix + "(.+?)\\b";
        } else {
            //add prefix and suffix
            return prefix + "(.*?)" + suffix;
        }
    }

    protected String resolve(Map<String, Object> params, String key) {
        Object target = getFromParam(params, key);
        if (target == null) {
            return nullAs;
        } else {
            Class cls = target.getClass();
            if (converts.containsKey(cls)) {
                return converts.get(cls).apply(target);
            }
            for (Class keyCls : converts.keySet()) {
                if (ClassHelper.isAssignable(target, keyCls)) {
                    return converts.get(keyCls).apply(target);
                }
            }
            return useJson ? GsonUtils.toJson(target) : target.toString();
        }
    }

    protected Object getFromParam(Map<String, Object> params, String key) {
        return params.get(key);
    }

    /**
     * 处理$反转义
     */
    private String encodeReplacement(String str) {
        return str == null ? this.unassigned : encodeDollar(str);
    }

    private String encodeDollar(String str) {
        return StringUtils.isBlank(str) ? "" :
                (str.contains("$") ? str.replaceAll("\\$", "\\\\\\$") : str);
    }


    public static void main(String[] args) throws Exception {
        SimpleTemplateFormat d_templateFormat = new SimpleTemplateFormat("${", "}").setUnassigned("$^_^");
        SimpleTemplateFormat a_templateFormat = new SimpleTemplateFormat("@").setUnassigned("$^_^");
        SimpleTemplateFormat n_templateFormat = new SimpleTemplateFormat();
        //被替换关键字的的数据源
        Map<String, Object> params = new HashMap<>();
        params.put("cat", "Garfield");
        params.put("beverage", "$coffee");

        //匹配类似velocity规则的字符串
        String d_template = "${unknown} ${cat} really needs some ${beverage}.";
        System.out.println(d_templateFormat.replace(d_template, params));


        String a_template = "@cat @unknown really needs some @beverage.";
        System.out.println(a_templateFormat.replace(a_template, params));

        String n_template = "cat really needs some beverage.";
        System.out.println(n_templateFormat.replace(n_template, params));
    }
}
