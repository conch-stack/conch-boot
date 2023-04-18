package ltd.beihu.core.tools.stream;

import java.util.*;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.*;

public abstract class Streams {


    //region copy from Arrays---------------------------------------------------

    /**
     * Returns a {@link Spliterator} covering all of the specified array.
     * <p>
     * <p>The spliterator reports {@link Spliterator#SIZED},
     * {@link Spliterator#SUBSIZED}, {@link Spliterator#ORDERED}, and
     * {@link Spliterator#IMMUTABLE}.
     *
     * @param <T>   type of elements
     * @param array the array, assumed to be unmodified during use
     * @return a spliterator for the array elements
     * @since 1.8
     */
    public static <T> Spliterator<T> spliterator(T[] array) {
        return Spliterators.spliterator(array,
                Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    /**
     * Returns a {@link Spliterator} covering the specified range of the
     * specified array.
     * <p>
     * <p>The spliterator reports {@link Spliterator#SIZED},
     * {@link Spliterator#SUBSIZED}, {@link Spliterator#ORDERED}, and
     * {@link Spliterator#IMMUTABLE}.
     *
     * @param <T>            type of elements
     * @param array          the array, assumed to be unmodified during use
     * @param startInclusive the first index to cover, inclusive
     * @param endExclusive   index immediately past the last index to cover
     * @return a spliterator for the array elements
     * @throws ArrayIndexOutOfBoundsException if {@code startInclusive} is
     *                                        negative, {@code endExclusive} is less than
     *                                        {@code startInclusive}, or {@code endExclusive} is greater than
     *                                        the array size
     * @since 1.8
     */
    public static <T> Spliterator<T> spliterator(T[] array, int startInclusive, int endExclusive) {
        return Spliterators.spliterator(array, startInclusive, endExclusive,
                Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    /**
     * Returns a {@link Spliterator.OfInt} covering all of the specified array.
     * <p>
     * <p>The spliterator reports {@link Spliterator#SIZED},
     * {@link Spliterator#SUBSIZED}, {@link Spliterator#ORDERED}, and
     * {@link Spliterator#IMMUTABLE}.
     *
     * @param array the array, assumed to be unmodified during use
     * @return a spliterator for the array elements
     * @since 1.8
     */
    public static Spliterator.OfInt spliterator(int[] array) {
        return Spliterators.spliterator(array,
                Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    /**
     * Returns a {@link Spliterator.OfInt} covering the specified range of the
     * specified array.
     * <p>
     * <p>The spliterator reports {@link Spliterator#SIZED},
     * {@link Spliterator#SUBSIZED}, {@link Spliterator#ORDERED}, and
     * {@link Spliterator#IMMUTABLE}.
     *
     * @param array          the array, assumed to be unmodified during use
     * @param startInclusive the first index to cover, inclusive
     * @param endExclusive   index immediately past the last index to cover
     * @return a spliterator for the array elements
     * @throws ArrayIndexOutOfBoundsException if {@code startInclusive} is
     *                                        negative, {@code endExclusive} is less than
     *                                        {@code startInclusive}, or {@code endExclusive} is greater than
     *                                        the array size
     * @since 1.8
     */
    public static Spliterator.OfInt spliterator(int[] array, int startInclusive, int endExclusive) {
        return Spliterators.spliterator(array, startInclusive, endExclusive,
                Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    /**
     * Returns a {@link Spliterator.OfLong} covering all of the specified array.
     * <p>
     * <p>The spliterator reports {@link Spliterator#SIZED},
     * {@link Spliterator#SUBSIZED}, {@link Spliterator#ORDERED}, and
     * {@link Spliterator#IMMUTABLE}.
     *
     * @param array the array, assumed to be unmodified during use
     * @return the spliterator for the array elements
     * @since 1.8
     */
    public static Spliterator.OfLong spliterator(long[] array) {
        return Spliterators.spliterator(array,
                Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    /**
     * Returns a {@link Spliterator.OfLong} covering the specified range of the
     * specified array.
     * <p>
     * <p>The spliterator reports {@link Spliterator#SIZED},
     * {@link Spliterator#SUBSIZED}, {@link Spliterator#ORDERED}, and
     * {@link Spliterator#IMMUTABLE}.
     *
     * @param array          the array, assumed to be unmodified during use
     * @param startInclusive the first index to cover, inclusive
     * @param endExclusive   index immediately past the last index to cover
     * @return a spliterator for the array elements
     * @throws ArrayIndexOutOfBoundsException if {@code startInclusive} is
     *                                        negative, {@code endExclusive} is less than
     *                                        {@code startInclusive}, or {@code endExclusive} is greater than
     *                                        the array size
     * @since 1.8
     */
    public static Spliterator.OfLong spliterator(long[] array, int startInclusive, int endExclusive) {
        return Spliterators.spliterator(array, startInclusive, endExclusive,
                Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    /**
     * Returns a {@link Spliterator.OfDouble} covering all of the specified
     * array.
     * <p>
     * <p>The spliterator reports {@link Spliterator#SIZED},
     * {@link Spliterator#SUBSIZED}, {@link Spliterator#ORDERED}, and
     * {@link Spliterator#IMMUTABLE}.
     *
     * @param array the array, assumed to be unmodified during use
     * @return a spliterator for the array elements
     * @since 1.8
     */
    public static Spliterator.OfDouble spliterator(double[] array) {
        return Spliterators.spliterator(array,
                Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    /**
     * Returns a {@link Spliterator.OfDouble} covering the specified range of
     * the specified array.
     * <p>
     * <p>The spliterator reports {@link Spliterator#SIZED},
     * {@link Spliterator#SUBSIZED}, {@link Spliterator#ORDERED}, and
     * {@link Spliterator#IMMUTABLE}.
     *
     * @param array          the array, assumed to be unmodified during use
     * @param startInclusive the first index to cover, inclusive
     * @param endExclusive   index immediately past the last index to cover
     * @return a spliterator for the array elements
     * @throws ArrayIndexOutOfBoundsException if {@code startInclusive} is
     *                                        negative, {@code endExclusive} is less than
     *                                        {@code startInclusive}, or {@code endExclusive} is greater than
     *                                        the array size
     * @since 1.8
     */
    public static Spliterator.OfDouble spliterator(double[] array, int startInclusive, int endExclusive) {
        return Spliterators.spliterator(array, startInclusive, endExclusive,
                Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    /**
     * Returns a sequential {@link Stream} with the specified array as its
     * source.
     *
     * @param <T>      the type of the stream elements
     * @param elements the elements to be passed to stream
     * @return a {@code Stream} for the array
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    public static <T> Stream<T> of(final T... elements) {
        Objects.requireNonNull(elements);
        if (elements.length == 0) {
            return Stream.<T>empty();
        }
        return of(elements, 0, elements.length);
    }

    /**
     * Returns a sequential {@link Stream} with the specified range of the
     * specified array as its source.
     *
     * @param <T>            the type of the array elements
     * @param array          the array, assumed to be unmodified during use
     * @param startInclusive the first index to cover, inclusive
     * @param endExclusive   index immediately past the last index to cover
     * @return a {@code Stream} for the array range
     * @throws ArrayIndexOutOfBoundsException if {@code startInclusive} is
     *                                        negative, {@code endExclusive} is less than
     *                                        {@code startInclusive}, or {@code endExclusive} is greater than
     *                                        the array size
     * @since 1.8
     */
    public static <T> Stream<T> of(T[] array, int startInclusive, int endExclusive) {
        return StreamSupport.stream(spliterator(array, startInclusive, endExclusive), false);
    }

    /**
     * Returns a sequential {@link IntStream} with the specified array as its
     * source.
     *
     * @param array the array, assumed to be unmodified during use
     * @return an {@code IntStream} for the array
     * @since 1.8
     */
    public static IntStream of(int... array) {
        Objects.requireNonNull(array);
        if (array.length == 0) {
            return IntStream.empty();
        }
        return of(array, 0, array.length);
    }

    /**
     * Returns a sequential {@link IntStream} with the specified range of the
     * specified array as its source.
     *
     * @param array          the array, assumed to be unmodified during use
     * @param startInclusive the first index to cover, inclusive
     * @param endExclusive   index immediately past the last index to cover
     * @return an {@code IntStream} for the array range
     * @throws ArrayIndexOutOfBoundsException if {@code startInclusive} is
     *                                        negative, {@code endExclusive} is less than
     *                                        {@code startInclusive}, or {@code endExclusive} is greater than
     *                                        the array size
     * @since 1.8
     */
    public static IntStream of(int[] array, int startInclusive, int endExclusive) {
        return StreamSupport.intStream(spliterator(array, startInclusive, endExclusive), false);
    }

    /**
     * Returns a sequential {@link LongStream} with the specified array as its
     * source.
     *
     * @param array the array, assumed to be unmodified during use
     * @return a {@code LongStream} for the array
     * @since 1.8
     */
    public static LongStream of(long... array) {
        Objects.requireNonNull(array);
        if (array.length == 0) {
            return LongStream.empty();
        }
        return of(array, 0, array.length);
    }

    /**
     * Returns a sequential {@link LongStream} with the specified range of the
     * specified array as its source.
     *
     * @param array          the array, assumed to be unmodified during use
     * @param startInclusive the first index to cover, inclusive
     * @param endExclusive   index immediately past the last index to cover
     * @return a {@code LongStream} for the array range
     * @throws ArrayIndexOutOfBoundsException if {@code startInclusive} is
     *                                        negative, {@code endExclusive} is less than
     *                                        {@code startInclusive}, or {@code endExclusive} is greater than
     *                                        the array size
     * @since 1.8
     */
    public static LongStream of(long[] array, int startInclusive, int endExclusive) {
        return StreamSupport.longStream(spliterator(array, startInclusive, endExclusive), false);
    }

    /**
     * Returns a sequential {@link DoubleStream} with the specified array as its
     * source.
     *
     * @param array the array, assumed to be unmodified during use
     * @return a {@code DoubleStream} for the array
     * @since 1.8
     */
    public static DoubleStream of(double... array) {
        Objects.requireNonNull(array);
        if (array.length == 0) {
            return DoubleStream.empty();
        }
        return of(array, 0, array.length);
    }

    /**
     * Returns a sequential {@link DoubleStream} with the specified range of the
     * specified array as its source.
     *
     * @param array          the array, assumed to be unmodified during use
     * @param startInclusive the first index to cover, inclusive
     * @param endExclusive   index immediately past the last index to cover
     * @return a {@code DoubleStream} for the array range
     * @throws ArrayIndexOutOfBoundsException if {@code startInclusive} is
     *                                        negative, {@code endExclusive} is less than
     *                                        {@code startInclusive}, or {@code endExclusive} is greater than
     *                                        the array size
     * @since 1.8
     */
    public static DoubleStream of(double[] array, int startInclusive, int endExclusive) {
        return StreamSupport.doubleStream(spliterator(array, startInclusive, endExclusive), false);
    }
    //endregion copy from Arrays---------------------------------------------------

    /**
     * Returns an empty stream.
     *
     * @param <T> the type of the stream elements
     * @return the new empty stream
     */
    public static <T> Stream<T> empty() {
        return Stream.empty();
    }

    /**
     * Creates a {@code Stream} from {@code Map} entries.
     *
     * @param <K> the type of map keys
     * @param <V> the type of map values
     * @param map the map with elements to be passed to stream
     * @return the new stream
     * @throws NullPointerException if {@code map} is null
     */
    public static <K, V> Stream<Map.Entry<K, V>> of(Map<K, V> map) {
        Objects.requireNonNull(map);
        return map.entrySet().stream();
    }


    /**
     * Creates a {@code Stream} from any class that implements {@code Iterator} interface.
     *
     * @param <T>      the type of the stream elements
     * @param iterator the iterator with elements to be passed to stream
     * @return the new stream
     * @throws NullPointerException if {@code iterator} is null
     */
    public static <T> Stream<T> of(Iterator<? extends T> iterator) {
        Objects.requireNonNull(iterator);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
    }

    /**
     * Creates a {@code Stream} from any class that implements {@code Iterable} interface.
     *
     * @param <T>      the type of the stream elements
     * @param iterable the {@code Iterable} with elements to be passed to stream
     * @return the new stream
     * @throws NullPointerException if {@code iterable} is null
     */
    public static <T> Stream<T> of(Iterable<T> iterable) {
        Objects.requireNonNull(iterable);
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * If specified element is null, returns an empty {@code Stream},
     * otherwise returns a {@code Stream} containing a single element.
     *
     * @param <T>     the type of the stream element
     * @param element the element to be passed to stream if it is non-null
     * @return the new stream
     * @since 1.1.5
     */
    @SuppressWarnings("unchecked")
    public static <T> Stream<T> ofNullable(T element) {
        return (element == null) ? Stream.<T>empty() : Stream.of(element);
    }

    /**
     * If specified array is null, returns an empty {@code Stream},
     * otherwise returns a {@code Stream} containing elements of this array.
     *
     * @param <T>   the type of the stream elements
     * @param array the array whose elements to be passed to stream
     * @return the new stream
     * @since 1.1.9
     */
    public static <T> Stream<T> ofNullable(final T[] array) {
        return (array == null) ? Stream.<T>empty() : Stream.of(array);
    }

    /**
     * If specified map is null, returns an empty {@code Stream},
     * otherwise returns a {@code Stream} containing entries of this map.
     *
     * @param <K> the type of map keys
     * @param <V> the type of map values
     * @param map the map with elements to be passed to stream
     * @return the new stream
     * @since 1.1.9
     */
    public static <K, V> Stream<Map.Entry<K, V>> ofNullable(Map<K, V> map) {
        return (map == null) ? Stream.<Map.Entry<K, V>>empty() : of(map);
    }

    /**
     * If specified iterator is null, returns an empty {@code Stream},
     * otherwise returns a {@code Stream} containing entries of this iterator.
     *
     * @param <T>      the type of the stream elements
     * @param iterator the iterator with elements to be passed to stream
     * @return the new stream
     * @since 1.1.9
     */
    public static <T> Stream<T> ofNullable(Iterator<? extends T> iterator) {
        return (iterator == null) ? Stream.<T>empty() : of(iterator);
    }

    /**
     * If specified iterable is null, returns an empty {@code Stream},
     * otherwise returns a {@code Stream} containing elements of this iterable.
     *
     * @param <T>      the type of the stream elements
     * @param iterable the {@code Iterable} with elements to be passed to stream
     * @return the new stream
     * @since 1.1.5
     */
    public static <T> Stream<T> ofNullable(Iterable<T> iterable) {
        return (iterable == null) ? Stream.<T>empty() : of(iterable);
    }

    /**
     * Creates a {@code Stream<Integer>} from not closed range
     * (from {@code from} inclusive to {@code to} exclusive and incremental step {@code 1}).
     *
     * @param from the initial value (inclusive)
     * @param to   the upper bound (exclusive)
     * @return the new stream
     * @see IntStream#range(int, int)
     */
    public static Stream<Integer> range(final int from, final int to) {
        return IntStream.range(from, to).boxed();
    }

    /**
     * Creates a {@code Stream<Long>} from not closed range
     * (from {@code from} inclusive to {@code to} exclusive and incremental step {@code 1}).
     *
     * @param from the initial value (inclusive)
     * @param to   the upper bound (exclusive)
     * @return the new stream
     */
    public static Stream<Long> range(final long from, final long to) {
        return LongStream.range(from, to).boxed();
    }

    /**
     * Creates a {@code Stream<Integer>} from closed range
     * (from {@code from} inclusive to {@code to} inclusive and incremental step {@code 1}).
     *
     * @param from the initial value (inclusive)
     * @param to   the upper bound (inclusive)
     * @return the new stream
     * @see IntStream#rangeClosed(int, int)
     */
    public static Stream<Integer> rangeClosed(final int from, final int to) {
        return IntStream.rangeClosed(from, to).boxed();
    }

    /**
     * Creates a {@code Stream<Long>} from closed range
     * (from {@code from} inclusive to {@code to} inclusive and incremental step {@code 1}).
     *
     * @param from the initial value (inclusive)
     * @param to   the upper bound (inclusive)
     * @return the new stream
     */
    public static Stream<Long> rangeClosed(final long from, final long to) {
        return LongStream.rangeClosed(from, to).boxed();
    }

    /**
     * Creates a {@code Stream} from a queue.
     *
     * @param <T>   the type of the stream elements
     * @param queue the queue with elements to be poll to stream
     * @return the new stream
     * @throws NullPointerException if {@code iterator} is null
     */
    public static <T> Stream<T> poll(Queue<? extends T> queue) {
        return takeNotNull(queue::poll);
    }

    /**
     * Creates a {@code Stream} from a stack.
     *
     * @param <T>   the type of the stream elements
     * @param stack the stack with elements to be pop to stream
     * @return the new stream
     * @throws NullPointerException if {@code iterator} is null
     */
    public static <T> Stream<T> pop(Stack<? extends T> stack) {
        return takeNotNull(stack::pop);
    }

    /**
     * Creates a {@code Stream} by elements that generated by {@code Supplier}.
     *
     * @param <T>      the type of the stream elements
     * @param supplier the {@code Supplier} of generated elements
     * @return the new stream
     * @throws NullPointerException if {@code supplier} is null
     */
    public static <T> Stream<T> generate(final Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        return Stream.generate(supplier);
    }

    /**
     * Creates a {@code Stream} by elements that generated by {@code Supplier}.
     *
     * @param <T>      the type of the stream elements
     * @param supplier the {@code Supplier} of generated elements
     * @return the new stream
     * @throws NullPointerException if {@code supplier} is null
     */
    public static <T> Stream<T> takeNotNull(final Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        return of(new SupplierIterator<>(supplier));
    }

    private static class SupplierIterator<T> implements Iterator<T> {
        private final Supplier<T> supplier;
        private T buff;

        public SupplierIterator(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public boolean hasNext() {
            return buff != null || (buff = supplier.get()) != null;
        }

        @Override
        public T next() {
            T t = buff;
            buff = null;
            return t;
        }
    }

    /**
     * Creates a {@code Stream} by iterative application {@code UnaryOperator} function
     * to an initial element {@code seed}. Produces {@code Stream} consisting of
     * {@code seed}, {@code op(seed)}, {@code op(op(seed))}, etc.
     * <p>
     * <p>Example:
     * <pre>
     * seed: 1
     * op: (a) -&gt; a + 5
     * result: [1, 6, 11, 16, ...]
     * </pre>
     *
     * @param <T>  the type of the stream elements
     * @param seed the initial value
     * @param op   operator to produce new element by previous one
     * @return the new stream
     * @throws NullPointerException if {@code op} is null
     */
    public static <T> Stream<T> iterate(final T seed, final UnaryOperator<T> op) {
        Objects.requireNonNull(op);
        return Stream.iterate(seed, op);
    }

    /**
     * Creates a lazily concatenated stream whose elements are all the
     * elements of the first stream followed by all the elements of the
     * second stream.  The resulting stream is ordered if both
     * of the input streams are ordered, and parallel if either of the input
     * streams is parallel.  When the resulting stream is closed, the close
     * handlers for both input streams are invoked.
     * <p>
     * <p>
     * <p>Example:
     * <pre>
     * stream 1: [1, 2, 3, 4]
     * stream 2: [5, 6]
     * result:   [1, 2, 3, 4, 5, 6]
     * </pre>
     *
     * @param <T>     The type of stream elements
     * @param stream1 the first stream
     * @param stream2 the second stream
     * @return the concatenation of the two input streams
     * @implNote Use caution when constructing streams from repeated concatenation.
     * Accessing an element of a deeply concatenated stream can result in deep
     * call chains, or even {@code StackOverflowException}.
     */
    public static <T> Stream<T> concat(Stream<? extends T> stream1, Stream<? extends T> stream2) {
        return Stream.concat(stream1, stream2);
    }

    /**
     * Concatenates two iterators to a stream.
     * <p>
     * <p>Example:
     * <pre>
     * iterator 1: [1, 2, 3, 4]
     * iterator 2: [5, 6]
     * result:     [1, 2, 3, 4, 5, 6]
     * </pre>
     *
     * @param <T>       The type of iterator elements
     * @param iterator1 the first iterator
     * @param iterator2 the second iterator
     * @return the new stream
     * @throws NullPointerException if {@code iterator1} or {@code iterator2} is null
     * @since 1.1.9
     */
    public static <T> Stream<T> concat(Iterator<? extends T> iterator1, Iterator<? extends T> iterator2) {
        Objects.requireNonNull(iterator1);
        Objects.requireNonNull(iterator2);
        return concat(of(iterator1), of(iterator2));
    }

}
