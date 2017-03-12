package cz.zcu.fav.sportevents.dao;

import cz.zcu.fav.sportevents.model.ContestantCategory;
import cz.zcu.fav.sportevents.model.TeamCategory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeamCategoryDAO {

    @Autowired
    SessionFactory sessionFactory;

    public List<ContestantCategory> getDefaultCategories(){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(TeamCategory.class).add(Restrictions.eq("defaultCategory",true));
        return criteria.list();
    }

    public void save(TeamCategory teamCategory){
        sessionFactory.getCurrentSession().save(teamCategory);
    }

    public TeamCategory getCategoryById(int id){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(TeamCategory.class).add(Restrictions.eq("id",id));
        criteria.setMaxResults(1);
        return (TeamCategory) criteria.uniqueResult();
    }

}
