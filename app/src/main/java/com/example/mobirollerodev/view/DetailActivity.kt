package com.example.mobirollerodev.view

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mobirollerodev.R
import com.example.mobirollerodev.databinding.ActivityDetailBinding
import com.example.mobirollerodev.databinding.ActivityUploadBinding
import com.example.mobirollerodev.model.Product
import com.example.mobirollerodev.viewmodel.DetailViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var viewModel : DetailViewModel
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val id:String = intent.getStringExtra("idOfProduct").toString()
        val categ:String = intent.getStringExtra("categoryOfProduct").toString()
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.getData(categ,id)

        binding.detailBackButton.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.deletebutton.setOnClickListener {
          viewModel.deleteDataFromFirebase(categ,id)
            Toast.makeText(this,"Product deleted",Toast.LENGTH_LONG).show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.updatebutton.setOnClickListener {
            if (binding.detailDescription.text.isNotEmpty() && binding.detailName.text.isNotEmpty()
                && binding.detailPrice.text.isNotEmpty()
            ) {

                val hashMapProduct = hashMapOf(
                    "productName" to binding.detailName.text.toString(),
                    "productDescription" to binding.detailDescription.text.toString(),
                    "productPrice" to binding.detailPrice.text.toString(),
                    )
                viewModel.updateData(categ,id,hashMapProduct)
                Toast.makeText(this,"Product Updated",Toast.LENGTH_LONG).show()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        observeLiveData()
    }


    private fun observeLiveData(){
        viewModel.productLiveData.observe(this, Observer { product->
            product?.let {
                detail_name.setText(product.productName)
                detail_description.setText(product.productDescription)
                detail_price.setText(product.productPrice)
                detail_category.text=product.productCategory
                Picasso.get().load(product.productImage).resize(250,250).into(detailImage)
            }

        })
    }
}