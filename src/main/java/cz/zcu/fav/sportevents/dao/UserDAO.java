package cz.zcu.fav.sportevents.dao;

import cz.zcu.fav.sportevents.model.User;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by HARD on 12.12.2016.
 */


public class UserDAO{

    @Autowired
    SessionFactory sessionFactory;

   /* public User load(final String id) {
        return hibernateTemplate.load(User.class,id);
    }

    public List<User> getAll() {
        return hibernateTemplate.loadAll(User.class);
    }
*/

    public User get(final String login) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(User.class)
                .add(Restrictions.eq("login",login));

        criteria.setMaxResults(1);
        User user = (User)criteria.uniqueResult();
        return user;
    }

    public void save(final User object) {
        Session session = sessionFactory.getCurrentSession();
        session.save(object);
    }

    public List findByEmail(String email){
        String sql = "SELECT * FROM User WHERE email = :email";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(sql);
        query.setParameter("email", email);
        List results = query.list();
        return results;
    }
/*
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
*/
}
