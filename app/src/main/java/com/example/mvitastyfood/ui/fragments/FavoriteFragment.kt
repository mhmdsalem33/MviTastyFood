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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mvitastyfood.databinding.FragmentFavoriteBinding
import com.example.mvitastyfood.ui.activites.MealActivity
import com.example.mvitastyfood.ui.adapters.FavoriteAdapter
import com.example.mvitastyfood.ui.viewModels.FavoriteViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var binding         :  FragmentFavoriteBinding
    private val favoriteMvvm             :  FavoriteViewModel by viewModels()
    private lateinit var favoriteAdapter :  FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteAdapter = FavoriteAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater , container  , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFavoriteSavedMeals()
        setupFavoriteRecView()
        onFavoriteItemClick()
        itemTouchHelper()

    }

    private fun itemTouchHelper() {
        val itemTouchHelper = object: ItemTouchHelper.SimpleCallback(
            0 ,
            ItemTouchHelper.UP   or ItemTouchHelper.DOWN or
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position     = viewHolder.adapterPosition
                val itemPosition = favoriteAdapter.differ.currentList[position]
                    favoriteMvvm.deleteMeal(itemPosition)


                Snackbar.make(binding.root , "Meal Deleted" , Snackbar.LENGTH_LONG).setAction(
                    "restore",
                    View.OnClickListener{
                        favoriteMvvm.upsertMeal(itemPosition)
                    }
                ).show()
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.favoriteRec)
    }

    private fun onFavoriteItemClick() {
        favoriteAdapter.onItemFavoriteClick = { data ->
                val intent = Intent(requireContext()     , MealActivity::class.java)
                intent.putExtra("mealId"            , data.idMeal)
                intent.putExtra("mealName"          , data.strMeal)
                intent.putExtra("mealThumb"         , data.strMealThumb)
                startActivity(intent)
        }
    }


    private fun observeFavoriteSavedMeals() {
        lifecycleScope.launchWhenStarted {
            favoriteMvvm.getSavedMeals().collect{
                favoriteAdapter.differ.submitList(it)
                binding.countFavorites.text = it.size.toString()
            }
        }
    }

    private fun setupFavoriteRecView() {
        binding.favoriteRec.apply {
            layoutManager = GridLayoutManager(requireContext() , 2 , RecyclerView.VERTICAL ,false)
            adapter       = favoriteAdapter
        }
    }


}