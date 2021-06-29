package com.fitness.tracker.service

import com.fitness.tracker.model.Food
import com.fitness.tracker.repository.FoodRepository
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@CompileStatic
class FoodService {

    @Autowired
    final FoodRepository foodRepository

    List<Food> findAll(){
        foodRepository.findAll()
    }
}
