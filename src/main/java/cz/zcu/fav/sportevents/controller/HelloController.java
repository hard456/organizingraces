package cz.zcu.fav.sportevents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloController {

	@RequestMapping(value = "/",method = RequestMethod.GET)
	 public String home() {
		return "home";
	}
	@RequestMapping(value = "/guide",method = RequestMethod.GET)
	public String guide() {
		return "guide";
	}
}