package com.nabob.conch.tools.collections;

import org.apache.commons.lang3.tuple.Pair;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface LeftJoinCollectOp<L, R, K> {
    LeftJoinCollectOp<L, R, K> with(BiConsumer<L, R> resultConsumer);

    void foreach(BiConsumer<L, R> resultConsumer);

    <NR> LeftJoinCollectOp<L, NR, K> mapR(Function<? super R, ? extends NR> mapper);

    SmartStream<L> toL();

    SmartStream<R> toR();

    SmartStream<Pair<L, R>> toPair();

    <F> SmartStream<F> to(BiFunction<L, R, F> resultFunction);
}
