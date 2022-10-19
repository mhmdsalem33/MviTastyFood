package com.example.mvitastyfood.data.db

import androidx.room.*
import com.example.mvitastyfood.data.model.Meal
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMeal(meal  : Meal)


    @Delete
    suspend fun deleteMeal(meal: Meal)


    @Query("SELECT * FROM mealInformation")
    fun getSavedMeals() : Flow<List<Meal>>

}