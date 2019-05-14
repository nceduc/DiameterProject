package com.company.backend.controller;

import com.company.backend.entity.User;
import com.company.backend.service.UserService;
import com.company.backend.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.ConnectException;

@Controller
public class RegistrationController {
    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserService userService;


    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        try {
            userService.save(userForm);
        }
        catch (ConnectException e){
            return "redirect:/registration?disabled";
        }

        return "redirect:/registration?success";
    }

}
