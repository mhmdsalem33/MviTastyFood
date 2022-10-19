package com.example.mvitastyfood.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvitastyfood.data.model.Meal
import com.example.mvitastyfood.data.repositores.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val favoriteRepo : FavoriteRepository) :ViewModel(){

    fun upsertMeal(meal: Meal) = viewModelScope.launch { favoriteRepo.upsertMeal(meal) }
    fun deleteMeal(meal: Meal) = viewModelScope.launch { favoriteRepo.deleteMeal(meal) }
    fun getSavedMeals()        = favoriteRepo.getSavedMeal






}