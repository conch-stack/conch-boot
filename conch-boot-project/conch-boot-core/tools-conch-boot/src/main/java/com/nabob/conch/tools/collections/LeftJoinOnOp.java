package com.nabob.conch.tools.collections;

import java.util.function.Function;
import java.util.function.Predicate;

public interface LeftJoinOnOp<L, R> {
    LeftJoinOnOp<L, R> filter(Predicate<? super R> predicate);

    <NR> LeftJoinOnOp<L, NR> map(Function<? super R, ? extends NR> mapper);

    <F, K> LeftJoinCollectOp<L, R, K> on(Function<L, K> leftKeyFunction, Function<R, K> rightKeyFunction);

    <F, K> LeftJoinMultiCollectOp<L, R, K> onMulti(Function<L, K> leftKeyFunction, Function<R, K> rightKeyFunction);
}
