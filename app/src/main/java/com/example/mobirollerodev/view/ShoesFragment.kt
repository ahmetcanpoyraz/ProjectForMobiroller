package com.example.mobirollerodev.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobirollerodev.R
import com.example.mobirollerodev.adapter.RecyclerAdapter
import com.example.mobirollerodev.model.Product
import com.example.mobirollerodev.viewmodel.ShoesViewModel
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.android.synthetic.main.fragment_products.*



class ShoesFragment : Fragment() {
    private lateinit var viewModel : ShoesViewModel
    private val productAdapter = RecyclerAdapter(arrayListOf())
    private var gridLayoutManager: GridLayoutManager? =null
    private var orderBy = "Date"
    private lateinit var autoCompleteTextView: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_products, container, false)
        autoCompleteTextView= view.findViewById(R.id.autoCompleteTextView2)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderbys = resources.getStringArray(R.array.orderby)
        val arrayAdapter =
            activity?.let { ArrayAdapter(it.applicationContext,R.layout.dropdown_item,orderbys) }

        autoCompleteTextView.setAdapter(arrayAdapter)

        autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                orderBy = orderbys.get(position).toString()
                viewModel = ViewModelProvider(this).get(ShoesViewModel::class.java)
                viewModel.getData(orderBy)
            }

        viewModel = ViewModelProvider(this).get(ShoesViewModel::class.java)
        viewModel.getData(orderBy)
        gridLayoutManager = GridLayoutManager(context,2, LinearLayoutManager.VERTICAL,false)
        recycler_products.layoutManager = gridLayoutManager
        recycler_products.adapter = productAdapter


        swipeRefreshLayout.setOnRefreshListener {
            recycler_products.visibility = View.GONE
            error_products.visibility = View.GONE
            productLoading.visibility = View.VISIBLE
            viewModel.getData(orderBy)
            swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()

    }
    private fun observeLiveData(){
        viewModel.products.observe(viewLifecycleOwner, Observer { products ->
            products?.let {
                error_products.visibility = View.GONE
                recycler_products.visibility = View.VISIBLE
                if (productAdapter != null) {
                    productAdapter.updateProductList(products as ArrayList<Product>)
                }
            }
        })
        viewModel.productError.observe(viewLifecycleOwner, Observer { error->
            error?.let {
                if(it){
                    error_products.visibility = View.VISIBLE
                }else{
                    error_products.visibility = View.GONE
                }
            }
        })
        viewModel.productLoading.observe(viewLifecycleOwner, Observer { loading->
            loading.let {
                if(it){
                    productLoading.visibility = View.VISIBLE
                    recycler_products.visibility = View.GONE
                    error_products.visibility = View.GONE
                }else{
                    productLoading.visibility = View.GONE
                }
            }
        })
    }


}