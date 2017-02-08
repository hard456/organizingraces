package cz.zcu.fav.sportevents.service;

import cz.zcu.fav.sportevents.dao.UserDAO;
import cz.zcu.fav.sportevents.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Transactional(readOnly = false)
    public void addUser(User user){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        userDAO.save(user);
    }

    @Transactional
    public boolean checkEmail(User user){
        if(userDAO.findByEmail(user.getEmail()) == null){
            return true;
        }
        return false;
    }

    @Transactional
    public boolean checkUserName(User user){
        if(userDAO.get(user.getLogin()) == null){
            return true;
        }
        return false;
    }

    @Transactional
    public User getUser(String login){
        return userDAO.get(login);
    }

}
