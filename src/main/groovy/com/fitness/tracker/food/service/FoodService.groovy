package com.fitness.tracker.food.service

import com.fitness.tracker.food.model.Food
import com.fitness.tracker.food.repository.FoodRepository
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
