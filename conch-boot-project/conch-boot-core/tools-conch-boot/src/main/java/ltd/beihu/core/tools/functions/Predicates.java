package ltd.beihu.core.tools.functions;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class Predicates {

    public static <T> Predicate<T> from(Function<T, Boolean> function) {
        return function::apply;
    }

    /**
     * Creates a safe {@code Predicate},
     *
     * @param <T>                the type of the input to the predicate
     * @param throwablePredicate the predicate that may throw an exception
     * @return the predicate result or {@code null} if exception was thrown
     * @throws NullPointerException if {@code throwablePredicate} is null
     */
    public static <T> Predicate<T> uncheck(
            ThrowablePredicate<? super T, Throwable> throwablePredicate) {
        Objects.requireNonNull(throwablePredicate);
        return value -> {
            try {
                return throwablePredicate.test(value);
            } catch (Throwable throwable) {
                ExceptionUtils.wrapAndThrow(throwable);
                //never
                return false;
            }
        };
    }

    /**
     * Creates a safe {@code Predicate} that return true while failed,
     *
     * @param <T>                the type of the input of the predicate
     * @param throwablePredicate the predicate that may throw an exception
     * @return the predicate result or {@code null} if exception was thrown
     * @throws NullPointerException if {@code throwablePredicate} is null
     */
    public static <T> Predicate<T> failedAsTrue(
            ThrowablePredicate<? super T, Throwable> throwablePredicate) {
        return failedAs(throwablePredicate, true);
    }

    /**
     * Creates a safe {@code Predicate} that return false while failed,
     *
     * @param <T>                the type of the input of the predicate
     * @param throwablePredicate the predicate that may throw an exception
     * @return the predicate result or {@code null} if exception was thrown
     * @throws NullPointerException if {@code throwablePredicate} is null
     */
    public static <T> Predicate<T> failedAsFalse(
            ThrowablePredicate<? super T, Throwable> throwablePredicate) {
        return failedAs(throwablePredicate, false);
    }

    /**
     * Creates a safe {@code Predicate},
     *
     * @param <T>                the type of the input of the predicate
     * @param throwablePredicate the predicate that may throw an exception
     * @param resultIfFailed     the result which returned if exception was thrown
     * @return the predicate result or {@code resultIfFailed}
     * @throws NullPointerException if {@code throwablePredicate} is null
     * @see #failedAsFalse(ThrowablePredicate)
     */
    public static <T> Predicate<T> failedAs(
            final ThrowablePredicate<? super T, Throwable> throwablePredicate,
            final boolean resultIfFailed) {
        Objects.requireNonNull(throwablePredicate);
        return value -> {
            try {
                return throwablePredicate.test(value);
            } catch (Throwable throwable) {
                return resultIfFailed;
            }
        };
    }

    /**
     * Creates a safe {@code BiPredicate},
     *
     * @param <T,U>                the type of the input to the predicate
     * @param throwableBiPredicate the predicate that may throw an exception
     * @return the predicate result or {@code null} if exception was thrown
     * @throws NullPointerException if {@code throwableBiPredicate} is null
     */
    public static <T, U> BiPredicate<T, U> uncheck(
            ThrowableBiPredicate<? super T, ? super U, Throwable> throwableBiPredicate) {
        Objects.requireNonNull(throwableBiPredicate);
        return (t, u) -> {
            try {
                return throwableBiPredicate.test(t, u);
            } catch (Throwable throwable) {
                ExceptionUtils.wrapAndThrow(throwable);
                //never
                return false;
            }
        };
    }

    /**
     * Creates a safe {@code BiPredicate} that return true while failed,
     *
     * @param <T>                  the type of the first argument to the predicate
     * @param <U>                  the type of the second argument the predicate
     * @param throwableBiPredicate the predicate that may throw an exception
     * @return the predicate result or {@code null} if exception was thrown
     * @throws NullPointerException if {@code throwableBiPredicate} is null
     */
    public static <T, U> BiPredicate<T, U> failedAsTrue(
            ThrowableBiPredicate<? super T, ? super U, Throwable> throwableBiPredicate) {
        return failedAs(throwableBiPredicate, true);
    }

    /**
     * Creates a safe {@code BiPredicate} that return false while failed,
     *
     * @param <T>                  the type of the first argument to the predicate
     * @param <U>                  the type of the second argument the predicate
     * @param throwableBiPredicate the predicate that may throw an exception
     * @return the predicate result or {@code null} if exception was thrown
     * @throws NullPointerException if {@code throwableBiPredicate} is null
     */
    public static <T, U> BiPredicate<T, U> failedAsFalse(
            ThrowableBiPredicate<? super T, ? super U, Throwable> throwableBiPredicate) {
        return failedAs(throwableBiPredicate, false);
    }

    /**
     * Creates a safe {@code BiPredicate},
     *
     * @param <T>                  the type of the first argument to the predicate
     * @param <U>                  the type of the second argument the predicate
     * @param throwableBiPredicate the predicate that may throw an exception
     * @param resultIfFailed       the result which returned if exception was thrown
     * @return the predicate result or {@code resultIfFailed}
     * @throws NullPointerException if {@code throwableBiPredicate} is null
     * @see #failedAsFalse(ThrowableBiPredicate)
     */
    public static <T, U> BiPredicate<T, U> failedAs(
            final ThrowableBiPredicate<? super T, ? super U, Throwable> throwableBiPredicate,
            final boolean resultIfFailed) {
        Objects.requireNonNull(throwableBiPredicate);
        return (t, u) -> {
            try {
                return throwableBiPredicate.test(t, u);
            } catch (Throwable throwable) {
                return resultIfFailed;
            }
        };
    }

    public static <T> Predicate<T> indexed(BiPredicate<T, Integer> biPredicate) {
        return new Predicate<T>() {
            private int index = 0;

            @Override
            public boolean test(T t) {
                return biPredicate.test(t, index++);
            }

        };
    }
}
