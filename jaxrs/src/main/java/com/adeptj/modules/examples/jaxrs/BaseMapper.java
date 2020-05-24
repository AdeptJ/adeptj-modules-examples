package com.adeptj.modules.examples.jaxrs;

import java.util.List;

public interface BaseMapper<T> {

    T findById(Object id);

    List<T> findAll();

    void insert(T object);

    void deleteById(Object id);
}
