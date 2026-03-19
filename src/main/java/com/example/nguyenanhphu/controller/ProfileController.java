package com.example.nguyenanhphu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ProfileController {
    
    @GetMapping
    public String home() {
        return "redirect:/profile";
    }
    
    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("name", "Nguyễn Anh Phú");
        model.addAttribute("email", "nguyenanphu@example.com");
        model.addAttribute("phone", "0123456789");
        model.addAttribute("address", "Đà Nẵng, Việt Nam");
        model.addAttribute("bio", "Lập trình viên Spring Boot");
        model.addAttribute("skills", java.util.Arrays.asList("Java", "Spring Boot", "Docker", "MySQL"));
        return "profile";
    }
}