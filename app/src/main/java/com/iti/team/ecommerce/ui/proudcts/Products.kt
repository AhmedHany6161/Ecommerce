package com.ITI.team.ecommerce.ui.proudcts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.model.data_classes.Product
import com.iti.team.ecommerce.ui.proudcts.ProductAdapter

class Products : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.products_fragment, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.products_rec)
        val adapter =ProductAdapter(listOf(Product("astfftftftftftsas",
            "000","tft"
            ,222),Product("abbssas",
            "000","tooojijijijift"
            ,33),
            Product("jhuhuhu",
                "000","tyyyyft"
                ,5588888),
            Product("askjkjksas",
                "000","lkl"
                ,55777),
            Product("jjkkkkkj",
                "000","lllll"
                ,55777)))
        val gridLayoutManager =GridLayoutManager(context,2)
        gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager =  gridLayoutManager
        recyclerView.adapter = adapter
        return view
    }

}