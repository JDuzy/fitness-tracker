package com.fitness.tracker.repository

import com.fitness.tracker.model.Credentials
import com.fitness.tracker.model.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PersonRepository extends JpaRepository<Person, Long>{

    Optional<Person> findUserByCredentials(Credentials credentials);
}
