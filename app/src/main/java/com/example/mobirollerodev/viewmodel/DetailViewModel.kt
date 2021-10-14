package com.example.mobirollerodev.viewmodel

import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobirollerodev.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailViewModel: ViewModel() {

    val products = MutableLiveData<List<Product>>()
    val productError = MutableLiveData<Boolean>()
    private lateinit var db : FirebaseFirestore
    private val productArrayList = ArrayList<Product>()
    val productLiveData = MutableLiveData<Product>()

    fun getData(category : String, productId : String){
        db = Firebase.firestore
        db.collection("$category").whereEqualTo("productId","$productId").addSnapshotListener{ value, error ->

            if(error != null){
                productError.value=true
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
                            //  val date = document.get("productDate") as java.util.Date
                            val id = document.get("productId") as String
                            val product = Product(id,category,price,name,description,image)

                            productLiveData.value=product
                            // productArrayList.add(tshirts)
                          //  products.value=productArrayList
                            productError.value=false
                        }
                    }
                }
            }
        }
    }

    fun deleteDataFromFirebase(category : String, productId : String){

        val db = Firebase.firestore
        val query = db.collection("$category").whereEqualTo("productId","$productId").get()
        query.addOnSuccessListener {
            for(document in it){
                db.collection("$category").document(document.id).delete().addOnSuccessListener {

                }
            }
        }
    }

    fun updateData(category : String, productId : String,productHashMap : HashMap<String,String>){
        val db = Firebase.firestore
        val query = db.collection("$category").whereEqualTo("productId","$productId").get()
        query.addOnSuccessListener {
            for(document in it){
                db.collection("$category").document(document.id).set(productHashMap, SetOptions.merge()).addOnSuccessListener {
                }
            }
        }
    }


}