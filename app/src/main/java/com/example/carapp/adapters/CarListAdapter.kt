package com.example.carapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.carapp.R
import com.example.carapp.model.Cars

class CarListAdapter(private val context: Context,private val carsList: List<Cars>): RecyclerView.Adapter<CarListAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarListAdapter.ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.car_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarListAdapter.ViewHolder, position: Int) {
         val carItem = carsList[position]

        // sets the image to the imageview from our itemHolder class
        //holder.imgProducts.setImageResource(ItemsViewModel.image.toUri())

        // sets the text to the textview from our itemHolder class
        holder.txtTitle.text = "${carItem.carname}"
        holder.txtPrice.text = "Price: ${carItem.price}"
        holder.txtModel.text = "Car Model:${carItem.model}"
        Glide.with(context)
            .load(carItem.image)
            .placeholder(R.drawable.directions_car)
            .error(R.drawable.directions_car)
            .circleCrop()
            .thumbnail(0.5f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imgProducts)

        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position,carItem)
        }

    }

    override fun getItemCount(): Int {
        return carsList.size
    }
    // Set the click listener for the adapter
    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }

    // Interface for the click listener
    interface OnClickListener {
        fun onClick(position: Int, model: Cars)
    }
    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imgProducts: ImageView = itemView.findViewById(R.id.img_product)
        val txtTitle: TextView = itemView.findViewById(R.id.text_title)
        val txtPrice: TextView = itemView.findViewById(R.id.text_price)
        val txtModel: TextView = itemView.findViewById(R.id.text_model)
    }
}