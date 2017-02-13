package cz.zcu.fav.sportevents.dao;

import cz.zcu.fav.sportevents.model.Race;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by HARD on 07.02.2017.
 */
public class RaceDAO {

    @Autowired
    SessionFactory sessionFactory;

    public void save(Race race){
        sessionFactory.getCurrentSession().save(race);
    }

    public List<Race> listByUserId(int id){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Race.class)
                .add(Restrictions.eq("userId",id));

        return criteria.list();
    }

    public Race getRaceByUserId(int userId, String name){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Race.class)
                .add(Restrictions.eq("userId", userId))
                .add(Restrictions.eq("name",name));
        criteria.setMaxResults(1);
        return (Race)criteria.uniqueResult();
    }

    public Race getRaceById(int id){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Race.class)
                .add(Restrictions.eq("id",id));
        criteria.setMaxResults(1);
        return (Race)criteria.uniqueResult();
    }

}
