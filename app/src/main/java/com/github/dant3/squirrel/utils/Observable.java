package com.github.dant3.squirrel.utils;

public interface Observable<T> {
    void addObserver(Observer<? super T> observer);
    void removeObserver(Observer<? super T> observer);
    void removeObservers();
    int observersCount();

    void notifyObservers();
}
