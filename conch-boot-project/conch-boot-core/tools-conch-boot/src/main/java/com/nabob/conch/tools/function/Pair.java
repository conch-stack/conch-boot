package com.nabob.conch.tools.function;

public class Pair<T, V> {
    private T _first;
    private V _second;

    public Pair() {
    }

    public Pair(T first, V _second) {
        this._first = first;
        this._second = _second;
    }

    public T get_first() {
        return _first;
    }

    public void set_first(T _first) {
        this._first = _first;
    }

    public V get_second() {
        return _second;
    }

    public void set_second(V _second) {
        this._second = _second;
    }
}
