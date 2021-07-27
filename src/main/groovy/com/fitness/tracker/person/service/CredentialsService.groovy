package com.fitness.tracker.person.service

import com.fitness.tracker.person.model.Credentials
import com.fitness.tracker.person.model.Person
import com.fitness.tracker.person.repository.CredentialsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError

import javax.transaction.Transactional

class CredentialsService implements UserDetailsService{

    @Autowired
    CredentialsRepository credentialsRepository

    @Autowired
    final BCryptPasswordEncoder bCryptPasswordEncoder


    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        findCredentialsByEmail(email)
    }

    Credentials findCredentialsByEmail(String email){
        credentialsRepository.findCredentialsByEmail(email).orElseThrow({ new UsernameNotFoundException("No user found with email: ${email}")})
    }

    Credentials getPrincipal(){
        Credentials credentials = null
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Credentials){
            credentials = (Credentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        }
        credentials
    }
    void wasRegisteredValidly(Credentials credentials, BindingResult bindingResult) {
        if (emailUsedExists(credentials)){
            bindingResult.addError(new FieldError("user", "credentials.email", "Email adress already in use"))
        }

        if (!credentials.passwordsMatch()){
            bindingResult.addError(new FieldError("user", "credentials.rpassword", "Passwords must match"))
        }
    }

    Boolean emailUsedExists(Credentials credentials){
        credentialsRepository.findCredentialsByEmail(credentials.email).isPresent()
    }

    @Transactional
    Credentials register(Credentials credentials){
        if (emailUsedExists(credentials)){
            throw new IllegalStateException("Email already taken")
        }
        String encodedPassword = bCryptPasswordEncoder.encode(credentials.password)
        credentials.password = encodedPassword
        credentials.person.setNutritionalObjective()
        credentialsRepository.save(credentials)
    }
}
