package cz.zcu.fav.sportevents.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "contestant_subcategory")
public class ContestantSubcategory implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "category_id", nullable = false)
    private int categoryId;

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
