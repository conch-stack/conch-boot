package ltd.beihu.core.tools.mapext;

import ltd.beihu.core.tools.utils.LockUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * [HashMap的LRU实现]
 */
public class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 3566536751086052834L;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private final int maxCapacity;
    private final transient ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public LRULinkedHashMap(int maxCapacity) {
        super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.size() > this.maxCapacity;
    }

    public boolean containsKey(Object key) {
        return LockUtils.callWithLock(this.lock.readLock(), () -> super.containsKey(key), false);
    }

    public V get(Object key) {
        return LockUtils.callWithLock(this.lock.readLock(), () -> super.get(key));
    }

    public V put(K key, V value) {
        return LockUtils.callWithLock(this.lock.writeLock(), () -> super.put(key, value));
    }

    public int size() {
        return LockUtils.callWithLock(this.lock.readLock(), super::size, 0);
    }

    public void clear() {
        LockUtils.runWithLock(this.lock.writeLock(), super::clear);
    }

    public Collection<Map.Entry<K, V>> getAll() {
        return LockUtils.callWithLock(this.lock.readLock(), () -> new ArrayList<>(super.entrySet()), null);
    }
}
