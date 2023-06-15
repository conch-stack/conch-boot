package com.nabob.conch.tools.constant;


public class SysConfig {
    /**
     * logger,可选值 slf4j, jcl, log4j, jdk 默认加载顺序 slf4j > jcl > log4j > jdk
     */
    public final static String COMMON_LOGGER = "common.logger";

    public final static Integer DEFAULT_BUFF_SIZE = 5000;

    public final static Long DEFAULT_PAUSE = 1000L;

    public final static Long DEFAULT_DURATION = 5000L;

    public final static Integer DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 4;

    /**
     * token前缀
     */
    public static final String TOKEN_PREFIX_BEARER = "Bearer";
    /**
     * token header头名称
     */
    public static final String TOKEN_HEADER_NAME = "Authorization";

    /**
     * 逗号分隔符 ,
     */
    public static final String SEPARATOR_COMMA = ",";
    /**
     * 下划线分隔符_
     */
    public static final String SEPARATOR_UNDERSCORE = "_";
    /**
     * 横杠分隔符 -
     */
    public static final String SEPARATOR_CROSSBAR = "-";
    /**
     * 冒号分隔符
     */
    public final static  String SEPARATOR_COLON = ":";
    /**
     * 斜杠路径分隔符
     */
    public final static  String SEPARATOR_SLASH = "/";
    /**
     * 竖线分隔符，or
     */
    public final static  String SEPARATOR_OR = "|";
    /**
     * 分号分隔符
     */
    public final static  String SEPARATOR_SEMICOLON = ";";
    /**
     * 点分隔符
     */
    public static final String SEPARATOR_DOT = ".";
    /**
     * 排序 - 降序标记
     */
    public static final String ORDER_DESC = "DESC";
}
