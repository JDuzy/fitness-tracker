package com.fitness.tracker.service

import com.fitness.tracker.model.Credentials
import com.fitness.tracker.model.Person
import com.fitness.tracker.repository.CredentialsRepository
import com.fitness.tracker.repository.PersonRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError

import javax.transaction.Transactional


@Service
@CompileStatic
class PersonService implements UserDetailsService{

    final PersonRepository userRepository
    final CredentialsRepository credentialsRepository
    final BCryptPasswordEncoder bCryptPasswordEncoder
    final static String USER_NOT_FOUND_MSG = "user with email %s not found"

    @Autowired
    PersonService(PersonRepository userRepository, CredentialsRepository credentialsRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository
        this.credentialsRepository = credentialsRepository
        this.bCryptPasswordEncoder = bCryptPasswordEncoder
    }

    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        findUserByEmail(email).orElseThrow( { new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)) })
    }

    boolean userExists(Person user){
        Optional<Credentials> credentials = credentialsRepository.findCredentialsByEmail(user.email)
        credentials.isPresent()
    }

    @Transactional
    Person save(Person user){
        credentialsRepository.save(user.credentials)
        userRepository.save(user)
    }

    Person getPrincipal(){
        Person user = null;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Person){
            user = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        }
        user
    }

    Person register(Person user){
        if (userExists(user)){
            throw new IllegalStateException("Email already taken")
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.password)
        user.password = encodedPassword
        save(user)
    }

    @Transactional
    Optional<Person> findUserByEmail(String email){
        Optional<Credentials> credentials = credentialsRepository.findCredentialsByEmail(email)
        Optional<Person> user = Optional.empty()
        credentials.ifPresent({ user = userRepository.findUserByCredentials(credentials.get())})
        user
    }


    void wasRegistratedValidly(Person user, BindingResult bindingResult) {
        if (userExists(user)){
            bindingResult.addError(new FieldError("user", "credentials.email", "Email adress already in use"))
        }

        if (!user.passwordsMatch()){
            bindingResult.addError(new FieldError("user", "credentials.rpassword", "Passwords must match"))
        }
    }
}
