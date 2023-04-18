package ltd.beihu.core.tools.mapext;

import ltd.beihu.core.tools.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Project: [ceres]
 * @Description: [description]
 * @Author: [toming]
 * @CreateDate: [12/5/16 2:10 PM]
 * @Version: [v1.0]
 */
public class MapWithList<K, V> {
    private HashMap<K, List<V>> core = new HashMap<>();

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously doesn't contained a mapping for the key,
     * then create a List for the value;
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the new value associated with <tt>key</tt>
     */
    public List<V> put(K key, V value) {
        List<V> oldValue = core.get(key);
        if (CollectionUtils.isEmpty(oldValue)) {
            List<V> newValue = new ArrayList<V>();
            newValue.add(value);
            core.put(key, newValue);
            return newValue;
        } else {
            oldValue.add(value);
            return oldValue;
        }
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     * <p>
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     * <p>
     * <p>A return value of {@code null} does not <i>necessarily</i>
     * indicate that the map contains no mapping for the key; it's also
     * possible that the map explicitly maps the key to {@code null}.
     * The {@link #containsKey containsKey} operation may be used to
     * distinguish these two cases.
     *
     * @see #put(Object, Object)
     */
    public List<V> get(K key) {
        return core.get(key);
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the
     * specified key.
     *
     * @param key The key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     * key.
     */
    public boolean containsKey(K key) {
        return core.containsKey(key);
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys
     *                              (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @implSpec The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     * @since 1.8
     */
    public List<V> getOrDefault(K key, List<V> defaultValue) {
        return core.getOrDefault(key, defaultValue);
    }
}
