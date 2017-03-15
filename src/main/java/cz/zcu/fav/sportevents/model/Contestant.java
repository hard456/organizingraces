package cz.zcu.fav.sportevents.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "contestant")
public class Contestant implements Serializable{

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "firstname", nullable = false, length = 32)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 32)
    private String lastname;

    @Column(name = "email", nullable = false, length = 32)
    private String email;

    @ManyToOne
    @JoinColumn(name = "con_subcat_id")
    private ContestantSubcategory category;

    @Column(name = "paid", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean paid;

    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public ContestantSubcategory getCategory() {
        return category;
    }

    public void setCategory(ContestantSubcategory category) {
        this.category = category;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}
