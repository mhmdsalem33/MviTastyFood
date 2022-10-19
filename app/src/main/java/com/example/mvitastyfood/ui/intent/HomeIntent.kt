package com.example.mvitastyfood.ui.intent

sealed class HomeIntent {
    object GetRandomMeal     : HomeIntent()
    object GetOverPopular    : HomeIntent()
    object GetHomeCategories : HomeIntent()
}