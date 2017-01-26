package cz.zcu.fav.sportevents.service;

import cz.zcu.fav.sportevents.dao.UserDAO;
import cz.zcu.fav.sportevents.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegistrationService {

    @Autowired
    private UserDAO userDAO;

    @Transactional(readOnly = false)
    public void addUser(User user, String passwordAgain){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        userDAO.save(user);
    }

    public boolean checkSizeParameters(User user){
        if(user.getLogin().length() > 32 || user.getLogin().length() < 3){
            return false;
        }
        else if(user.getEmail().length() > 32 || user.getEmail().length() < 6){
            return false;
        }
        else if(user.getFirstname().length() > 32 || user.getFirstname().length() < 2){
            return false;
        }
        else if(user.getSurname().length() > 32 || user.getSurname().length() < 2){
            return false;
        }
        else if(user.getPassword().length() > 256 || user.getEmail().length() < 8){
            return false;
        }
        return true;
    }

    @Transactional
    public boolean checkEmail(User user){
        List list;
        list = userDAO.findByEmail(user.getEmail());
        if(list.size() == 0){
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

}
