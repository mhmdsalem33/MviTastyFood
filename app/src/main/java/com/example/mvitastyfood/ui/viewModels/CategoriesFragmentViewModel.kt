package com.example.mvitastyfood.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvitastyfood.data.model.CategoriesList
import com.example.mvitastyfood.data.repositores.CategoriesFragmentRepository
import com.example.mvitastyfood.ui.intent.CategoriesIntent
import com.example.mvitastyfood.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesFragmentViewModel @Inject constructor(private val categoriesRepo : CategoriesFragmentRepository) : ViewModel(){


    val categoriesIntent = Channel<CategoriesIntent>(Channel.UNLIMITED)

    private val _getCategories = MutableStateFlow<Resource<CategoriesList>>(Resource.Idle())
    val getCategories : StateFlow<Resource<CategoriesList>> get() = _getCategories


    init {
        processIntent()
    }

    private fun processIntent()
    {
        viewModelScope.launch {
            categoriesIntent.consumeAsFlow().collect{
                when(it)
                {
                    is CategoriesIntent.GetCategories -> getHomeCategories()
                }
            }
        }
    }


    private fun getHomeCategories() {
        viewModelScope.launch {
            try {
                val response = categoriesRepo.getCategories()
                if (response.isSuccessful)
                {
                    _getCategories.value = Resource.Loading()
                    response.body()?.let {
                        _getCategories.value = Resource.Success(it)
                    }
                    Log.d("testApp" , "Success to get  categories fragment ")
                }
                else
                {
                    _getCategories.value =  Resource.Error(message = response.message().toString())
                    Log.d("testApp" ,   response.code().toString())
                }
            }catch (e:Exception)
            {
                    _getCategories.value =  Resource.Error(message = e.message.toString())
                    Log.d("testApp" , e.message.toString())
            }
        }
    }


}