package com.foxminded.university.config;

import com.foxminded.university.domain.models.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/**").hasAnyRole(Roles.STUDENT.name(), Roles.TEACHER.name(), Roles.DEPARTMENT_HEAD.name())
                .antMatchers(HttpMethod.POST, "/**").hasAnyRole(Roles.TEACHER.name(), Roles.DEPARTMENT_HEAD.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("student")
                        .password(passwordEncoder().encode("student"))
                        .roles(Roles.STUDENT.name())
                        .build(),
                User.builder()
                        .username("teacher")
                        .password(passwordEncoder().encode("teacher"))
                        .roles(Roles.TEACHER.name())
                        .build());

    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
