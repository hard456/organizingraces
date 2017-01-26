package cz.zcu.fav.sportevents.dao;

import cz.zcu.fav.sportevents.model.User;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by HARD on 12.12.2016.
 */
@Transactional(readOnly = false)
@Service
public class UserDAO{

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    SessionFactory sessionFactory;


    public User load(final String id) {
        return hibernateTemplate.load(User.class,id);
    }

    public User get(final String id) {
        return hibernateTemplate.get(User.class, id);
    }


    public List<User> getAll() {
        return hibernateTemplate.loadAll(User.class);
    }

    @Transactional(readOnly = false)
    public void save(final User object) {
        Session session = this.sessionFactory.openSession();
        session.save(object);
        session.flush();
    }

    @Transactional
    public List findByEmail(String email){
        Session session = this.sessionFactory.openSession();
        String sql = "SELECT * FROM User WHERE email = :email";
        SQLQuery query = session.createSQLQuery(sql);
        query.setParameter("email", email);
        List results = query.list();
        return results;
    }

    public void saveOrUpdate(final User object) {
        hibernateTemplate.saveOrUpdate(object);
    }

    public void delete(final User object) {
        hibernateTemplate.delete(object);
    }

    public Long count() {
        return new Long(hibernateTemplate.loadAll(User.class).size());
    }

    public void flush() {
        hibernateTemplate.flush();
    }

}
