package com.interview.aquariux.trade.service;

import com.interview.aquariux.trade.repo.BaseRepo;

import java.io.Serializable;
import java.util.Collection;

public interface BaseService<T, ID extends Serializable, R extends BaseRepo<T, ID>> {
    T findById(ID id);

    T save(T t);

    void save(Collection<T> vos);

    T saveRaw(T vo) throws Exception;
}
