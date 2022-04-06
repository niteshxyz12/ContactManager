package com.example.smartcontactmanager.controller;


import com.example.smartcontactmanager.dao.UserRepository;
import com.example.smartcontactmanager.entities.User;
import com.example.smartcontactmanager.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class HomeController {


     @Autowired
     private BCryptPasswordEncoder passwordEncoder;
     @Autowired
     private UserRepository userRepository;
     @GetMapping("/")
     public String home(Model model){

         model.addAttribute("title","Home-smart contact manager");
         return "home";
     }

     @GetMapping("/about")
     public String about(Model model){

         model.addAttribute("title","About-smart contact manager");
         return "about";
     }

    @GetMapping("/signup")
    public String signup(Model model){

        model.addAttribute("title","Register-smart contact manager");
        model.addAttribute("user",new User());

        return "signup";
    }


    @PostMapping("/do_register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,@RequestParam(value="agreement",defaultValue = "false") boolean agreement,
                               Model model, HttpSession session) {

        System.out.println("HELLO WORLD " + user.getName());
        System.out.println("HELLO WORLD " + user.getName().length());

        try{
            if(!agreement){
                System.out.println("You have not agreed the terms and condition");
                throw new Exception("You have not agreed the terms and condition");
            }
            System.out.println(result1.getAllErrors());
            System.out.println(result1.getErrorCount());

            if(result1.hasErrors()){
                System.out.println("ERROR "+result1.toString());
                model.addAttribute("user",user);
                return "signup";
            }

        user.setRole("ROLE_USER");
        user.setEnabled(true);
        user.setImageurl("default.png");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = this.userRepository.save(user);

        model.addAttribute("user", new User());

            session.setAttribute("message", new Message("Successfully Registered !!" , "alert-success"));
            return "signup";
        }
        catch(Exception e){
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something Went Wrong !!"+ e.getMessage(), "alert-danger"));


            return "signup";
        }
    }

    @GetMapping("/signin")
    public String customLogin(Model model){
         model.addAttribute("title","Login-smart contact manager");
         return "login";
    }

    @GetMapping("/Logout")
    public String lgout(){
         return "home";
    }






}











