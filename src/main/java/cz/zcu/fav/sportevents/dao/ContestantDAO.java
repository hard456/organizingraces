package cz.zcu.fav.sportevents.dao;

import cz.zcu.fav.sportevents.model.Contestant;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

        String sql = "SELECT c.* FROM contestant c LEFT JOIN race_registration r ON r.contestant_id = c.id WHERE r.contestant_id IS NULL AND c.race_id = :race_id";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(sql).addEntity(Contestant.class).setParameter("race_id",race_id);
        return query.list();
    }

}
