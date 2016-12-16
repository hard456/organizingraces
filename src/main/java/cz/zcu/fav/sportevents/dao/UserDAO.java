package cz.zcu.fav.sportevents.dao;

import cz.zcu.fav.sportevents.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HARD on 12.12.2016.
 */
@Transactional
@Service
public class UserDAO implements GenericDAO<User,String> {

    @Autowired
    private HibernateTemplate hibernateTemplate;
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public User load(final String id) {
        return hibernateTemplate.load(User.class,id);
    }

    @Override
    public User get(final String id) {
        return hibernateTemplate.get(User.class, id);
    }

    @Override
    public List<User> getAll() {
        return hibernateTemplate.loadAll(User.class);
    }

    @Override
    public Serializable save(final User object) {
        return hibernateTemplate.save(object);
    }

    @Override
    public void saveOrUpdate(final User object) {
        hibernateTemplate.saveOrUpdate(object);
    }

    @Override
    public void delete(final User object) {
        hibernateTemplate.delete(object);
    }

    @Override
    public Long count() {
        return new Long(hibernateTemplate.loadAll(User.class).size());
    }

    @Override
    public void flush() {
        hibernateTemplate.flush();
    }

}
