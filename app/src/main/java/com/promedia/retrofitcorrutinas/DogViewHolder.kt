package com.promedia.retrofitcorrutinas

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.promedia.retrofitcorrutinas.databinding.ItemDogBinding
import com.squareup.picasso.Picasso

class DogViewHolder(view:View):RecyclerView.ViewHolder(view) {
    private val b = ItemDogBinding.bind(view)
    fun bind(image:String){
        // Cargo la imagen de internet del parámetro en el ImageView
        Picasso.get()
            .load(image)
            .into(b.ivDog)
    }
}