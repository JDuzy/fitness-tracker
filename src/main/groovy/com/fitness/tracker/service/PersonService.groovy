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

    final PersonRepository personRepository
    final CredentialsRepository credentialsRepository
    final BCryptPasswordEncoder bCryptPasswordEncoder
    final static String PERSON_NOT_FOUND_MSG = "user with email %s not found"

    @Autowired
    PersonService(PersonRepository personRepository, CredentialsRepository credentialsRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.personRepository = personRepository
        this.credentialsRepository = credentialsRepository
        this.bCryptPasswordEncoder = bCryptPasswordEncoder
    }

    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        findPersonByEmail(email).orElseThrow( { new UsernameNotFoundException(String.format(PERSON_NOT_FOUND_MSG, email)) })
    }

    boolean personExists(Person person){
        Optional<Credentials> credentials = credentialsRepository.findCredentialsByEmail(person.email)
        credentials.isPresent()
    }

    @Transactional
    Person save(Person person){
        credentialsRepository.save(person.credentials)  //Already done with cascadetype.all?
        personRepository.save(person)
    }

    Person getPrincipal(){
        Person person = null;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Person){
            person = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        }
        person
    }

    Person register(Person person){
        if (personExists(person)){
            throw new IllegalStateException("Email already taken")
        }
        String encodedPassword = bCryptPasswordEncoder.encode(person.password)
        person.password = encodedPassword
        person.setNutritionalObjective()
        save(person)
    }

    @Transactional
    Optional<Person> findPersonByEmail(String email){
        Optional<Credentials> credentials = credentialsRepository.findCredentialsByEmail(email)
        Optional<Person> person = Optional.empty()
        credentials.ifPresent({ person = personRepository.findPersonByCredentials(credentials.get())})
        person
    }


    void wasRegistratedValidly(Person person, BindingResult bindingResult) {
        if (personExists(person)){
            bindingResult.addError(new FieldError("user", "credentials.email", "Email adress already in use"))
        }

        if (!person.passwordsMatch()){
            bindingResult.addError(new FieldError("user", "credentials.rpassword", "Passwords must match"))
        }
    }
}
