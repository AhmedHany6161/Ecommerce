package com.iti.team.ecommerce.ui.proudcts

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.model.data_classes.Products
import com.iti.team.ecommerce.model.reposatory.ModelRepo
import com.iti.team.ecommerce.model.reposatory.ModelRepository
import java.net.URL

class ProductAdapter(private var dataSet: List<Products>) :

    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    fun setData(products:List<Products>){
        dataSet = products
    }
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.product_name)
        private val price: TextView = view.findViewById(R.id.product_price)
        private val brand: TextView = view.findViewById(R.id.product_brand)
        private val image: ImageView = view.findViewById(R.id.product_image)
        private val addCart: ImageView = view.findViewById(R.id.add_item_cart)
        private val addFav: ImageView = view.findViewById(R.id.add_fav_item)

        fun bind(item: Products){
            name.text = item.title
            price.text = "EGP ${item.variants[0]?.price}"
            brand.text = item.vendor
            Glide.with(view).load("https://cdn.shopify.com/s/files/1/0567/9310/4582/products/7883dc186e15bf29dad696e1e989e914.jpg?v=1621288214").into(image)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.product_holder, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

}