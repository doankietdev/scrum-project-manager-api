package com.doankietdev.identityservice.domain.repository;

public interface BaseRepository<T, Create, Update> {
    T findById(String id);

    T save(Create data);

    T updateById(String id, Update updateData);

    boolean deleteById(String id);

    boolean deletePermanentById(String id);

    boolean restoreById(String id);
}
