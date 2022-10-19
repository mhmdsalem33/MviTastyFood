package com.example.mvitastyfood.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvitastyfood.databinding.FragmentSearchBinding
import com.example.mvitastyfood.ui.activites.MealActivity
import com.example.mvitastyfood.ui.adapters.SearchAdapter
import com.example.mvitastyfood.ui.viewModels.SearchViewModel
import com.example.mvitastyfood.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay


@AndroidEntryPoint
class SearchFragment : Fragment() {



    private lateinit var binding : FragmentSearchBinding
    private val searchMvvm       : SearchViewModel by viewModels()
    private lateinit var  searchAdapter  :SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchAdapter = SearchAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater , container , false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        search()
        observerSearchMeals()
        setupSearchRecView()
        onItemSearchCLick()

    }

    private fun onItemSearchCLick() {
        searchAdapter.onItemSearchCLick = { data ->
            val intent = Intent(requireContext()  , MealActivity::class.java)
            intent.putExtra("mealId"     , data.idMeal)
            intent.putExtra("mealName"   , data.strMeal)
            intent.putExtra("mealThumb"  , data.strMealThumb)
            startActivity(intent)
        }
    }

    private fun setupSearchRecView() {
        binding.searchRecView.apply {
            layoutManager = GridLayoutManager(requireContext() , 2 , RecyclerView.VERTICAL , false)
            adapter       = searchAdapter
        }
    }

    private fun observerSearchMeals() {
       lifecycleScope.launchWhenStarted {
           searchMvvm.getSearchMeal.collect{
               when(it)
               {
                   is Resource.Loading -> Log.d("testApp" , "Loading search Meal")
                   is Resource.Success -> searchAdapter.differ.submitList(it.data)
                   is Resource.Error   -> Log.d("testApp" , it.message.toString())
                   else -> Unit
               }
           }
       }
    }

    private fun search() {
        var searchJob : Job ? = null
        binding.edtSearch.addTextChangedListener { searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launchWhenStarted {
                delay(500)
                searchMvvm.getSearchMeal(searchQuery.toString())
            }
        }

    }


}