package com.github.dant3.squirrel.utils;

import org.apache.commons.lang3.Validate;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ObservableSupport<T> implements Observable<T> {
    private final T observed;
    private final Set<Observer<? super T>> observers = new CopyOnWriteArraySet<Observer<? super T>>();

    public static <T> ObservableSupport<T> of(T observed) {
        return new ObservableSupport<T>(observed);
    }

    @SuppressWarnings("unchecked")
    protected ObservableSupport() {
        this.observed = (T) this;
    }

    public ObservableSupport(T observed) {
        this.observed = Validate.notNull(observed);
    }

    @Override
    public void addObserver(Observer<? super T> observer) {
        observers.add(Validate.notNull(observer));
    }

    @Override
    public void removeObserver(Observer<? super T> observer) {
        observers.remove(Validate.notNull(observer));
    }

    @Override
    public void removeObservers() {
        observers.clear();
    }

    @Override
    public int observersCount() {
        return observers.size();
    }

    @Override
    public void notifyObservers() {
        notifyObservers(getObserved());
    }

    protected final void notifyObservers(T observed) {
        for (Observer<? super T> observer : observers) {
            observer.handleUpdate(observed);
        }
    }

    protected T getObserved() {
        return observed;
    }
}
