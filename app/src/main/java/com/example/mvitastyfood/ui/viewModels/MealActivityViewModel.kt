package com.example.mvitastyfood.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvitastyfood.data.model.Meal
import com.example.mvitastyfood.data.repositores.MealActivityRepository
import com.example.mvitastyfood.ui.intent.MealActivityIntent
import com.example.mvitastyfood.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MealActivityViewModel @Inject constructor(private val mealRepo : MealActivityRepository) : ViewModel(){


    val mealIntent = Channel<MealActivityIntent>(Channel.UNLIMITED)

    private val _getMealInformationData = MutableStateFlow<Resource<Meal>>(Resource.Idle())
    val  getMealInformationData  :StateFlow<Resource<Meal>> get() = _getMealInformationData

      fun processMealIntent(mealId :String)
    {
        viewModelScope.launch {
            mealIntent.consumeAsFlow().collect{
                when(it)
                {
                    is MealActivityIntent.GetMealInformation -> { getMealInformation(mealId) }
                }
            }
        }
    }

    private fun getMealInformation(mealId :String)
    {
     viewModelScope.launch {
         try {
            val response = mealRepo.getMealInformation(mealId)
            if (response.isSuccessful)
            {
                    _getMealInformationData.value  = Resource.Loading()
                response.body()?.meals?.first()?.let {
                    _getMealInformationData.value  = Resource.Success(it)
                }
                Log.d("testApp" , "Success to get meal information")
            }
             else
            {
                Log.d("testApp" , response.code().toString() +"Failed to get meal information")
                _getMealInformationData.value  = Resource.Error(message = response.message().toString())
            }
         }catch (e :Exception)
         {
              _getMealInformationData.value  = Resource.Error(message = e.message.toString())
         }
     }
    }


}