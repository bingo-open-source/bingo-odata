package odata.zinternal.lang;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A Map implementation whose structure is immutable after construction.
 * <p>Useful for apis that assume a returned map remains unmodified.
 * <p>All mutation methods throw <code>UnsupportedOperationException</code>
 *
 * @param <K>  the map key type
 * @param <V>  the map value type
 */
public class ImmutableMap<K, V> implements Map<K, V> {

    public static class Builder<K, V> {
        private final LinkedHashMap<K, V> map = new LinkedHashMap<K, V>();

        public Builder<K, V> put(K key, V value) {
            map.put(key, value);
            return this;
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            this.map.putAll(map);
            return this;
        }

        public ImmutableMap<K, V> build() {
            return new ImmutableMap<K, V>(map);
        }
    }

    private final LinkedHashMap<K, V> map;

    private ImmutableMap(LinkedHashMap<K, V> map) {
        this.map = map;
    }

    public static <K, V> ImmutableMap<K, V> of() {
        return new Builder<K, V>().build();
    }

    public static <K, V> ImmutableMap<K, V> of(K key1, V value1) {
        return new Builder<K, V>().put(key1, value1).build();
    }

    public static <K, V> ImmutableMap<K, V> of(K key1, V value1, K key2, V value2) {
        return new Builder<K, V>().put(key1, value1).put(key2, value2).build();
    }

    public static <K, V> ImmutableMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3) {
        return new Builder<K, V>().put(key1, value1).put(key2, value2).put(key3, value3).build();
    }

    public static <K, V> ImmutableMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
        return new Builder<K, V>().put(key1, value1).put(key2, value2).put(key3, value3).put(key4, value4).build();
    }

    public static <K, V> ImmutableMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
        return new Builder<K, V>().put(key1, value1).put(key2, value2).put(key3, value3).put(key4, value4).put(key5, value5).build();
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public V get(Object key) {
        return map.get(key);
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public Collection<V> values() {
        return map.values();
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    private static UnsupportedOperationException newModificationUnsupported() {
        return new UnsupportedOperationException(ImmutableMap.class.getSimpleName() + " cannot be modified");
    }

    public V put(K key, V value) {
        throw newModificationUnsupported();
    }

    public V remove(Object key) {
        throw newModificationUnsupported();
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        throw newModificationUnsupported();
    }

    public void clear() {
        throw newModificationUnsupported();
    }
}
