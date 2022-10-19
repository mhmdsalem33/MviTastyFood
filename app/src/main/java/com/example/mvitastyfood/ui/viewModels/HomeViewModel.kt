package com.example.mvitastyfood.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvitastyfood.data.model.Meal
import com.example.mvitastyfood.data.repositores.HomeRepository
import com.example.mvitastyfood.ui.intent.HomeIntent
import com.example.mvitastyfood.ui.viewState.HomeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {


    val mainIntent = Channel<HomeIntent>(Channel.UNLIMITED)

    private val _getRandomMeal = MutableStateFlow<HomeViewState>(HomeViewState.Idle)
    val getRandomMeal : StateFlow<HomeViewState> get() =  _getRandomMeal

    init {
        processMainIntent()
    }

    private fun processMainIntent()
    {
        viewModelScope.launch {
            mainIntent.consumeAsFlow().collect{
                when(it)
                {
                    is HomeIntent.GetRandomMeal     ->    getRandomMeal()
                    is HomeIntent.GetOverPopular    ->    getOverPopularItems()
                    is HomeIntent.GetHomeCategories ->    getHomeCategories()
                }
            }
        }
    }

    private var saveStateRandomMeal : Meal? = null
    private fun getRandomMeal(){
        saveStateRandomMeal?.let { randomMeal ->
            _getRandomMeal.value = HomeViewState.SuccessRandomMeal(randomMeal)
            return
        }
        viewModelScope.launch {
           try {
               val response = homeRepository.getRandomMeal()
               if (response.isSuccessful)
               {

                     _getRandomMeal.value = HomeViewState.Loading
                      response.body()?.meals?.first()?.let {
                     _getRandomMeal.value = HomeViewState.SuccessRandomMeal(data = it)
                      saveStateRandomMeal = it
                   }
                   Log.d("testApp" , "Success to connect to getRandomMeal")
               }
               else
               {
                   Log.d("testApp" , response.code().toString() )
                   _getRandomMeal.value = HomeViewState.Error(message = response.message().toString())
               }
           }catch (e: Exception)
           {
               Log.d("testApp" , "getRandomMeal ${e.message.toString()}")
               _getRandomMeal.value = HomeViewState.Error(message = e.message.toString())
           }
        }
    }


    private val _getOverPopular = MutableStateFlow<HomeViewState>(HomeViewState.Idle)
    val getOverPopular : StateFlow<HomeViewState> get() =  _getOverPopular
    private fun getOverPopularItems(){

        viewModelScope.launch {
           try {
               val response  =  homeRepository.getOverPopular()
               if (response.isSuccessful)
               {
                   _getOverPopular.value = HomeViewState.Loading
                  response.body()?.let {
                      _getOverPopular.value = HomeViewState.SuccessOverPopular(data = it)
                  }
                   Log.d("testApp" , "Success To connected to over")

               }
               else
               {
                   Log.d("testApp" , "Failed To connected to over" +response.code().toString())
               }
           }catch (e:Exception)
           {
               Log.d("testApp" , e.message.toString())
           }
        }
    }


    private val _getCategories = MutableStateFlow<HomeViewState>(HomeViewState.Idle)
    val getCategories  :StateFlow<HomeViewState> get() = _getCategories


    private fun getHomeCategories() {
        viewModelScope.launch {
            try {
                val response = homeRepository.getHomeCategories()
                if (response.isSuccessful)
                {
                    _getCategories.value = HomeViewState.Loading
                    response.body()?.let {
                    _getCategories.value = HomeViewState.SuccessCategories(it)
                    }
                    Log.d("testApp" , "Success to get home categories")
                }
                else
                {
                    _getCategories.value = HomeViewState.Error(response.message().toString())
                    Log.d("testApp" , response.code().toString())
                }
            }catch (e:Exception)
            {
                _getCategories.value = HomeViewState.Error(e.message.toString())
                Log.d("testApp" , e.message.toString())
            }
        }
    }




}