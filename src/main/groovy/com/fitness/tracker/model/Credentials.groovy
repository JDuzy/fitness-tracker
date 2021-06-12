package com.fitness.tracker.model

import groovy.transform.CompileStatic

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "credentials")
@CompileStatic
class Credentials {
    @Id
    @Column( name = "user_id", updatable = false, nullable = false)
    Long id

    @NotBlank(message = "Enter a valid ")
    String userName

    @Email(message = "Enter a valid email")
    String email

    @NotBlank(message = "Enter a valid ")
    String password

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    User user

}
