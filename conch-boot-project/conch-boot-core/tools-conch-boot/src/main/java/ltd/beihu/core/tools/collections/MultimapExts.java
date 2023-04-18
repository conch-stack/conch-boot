package ltd.beihu.core.tools.collections;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import ltd.beihu.core.tools.utils.CollectionUtils;

import java.util.Map;

/**
 * Created by toming on 17-5-21.
 */
public class MultimapExts {

    @SuppressWarnings("all")
    public static <T, K, V> ImmutableListMultimap<K, V> index(
            Iterable<T> resources,
            Function<? super T, K> keyFunction,
            Function<? super T, V> valueFunction) {
        Preconditions.checkNotNull(keyFunction);
        Preconditions.checkNotNull(valueFunction);
        ImmutableListMultimap.Builder<K, V> builder =
                ImmutableListMultimap.builder();
        for (T t : resources) {
            builder.put(keyFunction.apply(t), valueFunction.apply(t));
        }
        return builder.build();
    }

    public static <K, V, NV> ListMultimap<K, NV> mapValue(ListMultimap<K, V> map, java.util.function.Function<? super V, ? extends NV> mapper) {

        ImmutableListMultimap.Builder<K, NV> builder =
                ImmutableListMultimap.builder();
        for (K t : map.keySet()) {
            builder.putAll(t, CollectionUtils.map(map.get(t), mapper::apply));
        }
        return builder.build();
    }


    public static <K, V, NV> Map<K, NV> mapValue(Map<K, V> map, java.util.function.Function<? super V, ? extends NV> mapper) {

        ImmutableMap.Builder<K, NV> builder =
                ImmutableMap.builder();
        for (K t : map.keySet()) {
            builder.put(t, mapper.apply(map.get(t)));
        }
        return builder.build();
    }
}
