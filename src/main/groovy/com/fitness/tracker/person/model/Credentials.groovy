package com.fitness.tracker.person.model

import groovy.transform.CompileStatic
import org.hibernate.validator.constraints.Length
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

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
import javax.persistence.Transient
import javax.persistence.UniqueConstraint
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@Entity
@Table(name = "credentials", uniqueConstraints =  @UniqueConstraint(name = "email_unique", columnNames = "email"))
@CompileStatic
class Credentials implements UserDetails{

    @Id
    @SequenceGenerator(name = 'credentials_sequence', sequenceName = 'credentials_sequence', allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credentials_sequence")
    @Column( name = "person_id", updatable = false, nullable = false)
    Long id

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @Valid
    Person person = new Person()

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

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("USER")
        Collections.singletonList(authority)
    }

    @Override
    String getPassword() {
        return password
    }

    @Override
    String getUsername() {
        return email
    }

    String getEmail() {
        return email
    }

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
        return true
    }

    boolean passwordsMatch(){
        if(password != null && rpassword != null){
            return password == rpassword
        }
        false
    }

}
