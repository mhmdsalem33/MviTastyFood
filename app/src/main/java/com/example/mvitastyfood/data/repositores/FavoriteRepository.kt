package com.example.mvitastyfood.data.repositores

import com.example.mvitastyfood.data.db.MealDatabase
import com.example.mvitastyfood.data.model.Meal
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val mealDatabase : MealDatabase) {

    private val db = mealDatabase.mealDao()
    suspend fun upsertMeal(meal: Meal) = db.upsertMeal(meal)
    suspend fun deleteMeal(meal: Meal) = db.deleteMeal(meal)
    val getSavedMeal = db.getSavedMeals()

}