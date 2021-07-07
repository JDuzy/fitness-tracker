package com.fitness.tracker.model

import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.hibernate.validator.constraints.Length
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

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
import javax.persistence.Transient
import javax.persistence.UniqueConstraint
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@Entity
@Table(name = "credentials", uniqueConstraints =  @UniqueConstraint(name = "user_email_unique", columnNames = "email"))
@CompileStatic
class Credentials {

    @Id
    @SequenceGenerator(name = 'credentials_sequence', sequenceName = 'credentials_sequence', allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credentials_sequence")
    @Column( name = "user_id", updatable = false, nullable = false)
    Long id

    @Pattern(regexp = '^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$',
             message = """The number of characters must be between 5 to 20.
The only special characters allowed are: .-_ 
These characters can't be used consecutively and must not be the first or last character""")

    @NotBlank(message = "Enter your username")
    String userName

    @NotBlank(message = "Enter your email")
    @Email(message = "Enter a valid email")
    String email

    @NotBlank(message = "Enter your password")
    @Length(min = 6, message = "Password must be at least 6 characters long")
    String password

    @NotBlank(message = "Enter your password")
    @Length(min = 6, message = "Password must be at least 6 characters long")
    @Transient
    String rpassword


    boolean passwordsMatch(){
        if(password != null && rpassword != null){
            return password == rpassword
        }
        false
    }

}
