package ltd.beihu.core.tools.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class NativePath {
    private static final Logger logger = LoggerFactory.getLogger(NativePath.class);

    private NativePath() {
    }

    public static Path get(String path) {
        if (path.startsWith("/")) {
            return Paths.get(path);
        }

        String java_class_path = get_class_path();
        if (java_class_path.endsWith(".jar")) {
            int lastIndexOf = java_class_path.lastIndexOf("/");
            if (lastIndexOf == -1) {
                java_class_path = "";
            } else {
                java_class_path = java_class_path.substring(0, lastIndexOf);
            }
        }

        if (!java_class_path.isEmpty() && !java_class_path.endsWith("/")) {
            java_class_path = java_class_path.concat("/");
        }

        java_class_path = java_class_path.concat(path);
        logger.info("final path ---> :".concat(java_class_path));
        return Paths.get(java_class_path);
    }

    public static String get_class_path() {
        String java_class_path = NativePath.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (java_class_path.startsWith("file:"))
            java_class_path = java_class_path.substring(5);
        logger.info("java_class_path -> :".concat(java_class_path));
        logger.info(System.getProperty("os.name"));
        int indexof_classes;
        int webroot;
        int indexof_web_inf;
        int comma;
        String webroot1;
        if (System.getProperty("os.name").indexOf("Windows") != -1) {
            indexof_classes = java_class_path.indexOf("\\classes");
            if (indexof_classes != -1) {
                java_class_path = java_class_path.substring(0, indexof_classes).concat("\\classes");
                webroot = java_class_path.lastIndexOf(";");
                if (webroot != -1) {
                    java_class_path = java_class_path.substring(webroot + 1);
                }

                logger.info("windows code start --> :".concat(java_class_path));
            } else {
                webroot1 = NativePath.class.getResource("").getFile();
                webroot1 = webroot1.replace("file:/", "");
                indexof_web_inf = webroot1.indexOf("/WEB-INF/");
                if (indexof_web_inf != -1) {
                    java_class_path = webroot1.substring(0, indexof_web_inf).concat("/WEB-INF/classes");
                    logger.info("windows server start --> :".concat(java_class_path));
                } else {
                    comma = java_class_path.indexOf(";");
                    if (comma > 0) {
                        java_class_path = java_class_path.substring(0, comma);
                    }

                    logger.info("windows jar start --> :".concat(java_class_path));
                }
            }
        } else {
            indexof_classes = java_class_path.indexOf("/classes");
            if (indexof_classes != -1) {
                java_class_path = java_class_path.substring(0, indexof_classes).concat("/classes");
                webroot = java_class_path.lastIndexOf(":");
                if (webroot != -1) {
                    java_class_path = java_class_path.substring(webroot + 1);
                }

                logger.info("linux code start --> :".concat(java_class_path));
            } else {
                webroot1 = NativePath.class.getResource("").getFile();
                webroot1 = webroot1.replace("file:", "");
                indexof_web_inf = webroot1.indexOf("/WEB-INF/");
                if (indexof_web_inf != -1) {
                    java_class_path = webroot1.substring(0, indexof_web_inf).concat("/WEB-INF/classes");
                    logger.info("linux server start --> :".concat(java_class_path));
                } else {
                    comma = java_class_path.indexOf(":");
                    if (comma > 0) {
                        java_class_path = java_class_path.substring(0, comma);
                    }

                    logger.info("linux jar start --> :".concat(java_class_path));
                }
            }
        }

        return java_class_path;
    }
}

