package com.example.mvitastyfood.ui.activites

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.mvitastyfood.data.model.Meal
import com.example.mvitastyfood.databinding.ActivityMealBinding
import com.example.mvitastyfood.ui.intent.MealActivityIntent
import com.example.mvitastyfood.ui.viewModels.FavoriteViewModel
import com.example.mvitastyfood.ui.viewModels.MealActivityViewModel
import com.example.mvitastyfood.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealActivity : AppCompatActivity() {

    private lateinit var binding    : ActivityMealBinding
    private val mealMvvm            : MealActivityViewModel by viewModels()
    private val favoriteMvvm        : FavoriteViewModel     by viewModels()
    private lateinit var mealId     : String
    private lateinit var mealThumb  : String
    private lateinit var mealName   : String
    private lateinit var youtube    : String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getViewsInformation()
        setTopInformationInViews()

        processMealIntent()
        observeGetMealInformation()
        onYoutubeItemClick()
        onSaveMeal()


    }

    private fun onSaveMeal() {
        binding.btnFav.setOnClickListener {
            mealToSave?.let {
                favoriteMvvm.upsertMeal(it)
                Toast.makeText(this@MealActivity, "Meal Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeItemClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW , Uri.parse(youtube))
                startActivity(intent)
        }
    }

    private var mealToSave : Meal ? = null
    private fun observeGetMealInformation() {
     lifecycleScope.launchWhenStarted {
         mealMvvm.getMealInformationData.collect{
             val data = it.data
             when(it)
             {
                 is Resource.Loading -> Log.d("testApp" , "Loading meal information")
                 is Resource.Success -> {
                     binding.location.text             = data?.strArea
                     binding.category.text             = data?.strCategory
                     binding.detailsInstructions.text  = data?.strInstructions
                     youtube                           = data?.strYoutube.toString()
                     mealToSave = data
                 }
                 is Resource.Error   -> {Log.d("testApp" , it.message.toString())}
                 else -> Unit
             }
         }
     }
    }

    private fun processMealIntent() {
         mealMvvm.processMealIntent(mealId)
        lifecycleScope.launchWhenStarted {
            mealMvvm.mealIntent.send(MealActivityIntent.GetMealInformation)
        }

    }

    private fun getViewsInformation() {
           val intent = intent
           if (intent != null)
           {
               mealId    = intent.getStringExtra("mealId").toString()
               mealName  = intent.getStringExtra("mealName").toString()
               mealThumb = intent.getStringExtra("mealThumb").toString()
           }
    }

    private fun setTopInformationInViews() {
        binding.collapsing.title = mealName

        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.mealImage)
    }



}