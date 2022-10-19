package com.example.mvitastyfood.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.platform.LocalDensity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvitastyfood.R
import com.example.mvitastyfood.databinding.FragmentHomeBinding
import com.example.mvitastyfood.ui.activites.CategoryActivity
import com.example.mvitastyfood.ui.activites.MealActivity
import com.example.mvitastyfood.ui.adapters.CategoriesAdapter
import com.example.mvitastyfood.ui.adapters.OverAdapter
import com.example.mvitastyfood.ui.intent.HomeIntent
import com.example.mvitastyfood.ui.viewModels.HomeViewModel
import com.example.mvitastyfood.ui.viewState.HomeViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private val homeMvvm : HomeViewModel by viewModels()
    private lateinit var overAdapter       : OverAdapter
    private lateinit var categoriesAdapter : CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overAdapter       = OverAdapter()
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater , container , false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            processGetRandomMealIntent()
            observeGetRandomMeal()
            onRandomMealClick()

            observeGetOverPopular()
            setupOverRecView()
            onItemOverClick()

            observeGetHomeCategories()
            setupCategoriesRecView()
            onCategoriesItemClick()

            onItemSearchClick()




    }

    private fun onItemSearchClick() {
        binding.search.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onCategoriesItemClick() {
        categoriesAdapter.onCategoriesItemClick = { data ->
            val intent = Intent(requireContext()     , CategoryActivity::class.java)
                intent.putExtra("categoryName"  , data.strCategory)
            startActivity(intent)
        }
    }

    private fun onItemOverClick() {
        overAdapter.onOverItemClick = { data ->
            val intent = Intent(requireContext() , MealActivity::class.java)
                intent.putExtra("mealId"        , data.idMeal)
                intent.putExtra("mealName"      , data.strMeal)
                intent.putExtra("mealThumb"     , data.strMealThumb)
            startActivity(intent)
        }
    }


    private fun onRandomMealClick() {
       lifecycleScope.launchWhenStarted {
           homeMvvm.getRandomMeal.collect{ data ->
               when(data)
               {
                   is HomeViewState.SuccessRandomMeal -> {
                       binding.cartHome.setOnClickListener {
                           val intent = Intent(requireContext()    , MealActivity::class.java)
                               intent.putExtra("mealId"       , data.data.idMeal)
                               intent.putExtra("mealName"     , data.data.strMeal)
                               intent.putExtra("mealThumb"    , data.data.strMealThumb)
                           startActivity(intent)
                       }
                   }
                   else -> Unit
               }
           }
       }
    }


    private fun processGetRandomMealIntent() {
        lifecycleScope.launchWhenStarted {
            homeMvvm.mainIntent.send(HomeIntent.GetRandomMeal)
            homeMvvm.mainIntent.send(HomeIntent.GetOverPopular)
            homeMvvm.mainIntent.send(HomeIntent.GetHomeCategories)
        }
    }

    private fun observeGetRandomMeal() {
        lifecycleScope.launchWhenStarted {
            homeMvvm.getRandomMeal.collect{
                when(it)
                {
                    is HomeViewState.Loading -> { Log.d("testApp" , "Loading get Random Meal")}
                    is HomeViewState.SuccessRandomMeal -> {
                        Glide.with(requireContext())
                            .load(it.data.strMealThumb)
                            .into(binding.randomImage)
                    }
                    is HomeViewState.Error   -> { Log.d("testApp" , it.message)}
                    else -> Unit
                }
            }
        }
    }

    private fun observeGetOverPopular() {
        lifecycleScope.launchWhenStarted {
            homeMvvm.getOverPopular.collect{
                when(it){
                    is HomeViewState.Loading            -> {Log.d("testApp" , "Loading")}
                    is HomeViewState.SuccessOverPopular -> {overAdapter.differ.submitList(it.data.meals)}
                    is HomeViewState.Error              -> {Log.d("testApp" , it.message)}
                    else -> Unit
                }
            }
        }
    }


    private fun setupOverRecView() {
        binding.overRec.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL , false)
            adapter       = overAdapter
        }
    }



    private fun observeGetHomeCategories() {
       lifecycleScope.launchWhenStarted {
           homeMvvm.getCategories.collect{
               when(it)
               {
                   is HomeViewState.Loading           -> { Log.d("testApp"  , "Loading getCategories") }
                   is HomeViewState.SuccessCategories -> { categoriesAdapter.differ.submitList(it.data.categories)}
                   is HomeViewState.Error             -> { Log.d("testApp"  , it.message)}
                   else -> Unit
               }
           }
       }
    }

    private fun setupCategoriesRecView() {
        binding.categoriesRec.apply {
            layoutManager = GridLayoutManager(requireContext() , 3 , RecyclerView.VERTICAL , false)
            adapter       = categoriesAdapter
        }
    }



}