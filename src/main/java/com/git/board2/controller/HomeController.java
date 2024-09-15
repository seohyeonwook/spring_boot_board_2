package com.git.board2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller // restcontroller 하면 안됨

public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
