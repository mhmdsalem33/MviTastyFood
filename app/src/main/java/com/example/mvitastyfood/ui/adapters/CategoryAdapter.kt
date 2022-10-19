package com.example.mvitastyfood.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvitastyfood.data.model.Categories
import com.example.mvitastyfood.data.model.Category
import com.example.mvitastyfood.databinding.CateogiesRowBinding
import com.example.mvitastyfood.databinding.FavoriteRowBinding

class CategoryAdapter () : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    lateinit var onCategoyItemClick :((Category) -> Unit)

    private val diffUtilCallback = object :DiffUtil.ItemCallback<Category>()
    {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }
        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this , diffUtilCallback)

    inner class ViewHolder (val binding : FavoriteRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FavoriteRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val data = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(data.strMealThumb)
            .into(holder.binding.favroiteImg)

        holder.binding.favoriteName.text = data.strMeal


        holder.itemView.setOnClickListener {
            onCategoyItemClick.invoke(data)
        }
    }

    override fun getItemCount() = differ.currentList.size

}