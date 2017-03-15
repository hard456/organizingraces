package cz.zcu.fav.sportevents.dao;

import cz.zcu.fav.sportevents.model.ContestantSubcategory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContestantSubcategoryDAO {

    @Autowired
    SessionFactory sessionFactory;

    public List<ContestantSubcategory> getListByCategoryId(int category_id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ContestantSubcategory.class).add(Restrictions.eq("contestantCategory.id", category_id));
        return criteria.list();
    }

    public void save(ContestantSubcategory category){
        sessionFactory.getCurrentSession().save(category);
    }

}
