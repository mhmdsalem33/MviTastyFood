package com.example.mvitastyfood.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvitastyfood.data.model.Meal
import com.example.mvitastyfood.data.repositores.SearchRepository
import com.example.mvitastyfood.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepo : SearchRepository) : ViewModel(){

    private val _getSearchMeals  = MutableStateFlow<Resource<List<Meal>>>(Resource.Idle())
    val getSearchMeal : StateFlow<Resource<List<Meal>>> get() =  _getSearchMeals

     fun getSearchMeal(mealName : String)
    {
        viewModelScope.launch {
            try {
                val response = searchRepo.getSearchMeals(mealName)
                if (response.isSuccessful)
                {
                        _getSearchMeals.emit(Resource.Loading())
                    response.body()?.meals?.let {
                        _getSearchMeals.emit(Resource.Success(it))
                    }
                    Log.d("testApp" , "Success to connected to search Meals APi")
                }
                else
                {
                    _getSearchMeals.value = Resource.Error(message = response.message().toString())
                    Log.d("testApp" , "Failed to connected to search Meals APi")
                }
            }catch (e :Exception)
            {
                _getSearchMeals.value = Resource.Error(message = e.message.toString())
                Log.d("testApp" , e.message.toString())
            }
        }
    }

}