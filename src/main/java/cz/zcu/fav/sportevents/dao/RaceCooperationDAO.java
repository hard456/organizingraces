package cz.zcu.fav.sportevents.dao;

import cz.zcu.fav.sportevents.model.RaceCooperation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RaceCooperationDAO {

    @Autowired
    SessionFactory sessionFactory;

    public List<RaceCooperation> getCooperatorsByRaceId(int race_id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(RaceCooperation.class)
                .add(Restrictions.eq("race.id", race_id));
        criteria.setMaxResults(1);
        return criteria.list();
    }

    public RaceCooperation isUserRaceCooperator(int race_id, int user_id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(RaceCooperation.class)
                .add(Restrictions.eq("race.id", race_id))
                .add(Restrictions.eq("user.id", user_id));
        criteria.setMaxResults(1);
        return (RaceCooperation) criteria.uniqueResult();
    }

    public void save(RaceCooperation cooperation){
        sessionFactory.getCurrentSession().save(cooperation);
    }

}
