package com.example.mobirollerodev.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobirollerodev.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShoesViewModel : ViewModel() {
    val products = MutableLiveData<List<Product>>()
    val productError = MutableLiveData<Boolean>()
    val productLoading = MutableLiveData<Boolean>()
    private lateinit var db : FirebaseFirestore
    private val productArrayList = ArrayList<Product>()

    fun getData(orderValue : String){
        productLoading.value=true
        db = Firebase.firestore
        db.collection("Shoes").orderBy("product"+"$orderValue",Query.Direction.ASCENDING).addSnapshotListener{ value, error ->

            if(error != null){
                productError.value=true
                productLoading.value=false
                error.printStackTrace()
            }else{

                if(value != null){
                    if (!value.isEmpty){
                        val documents = value.documents
                        productArrayList.clear()
                        for (document in documents){
                            val name = document.get("productName") as String
                            val category = document.get("productCategory") as String
                            val price = document.get("productPrice") as String
                            val image = document.get("downloadUrl") as String
                            val description = document.get("productDescription") as String
                            val id = document.get("productId") as String
                            val tshirts = Product(id,category,price,name,description,image)
                            productArrayList.add(tshirts)
                            products.value=productArrayList
                            productLoading.value=false
                            productError.value=false
                        }
                    }
                }
            }
        }
    }
}