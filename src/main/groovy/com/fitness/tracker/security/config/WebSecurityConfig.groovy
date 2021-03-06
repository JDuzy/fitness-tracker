package com.fitness.tracker.security.config

import com.fitness.tracker.security.service.CredentialsService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import java.time.LocalDate

@Configuration
@EnableWebSecurity
@CompileStatic
class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    final CredentialsService credentialsService
    @Autowired
    final BCryptPasswordEncoder bCryptPasswordEncoder

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/h2-console/**", "/registration/**", "/style.css").permitAll()
                .anyRequest()
                .authenticated()

        http
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/food/registration?registrationDate=${LocalDate.now().toString()}")
                .permitAll()

        http
                .logout()
                .permitAll()

        http.headers().frameOptions().disable()

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(bCryptPasswordEncoder)
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        (UserDetailsService){ String email -> credentialsService.findCredentialsByEmail(email) }
    }

}
