package util;

@FunctionalInterface
public interface ConsumerFunction<T> {

    void run(T parameter);
}
