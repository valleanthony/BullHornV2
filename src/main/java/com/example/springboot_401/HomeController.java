package com.example.springboot_401;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

//    @RequestMapping("/")
//    public String index(){
//        return "index";
//    }
    @PostConstruct
    public void filltables(){
        Role admin = new Role();
        admin.setRole("ADMIN");

        Role user = new Role();
        user.setRole("USER");

        roleRepo.save(admin);
        roleRepo.save(user);

        Role adminRole = roleRepo.findByRole("ADMIN");
        Role userRole = roleRepo.findByRole("USER");
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/secure")
    public String securePage(Principal principal, Model model, User user){
        User myuser = ((CustomUserDetails)((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getUser();
        model.addAttribute("myuser",myuser);
        model.addAttribute("user", userService.getCurrentUser());

        return  "secure";
    }

    @RequestMapping("/admin")
    public String admin(Model model){
        model.addAttribute("user", userService.getCurrentUser());
        return "admin";
    }

    @RequestMapping(value = "/register", method=RequestMethod.GET)
    public String showRegistration(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }


    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String processRegistrationpage(@Valid @ModelAttribute ("user") User user,
                                          BindingResult result, Model
                                          model){
        model.addAttribute("user", user);
        if (result.hasErrors()){
            return "registration";
        }
        else{
//            userRepo.save(user);
            userService.saveUser(user);

            model.addAttribute("message","user account created");
        }
        return"main";
    }

}
