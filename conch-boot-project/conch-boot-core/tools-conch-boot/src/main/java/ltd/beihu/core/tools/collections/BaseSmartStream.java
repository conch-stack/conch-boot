package ltd.beihu.core.tools.collections;

import com.google.common.collect.*;
import ltd.beihu.core.tools.stream.Streams;
import ltd.beihu.core.tools.utils.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * @Author: toming
 */
public class BaseSmartStream<L> implements SmartStream<L> {

    //region construct
    private Stream<L> left;

    private BaseSmartStream(Stream<L> left) {
        this.left = left;
    }

    public static <L> SmartStream<L> load(Collection<L> left) {
        return new BaseSmartStream<L>(left.stream());
    }

    public static <L> SmartStream<L> load(Stream<L> left) {
        return new BaseSmartStream<L>(left);
    }
    //endregion construct

    //region implement Stream
    @Override
    public SmartStream<L> filter(Predicate<? super L> predicate) {
        left = left.filter(predicate);
        return this;
    }

    @Override
    public <R> SmartStream<R> map(Function<? super L, ? extends R> mapper) {
        return new BaseSmartStream<R>(left.map(mapper));
    }

    @Override
    public IntStream mapToInt(ToIntFunction<? super L> mapper) {
        return this.left.mapToInt(mapper);
    }

    @Override
    public LongStream mapToLong(ToLongFunction<? super L> mapper) {
        return this.left.mapToLong(mapper);
    }

    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super L> mapper) {
        return this.left.mapToDouble(mapper);
    }

    @Override
    public <R> Stream<R> flatMap(Function<? super L, ? extends Stream<? extends R>> mapper) {
        return this.left.flatMap(mapper);
    }

    @Override
    public IntStream flatMapToInt(Function<? super L, ? extends IntStream> mapper) {
        return this.left.flatMapToInt(mapper);
    }

    @Override
    public LongStream flatMapToLong(Function<? super L, ? extends LongStream> mapper) {
        return this.left.flatMapToLong(mapper);
    }

    @Override
    public DoubleStream flatMapToDouble(Function<? super L, ? extends DoubleStream> mapper) {
        return this.left.flatMapToDouble(mapper);
    }

    @Override
    public SmartStream<L> distinct() {
        this.left = this.left.distinct();
        return this;
    }

    @Override
    public SmartStream<L> sorted() {
        this.left = this.left.sorted();
        return this;
    }

    @Override
    public SmartStream<L> sorted(Comparator<? super L> comparator) {
        this.left = this.left.sorted(comparator);
        return this;
    }

    @Override
    public SmartStream<L> peek(Consumer<? super L> action) {
        this.left = this.left.peek(action);
        return this;
    }

    @Override
    public SmartStream<L> limit(long maxSize) {
        this.left = this.left.limit(maxSize);
        return this;
    }

    @Override
    public SmartStream<L> skip(long n) {
        this.left = this.left.skip(n);
        return this;
    }

    @Override
    public void forEach(Consumer<? super L> action) {
        this.left.forEach(action);
    }

    @Override
    public void forEachOrdered(Consumer<? super L> action) {
        this.left.forEachOrdered(action);
    }

    @Override
    public Object[] toArray() {
        return this.left.toArray();
    }

    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        return this.left.toArray(generator);
    }

    @Override
    public L reduce(L identity, BinaryOperator<L> accumulator) {
        return this.left.reduce(identity, accumulator);
    }

    @Override
    public Optional<L> reduce(BinaryOperator<L> accumulator) {
        return this.left.reduce(accumulator);
    }

    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super L, U> accumulator, BinaryOperator<U> combiner) {
        return this.left.reduce(identity, accumulator, combiner);
    }

    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super L> accumulator, BiConsumer<R, R> combiner) {
        return this.left.collect(supplier, accumulator, combiner);
    }

    @Override
    public <R, A> R collect(Collector<? super L, A, R> collector) {
        return this.left.collect(collector);
    }

    @Override
    public Optional<L> min(Comparator<? super L> comparator) {
        return this.left.min(comparator);
    }

    @Override
    public Optional<L> max(Comparator<? super L> comparator) {
        return this.left.max(comparator);
    }

    @Override
    public long count() {
        return this.left.count();
    }

    @Override
    public boolean anyMatch(Predicate<? super L> predicate) {
        return this.left.anyMatch(predicate);
    }

    @Override
    public boolean allMatch(Predicate<? super L> predicate) {
        return this.left.allMatch(predicate);
    }

    @Override
    public boolean noneMatch(Predicate<? super L> predicate) {
        return this.left.noneMatch(predicate);
    }

    @Override
    public Optional<L> findFirst() {
        return this.left.findFirst();
    }

    @Override
    public Optional<L> findAny() {
        return this.left.findAny();
    }
    //endregion

    //region implement BaseStream
    @Override
    public Iterator<L> iterator() {
        return left.iterator();
    }

    @Override
    public Spliterator<L> spliterator() {
        return left.spliterator();
    }

    @Override
    public boolean isParallel() {
        return left.isParallel();
    }

    @Override
    public SmartStream<L> sequential() {
        return this;
    }

    @Override
    public SmartStream<L> parallel() {
        left = left.parallel();
        return this;
    }

    @Override
    public SmartStream<L> unordered() {
        left = left.unordered();
        return this;
    }

    @Override
    public SmartStream<L> onClose(Runnable closeHandler) {
        left = left.onClose(closeHandler);
        return this;
    }

    @Override
    public void close() {
        left.close();
    }
    //endregion

    //region left join

    /**
     * 左联
     * left-null
     * left-right
     *
     * @param right 右表
     * @param <R>   　右类型
     * @return 左联中间步骤
     */
    @Override
    public <R> LeftJoinOnOp<L, R> leftJoin(Collection<R> right) {
        return new LeftJoinOnOpImpl<>(left, right == null ? new ArrayList<>() : right);
    }


    /**
     * @param <R> 　右类型
     */
    public static class LeftJoinOnOpImpl<L, R> implements LeftJoinOnOp<L, R> {

        private Stream<L> left;

        private Collection<R> right;

        LeftJoinOnOpImpl(Stream<L> left, Collection<R> right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public LeftJoinOnOp<L, R> filter(Predicate<? super R> predicate) {
            right = right.stream().filter(predicate).collect(Collectors.toList());
            return this;
        }

        @Override
        public <NR> LeftJoinOnOp<L, NR> map(Function<? super R, ? extends NR> mapper) {
            return new LeftJoinOnOpImpl<>(left, right.stream().map(mapper).collect(Collectors.toList()));
        }

        @Override
        public <F, K> LeftJoinCollectOp<L, R, K> on(Function<L, K> leftKeyFunction, Function<R, K> rightKeyFunction) {

            ImmutableMap<K, R> rightIndexer = Maps.uniqueIndex(right, rightKeyFunction::apply);
            return new LeftJoinCollectOpImpl<>(left, rightIndexer, leftKeyFunction);
        }

        @Override
        public <F, K> LeftJoinMultiCollectOp<L, R, K> onMulti(Function<L, K> leftKeyFunction, Function<R, K> rightKeyFunction) {
            ImmutableListMultimap<K, R> rightIndexer = Multimaps.index(right, rightKeyFunction::apply);
            return new LeftJoinMultiCollectOpImpl<>(left, rightIndexer, leftKeyFunction);
        }
    }

    /**
     * 1:1
     *
     * @param <R> 　右类型
     * @param <K> 键类型
     */
    public static class LeftJoinCollectOpImpl<L, R, K> implements LeftJoinCollectOp<L, R, K> {

        private Stream<L> left;

        /**
         * 右表索引
         */
        private Map<K, R> right;

        /**
         * 左键转换器
         */
        Function<L, K> leftKeyFunction;

        LeftJoinCollectOpImpl(Stream<L> left, Map<K, R> right, Function<L, K> leftKeyFunction) {
            this.left = left;
            this.right = right;
            this.leftKeyFunction = leftKeyFunction;
        }

        @Override
        public LeftJoinCollectOp<L, R, K> with(BiConsumer<L, R> resultConsumer) {
            foreach(resultConsumer);
            return this;
        }

        @Override
        public void foreach(BiConsumer<L, R> resultConsumer) {
            left.forEach(l -> {
                resultConsumer.accept(l, right.get(leftKeyFunction.apply(l)));
            });
        }

        @Override
        public <NR> LeftJoinCollectOp<L, NR, K> mapR(Function<? super R, ? extends NR> mapper) {
            return new LeftJoinCollectOpImpl<L, NR, K>(left, MultimapExts.mapValue(right, mapper::apply), leftKeyFunction);
        }

        @Override
        public SmartStream<L> toL() {
            return new BaseSmartStream<L>(left);
        }

        @Override
        public SmartStream<R> toR() {
            return new BaseSmartStream<R>(right.values().stream());
        }

        @Override
        public SmartStream<Pair<L, R>> toPair() {
            return to(Pair::of);
        }

        @Override
        public <F> SmartStream<F> to(BiFunction<L, R, F> resultFunction) {
            return new BaseSmartStream<F>(left.map(l -> resultFunction.apply(l, right.get(leftKeyFunction.apply(l)))));
        }
    }

    /**
     * 1:n
     *
     * @param <R> 　右类型
     * @param <K> 键类型
     */
    public static class LeftJoinMultiCollectOpImpl<L, R, K> implements LeftJoinMultiCollectOp<L, R, K> {

        private Stream<L> left;

        private ListMultimap<K, R> right;

        Function<L, K> leftKeyFunction;

        LeftJoinMultiCollectOpImpl(Stream<L> left, ListMultimap<K, R> right, Function<L, K> leftKeyFunction) {
            this.left = left;
            this.right = right;
            this.leftKeyFunction = leftKeyFunction;
        }

        @Override
        public <NR> LeftJoinMultiCollectOp<L, NR, K> mapR(Function<? super R, ? extends NR> mapper) {
            return new LeftJoinMultiCollectOpImpl<L, NR, K>(left, MultimapExts.mapValue(right, mapper::apply), leftKeyFunction);
        }

        @Override
        public LeftJoinMultiCollectOp<L, R, K> with(BiConsumer<L, R> resultConsumer) {
            foreach(resultConsumer);
            return this;
        }

        @Override
        public LeftJoinMultiCollectOp<L, R, K> withMulti(BiConsumer<L, List<R>> resultConsumer) {
            foreachMulti(resultConsumer);
            return this;
        }

        @Override
        public void foreach(BiConsumer<L, R> resultConsumer) {
            left.forEach(l -> {
                right.get(leftKeyFunction.apply(l)).forEach(r -> resultConsumer.accept(l, r));
            });
        }

        @Override
        public void foreachMulti(BiConsumer<L, List<R>> resultConsumer) {
            left.forEach(l -> {
                resultConsumer.accept(l, right.get(leftKeyFunction.apply(l)));
            });
        }

        @Override
        public SmartStream<L> toL() {
            return new BaseSmartStream<L>(left);
        }

        @Override
        public SmartStream<R> toR() {
            return new BaseSmartStream<R>(right.values().stream());
        }

        @Override
        public SmartStream<Pair<L, R>> toPair() {
            return to(Pair::of);
        }

        @Override
        public SmartStream<Pair<L, List<R>>> toMultiPair() {
            return toMulti(Pair::of);
        }

        @Override
        public <F> SmartStream<F> to(BiFunction<L, R, F> resultFunction) {
            return new BaseSmartStream<F>(((ArrayList<F>) (left.map(l -> CollectionUtils.map(right.get(leftKeyFunction.apply(l)),
                    r -> resultFunction.apply(l, r)))
                    .reduce(ListUtils::union)
                    .orElse(new ArrayList<F>())
            )).stream()
            );
        }

        @Override
        public <F> SmartStream<F> toMulti(BiFunction<L, List<R>, F> resultFunction) {
            return new BaseSmartStream<F>(left.map(l -> resultFunction.apply(l, right.get(leftKeyFunction.apply(l)))));
        }

    }
    //endregion left join

    //region cross join

    /**
     * 全集关联
     * left-null
     * left-right
     * null-right
     *
     * @param right 右表
     * @param <R>   　右类型
     * @return 全联中间步骤
     */
    @Override
    public <R> CrossJoinOnOp<L, R> crossJoin(Collection<R> right) {
        return new CrossJoinOnOpImpl<>(left, right);
    }

    /**
     * @param <R> 　右类型
     */
    public static class CrossJoinOnOpImpl<L, R> implements CrossJoinOnOp<L, R> {

        private Stream<L> left;

        private Collection<R> right;

        CrossJoinOnOpImpl(Stream<L> left, Collection<R> right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public CrossJoinOnOp<L, R> filter(Predicate<? super R> predicate) {
            right = right.stream().filter(predicate).collect(Collectors.toList());
            return this;
        }

        @Override
        public <NR> CrossJoinOnOp<L, NR> map(Function<? super R, ? extends NR> mapper) {
            return new CrossJoinOnOpImpl<>(left, right.stream().map(mapper).collect(Collectors.toList()));
        }

        @Override
        public <F, K> CrossJoinCollectOp<L, R, K> on(Function<L, K> leftKeyFunction, Function<R, K> rightKeyFunction) {

            ImmutableMap<K, R> rightIndexer = Maps.uniqueIndex(right, rightKeyFunction::apply);
            return new CrossJoinCollectOp<>(left, rightIndexer, leftKeyFunction);
        }
    }

    /**
     * 1:1
     *
     * @param <R> 　右类型
     * @param <K> 键类型
     */
    public static class CrossJoinCollectOp<L, R, K> {

        private Stream<L> left;

        /**
         * 右表索引
         */
        private Map<K, R> right;

        /**
         * 左键转换器
         */
        Function<L, K> leftKeyFunction;

        CrossJoinCollectOp(Stream<L> left, Map<K, R> right, Function<L, K> leftKeyFunction) {
            this.left = left;
            this.right = right;
            this.leftKeyFunction = leftKeyFunction;
        }

        public CrossJoinCollectOp<L, R, K> with(BiConsumer<L, R> resultConsumer) {
            foreach(resultConsumer);
            return this;
        }

        public void foreach(BiConsumer<L, R> resultConsumer) {
            HashSet<K> rightKeys = new HashSet<K>(right.keySet());
            left.forEach(l -> {
                K k = leftKeyFunction.apply(l);
                resultConsumer.accept(l, right.get(k));
                rightKeys.remove(k);
            });
            rightKeys.forEach(k -> resultConsumer.accept(null, right.get(k)));
        }

        public <NR> CrossJoinCollectOp<L, NR, K> mapR(Function<? super R, ? extends NR> mapper) {
            return new CrossJoinCollectOp<L, NR, K>(left, MultimapExts.mapValue(right, mapper::apply), leftKeyFunction);
        }

        public SmartStream<L> toL() {
            return new BaseSmartStream<L>(left);
        }

        public SmartStream<R> toR() {
            return new BaseSmartStream<R>(right.values().stream());
        }

        public SmartStream<Pair<L, R>> toPair() {
            return to(Pair::of);
        }

        public <F> SmartStream<F> to(BiFunction<L, R, F> resultFunction) {
            HashSet<K> rightKeys = new HashSet<K>(right.keySet());
            return new BaseSmartStream<F>(Streams.concat(left.map(l -> {
                K k = leftKeyFunction.apply(l);
                rightKeys.remove(k);
                return resultFunction.apply(l, right.get(k));
            }), rightKeys.stream().map(k -> resultFunction.apply(null, right.get(k)))));
        }
    }
    //endregion cross join

}
