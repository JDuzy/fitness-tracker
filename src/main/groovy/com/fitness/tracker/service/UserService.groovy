package com.fitness.tracker.service

import com.fitness.tracker.model.Credentials
import com.fitness.tracker.model.User
import com.fitness.tracker.repository.CredentialsRepository
import com.fitness.tracker.repository.UserRepository
import com.fitness.tracker.security.PasswordEncoder
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@CompileStatic
class UserService implements UserDetailsService{

    final UserRepository userRepository
    final CredentialsRepository credentialsRepository
    final BCryptPasswordEncoder bCryptPasswordEncoder
    final static String USER_NOT_FOUND_MSG = "user with email %s not found"

    @Autowired
    UserService(UserRepository userRepository, CredentialsRepository credentialsRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository
        this.credentialsRepository = credentialsRepository
        this.bCryptPasswordEncoder = bCryptPasswordEncoder
    }

    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        credentialsRepository.findCredentialsByEmail(email).orElseThrow( { new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)) })
    }

    boolean userExists(String email){
        Optional<Credentials> credentials = credentialsRepository.findCredentialsByEmail(email)
        credentials.isPresent()
    }

    @Transactional
    User save(User user){
        credentialsRepository.save(user.credentials)
        userRepository.save(user)
    }

    User register(User user){
        if (userExists(user.credentials.email)){
            throw new IllegalStateException("Email already taken")
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.credentials.password)
        user.credentials.password = encodedPassword
        save(user)
    }



}
