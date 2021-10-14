package com.example.mobirollerodev.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobirollerodev.R
import com.example.mobirollerodev.model.Product
import com.example.mobirollerodev.view.DetailActivity
import com.squareup.picasso.Picasso

class RecyclerAdapter(var arrayList: ArrayList<Product>) : RecyclerView.Adapter<RecyclerAdapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {

        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.grid_layout_list_item,parent,false)
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
       var  product: Product = arrayList.get(position)
        holder.nameProduct.text = product.productName
        holder.priceProduct.text = product.productPrice.toString()
        Picasso.get().load(product.productImage).resize(250,250).into(holder.imageProduct)

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context,DetailActivity::class.java)
            intent.putExtra("idOfProduct",arrayList.get(position).productId)
            intent.putExtra("categoryOfProduct",arrayList.get(position).productCategory)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var imageProduct = itemView.findViewById<ImageView>(R.id.item_image)
        var nameProduct = itemView.findViewById<TextView>(R.id.item_name)
        var priceProduct = itemView.findViewById<TextView>(R.id.item_price)

    }

    fun updateProductList(newarrayList: ArrayList<Product>){

        arrayList.clear()
        arrayList.addAll(newarrayList)
        notifyDataSetChanged()

    }


}