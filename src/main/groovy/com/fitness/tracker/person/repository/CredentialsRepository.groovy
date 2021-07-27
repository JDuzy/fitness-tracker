package com.fitness.tracker.person.repository

import com.fitness.tracker.person.model.Credentials
import com.fitness.tracker.person.model.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
interface CredentialsRepository extends JpaRepository<Credentials, Long> {

    Optional<Credentials> findCredentialsByEmail(String email)

    Optional<Person> findPersonById(Long credentialsId)
}