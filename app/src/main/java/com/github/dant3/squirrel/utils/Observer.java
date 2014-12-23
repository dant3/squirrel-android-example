package com.github.dant3.squirrel.utils;

public interface Observer<T> {
    void handleUpdate(T updatedObject);
}
