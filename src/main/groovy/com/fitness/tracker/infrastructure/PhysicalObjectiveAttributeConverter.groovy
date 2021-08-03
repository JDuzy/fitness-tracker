package com.fitness.tracker.infrastructure

import com.fitness.tracker.person.model.PhysicalObjective

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class PhysicalObjectiveAttributeConverter implements AttributeConverter<PhysicalObjective, BigDecimal>{

    @Override
    BigDecimal convertToDatabaseColumn(PhysicalObjective attribute) {
        attribute == null ? null : attribute.addedCaloriesFromObjective
    }

    @Override
    PhysicalObjective convertToEntityAttribute(BigDecimal dbData) {
        dbData == null ? null : new PhysicalObjective(addedCaloriesFromObjective: dbData)
    }
}
