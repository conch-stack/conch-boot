package ltd.beihu.core.tools.utils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Project: [angel]
 * @Package: [util]
 * @Description: [集合操作工具类]
 * @Author: [toming]
 * @CreateDate: [11/9/16 5:34 PM]
 * @Version: [v1.0]
 */
public final class CollectionUtils {

    /**
     * 判断集合是否为空
     *
     * @param array -待判断集合
     * @return -集合为null或无任何元素时返回false,否则返回true
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static <T> T findOne(Collection<T> collection, Predicate<? super T> predicate) {
        Optional<T> result = collection.parallelStream().filter(predicate).findAny();
        return result.isPresent() ? result.get() : null;
    }

    /**
     * 判断集合中是否存在符合条件的元素
     *
     * @param collection -搜索的集合
     * @param predicate  -查询条件
     * @param <T>        -集合泛型T
     * @return 存在符合条件的元素则返回true, 否则返回false
     */
    public static <T> boolean exist(Collection<T> collection, Predicate<? super T> predicate) {
        Optional<T> result = collection.stream().filter(predicate).findAny();
        return result.isPresent();
    }

    /**
     * 判断集合中是否存在符合条件的元素
     *
     * @param tArray    -搜索的数组
     * @param predicate -查询条件
     * @param <T>       -集合泛型T
     * @return 存在符合条件的元素则返回true, 否则返回false
     */
    public static <T> boolean exist(T[] tArray, Predicate<? super T> predicate) {
        Optional<T> result = Arrays.stream(tArray).filter(predicate).findAny();
        return result.isPresent();
    }

    /**
     * 判断集合中是否存在符合条件的元素
     *
     * @param tArray -搜索的数组
     * @param find   -查询元素
     * @param <T>    -集合泛型T
     * @return 存在符合条件的元素则返回true, 否则返回false
     */
    public static <T> boolean exist(T[] tArray, T find) {
        Optional<T> result = Arrays.stream(tArray).filter(find::equals).findAny();
        return result.isPresent();
    }

    public static <T> T findOne(T[] tArray, Predicate<? super T> predicate) {
        Optional<T> result = Arrays.asList(tArray).parallelStream().filter(predicate).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    public static <T> List<T> newArrayListOnNull(List<T> list) {
        return list == null ? new ArrayList<T>() : list;
    }

    public static <T> Set<T> newHashSetOnNull(Set<T> set) {
        return set == null ? new HashSet<T>() : set;
    }

    public static <K, V> Map<K, V> newHashMapOnNull(Map<K, V> map) {
        if (map == null) {
            map = new HashMap<K, V>();
        }
        return map;
    }

    public static <T> Set<T> createHashSet(T... arr) {
        int size = arr == null ? 0 : arr.length;
        Set<T> set = new HashSet<T>(size);
        if (arr != null && arr.length > 0) {
            Collections.addAll(set, arr);
        }
        return set;
    }

    public static Map<String, String> toMap(Properties properties) {
        if (properties == null) {
            return new HashMap<String, String>(0);
        }
        Map<String, String> map = new HashMap<String, String>(properties.size());

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            map.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return map;
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && map.size() > 0;
    }

    /**
     * 判断map是否为空
     *
     * @param map -待判断map
     * @return -map为null或无任何元素时返回false,否则返回true
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return !isNotEmpty(map);
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && collection.size() > 0;
    }

    /**
     * 判断集合是否为空
     *
     * @param collection -待判断集合
     * @return -集合为null或无任何元素时返回false,否则返回true
     */
    public static boolean isEmpty(Collection<?> collection) {
        return !isNotEmpty(collection);
    }

    public static <K, V> V getValue(Map<K, V> map, K key) {
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    public static int sizeOf(Collection<?> collection) {
        if (isEmpty(collection)) {
            return 0;
        }
        return collection.size();
    }

    public static int sizeOf(Map<?, ?> map) {
        if (map == null) {
            return 0;
        }
        return map.size();
    }

    /**
     * 返回第一个列表中比第二个多出来的元素
     */
    public static <T> List<T> getLeftDiff(List<T> list1, List<T> list2) {
        if (isEmpty(list2)) {
            return list1;
        }
        List<T> list = new ArrayList<T>();
        if (isNotEmpty(list1)) {
            for (T o : list1) {
                if (!list2.contains(o)) {
                    list.add(o);
                }
            }
        }
        return list;
    }

    public static <T> List<T> setToList(Set<T> set) {
        if (set == null) {
            return null;
        }
        return new ArrayList<T>(set);
    }

    /**
     * 统计可迭代元素的数量
     *
     * @param iterable -可迭代元素
     * @return -可迭代元素的数量
     */
    public static int count(Iterable iterable) {
        int count = 0;
        Iterator iterator = iterable == null ? null : iterable.iterator();
        if (iterator != null) {
            while (iterator.hasNext()) {
                ++count;
            }
        }
        return count;
    }

    /**
     * 统计迭代器中元素的数量
     *
     * @param iterator -迭代器
     * @return -迭代器中元素的数量
     */
    public static int count(Iterator iterator) {
        int count = 0;
        if (iterator != null) {
            while (iterator.hasNext()) {
                ++count;
            }
        }
        return count;
    }

    /**
     * 可迭代元素转换到数组
     *
     * @param iterable -可迭代元素
     * @param <T>      -迭代类型
     * @return -转换后的数组
     */
    public static <T> List<T> iterable2List(Iterable<T> iterable) {
        List<T> result = new ArrayList<>();
        Iterator<T> iterator = iterable.iterator();
        if (iterator != null) {
            while (iterator.hasNext()) {
                result.add(iterator.next());
            }
        }
        return result;
    }

    /**
     * 迭代器转换到数组
     *
     * @param iterator -迭代器
     * @param <T>      -迭代类型
     * @return -转换后的数组
     */
    public static <T> List<T> iterator2List(Iterator<T> iterator) {
        List<T> result = new ArrayList<>();
        if (iterator != null) {
            while (iterator.hasNext()) {
                result.add(iterator.next());
            }
        }
        return result;
    }

    /**
     * 迭代器转换转换
     *
     * @param iterator  -迭代器
     * @param converter -转换器
     * @param <T>       -迭代类型
     * @param <O>       -转换类型
     * @return -转换结果集合
     */
    public static <T, O> List<O> iteratorConverter(Iterator<T> iterator, Function<T, O> converter) {
        List<O> result = new ArrayList<>();
        if (iterator != null) {
            while (iterator.hasNext()) {
                result.add(converter.apply(iterator.next()));
            }
        }
        return result;
    }

    /**
     * 可迭代元素转换
     *
     * @param iterable  -可迭代
     * @param converter -转换器
     * @param <T>       -迭代类型
     * @param <O>       -转换类型
     * @return -转换结果集合
     */
    public static <T, O> List<O> iterableConverter(Iterable<T> iterable, Function<T, O> converter) {
        List<O> result = new ArrayList<>();
        Iterator<T> iterator = iterable.iterator();
        if (iterator != null) {
            while (iterator.hasNext()) {
                result.add(converter.apply(iterator.next()));
            }
        }
        return result;
    }

    /**
     * 对数组进行map操作
     *
     * @param tArray    -源数组
     * @param mapper    -map函数
     * @param generator -结果构造器
     * @param <T>       -源类型
     * @param <R>       -结果类型
     * @return -结果集数组
     */
    public static <T, R> R[] map(T[] tArray, Function<? super T, ? extends R> mapper, IntFunction<R[]> generator) {
        return tArray == null ? generator.apply(0) : Arrays.stream(tArray).map(mapper).toArray(generator);
    }

    /**
     * 对List进行map操作
     *
     * @param tList  -源List
     * @param mapper -map函数
     * @param <T>    -源类型
     * @param <R>    -结果类型
     * @return -结果集数组
     */
    public static <T, R> List<R> map(List<T> tList, Function<? super T, ? extends R> mapper) {
        return tList == null ? null : tList.stream().map(mapper).collect(Collectors.toList());
    }

    public static <T> List<T> filter(List<T> tList, Predicate<? super T> predicate) {
        return tList == null ? null : tList.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <T> Object[] erasure(T[] array) {
        if (array == null)
            return null;
        Object[] objects = new Object[array.length];
        System.arraycopy(array, 0, objects, 0, array.length);
        return objects;
    }

    public static <T> T[] toArray(Collection<? extends T> collection, Class<T> cls) {
        T[] array = (T[]) Array.newInstance(cls, collection.size());
        return collection.toArray(array);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> collection) {
        T[] array = (T[]) Array.newInstance(componentType(collection), sizeOf(collection));
        return collection.toArray(array);
    }

    public static Class componentType(Collection collection) {

        if (collection == null) {
            return Object.class;
        }

        Class<?> componentType = collection.getClass().getComponentType();

        if (componentType != null) {
            return componentType;
        }

        if (collection.size() == 0) {
            return Object.class;
        }

        return collection.iterator().next().getClass();
    }
}
