package com.example.mvitastyfood.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvitastyfood.data.model.Meal
import com.example.mvitastyfood.databinding.FragmentCategoriesBinding
import com.example.mvitastyfood.ui.activites.CategoryActivity
import com.example.mvitastyfood.ui.activites.MealActivity
import com.example.mvitastyfood.ui.adapters.CategoriesFragmentAdapter
import com.example.mvitastyfood.ui.intent.CategoriesIntent
import com.example.mvitastyfood.ui.viewModels.CategoriesFragmentViewModel
import com.example.mvitastyfood.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : Fragment() {


    private lateinit var binding : FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesFragmentAdapter
    private val categoriesMVvm  : CategoriesFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoriesAdapter = CategoriesFragmentAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(inflater , container , false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        processCategoriesIntent()
        setupCategoriesRecView()
        observeCategories()
        onCategoriesItemClick()

    }


    private fun processCategoriesIntent() {
        lifecycleScope.launchWhenStarted {
            categoriesMVvm.categoriesIntent.send(CategoriesIntent.GetCategories)
        }
    }

    private fun observeCategories() {
       lifecycleScope.launchWhenStarted {
           categoriesMVvm.getCategories.collect{
               when(it)
               {
                   is Resource.Loading -> Log.d("testApp"  , "Loading categories fragment")
                   is Resource.Success -> categoriesAdapter.differ.submitList(it.data?.categories)
                   is Resource.Error   -> Log.d("testApp"  , it.message.toString())
                   else -> Unit
               }
           }
       }
    }

    private fun setupCategoriesRecView() {
        binding.categoryFragmentRec.apply {
            layoutManager  = GridLayoutManager(requireContext() , 3 , RecyclerView.VERTICAL , false)
            adapter        = categoriesAdapter
        }
    }

    private fun onCategoriesItemClick() {
        categoriesAdapter.onCategoriesItemClick = { data ->
            val intent = Intent(requireContext()    , CategoryActivity::class.java)
                intent.putExtra("categoryName" , data.strCategory)
                startActivity(intent)
        }
    }


}