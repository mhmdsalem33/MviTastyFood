package com.example.mvitastyfood.data.repositores

import com.example.mvitastyfood.data.remote.MealApi
import javax.inject.Inject

class MealActivityRepository @Inject constructor(private val mealApi: MealApi) {
    suspend fun getMealInformation(mealId :String) = mealApi.getMealInformation(mealId)
}