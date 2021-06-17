package com.fitness.tracker.repository

import com.fitness.tracker.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository extends JpaRepository<User, Long>{

}
