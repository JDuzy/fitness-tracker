package com.fitness.tracker.model

import groovy.transform.CompileStatic
import groovy.transform.ToString

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.PrimaryKeyJoinColumn
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.Transient
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive
import java.time.LocalDate
import java.time.Period

@Entity
@Table(name = "user")
@CompileStatic
@ToString
class User {

    @Id
    @Column( name = "id", updatable = false, nullable = false)
    Long id

    //@NotBlank(message = "Enter a valid date")
    LocalDate dateOfBirth

    @Transient
    int age

    @NotBlank(message = "Enter a valid sex")
    String sex
    @Positive
    float weight
    float height

    @NotBlank(message = "Enter a valid physicalActivity")
    String physicalActivity

    float weightChangePerWeek

    @NotNull
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @Valid
    Credentials credentials = new Credentials()

    int getAge(){
        Period.between(this.dateOfBirth, LocalDate.now()).getYears()
    }


}
