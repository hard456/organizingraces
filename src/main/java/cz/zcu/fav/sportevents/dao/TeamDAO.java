package cz.zcu.fav.sportevents.dao;

import cz.zcu.fav.sportevents.model.Team;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeamDAO {

    @Autowired
    SessionFactory sessionFactory;

    public void save(Team team){
        sessionFactory.getCurrentSession().save(team);
    }

    public List<Team> getTeamsByRaceId(int race_id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Team.class)
                .add(Restrictions.eq("race.id",race_id));
        return criteria.list();
    }

    public Team getTeamById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Team.class)
                .add(Restrictions.eq("id",id));
        criteria.setMaxResults(1);
        return (Team) criteria.uniqueResult();
    }

    public void delete(Team team) {
        sessionFactory.getCurrentSession().delete(team);
    }
}
