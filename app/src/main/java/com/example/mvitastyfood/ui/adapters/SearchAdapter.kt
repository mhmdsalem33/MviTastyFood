package com.example.mvitastyfood.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvitastyfood.data.model.Meal
import com.example.mvitastyfood.databinding.FavoriteRowBinding

class SearchAdapter()  :RecyclerView.Adapter<SearchAdapter.VewHolder>() {


    var onItemSearchCLick :((Meal) -> Unit) ? = null

    private val diffUtilCallback = object :DiffUtil.ItemCallback<Meal>(){
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this , diffUtilCallback)

   inner class VewHolder(val binding : FavoriteRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VewHolder {
       return  VewHolder(FavoriteRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: VewHolder, position: Int) {
       val data = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(data.strMealThumb)
            .into(holder.binding.favroiteImg)
        holder.binding.favoriteName.text = data.strMeal

        holder.itemView.setOnClickListener {
            onItemSearchCLick?.invoke(data)
        }
    }

    override fun getItemCount() = differ.currentList.size
}