package cz.zcu.fav.sportevents.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthenticationController {

    /**
     * Zobrazení šablony pro přihlášení.
     * @return
     */
    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView loginPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("user/login");
        return model;
    }

    /**
     * Zobrazení šablony přístup zamítnut.
     * @return
     */
    @RequestMapping(value = {"/accessdenied"}, method = RequestMethod.GET)
    public ModelAndView accessDeniedPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("user/accessdenied");
        return model;
    }

    /**
     * Odhlášení uživatele a přesměrování na úvodní stránku.
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

}
