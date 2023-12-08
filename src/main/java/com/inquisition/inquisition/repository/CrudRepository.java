package com.inquisition.inquisition.repository;

import java.util.List;

import org.jooq.Condition;

public interface CrudRepository<T> {

    Integer SUCCESS_CODE = 1;

    T insert(T t);

    T update(T t);

    T find(Integer id);

    List<T> findAll(Condition condition);

    Boolean delete(Integer id);
}
