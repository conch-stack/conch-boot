package ltd.beihu.core.tools.collections;

import java.util.function.Function;
import java.util.function.Predicate;

public interface CrossJoinOnOp<L, R> {
    CrossJoinOnOp<L, R> filter(Predicate<? super R> predicate);

    <NR> CrossJoinOnOp<L, NR> map(Function<? super R, ? extends NR> mapper);

    <F, K> BaseSmartStream.CrossJoinCollectOp<L, R, K> on(Function<L, K> leftKeyFunction, Function<R, K> rightKeyFunction);
}
