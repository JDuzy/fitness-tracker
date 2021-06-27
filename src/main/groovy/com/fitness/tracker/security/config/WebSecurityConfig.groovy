package com.fitness.tracker.security.config

import com.fitness.tracker.model.User
import com.fitness.tracker.service.UserService
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
@CompileStatic
class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    Logger log = LoggerFactory.getLogger(WebSecurityConfig.class)
    @Autowired
    final UserService userService
    @Autowired
    final BCryptPasswordEncoder bCryptPasswordEncoder

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/h2-console/**", "/registration/**", "/").permitAll()
                .anyRequest()
                .authenticated()

        http
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/authenticated")
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
        return (UserDetailsService) { String email ->
            Optional<User> user = userService.findUserByEmail(email)
            if (user.isEmpty()){
                throw new UsernameNotFoundException("No username found with email: ${email}")
            }
            return user.get()
        }
    }

}
