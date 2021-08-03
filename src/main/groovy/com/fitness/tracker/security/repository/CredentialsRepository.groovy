package com.fitness.tracker.security.repository

import com.fitness.tracker.security.Credentials
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
interface CredentialsRepository extends JpaRepository<Credentials, Long> {

    Optional<Credentials> findCredentialsByEmail(String email)
}