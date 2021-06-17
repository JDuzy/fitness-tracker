package com.fitness.tracker.security.config

import com.fitness.tracker.service.UserService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
@CompileStatic
class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    final UserService userService
    @Autowired
    final BCryptPasswordEncoder bCryptPasswordEncoder

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
            .csrf().disable() //Delete this line?
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/registration/**").permitAll()
                .anyRequest()
                .authenticated()
        http.formLogin()
        http.headers().frameOptions().disable()
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider())
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider()
        provider.setPasswordEncoder(bCryptPasswordEncoder)
        provider.setUserDetailsService(userService)
        provider
    }
}
