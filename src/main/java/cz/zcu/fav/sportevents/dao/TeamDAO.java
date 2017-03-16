package cz.zcu.fav.sportevents.dao;

import cz.zcu.fav.sportevents.model.Team;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TeamDAO {

    @Autowired
    SessionFactory sessionFactory;

    public void save(Team team){
        sessionFactory.getCurrentSession().save(team);
    }

}
