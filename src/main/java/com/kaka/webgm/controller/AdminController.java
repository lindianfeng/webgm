package com.kaka.webgm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {
    @RequestMapping(path = "/403")
    public String toGameServerListView(Model model) {
        return "error/403";
    }
}
