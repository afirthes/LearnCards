package ru.windwail.learncards.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${secure-pass}")
    private String securePass;

    @Value("${secure-user}")
    private String secureUser;

    @Value("${secure-pass2}")
    private String securePass2;

    @Value("${secure-user2}")
    private String secureUser2;

    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfiguration(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(secureUser)
                .password(passwordEncoder.encode(securePass))
                .roles("USER")
                .and()
                .withUser(secureUser2)
                .password(passwordEncoder.encode(securePass2))
                .roles("USER");

    }
}