package util;

@FunctionalInterface
public interface CallbackWithParam<T> {
    void callback(T t);
}
