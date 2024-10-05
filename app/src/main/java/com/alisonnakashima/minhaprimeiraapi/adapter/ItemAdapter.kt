package com.alisonnakashima.minhaprimeiraapi.adapter

import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.WindowInsetsCompat.Type
import androidx.recyclerview.widget.RecyclerView
import com.alisonnakashima.minhaprimeiraapi.R
import com.alisonnakashima.minhaprimeiraapi.model.Item
import com.alisonnakashima.minhaprimeiraapi.ui.CircleTransform
import com.squareup.picasso.Picasso

class ItemAdapter (
    private val items: List<Item>,
    private val itemClickListener: (Item) -> Unit,
) : RecyclerView.Adapter<ItemAdapter.ItemViewHodler>(){


    class ItemViewHodler(view: View) : RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.image)
        val fullNameTextView: TextView = view.findViewById(R.id.name)
        val ageTextView: TextView = view.findViewById(R.id.age)
        val adressTextView: TextView = view.findViewById(R.id.address)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHodler {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ItemViewHodler(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHodler, position: Int) {
        val item = items[position]

        holder.itemView.setOnClickListener{
            itemClickListener.invoke(item)
        }

        holder.fullNameTextView.text = "${item.value.name} ${item.value.surname}"
        holder.ageTextView.text = holder.itemView.context.getString(R.string.item_age, item.value.age.toString())
        holder.adressTextView.text = item.value.address
        Picasso.get()
            .load(item.value.imageUrl)
            .placeholder(R.drawable.ic_download)
            .error(R.drawable.ic_error)
            .transform(CircleTransform())
            .into(holder.imageView)
    }
}
