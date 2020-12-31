package fr.devsCheese.bukkitTimer;

@FunctionalInterface
public interface Callback<T> {

    void call(T param);
}
