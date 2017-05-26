package cz.zcu.fav.sportevents.dao;

import cz.zcu.fav.sportevents.model.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO{

    @Autowired
    SessionFactory sessionFactory;

    public User getUserByLogin(String login) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(User.class)
                .add(Restrictions.eq("login",login));

        criteria.setMaxResults(1);
        return (User)criteria.uniqueResult();
    }

    public void save(final User object) {
        Session session = sessionFactory.getCurrentSession();
        session.save(object);
    }

    public User findByEmail(String email){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(User.class)
                .add(Restrictions.eq("email",email));

        criteria.setMaxResults(1);
        User user = (User)criteria.uniqueResult();
        return user;
    }

    public User findByPhone(String phone){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(User.class)
                .add(Restrictions.eq("phone",phone));

        criteria.setMaxResults(1);
        User user = (User)criteria.uniqueResult();
        return user;
    }

}
