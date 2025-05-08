package com.upc.campusflow.Repository;

import com.upc.campusflow.Model.GrupoForo;

import java.util.Collection;

public interface GrupoForoRepository {
    Collection<Object> findAll();

    GrupoForo save(GrupoForo grupoForo);

    <T> ScopedValue<T> findById(Long id);
}
