package lev.filippov.demomvc.controllers;

import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class IndexController {
    @GetMapping
    public String indexPage(Model model) {
        model.addAttribute("title", "Index page");
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
