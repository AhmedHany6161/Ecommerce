package com.iti.team.ecommerce.ui.proudcts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.model.data_classes.Products

class ProductAdapter(private var dataSet: List<Products>,private val viewModel: ProductsViewModel) :


    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    fun setData(products:List<Products>){
        dataSet = products
    }


    class ViewHolder(val view: View , private val viewModel: ProductsViewModel) : RecyclerView.ViewHolder(view) {

        private val name: TextView = view.findViewById(R.id.product_name)
        private val price: TextView = view.findViewById(R.id.product_price)
        private val brand: TextView = view.findViewById(R.id.product_brand)
        private val image: ImageView = view.findViewById(R.id.product_image)
        private val addCart: ImageView = view.findViewById(R.id.add_item_cart)
        private val addFav: ImageView = view.findViewById(R.id.add_fav_item)
        private val layout: ConstraintLayout = view.findViewById(R.id.product_layout)

        fun bind(item: Products){
            name.text = item.title
            price.text = "${item.variants[0]?.price} EGP"
            brand.text = item.vendor
            Glide.with(view).load(item.image.src).into(image)
            addCart.setOnClickListener {
                if (viewModel.isLogin()) {
                    viewModel.addToCart(item)
                } else {
                    viewModel.navigateToLogin()
                }
            }

            layout.setOnClickListener {
                viewModel.navigateToDetails(item)
            }

            if(viewModel.inWishList(item.productId?:-1)){
                addFav.setImageResource(R.drawable.ic_favorite_red)
            }else{
                addFav.setImageResource(R.drawable.ic_favorite)
            }
            addFav.setOnClickListener {
                if (viewModel.isLogin()) {
                    if (!viewModel.inWishList(item.productId ?: -1)) {
                        viewModel.addToWishList(item)
                        addFav.setImageResource(R.drawable.ic_favorite_red)
                    } else {
                        viewModel.removeFromWishList(item.productId ?: -1)
                        addFav.setImageResource(R.drawable.ic_favorite)
                    }
                } else {
                    viewModel.navigateToLogin()
                }
            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.product_holder, viewGroup, false)

        return ViewHolder(view,viewModel)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

}