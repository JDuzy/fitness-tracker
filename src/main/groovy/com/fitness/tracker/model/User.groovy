package com.fitness.tracker.model

import groovy.transform.CompileStatic

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.PrimaryKeyJoinColumn
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import java.time.LocalDate

@Entity
@Table(name = "user")
@CompileStatic
class User {

    @Id
    @SequenceGenerator(name = 'user_sequence', sequenceName = 'user_sequence', allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @Column( name = "id", updatable = false, nullable = false)
    Long id

    @NotBlank(message = "Enter a valid ")
    LocalDate dateOfBirth

    @NotBlank(message = "Enter a valid ")
    float weight

    @NotBlank(message = "Enter a valid ")
    String sex

    @NotBlank(message = "Enter a valid ")
    float height

    @NotBlank(message = "Enter a valid ")
    String physicalActivity

    @NotBlank(message = "Enter a valid ")
    float weightChangePerWeek

    @NotNull
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    Credentials credentials
}
