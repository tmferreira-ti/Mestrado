package com.markingcarlos.trabalho_mestrado.Config;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import java.util.Collection;

public class CustomLdapUserDetailsMapper implements UserDetailsContextMapper {

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        // Obtém o atributo 'mail' do contexto LDAP
        String email = ctx.getStringAttribute("mail");
        System.out.println("Email: " + email);

        // Cria e retorna um CustomUserDetails com o email e o username
        return new CustomUserDetails(username, email, authorities);
    }

    @Override
    public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
        throw new UnsupportedOperationException("Somente leitura");
    }

    // Classe CustomUserDetails que extende User, com o campo adicional de email
    public static class CustomUserDetails extends org.springframework.security.core.userdetails.User {
        private final String email;

        public CustomUserDetails(String username, String email, Collection<? extends GrantedAuthority> authorities) {
            super(username, "", authorities); // Senha vazia, pois usamos autenticação externa (LDAP)
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    }
}
