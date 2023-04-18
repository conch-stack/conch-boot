package com.nabob.conch.tools.functions;

import com.nabob.conch.tools.utils.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Useful suppliers.
 **/
public class Suppliers {

    public static <T> Supplier<T> until(Predicate<T> predicate, Supplier<T>... suppliers) {
        return () -> {
            for (Supplier<T> supplier : suppliers) {
                T t = supplier.get();
                if (predicate.test(t)) {
                    return t;
                }
            }
            return null;
        };
    }

    public static <T> Supplier<T> firstNotNull(Supplier<T>... suppliers) {
        return () -> {
            for (Supplier<T> supplier : suppliers) {
                T t = supplier.get();
                if (t != null)
                    return t;
            }
            return null;
        };
    }

    /**
     * Creates a safe {@code Supplier},
     *
     * @param <T>               the type of the input of the supplier
     * @param throwableSupplier the supplier that may throw an exception
     * @return the supplier result or {@code null} if exception was thrown
     * @throws NullPointerException if {@code throwableSupplier} is null
     */
    public static <T> Supplier<T> uncheck(
            ThrowableSupplier<? extends T, Throwable> throwableSupplier) {
        Objects.requireNonNull(throwableSupplier);
        return () -> {
            try {
                return throwableSupplier.get();
            } catch (Throwable throwable) {
                ExceptionUtils.wrapAndThrow(throwable);
                //never
                return null;
            }
        };
    }

    /**
     * Creates a safe {@code Supplier},
     *
     * @param <T>               the type of the input of the supplier
     * @param throwableSupplier the supplier that may throw an exception
     * @return the supplier result or {@code null} if exception was thrown
     * @throws NullPointerException if {@code throwableSupplier} is null
     */
    public static <T> Supplier<T> safe(
            ThrowableSupplier<? extends T, Throwable> throwableSupplier) {
        return safe(throwableSupplier, null);
    }

    /**
     * Creates a safe {@code Supplier},
     *
     * @param <T>               the type of the input of the supplier
     * @param throwableSupplier the supplier that may throw an exception
     * @param resultIfFailed    the result which returned if exception was thrown
     * @return the supplier result or {@code resultIfFailed}
     * @throws NullPointerException if {@code throwableSupplier} is null
     * @see #safe(ThrowableSupplier)
     */
    public static <T> Supplier<T> safe(
            final ThrowableSupplier<? extends T, Throwable> throwableSupplier,
            final T resultIfFailed) {
        Objects.requireNonNull(throwableSupplier);
        return () -> {
            try {
                return throwableSupplier.get();
            } catch (Throwable throwable) {
                return resultIfFailed;
            }
        };
    }

    public static <T> Supplier<T> random(List<T> list) {
        return new ListSupplier<>(list);
    }

    private static class ListSupplier<T> implements Supplier<T> {

        private List<T> list;

        /**
         * Random object used by random method. This has to be not local to the
         * random method so as to not return the same value in the same millisecond.
         */
        private static final Random RANDOM = new Random(System.currentTimeMillis());

        public ListSupplier(List<T> list) {
            this.list = list;
        }

        @Override
        public T get() {
            return list.get(RANDOM.nextInt(list.size()));
        }
    }

    public static <T> Supplier<T> randomImmutable(Collection<T> collection) {
        return new ArraySupplier<>(collection.toArray());
    }

    public static <T> Supplier<T> randomImmutable(Iterable<T> iterable) {
        return randomImmutable(CollectionUtils.iterable2List(iterable));
    }

    private static class ArraySupplier<T> implements Supplier<T> {

        private Object[] array;

        /**
         * Random object used by random method. This has to be not local to the
         * random method so as to not return the same value in the same millisecond.
         */
        private static final Random RANDOM = new Random(System.currentTimeMillis());

        public ArraySupplier(Object[] array) {
            this.array = array;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T get() {
            return (T) array[RANDOM.nextInt(array.length)];
        }
    }

    public static <T> Supplier<T> sequential(Iterable<T> iterable) {
        return sequential(iterable.iterator());
    }

    public static <T> Supplier<T> sequential(Iterator<T> iterator) {
        return iterator::next;
    }

    public static <T> Supplier<T> take(Iterable<T> iterable) {
        return take(iterable.iterator());
    }

    public static <T> Supplier<T> take(Iterator<T> iterator) {
        return () -> {
            T next = iterator.next();
            iterator.remove();
            return next;
        };
    }
}
