package ltd.beihu.core.tools.utils;

import ltd.beihu.core.tools.function.BiConsumer;
import ltd.beihu.core.tools.function.Generator;
import ltd.beihu.core.tools.function.TwoArgFunc;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/**
 * @Project: [ops]
 * @Description: [集合扩展性操作]
 * @Author: [toming]
 * @CreateDate: [10/26/16 2:15 PM]
 * @Version: [v1.0]
 */
public final class CollectionExtendUtil {


    /**
     * Gets a Collection size or {@code 0} if the Collection is
     * {@code null}.
     *
     * @param collection -a Collection or {@code null}
     * @return Collection size or {@code 0} if the Collection is
     * {@code null}.
     */
    public static int size(Collection collection) {
        return collection == null ? 0 : collection.size();
    }

    public static <T> T getOne(Collection<T> collection) {
        return CollectionUtils.isEmpty(collection) ? null : collection.iterator().next();
    }

    public static Object unboxIfSingle(Collection collection) {
        int size = size(collection);
        if (size == 0)
            return null;
        if (size == 1) {
            return getOne(collection);
        } else {
            return collection;
        }
    }

    /**
     * <p>Compares two Collection by size, returning {@code true} if their size are same.</p>
     * <p>if the Collection is null,it's size will be zero</p>
     * <pre>
     * sizeEqual(null, null)   = true
     * sizeEqual(null, empty)  = true
     * sizeEqual(empty, empty)  = true
     * sizeEqual([1,2], [1,2,3])  = false
     * </pre>
     *
     * @param one   -the first Collection, may be {@code null}
     * @param other -the second Collection, may be {@code null}
     * @return
     */
    public static boolean sizeEqual(Collection one, Collection other) {
        return size(one) == size(other);
    }

    public static <T, K> Map<K, List<T>> group(Collection<T> collection, Function<T, K> groupBy) {
        return group(collection, groupBy, ArrayList::new);
    }

    public static <T, K, V extends Collection<T>> Map<K, V> group(
            Collection<T> collection, Function<T, K> groupBy, Generator<V> generator) {
        Map<K, V> result = new HashMap<K, V>();
        for (T t : collection) {
            K key = groupBy.apply(t);
            V val = result.computeIfAbsent(key, k -> generator.generate());
            val.add(t);
        }
        return result;
    }

    public static <T, K, VN, V extends Collection<VN>> Map<K, V> group(
            Collection<T> collection, Function<T, K> groupBy,
            Function<T, VN> valueHandle, Generator<V> generator) {
        Map<K, V> result = new HashMap<K, V>();
        for (T t : collection) {
            K key = groupBy.apply(t);
            V val = result.computeIfAbsent(key, k -> generator.generate());
            val.add(valueHandle.apply(t));
        }
        return result;
    }

    /**
     * <p>convert collection to map</p>
     *
     * @param collection -a Collection
     * @param keyHandle  -handle which will be used to get key from element in the collection
     * @param <T>        -the type of collection
     * @param <K>        -the type of target key
     * @return -a map created by collection
     */
    public static <T, K> Map<K, T> collectionToMap(Collection<T> collection, Function<T, K> keyHandle) {
        Map<K, T> map = new HashMap<>();
//        for (T t : collection) {
//            map.put(keyHandle.apply(t), t);
//        }
        collection.stream().forEach(t -> map.put(keyHandle.apply(t), t));
        return map;
    }

    /**
     * <p>convert collection to map</p>
     *
     * @param collection -a Collection
     * @param keyHandle  -handle which will be used to get key from element in the collection
     * @param valHandle  -handle which will be used to get val from element in the collection
     * @param <T>        -the type of collection
     * @param <K>        -the type of target key
     * @return -a map created by collection
     */
    public static <T, K, V> Map<K, V> collectionToMap(Collection<T> collection, Function<T, K> keyHandle, Function<T, V> valHandle) {
        Map<K, V> map = new HashMap<>();
        collection.stream().forEach(t -> map.put(keyHandle.apply(t), valHandle.apply(t)));
        return map;
    }

    /**
     * <p>convert map to list</p>
     *
     * @param map       -a  map
     * @param keyHandle -handle which will get a new result from keyValuePir
     * @param <T>       -the type of result
     * @param <K>       -the type of key
     * @param <V>       -the type of value
     * @return -a list created by a map
     */
    public static <T, K, V> List<T> mapToList(Map<K, V> map, TwoArgFunc<K, V, T> keyHandle) {
        return map.keySet().stream().map(k -> keyHandle.apply(k, map.get(k))).collect(Collectors.toList());
    }

    public static void foreach(Object object, Consumer<Object> consumer) {
        foreach(object, consumer, (a, b) -> consumer.accept(org.apache.commons.lang3.tuple.Pair.of(a, b)));
    }

    public static void foreach(Object object, Consumer<Object> consumer, BiConsumer<Object, Object> entryConsumer) {
        if (object instanceof Map) {
            Set<Map.Entry> entrys = ((Map) object).entrySet();
            for (Map.Entry entry : entrys) {
                entryConsumer.accept(entry.getKey(), entry.getValue());
            }
        } else if (object instanceof Collection) {
            ((Collection) object).forEach(consumer);
        } else if (object instanceof Object[]) {
            for (Object o : ((Object[]) object)) {
                consumer.accept(o);
            }
        } else if (object instanceof Iterator) {
            Iterator it = (Iterator) object;
            while (it.hasNext()) {
                consumer.accept(it.next());
            }
        } else if (object instanceof Enumeration) {
            Enumeration it = (Enumeration) object;
            while (it.hasMoreElements()) {
                consumer.accept(it.nextElement());
            }
        } else {
            consumer.accept(null);
        }
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

    public static <T> Object[] erasure(T[] array) {
        if (array == null)
            return null;
        Object[] objects = new Object[array.length];
        System.arraycopy(array, 0, objects, 0, array.length);
        return objects;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<? extends T> collection, Class<T> cls) {
        T[] array = (T[]) Array.newInstance(cls, collection.size());
        return collection.toArray(array);
    }

}
