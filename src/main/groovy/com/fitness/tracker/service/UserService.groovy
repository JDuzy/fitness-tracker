package com.fitness.tracker.service

import com.fitness.tracker.model.Credentials
import com.fitness.tracker.model.User
import com.fitness.tracker.repository.CredentialsRepository
import com.fitness.tracker.repository.UserRepository
import com.fitness.tracker.security.PasswordEncoder
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
        findUserByEmail(email).orElseThrow( { new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)) })
    }

    boolean userExists(User user){
        Optional<Credentials> credentials = credentialsRepository.findCredentialsByEmail(user.email)
        credentials.isPresent()
    }

    @Transactional
    User save(User user){
        credentialsRepository.save(user.credentials)
        userRepository.save(user)
    }

    User getPrincipal(){
        User user = null;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User){
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        }
        user
    }

    User register(User user){
        if (userExists(user)){
            throw new IllegalStateException("Email already taken")
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.password)
        user.password = encodedPassword
        save(user)
    }

    @Transactional
    Optional<User> findUserByEmail(String email){
        Optional<Credentials> credentials = credentialsRepository.findCredentialsByEmail(email)
        Optional<User> user = Optional.empty()
        credentials.ifPresent({ user = userRepository.findUserByCredentials(credentials.get())})
        user
    }


    void wasRegistratedValidly(User user, BindingResult bindingResult) {
        if (userExists(user)){
            bindingResult.addError(new FieldError("user", "credentials.email", "Email adress already in use"))
        }

        if (!user.passwordsMatch()){
            bindingResult.addError(new FieldError("user", "credentials.rpassword", "Passwords must match"))
        }
    }
}
