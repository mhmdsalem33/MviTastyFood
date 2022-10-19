package com.example.mvitastyfood.data.repositores

import com.example.mvitastyfood.data.remote.MealApi
import javax.inject.Inject

class CategoriesFragmentRepository @Inject constructor(private val mealApi: MealApi) {
    suspend fun getCategories() = mealApi.getHomeCategories()
}