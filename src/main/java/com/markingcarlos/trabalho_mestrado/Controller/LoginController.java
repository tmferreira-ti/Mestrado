package com.markingcarlos.trabalho_mestrado.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login.html"; // Retorna a página de login, que lida com LDAP e OAuth2
    }
}
