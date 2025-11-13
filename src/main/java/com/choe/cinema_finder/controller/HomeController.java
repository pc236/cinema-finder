package com.choe.cinema_finder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "Cinema Finder API is running. Try /api/movies/trending";
    }
}