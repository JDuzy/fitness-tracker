package com.fitness.tracker.model

import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.springframework.format.annotation.DateTimeFormat

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
import javax.validation.constraints.Past
import javax.validation.constraints.Positive
import java.time.LocalDate
import java.time.Period

@Entity
@Table(name = "user")
@CompileStatic
@ToString
class User {

    @Id
    @SequenceGenerator(name = 'user_sequence', sequenceName = 'user_sequence', allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @Column( name = "id", updatable = false, nullable = false)
    Long id

    @NotNull(message = "Please enter a date of birth")
    @Past(message = "Please enter a valid date of birth")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth

    @Transient
    int age

    @NotBlank(message = "Enter your sex")
    String sex
    BigDecimal weight
    Integer height

    @NotBlank(message = "Please select your weekly amount of physical activity")
    String physicalActivity

    BigDecimal weightChangePerWeek

    @NotNull
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @Valid
    Credentials credentials = new Credentials(user: this)

    int getAge(){
        Period.between(this.dateOfBirth, LocalDate.now()).getYears()
    }


}
