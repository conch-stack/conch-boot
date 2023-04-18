package ltd.beihu.core.tools.utils;

import java.util.List;

/**
 * Created by tangming on 6/6/17.
 */
public class ListUtils {

    public static int size(List list) {
        return list == null ? 0 : list.size();
    }

    public static <T> T getFirst(List<T> list) {
        return org.apache.commons.collections.CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    public static Object unboxIfSingle(List list) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(list))
            return null;
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return list;
        }
    }
}
