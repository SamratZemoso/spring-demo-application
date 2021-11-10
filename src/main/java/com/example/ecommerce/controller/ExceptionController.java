package com.example.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExceptionController {

    @RequestMapping("/access-denied")
    public String accessDenied() {
        return "exception/access-denied";
    }

    @RequestMapping("/out-of-stock")
    public String outOfStockPage() {
        return "exception/out-of-stock";
    }

    @RequestMapping("/not-found")
    public String notFoundPage() {
        return "exception/not-found";
    }


}
