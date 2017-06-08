package cz.zcu.fav.sportevents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

    /**
     * Zobrazení chybové šablony s errorovým kódem
     * @param error_code kód, který se zobrazí v šabloně
     * @return
     */
    @RequestMapping(value = {"/error/{error_code}"}, method = RequestMethod.GET)
    public ModelAndView loginPage(@PathVariable int error_code) {
        ModelAndView model = new ModelAndView();
        model.setViewName("error/error_page");
        model.addObject("error",error_code);
        return model;
    }

}