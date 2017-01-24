package cz.zcu.fav.sportevents.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

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
            model.addObject("title", "Spring Security Hello World");
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

        private String getUser() {
            String userName = null;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                userName = ((UserDetails) principal).getUsername();
            } else {
                userName = principal.toString();
            }
            return userName;
        }

}