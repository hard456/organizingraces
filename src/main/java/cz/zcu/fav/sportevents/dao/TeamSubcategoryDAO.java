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
        Criteria criteria = session.createCriteria(TeamSubcategory.class).add(Restrictions.eq("categoryId", category_id));
        return criteria.list();
    }

    public void save(TeamSubcategory category){
        sessionFactory.getCurrentSession().save(category);
    }

}
