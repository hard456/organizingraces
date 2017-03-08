package cz.zcu.fav.sportevents.dao;

import cz.zcu.fav.sportevents.model.ContestantCategory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContestantCategoryDAO {

    @Autowired
    SessionFactory sessionFactory;

    public List<ContestantCategory> getDefaultCategories(){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ContestantCategory.class).add(Restrictions.eq("defaultCategory",true));
        return criteria.list();
    }

}
