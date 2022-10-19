package com.example.mvitastyfood.data.repositores

import com.example.mvitastyfood.data.remote.MealApi
import javax.inject.Inject

class SearchRepository @Inject  constructor(private val mealApi: MealApi) {
    suspend fun getSearchMeals(mealName : String) = mealApi.getSearchMeals(mealName)
}