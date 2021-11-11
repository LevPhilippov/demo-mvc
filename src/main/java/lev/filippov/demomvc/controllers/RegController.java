package lev.filippov.demomvc.controllers;

import lev.filippov.demomvc.exceptions.PasswordOrUsernameException;
import lev.filippov.demomvc.exceptions.UserAlreadyExistException;
import lev.filippov.demomvc.models.PrivateDetails;
import lev.filippov.demomvc.models.User;
import lev.filippov.demomvc.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping(path = "/registration")
public class RegController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegForm() {
        return "registration/reg_form";
    }

    @PostMapping
    public String showDetailsForm(Model model, HttpServletRequest request) {
        if(request.getParameter("roboCheck") == null) {
            throw new RuntimeException("You are fucking robot!");
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if(username.length()<1 || password.length()<1) {
            try{
                throw new PasswordOrUsernameException("Username or password are too short!");
            } catch (PasswordOrUsernameException e) {
                model.addAttribute("nullError",e);
                return "registration/reg_form";
            }
        }

        User user = null;
        try {
            user = userService.saveNewUser(username, password);
        } catch (UserAlreadyExistException e) {
            model.addAttribute("username",e.getUser().getUsername());
            model.addAttribute("existError",e);
            return "registration/reg_form";
        }

        System.out.println("User is " + user);
        model.addAttribute("userId", user.getId());
        model.addAttribute("details", new PrivateDetails());
        return "registration/details_form";
    }

    @PostMapping(path = "/proceed")
    public String proceed (@ModelAttribute PrivateDetails details, @RequestParam("userId") Long userId) throws Throwable {
        System.out.println("userId " + userId);
        System.out.println(details);
        userService.saveDetails(userId,details);
        return "/login";

    }

}
