package com.iti.team.ecommerce.ui.shop_bag

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.model.data_classes.Product
import android.content.Context

class ShoppingPageAdapter (
    private var dataSet: List<Product>,
    private val viewModel: ShoppingPageViewModel,
    private val context: Context?
) :

    RecyclerView.Adapter<ShoppingPageAdapter.ViewHolder>() {
    fun setData(list: List<Product>) {
        dataSet = list
    }

    class ViewHolder(val view: View, private val viewModel: ShoppingPageViewModel,private val context: Context?) :
        RecyclerView.ViewHolder(view), LifecycleOwner {
        private val name: TextView = view.findViewById(R.id.product_name)
        private val price: TextView = view.findViewById(R.id.product_price)
        private val count: TextView = view.findViewById(R.id.product_count)
        private val image: ImageView = view.findViewById(R.id.product_image)
        private val Fav: ImageView = view.findViewById(R.id.add_fav_item)
        private val delete :ImageView = view.findViewById(R.id.delete_item)
        private val plus : ImageView =view.findViewById(R.id.plus)
        private val minus :ImageView =view.findViewById(R.id.minus)


        fun bind(item: Product) {
            name.text = item.name
            price.text = "EGP ${item.price}"
            count.text = item.count.toString()
            Glide.with(view).load(item.image).into(image)


            Fav.setOnClickListener {
                addToFav(item)
            }
            delete.setOnClickListener {
                deleteDialog(item)
            }
            plus.setOnClickListener {
                viewModel.addToCard(item)
            }
            minus.setOnClickListener {
                if(item.count == 1){
                    deleteDialog(item)
                }else {
                    viewModel.removebyCount(item.id)
                }
            }
        }

        private fun deleteDialog(item: Product) {
            val builder = AlertDialog.Builder( context)
            builder.setTitle(R.string.shop_bag)
            builder.setMessage(R.string.dialog_delete)
            builder.setIcon(R.drawable.ic_shopping)
            builder.setPositiveButton(R.string.delete){dialogInterface, which ->
                viewModel.removeFromCard(item.id)
            }
            builder.setNeutralButton(R.string.cancel){dialogInterface, which ->

            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        private fun addToFav(item: Product) {
            val builder = AlertDialog.Builder( context)
            builder.setTitle(R.string.shop_bag)
            builder.setMessage(R.string.dialog_wishList)
            builder.setIcon(R.drawable.ic_shopping)
            builder.setPositiveButton(R.string.yes){dialogInterface, which ->
                viewModel.addToWishList(item)
                viewModel.removeFromCard(item.id)
            }
            builder.setNeutralButton(R.string.no){dialogInterface, which ->

            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        override fun getLifecycle(): Lifecycle {
            TODO("Not yet implemented")
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.shop_bag_item, viewGroup, false)

        return ViewHolder(view, viewModel, context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}
