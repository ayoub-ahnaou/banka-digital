package com.digital.banka.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EntryController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String root(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated())
            return "redirect:/login";

        String role = authentication.getAuthorities().toString();
        if (!role.contains("CLIENT") || !role.contains("ADMIN") || !role.contains("BANK_AGENT"))
            return "redirect:/dashboard";

        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        if (model == null)
            return "redirect:/login";

        model.addAttribute("username", auth.getName());
        model.addAttribute("roles", auth.getAuthorities());

        return "dashboard";
    }
}
