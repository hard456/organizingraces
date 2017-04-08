package cz.zcu.fav.sportevents.dao;

import cz.zcu.fav.sportevents.model.Race;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RaceDAO {

    @Autowired
    SessionFactory sessionFactory;

    public void save(Race race){
        sessionFactory.getCurrentSession().save(race);
    }

    public List<Race> listByUserId(int id){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Race.class)
                .add(Restrictions.eq("user.id",id))
                .addOrder(Order.desc("id"));
        return criteria.list();
    }

    public Race getRaceByUserId(String name){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Race.class)
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

    public List<Race> getRacesToRegistration(){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Race.class)
                .add(Restrictions.eq("evaluation",false))
                .addOrder(Order.desc("id"));
        return criteria.list();
    }

    public List<Race> getEvalutedRaces(){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Race.class)
                .add(Restrictions.eq("evaluation",true))
                .addOrder(Order.desc("id"));
        return criteria.list();
    }

    public void delete(Race race){
        sessionFactory.getCurrentSession().delete(race);
    }

    public void update(Race race) {
        sessionFactory.getCurrentSession().update(race);
    }
}
