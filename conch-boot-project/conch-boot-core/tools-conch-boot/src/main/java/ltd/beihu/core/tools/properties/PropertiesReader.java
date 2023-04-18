package ltd.beihu.core.tools.properties;

import ltd.beihu.core.tools.template.format.SimpleTemplateFormat;
import ltd.beihu.core.tools.template.format.TemplateFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * [配置读取类]
 */
public class PropertiesReader implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesReader.class);
    private String split = "=";
    private String annotation = "#";
    private Map<String, String> properties = new HashMap<>();

    private TemplateFormat templateFormat = new SimpleTemplateFormat("${", "}");

    /**
     * 设置分隔符
     *
     * @param split -分隔符,默认'='
     */
    public PropertiesReader setSplit(String split) {
        this.split = split;
        return this;
    }

    /**
     * 设置注释符
     *
     * @param annotation -注释符,默认'#'
     */
    public PropertiesReader setAnnotation(String annotation) {
        this.annotation = annotation;
        return this;
    }

    /**
     * 加载配置文件
     *
     * @param confFile 类路径下的配置文件
     */
    public PropertiesReader load(Class clazz, String confFile) {

        if (confFile.contains(",")) {
            for (String cf : confFile.split(",")) {
                loadConf(clazz, cf);
            }
        } else {
            loadConf(clazz, confFile);
        }
        return this;
    }

    /**
     * 加载配置文件
     *
     * @param confFile 类路径下的配置文件
     */
    public PropertiesReader loadConf(Class clazz, String confFile) {
        try {
            InputStream in = clazz.getClassLoader().getResourceAsStream(confFile);
            if (in == null) {
                logger.info("未找到配置文件：" + confFile);
                return null;
            } else {
                logger.info("加载配置文件：" + confFile);
                loadConf(in);
            }
        } catch (IOException e) {
            logger.info("加载配置文件{}失败：{}", confFile, ExceptionUtils.getStackTrace(e));
        }
        return this;
    }

    /**
     * 加载配置文件
     *
     * @param confFile 类路径下的配置文件
     */
    public PropertiesReader load(String confFile) {
        if (confFile.contains(",")) {
            for (String cf : confFile.split(",")) {
                loadConf(cf);
            }
        } else {
            loadConf(confFile);
        }
        return this;
    }

    private void loadConf(String confFile) {
        try {
            InputStream in = Files.newInputStream(NativePath.get(confFile), StandardOpenOption.READ);
            if (in == null) {
                logger.info("未找到配置文件：" + confFile);
            } else {
                logger.info("加载配置文件：" + confFile);
                loadConf(in);
            }
        } catch (IOException e) {
            logger.info("加载配置文件{}失败：{}", confFile, ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 加载配置文件
     *
     * @param in -输入流
     */
    private void loadConf(InputStream in) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if ("".equals(line) || line.startsWith(annotation)) {
                    continue;
                }
                int index = line.indexOf(split);
                if (index == -1) {
                    logger.error("错误的配置：" + line);
                    continue;
                }
                //有K V
                if (index > 0 && line.length() > index + 1) {
                    String key = line.substring(0, index).trim();
                    String value = line.substring(index + 1, line.length()).trim();
                    properties.put(format(key), format(value));
                }
                //有K无V
                else if (index > 0 && line.length() == index + 1) {
                    String key = line.substring(0, index).trim();
                    properties.put(format(key), "");
                } else {
                    logger.error("错误的配置：" + line);
                }
            }
        }
    }

    public Set<String> getNames() {
        return properties.keySet();
    }

    public String getProperty(String name) {
        return properties.get(name);
    }

    private String format(String val) {
        final Map map = properties;
        return templateFormat.replace(val, (Map<String, Object>) map);
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public void write(Map<String, String> map) {
        map.putAll(this.properties);
    }

    public void write(Object bean){
        try {
            BeanUtils.populate(bean, this.properties);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
    }

    @Override
    public void close() throws IOException {
        this.properties.clear();
        this.properties = null;
    }
}