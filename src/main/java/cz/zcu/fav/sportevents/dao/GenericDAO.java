package cz.zcu.fav.sportevents.dao;


import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T extends Serializable, K extends Serializable> {

    public T load(K id);

    public T get(K id);

    public List<T> getAll();

    public Serializable save(T object);

    public void saveOrUpdate(T object);

    public void delete(T object);

    public Long count();

    void flush();
}
