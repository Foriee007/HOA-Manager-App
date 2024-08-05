package org.cenchev.hoamanagerapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/resident")
public class ResidentController {

    @GetMapping("/search")
    public String showSearchForm() {
        return "resident/search";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "/resident/dashboard";
    }

}

