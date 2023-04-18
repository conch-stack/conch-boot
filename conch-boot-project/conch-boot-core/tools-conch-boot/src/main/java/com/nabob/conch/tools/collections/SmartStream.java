package com.nabob.conch.tools.collections;

import java.util.Collection;
import java.util.stream.Stream;

public interface SmartStream<L> extends Stream<L> {
    <R> LeftJoinOnOp<L, R> leftJoin(Collection<R> right);

    <R> CrossJoinOnOp<L, R> crossJoin(Collection<R> right);
}
