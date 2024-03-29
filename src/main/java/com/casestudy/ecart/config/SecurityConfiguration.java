package com.casestudy.ecart.config;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.sql.DataSource;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void globalSecurityConfig(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username,password,active from users where username = ?")
                .authoritiesByUsernameQuery("select username,authorize from users where username = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .antMatchers("/users/addUsers").permitAll()
                .antMatchers("/users/getUsers").permitAll()
                .antMatchers("/api/items").permitAll()
                .antMatchers("/api/items/{id}").permitAll()
                .antMatchers("/api/category/{type}").permitAll()
                .antMatchers("/api/{cat}/{c1}/{c2}").permitAll()
                .antMatchers("/api/{c1}/{c2}").permitAll()
                .antMatchers("/api/{b}").permitAll()
                .antMatchers("/api/{cat}/{b}").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic();
        http.cors();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
