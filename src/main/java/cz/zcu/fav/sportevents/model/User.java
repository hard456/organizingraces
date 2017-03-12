package cz.zcu.fav.sportevents.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "login", unique = true, nullable = false, length = 32)
    private String login;

    @Column(name = "firstname", nullable = false, length = 32)
    private String firstname;

    @Column(name = "surname", nullable = false, length = 32)
    private String surname;

    @Column(name = "email", unique = true, nullable = false, length = 32)
    private String email;

    @Column(name = "password", nullable = false, length = 256)
    private String password;

    @Column(name = "phone", nullable = true, length = 32)
    private String phone;

    public void setLogin(String login) {
        this.login = login;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
