package com.fitness.tracker.model.registration

import com.fitness.tracker.model.Food

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.PrimaryKeyJoinColumn
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "food_registration")
class FoodRegistration extends Registration{

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    Food food
}
