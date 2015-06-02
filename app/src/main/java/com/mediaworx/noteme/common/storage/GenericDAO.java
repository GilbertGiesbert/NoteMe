package com.mediaworx.noteme.common.storage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by martink on 04.03.2015.
 */
public interface GenericDAO<K extends Serializable, T>
{
    public T read(K id);
    public List<T> readAll();
    public T create();
    public T create(T value);
    public int update(T value);
    public int delete(T value);
    public int delete(K value);
}