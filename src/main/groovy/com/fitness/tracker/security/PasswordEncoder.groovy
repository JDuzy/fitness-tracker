package com.fitness.tracker.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class PasswordEncoder {

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        new BCryptPasswordEncoder()
    }
}
