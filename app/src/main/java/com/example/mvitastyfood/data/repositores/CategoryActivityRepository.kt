package com.example.mvitastyfood.data.repositores

import com.example.mvitastyfood.data.remote.MealApi
import com.example.mvitastyfood.ui.intent.CategoryIntent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import javax.inject.Inject

class CategoryActivityRepository @Inject constructor(private val mealApi: MealApi) {

    suspend fun getCategory(categoryName : String) = mealApi.getCategory(categoryName)

}