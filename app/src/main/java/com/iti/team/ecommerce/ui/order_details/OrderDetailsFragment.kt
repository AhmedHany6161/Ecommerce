package com.iti.team.ecommerce.ui.order_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.ui.proudcts.ProductsFragmentArgs

class OrderDetailsFragment : Fragment() {

    private val arg: OrderDetailsFragmentArgs by navArgs()
    private val viewModel: OrderDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.order_details_fragment, container, false)
        val back: ImageView = view.findViewById(R.id.order_details_back)
        val orderDetailsAdapter = OrderDetailsAdapter(ArrayList(), viewModel)
        val recyclerView: RecyclerView = view.findViewById(R.id.order_details_rec)
        setupRecyclerView(recyclerView, orderDetailsAdapter)
        viewModel.getImages(viewModel.setOrder(arg.orderDetails))
        observeForData(orderDetailsAdapter)
        back.setOnClickListener {
            findNavController().popBackStack()
        }
        return view
    }

    private fun observeForData(orderDetailsAdapter: OrderDetailsAdapter) {
        viewModel.orderDetails.observe(viewLifecycleOwner, {
            orderDetailsAdapter.setData(it)
            orderDetailsAdapter.notifyDataSetChanged()
        })
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        adapter: OrderDetailsAdapter
    ) {
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter
    }


}