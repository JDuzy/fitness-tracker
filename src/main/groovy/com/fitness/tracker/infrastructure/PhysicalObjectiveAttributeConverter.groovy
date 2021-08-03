package com.fitness.tracker.infrastructure

import com.fitness.tracker.person.model.PhysicalObjective
import groovy.transform.CompileStatic

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
@CompileStatic
class PhysicalObjectiveAttributeConverter implements AttributeConverter<PhysicalObjective, BigDecimal>{

    @Override
    BigDecimal convertToDatabaseColumn(PhysicalObjective attribute) {
        attribute == null ? null : attribute.addedCaloriesFromObjective
    }

    @Override
    PhysicalObjective convertToEntityAttribute(BigDecimal dbData) {
        dbData == null ? null : new PhysicalObjective(dbData)
    }
}
