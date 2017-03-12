package cz.zcu.fav.sportevents.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "contestant_subcategory")
public class ContestantSubcategory implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Size(max = 20)
    @Column(name = "name", length = 32)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ContestantCategory contestantCategory;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContestantCategory getContestantCategory() {
        return contestantCategory;
    }

    public void setContestantCategory(ContestantCategory contestantCategory) {
        this.contestantCategory = contestantCategory;
    }

}
