package com.example.mvitastyfood.data.repositores

import com.example.mvitastyfood.data.remote.MealApi
import javax.inject.Inject

class HomeRepository @Inject constructor(private val mealApi: MealApi) {

    suspend fun getRandomMeal()      = mealApi.getRandomMeal()
    suspend fun getOverPopular()     = mealApi.getOverPopular("Beef")
    suspend fun getHomeCategories()  = mealApi.getHomeCategories()

}