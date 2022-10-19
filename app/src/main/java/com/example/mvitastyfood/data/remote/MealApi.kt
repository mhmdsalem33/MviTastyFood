package com.example.mvitastyfood.data.remote


import com.example.mvitastyfood.data.model.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {


    @GET("random.php")
    suspend fun getRandomMeal()  : Response<MealList>

    @GET("filter.php")
    suspend fun getOverPopular(@Query("c") CategoryName : String) : Response<OverList>

    @GET("categories.php")
    suspend fun getHomeCategories() : Response<CategoriesList>

    @GET("lookup.php")
    suspend fun getMealInformation(@Query("i") mealId : String)  : Response<MealList>

    @GET("filter.php")
    suspend fun getCategory(@Query("c") categoryName :String)    : Response<CategoryList>

    @GET("search.php")
    suspend fun getSearchMeals(@Query("s") categoryName: String) : Response<MealList>



}