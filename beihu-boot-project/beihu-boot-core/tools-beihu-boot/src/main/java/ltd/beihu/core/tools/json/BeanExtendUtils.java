package ltd.beihu.core.tools.json;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project: [CommonUtil]
 * @Package: [com.pengshu.commonutils.json]
 * @Description: [Bean工具]
 * @Author: [toming]
 * @CreateDate: [6/17/16 5:47 PM]
 * @Version: [v1.0]
 */
public class BeanExtendUtils {

    public static Map<String, Object> bean2map(Object bean) {
        if (bean == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(bean);

                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }

        return map;
    }
}
