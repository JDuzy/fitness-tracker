package com.fitness.tracker.weight.model

import com.fitness.tracker.person.model.Registration

import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "weight_registration")
class WeightRegistration extends Registration{

    @NotNull
    BigDecimal weight

}