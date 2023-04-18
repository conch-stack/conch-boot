package com.nabob.conch.tools.collections;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface LeftJoinMultiCollectOp<L, R, K> {
    <NR> LeftJoinMultiCollectOp<L, NR, K> mapR(Function<? super R, ? extends NR> mapper);

    LeftJoinMultiCollectOp<L, R, K> with(BiConsumer<L, R> resultConsumer);

    LeftJoinMultiCollectOp<L, R, K> withMulti(BiConsumer<L, List<R>> resultConsumer);

    void foreach(BiConsumer<L, R> resultConsumer);

    void foreachMulti(BiConsumer<L, List<R>> resultConsumer);

    SmartStream<L> toL();

    SmartStream<R> toR();

    SmartStream<Pair<L, R>> toPair();

    SmartStream<Pair<L, List<R>>> toMultiPair();

    <F> SmartStream<F> to(BiFunction<L, R, F> resultFunction);

    <F> SmartStream<F> toMulti(BiFunction<L, List<R>, F> resultFunction);
}
