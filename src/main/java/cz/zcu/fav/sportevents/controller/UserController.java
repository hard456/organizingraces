package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.model.User;
import cz.zcu.fav.sportevents.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

        @Autowired
        private UserService userService;

        @RequestMapping(value = "/",method = RequestMethod.GET)
        public String home() {
        return "home";
    }

        @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
        public ModelAndView loginPage() {
            ModelAndView model = new ModelAndView();
            model.setViewName("login");
            return model;
        }

        @RequestMapping(value = {"/registration"}, method = RequestMethod.GET)
        public ModelAndView registrationPage() {
            ModelAndView model = new ModelAndView();
            model.setViewName("registration");
            return model;
        }

        @RequestMapping(value = {"/accessdenied"}, method = RequestMethod.GET)
        public ModelAndView accessDeniedPage() {
            ModelAndView model = new ModelAndView();
            model.setViewName("accessdenied");
            return model;
        }

        @RequestMapping(value = {"/userpage"}, method = RequestMethod.GET)
        public ModelAndView userPage() {
            ModelAndView model = new ModelAndView();
            model.addObject("user", getUser());
            model.setViewName("user");
            return model;
        }

        @RequestMapping(value = "/logout", method = RequestMethod.GET)
        public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            return "redirect:/";
        }

        public String getLoginName() {
            String userName = null;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                userName = ((UserDetails) principal).getUsername();
            } else {
                userName = principal.toString();
            }
            return userName;
        }

        public User getUser(){
            return userService.getUser(getLoginName());
        }

        @RequestMapping(value = {"/addUser"}, method = RequestMethod.POST)
        public ModelAndView addUser(@RequestParam(value="passwordAgain") String passwordAgain, User user) {
            ModelAndView model = new ModelAndView();
            model.setViewName("reg_result");
            if(!regParametersSize(user)){
                model.addObject("message", "Wrong input size: Password(8-256), Login(3-32), Firstname(2-32), Lastname(2-32), email(6-32)");
                return model;
            }
            else if (!userService.checkUserName(user)) {
                model.addObject("message", "Name already used.");
                return model;
            } else if (!userService.checkEmail(user)) {
                model.addObject("message", "Email already used.");
                return model;
            } else if (!user.getPassword().equals(passwordAgain)) {
                model.addObject("message", "Passwords are not identical.");
                return model;
            } else {
                userService.addUser(user);
                model.addObject("message", "Registration OK");
                return model;
            }
        }

        private boolean regParametersSize(User user){
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

}