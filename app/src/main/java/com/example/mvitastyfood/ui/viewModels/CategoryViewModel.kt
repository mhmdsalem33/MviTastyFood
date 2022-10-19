package com.example.mvitastyfood.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvitastyfood.data.model.Category
import com.example.mvitastyfood.data.repositores.CategoryActivityRepository
import com.example.mvitastyfood.ui.intent.CategoryIntent
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
class CategoryViewModel @Inject constructor(private val categoryRepo : CategoryActivityRepository) :ViewModel() {

    val categoryIntent = Channel<CategoryIntent>(Channel.UNLIMITED)



     fun processIntent(categoryName: String)
    {
      viewModelScope.launch {
          categoryIntent.consumeAsFlow().collect{
             when(it)
             {
                 is CategoryIntent.GetCategory -> getCategory(categoryName =  categoryName)
             }
          }
      }
    }

    private val _getCategoryData = MutableStateFlow<Resource<List<Category>>>(Resource.Idle())
    val getCategoryData :StateFlow<Resource<List<Category>>> get() = _getCategoryData

    private fun getCategory(categoryName :String)
    {
        viewModelScope.launch {
            try {
                val response = categoryRepo.getCategory(categoryName)
                if (response.isSuccessful)
                {
                    _getCategoryData.value = Resource.Loading()
                  response.body()?.meals?.let {
                      _getCategoryData.value = Resource.Success(it)
                  }
                    Log.d("testApp" , "Success to get category")
                }
                else
                {
                    Log.d("testApp" , response.code().toString()+"Failed to get category")
                    _getCategoryData.value = Resource.Error(message = response.message().toString())
                }

            }catch (e: Exception)
            {
                _getCategoryData.value = Resource.Error(message = e.message.toString())
                Log.d("testApp" , e.message.toString())
            }
        }
    }


}
