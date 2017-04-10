package cz.zcu.fav.sportevents.dao;

import cz.zcu.fav.sportevents.model.TeamSubcategory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeamSubcategoryDAO {

    @Autowired
    SessionFactory sessionFactory;

    public List<TeamSubcategory> getListByCategoryId(int category_id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(TeamSubcategory.class).add(Restrictions.eq("teamCategory.id", category_id));
        return criteria.list();
    }

    public void save(TeamSubcategory category){
        sessionFactory.getCurrentSession().save(category);
    }

    public TeamSubcategory getSubcategoryById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(TeamSubcategory.class)
                .add(Restrictions.eq("id", id));
        criteria.setMaxResults(1);
        return (TeamSubcategory) criteria.uniqueResult();
    }

    public TeamSubcategory getSubcategoryByName(String teamCategory, int raceTeamCategory) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(TeamSubcategory.class)
                .add(Restrictions.eq("name", teamCategory))
                .add(Restrictions.eq("teamCategory.id", raceTeamCategory));
        criteria.setMaxResults(1);
        return (TeamSubcategory) criteria.uniqueResult();
    }
}
