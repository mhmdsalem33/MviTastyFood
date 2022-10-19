package com.example.mvitastyfood.ui.viewState

import com.example.mvitastyfood.data.model.CategoriesList
import com.example.mvitastyfood.data.model.OverList
import com.example.mvitastyfood.data.model.Meal

sealed class HomeViewState{
    object Idle    : HomeViewState()
    object Loading : HomeViewState()
    data class SuccessRandomMeal (val data    : Meal)           : HomeViewState()
    data class SuccessOverPopular(val data    : OverList)       : HomeViewState()
    data class SuccessCategories (val data    : CategoriesList) : HomeViewState()
    data class Error(  val message : String)                    : HomeViewState()
}
