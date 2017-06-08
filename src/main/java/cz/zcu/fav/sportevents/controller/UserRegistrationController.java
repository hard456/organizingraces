package cz.zcu.fav.sportevents.controller;

import cz.zcu.fav.sportevents.form.UserRegistrationForm;
import cz.zcu.fav.sportevents.model.User;
import cz.zcu.fav.sportevents.service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    /**
     * Zobrazení šablony pro registraci uživatele.
     * @return
     */
    @RequestMapping(value = {"/registration"}, method = RequestMethod.GET)
    public ModelAndView registrationPage() {
        ModelAndView model = new ModelAndView();
        model.getModelMap().addAttribute("userRegistrationForm", new UserRegistrationForm());
        model.setViewName("user/registration");
        return model;
    }

    /**
     * Přidání uživatele do systému.
     * @param request
     * @param userRegistrationForm data z formuláře
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = {"/addUser"}, method = RequestMethod.POST)
    public ModelAndView addUser(HttpServletRequest request, @ModelAttribute("userRegistrationForm") UserRegistrationForm userRegistrationForm, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        model.setViewName("user/registration");
        if (bindingResult.hasErrors()) {
            model.addObject("message", "Data are invalid:<br> Password (8 - 256 length)<br>Login (3-32 length)<br>Firstname (2 - 32 length)<br>Lastname (2 - 32 length)<br>email (6 - 32 length)");
            return model;
        }
        if (!validUserParameters(request)) {
            model.addObject("message", "Data are invalid:<br> Password (8 - 256 length)<br>Login (3 - 32 length)<br>Firstname (2 - 32 length)<br>Lastname (2 - 32 length)<br>email (6 - 32 length)");
            return model;
        }

        User user = new User();

        user.setEmail(HtmlUtils.htmlEscape(userRegistrationForm.getUser().getEmail(), "UTF-8"));
        user.setFirstname(HtmlUtils.htmlEscape(userRegistrationForm.getUser().getFirstname(), "UTF-8"));
        user.setLogin(HtmlUtils.htmlEscape(userRegistrationForm.getUser().getLogin(), "UTF-8"));
        user.setPassword(HtmlUtils.htmlEscape(userRegistrationForm.getUser().getPassword(), "UTF-8"));
        user.setPhone(HtmlUtils.htmlEscape(userRegistrationForm.getUser().getPhone().replaceAll("\\s+",""), "UTF-8"));
        user.setSurname(HtmlUtils.htmlEscape(userRegistrationForm.getUser().getSurname(), "UTF-8"));
        String passwordAgain = HtmlUtils.htmlEscape(userRegistrationForm.getPasswordAgain(), "UTF-8");

        if (!validUserParametersSize(user)) {
            model.addObject("message", "Data are invalid:<br> Password (8 - 256 length)<br>Login (3 - 32 length)<br>Firstname (2 - 32 length)<br>Lastname (2 - 32 length)<br>email (6 - 32 length)");
            return model;
        }
        if (userService.checkUserName(user)) {
            model.addObject("message", "Name already used");
            return model;
        }
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            model.addObject("message", "Email is in invalid format");
            return model;
        }
        if (!user.getPhone().isEmpty()) {
            if (!user.getPhone().matches("^(\\+420)? ?[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}$")) {
                model.addObject("message", "Invalid phone format (only allowed):<br> +420123456789<br>+420 123 456 789<br>123 456 789<br>123456789");
                return model;
            }
            if (userService.checkPhone(user)) {
                model.addObject("message", "User with phone number already exists");
                return model;
            }
        }
        if (userService.checkEmail(user)) {
            model.addObject("message", "Email already used");
            return model;
        }
        if (!user.getPassword().equals(passwordAgain)) {
            model.addObject("message", "Passwords are not identical");
            return model;
        }
        userService.addUser(user);

        model.setViewName("user/login");
        model.addObject("after_reg",true);
        return model;

    }

    /**
     * Valiadce dat uživatele získané z formuláře.
     * @param user data uživatele
     * @return false - chyba, true - ok
     */
    private boolean validUserParametersSize(User user) {
        if (user.getLogin().length() > 32 || user.getLogin().length() < 3) {
            return false;
        } else if (user.getEmail().length() > 32 || user.getEmail().length() < 6) {
            return false;
        } else if (user.getFirstname().length() > 32 || user.getFirstname().length() < 3) {
            return false;
        } else if (user.getSurname().length() > 32 || user.getSurname().length() < 3) {
            return false;
        } else if (user.getPassword().length() > 256 || user.getPassword().length() < 8) {
            return false;
        }
        return true;
    }

    /**
     * Ověření existence potřebných atributů požadavku pro registraci uživatele.
     * @param request
     * @return false - chyba, true - ok
     */
    private boolean validUserParameters(HttpServletRequest request) {
        if (!request.getParameterMap().containsKey("user.login")) {
            return false;
        }
        if (!request.getParameterMap().containsKey("user.firstname")) {
            return false;
        }
        if (!request.getParameterMap().containsKey("user.surname")) {
            return false;
        }
        if (!request.getParameterMap().containsKey("user.phone")) {
            return false;
        }
        if (!request.getParameterMap().containsKey("user.email")) {
            return false;
        }
        if (!request.getParameterMap().containsKey("user.password")) {
            return false;
        }
        if (!request.getParameterMap().containsKey("passwordAgain")) {
            return false;
        }
        return true;
    }

}
