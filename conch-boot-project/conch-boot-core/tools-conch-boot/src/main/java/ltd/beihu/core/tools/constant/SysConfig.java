package ltd.beihu.core.tools.constant;


public class SysConfig {
    /**
     * logger,可选值 slf4j, jcl, log4j, jdk 默认加载顺序 slf4j > jcl > log4j > jdk
     */
    public final static String COMMON_LOGGER = "common.logger";

    public final static Integer DEFAULT_BUFF_SIZE = 5000;

    public final static Long DEFAULT_PAUSE = 1000L;

    public final static Long DEFAULT_DURATION = 5000L;

    public final static Integer DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 4;
}
