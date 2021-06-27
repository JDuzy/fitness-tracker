package com.fitness.tracker.model.registration

import groovy.transform.CompileStatic
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.SequenceGenerator
import javax.validation.constraints.NotNull
import java.time.LocalDate

@MappedSuperclass
@CompileStatic
class Registration {

    @Id
    @SequenceGenerator(name = 'registration_sequence', sequenceName = 'registration_sequence', allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registration_sequence")
    @Column( name = "id", updatable = false, nullable = false)
    Long id

    @NotNull
    LocalDate registrationDate
}
