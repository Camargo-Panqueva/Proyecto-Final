package util;

/**
 * Represents a function that consumes a parameter.
 * <p>
 * This interface represents a function that consumes a parameter.
 * </p>
 *
 * @param <T> the type of the parameter.
 */
@FunctionalInterface
public interface ConsumerFunction<T> {

    void run(T parameter);
}
