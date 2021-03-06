package cz.zcu.fav.sportevents.dao;

import cz.zcu.fav.sportevents.model.Contestant;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContestantDAO {

    @Autowired
    SessionFactory sessionFactory;

    public void save(Contestant contestant){
        sessionFactory.getCurrentSession().save(contestant);
    }

    public List<Contestant> getSoloContestants(int race_id){

        String sql = "SELECT c.* FROM contestant c LEFT JOIN team t ON c.team_id = t.id WHERE c.team_id IS NULL AND c.race_id = :race_id";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(sql).addEntity(Contestant.class).setParameter("race_id",race_id);
        return query.list();
    }

    public List<Contestant> getContestantsByRaceId(int race_id){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Contestant.class).add(Restrictions.eq("race.id",race_id));
        return criteria.list();
    }

    public List<Contestant> getListByUserId(int user_id, int race_id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Contestant.class)
                .add(Restrictions.eq("user.id",user_id))
                .add(Restrictions.eq("race.id",race_id));
        return criteria.list();
    }

    public Contestant getContestantById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Contestant.class)
                .add(Restrictions.eq("id",id));
        criteria.setMaxResults(1);
        return (Contestant) criteria.uniqueResult();
    }

    public void delete(Contestant contestant) {
        sessionFactory.getCurrentSession().delete(contestant);
    }

    public void update(Contestant contestant) {
        sessionFactory.getCurrentSession().update(contestant);
    }

    public List<Contestant> getContestantsByTeamId(int teamId){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Contestant.class)
                .add(Restrictions.eq("team.id",teamId));
        return criteria.list();
    }

}
