package com.iti.team.ecommerce.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.ui.wish.WishListViewModel

class ProfileWishAdapter(
    private var dataSet: List<Product>,
    private val viewModel: ProfileViewModel
) :

    RecyclerView.Adapter<ProfileWishAdapter.ViewHolder>() {
    fun setData(list: List<Product>) {
        dataSet = list
    }
    class ViewHolder(val view: View, private val viewModel: ProfileViewModel) :
        RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.product_name)
        private val price: TextView = view.findViewById(R.id.product_price)
        private val brand: TextView = view.findViewById(R.id.product_brand)
        private val image: ImageView = view.findViewById(R.id.product_image)
        private val addCart: ImageView = view.findViewById(R.id.add_item_cart)
        private val Fav: ImageView = view.findViewById(R.id.add_fav_item)

        fun bind(item: Product) {
            name.text = item.name
            price.text = "EGP ${item.price}"
            brand.text = item.brand
            Glide.with(view).load(item.image).into(image)
            Fav.setImageResource(R.drawable.ic_favorite_red)

            addCart.setOnClickListener {
                viewModel.addToCart(item)
            }

            view.setOnClickListener {
                viewModel.navigateToDetails(item)
            }

            Fav.setOnClickListener {
                viewModel.removeFromWishList(item.id)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.product_holder, viewGroup, false)

        return ViewHolder(view, viewModel)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}