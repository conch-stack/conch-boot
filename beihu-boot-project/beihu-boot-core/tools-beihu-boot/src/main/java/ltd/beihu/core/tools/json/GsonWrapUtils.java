package ltd.beihu.core.tools.json;

import ltd.beihu.core.tools.utils.Assert;

/**
 * 和class一起转json,效率低!
 */
public class GsonWrapUtils {
    public static String toJson(Object bean) {
        Wrapper wrapper = new Wrapper();
        wrapper.setClazz(bean.getClass().getName());
        wrapper.setData(GsonUtils.toJson(bean));
        return GsonUtils.toJson(wrapper);
    }

    public static Object fromJson(String json) throws ClassNotFoundException {
        if (json == null)
            return null;
        Wrapper wrapper = GsonUtils.fromJson(json, Wrapper.class);
        Assert.notNull(wrapper, "error to parse:[" + json + "] to Wrapper");
        Class clazz = Class.forName(wrapper.getClazz());
        Assert.notNull(clazz, "error to parse:[" + json + "],cause by the unknown clazz:[" + wrapper.getClazz() + "]");
        return GsonUtils.fromJson(wrapper.getData(), clazz);
    }

    public static class Wrapper {
        private String clazz;
        private String data;

        public String getClazz() {
            return clazz;
        }

        public void setClazz(String clazz) {
            this.clazz = clazz;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
