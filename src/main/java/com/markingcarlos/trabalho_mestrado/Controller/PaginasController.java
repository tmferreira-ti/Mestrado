package com.markingcarlos.trabalho_mestrado.Controller;

import com.markingcarlos.trabalho_mestrado.Repository.MoradorRepository;
import com.markingcarlos.trabalho_mestrado.Config.CustomLdapUserDetailsMapper.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginasController {

    @Autowired
    private MoradorRepository moradorRepository;

    @GetMapping("/admin")
    public String Index() {
        return "index";
    }

    @GetMapping("/")
    public String Reserva(Authentication authentication) {
        String email = null;

        // Verifica se é uma autenticação via OAuth2
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
            email = oAuth2Token.getPrincipal().getAttribute("email");

        } else if (authentication.getPrincipal() instanceof CustomUserDetails) {
            // Caso seja uma instância de CustomUserDetails (autenticação via LDAP com nosso mapeador)
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            email = userDetails.getEmail(); // Obtém o e-mail

        } else if (authentication.getPrincipal() instanceof UserDetails) {
            // Se for a instância padrão de UserDetails (LdapUserDetailsImpl)
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            email = userDetails.getUsername(); // Usa o username (LDAP usa sAMAccountName ou principal)
        }

        // Lógica de redirecionamento baseada no tipo de morador
        if (email != null && moradorRepository.existsByEmail(email)) {
            if (moradorRepository.findByEmail(email).getTipoMorador().equals("Gerente")) {
                return "index";
            } else {
                return "reserva";
            }
        }
        return "reserva"; // Se o usuário não for encontrado, retorna a página de reserva
    }
}
