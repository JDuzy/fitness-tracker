package com.fitness.tracker.service

import com.fitness.tracker.model.Credentials
import com.fitness.tracker.repository.CredentialsRepository
import com.fitness.tracker.repository.UserRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@CompileStatic
class UserService {

    final UserRepository userRepository
    final CredentialsRepository credentialsRepository


    @Autowired
    UserService(UserRepository userRepository, CredentialsRepository credentialsRepository){
        this.userRepository = userRepository
        this.credentialsRepository = credentialsRepository
    }

    boolean userExists(String email){
        Optional<Credentials> credentials = credentialsRepository.findCredentialsByEmail(email)
        credentials.isPresent()
    }
}
