package com.upc.campusflow.Repository;

import com.upc.campusflow.Model.Carrera;

import java.util.Collection;

public interface CarreraRepository {
    Collection<Object> findAll();

    Carrera save(Carrera exist);

    <T> ScopedValue<T> findById(Long id);
}
