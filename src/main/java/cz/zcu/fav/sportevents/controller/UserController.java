package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.form.UserRegistrationForm;
import cz.zcu.fav.sportevents.model.User;
import cz.zcu.fav.sportevents.service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "home";
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView loginPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("user/login");
        return model;
    }

    @RequestMapping(value = {"/registration"}, method = RequestMethod.GET)
    public ModelAndView registrationPage() {
        ModelAndView model = new ModelAndView();
        model.getModelMap().addAttribute("userRegistrationForm", new UserRegistrationForm());
        model.setViewName("user/registration");
        return model;
    }

    @RequestMapping(value = {"/accessdenied"}, method = RequestMethod.GET)
    public ModelAndView accessDeniedPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("user/accessdenied");
        return model;
    }

    @RequestMapping(value = {"/userpage"}, method = RequestMethod.GET)
    public ModelAndView userPage() {
        ModelAndView model = new ModelAndView();
        model.addObject("user", getUser());
        model.setViewName("user/user");
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
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    public User getUser() {
        return userService.getUser(getLoginName());
    }

    @RequestMapping(value = {"/addUser"}, method = RequestMethod.POST)
    public ModelAndView addUser(HttpServletRequest request, @ModelAttribute("userRegistrationForm") UserRegistrationForm userRegistrationForm, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        model.setViewName("user/reg_result");
        if(bindingResult.hasErrors()){
            model.addObject("invalid", true);
            model.addObject("message", "Data are invalid:<br> Password (8-256 length)<br>Login (3-32 length)<br>Firstname (2-32 length)<br>Lastname (2-32 length)<br>email (6-32 length)");
            return model;
        }
        if(!validUserParameters(request)){
            model.addObject("invalid", true);
            model.addObject("message", "Data are invalid:<br> Password (8-256 length)<br>Login (3-32 length)<br>Firstname (2-32 length)<br>Lastname (2-32 length)<br>email (6-32 length)");
            return model;
        }

        User user = new User();

        user.setEmail(HtmlUtils.htmlEscape(userRegistrationForm.getUser().getEmail(), "UTF-8"));
        user.setFirstname(HtmlUtils.htmlEscape(userRegistrationForm.getUser().getFirstname(), "UTF-8"));
        user.setLogin(HtmlUtils.htmlEscape(userRegistrationForm.getUser().getLogin(), "UTF-8"));
        user.setPassword(HtmlUtils.htmlEscape(userRegistrationForm.getUser().getPassword(), "UTF-8"));
        user.setPhone(HtmlUtils.htmlEscape(userRegistrationForm.getUser().getPhone(), "UTF-8"));
        user.setSurname(HtmlUtils.htmlEscape(userRegistrationForm.getUser().getSurname(), "UTF-8"));
        String passwordAgain = HtmlUtils.htmlEscape(userRegistrationForm.getPasswordAgain(), "UTF-8");

        if (!regParametersSize(user)) {
            model.addObject("invalid", true);
            model.addObject("message", "Data are invalid:<br> Password (8-256 length)<br>Login (3-32 length)<br>Firstname (2-32 length)<br>Lastname (2-32 length)<br>email (6-32 length)");
            return model;
        } else if (userService.checkUserName(user)) {
            model.addObject("invalid", true);
            model.addObject("message", "Name already used");
            return model;
        } else if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            model.addObject("invalid", true);
            model.addObject("message", "Email is in invalid format");
            return model;
        } else if (!user.getPhone().isEmpty()) {
            if (!user.getPhone().matches("^(\\+420)? ?[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}$")) {
                model.addObject("invalid", true);
                model.addObject("message", "Invalid phone format (only allowed):<br> +420123456789<br>+420 123 456 789<br>123 456 789<br>123456789");
                return model;
            }
            if (userService.checkPhone(user)) {
                model.addObject("invalid", true);
                model.addObject("message", "User with phone number already exists");
                return model;
            }
        } else if (userService.checkEmail(user)) {
            model.addObject("invalid", true);
            model.addObject("message", "Email already used");
            return model;
        } else if (!user.getPassword().equals(passwordAgain)) {
            model.addObject("invalid", true);
            model.addObject("message", "Passwords are not identical");
            return model;
        }
        userService.addUser(user);
        model.addObject("invalid", false);
        model.addObject("message", "You have successfully registered");
        return model;

    }

    private boolean regParametersSize(User user) {
        if (user.getLogin().length() > 32 || user.getLogin().length() < 3) {
            return false;
        } else if (user.getEmail().length() > 32 || user.getEmail().length() < 6) {
            return false;
        } else if (user.getFirstname().length() > 32 || user.getFirstname().length() < 3) {
            return false;
        } else if (user.getSurname().length() > 32 || user.getSurname().length() < 3) {
            return false;
        } else if (user.getPassword().length() > 256 || user.getEmail().length() < 8) {
            return false;
        }
        return true;
    }

    private boolean validUserParameters(HttpServletRequest request){
        if(!request.getParameterMap().containsKey("user.login")){
            return false;
        }
        if(!request.getParameterMap().containsKey("user.firstname")){
            return false;
        }
        if(!request.getParameterMap().containsKey("user.surname")){
            return false;
        }
        if(!request.getParameterMap().containsKey("user.phone")){
            return false;
        }
        if(!request.getParameterMap().containsKey("user.email")){
            return false;
        }
        if(!request.getParameterMap().containsKey("user.password")){
            return false;
        }
        if(!request.getParameterMap().containsKey("passwordAgain")){
            return false;
        }
        return true;
    }

}