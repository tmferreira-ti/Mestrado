package com.markingcarlos.trabalho_mestrado.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/login**", "/oauth2/authorization/**").permitAll()  // Permitir acesso à página de login
                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()  // Permitir acesso a arquivos estáticos
                                .anyRequest().authenticated()) // Protege todas as outras páginas
                .formLogin(form -> form
                        .loginPage("/login")  // Página de login
                        .permitAll()
                        .defaultSuccessUrl("/", true))  // Redireciona para "/" após o login bem-sucedido
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")  // Página de login para OAuth2
                        .defaultSuccessUrl("/", true));  // Redireciona para "/" após login OAuth2

        return http.build();
    }

    @Bean
    public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
        // Configura o ActiveDirectoryLdapAuthenticationProvider
        ActiveDirectoryLdapAuthenticationProvider provider =
                new ActiveDirectoryLdapAuthenticationProvider(
                        "unesp.local",           // Domínio do AD (Samba4)
                        "ldaps://unesp.local:636/"); // URL do servidor AD (usando LDAPS)

        // Define o mapeador customizado para associar o e-mail ao UserDetails
        provider.setUserDetailsContextMapper(customLdapUserDetailsMapper());

        return provider;
    }

    @Bean
    public UserDetailsContextMapper customLdapUserDetailsMapper() {
        return new CustomLdapUserDetailsMapper();
    }
}
