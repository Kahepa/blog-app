package com.example.springbootblogapplication.controllers;

import com.example.springbootblogapplication.models.Account;
import com.example.springbootblogapplication.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/register")
    public String getRegisterForm(Model model) {

        Account account = new Account();
        model.addAttribute("account", account);
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute Account account, Model model) {
        if (accountService.emailExists(account.getEmail())) {
            model.addAttribute("emailError", "Email already in use. Please use a different email.");
            return "register";
        }

        // save new user account
        accountService.save(account);
        return "redirect:/login";

    }
}
