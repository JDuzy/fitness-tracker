package com.fitness.tracker.person.repository

import com.fitness.tracker.person.model.Credentials
import com.fitness.tracker.person.model.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PersonRepository extends JpaRepository<Person, Long>{

    Optional<Person> findPersonByCredentials(Credentials credentials);
}
