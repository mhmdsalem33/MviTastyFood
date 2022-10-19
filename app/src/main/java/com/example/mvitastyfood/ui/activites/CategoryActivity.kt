package com.example.mvitastyfood.ui.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvitastyfood.databinding.ActivityCategoryBinding
import com.example.mvitastyfood.ui.adapters.CategoryAdapter
import com.example.mvitastyfood.ui.intent.CategoryIntent
import com.example.mvitastyfood.ui.viewModels.CategoryViewModel
import com.example.mvitastyfood.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {

    private lateinit var binding      : ActivityCategoryBinding
    private val categoryMvvm          : CategoryViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryName : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        categoryAdapter = CategoryAdapter()


        getCategoryName()
        processIntent()
        observerGetCategory()
        setupCategoryRecView()
        onCategoryItemClick()


    }

    private fun onCategoryItemClick() {
        categoryAdapter.onCategoyItemClick = { data ->
            val intent = Intent(applicationContext , MealActivity::class.java)
                intent.putExtra("mealId"          , data.idMeal)
                intent.putExtra("mealName"        , data.strMeal)
                intent.putExtra("mealThumb"       , data.strMealThumb)
            startActivity(intent)

        }
    }

    private fun setupCategoryRecView() {
        binding.categoryRec.apply {
            layoutManager = GridLayoutManager(this@CategoryActivity , 2 , RecyclerView.VERTICAL , false)
            adapter       = categoryAdapter
        }
    }

    private fun processIntent() {
        categoryMvvm.processIntent(categoryName)
        lifecycleScope.launchWhenStarted {
           categoryMvvm.categoryIntent.send(CategoryIntent.GetCategory)
       }
    }

    private fun observerGetCategory() {
        lifecycleScope.launchWhenStarted {
            categoryMvvm.getCategoryData.collect{
                when(it)
                {
                    is Resource.Loading -> {Log.d("testApp" ,  "Loading category")}
                    is Resource.Success -> categoryAdapter.differ.submitList(it.data)
                    is Resource.Error   -> {Log.d("testApp" ,  it.message.toString())}
                    else -> Unit
                }
            }
        }
    }

    private fun getCategoryName() {
        val intent = intent
        if (intent !=  null)
        {
            categoryName = intent.getStringExtra("categoryName").toString()
        }
    }

}