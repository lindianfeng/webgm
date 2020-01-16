package com.kaka.webgm.controller;

import com.kaka.webgm.util.GmUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/")
public class IndexController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(path = {"", "index"})
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute("ip", GmUtils.getClientIP(request));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        model.addAttribute("username", username);
        return "index";
    }

    @RequestMapping(path = {"login"})
    public String login() {
        return "login";
    }
}
