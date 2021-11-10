package com.example.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // home page
    @GetMapping({"/", "/home"})
    public String homePage() {
        return "index";
    }

}
