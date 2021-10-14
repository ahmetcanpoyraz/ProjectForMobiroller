package com.example.mobirollerodev.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.sql.Timestamp
import java.util.*
import java.util.jar.Manifest

class UploadViewModel : ViewModel() {

    val firestore : FirebaseFirestore = Firebase.firestore
    val storage : FirebaseStorage = Firebase.storage


    fun upload(uri: Uri,category : String, price : String, name : String, description : String ){
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"
        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)
        val uuid2 = UUID.randomUUID()
        val randId="$uuid2"
        if (uri != null){
            imageReference.putFile(uri!!).addOnSuccessListener {
            val uploadPictureReference = storage.reference.child("images").child(imageName)
                uploadPictureReference.downloadUrl.addOnSuccessListener {
                val downloadUrl = it.toString()

                    val postMap = hashMapOf<String,Any>()
                    postMap.put("productId",randId)
                    postMap.put("downloadUrl",downloadUrl)
                    postMap.put("productCategory",category)
                    postMap.put("productPrice",price)
                    postMap.put("productName",name)
                    postMap.put("productDescription",description)
                    postMap.put("productDate",com.google.firebase.Timestamp.now())

                    firestore.collection("$category").add(postMap).addOnSuccessListener {

                    }.addOnFailureListener{
                        it.printStackTrace()
                    }

                }

            }.addOnFailureListener{
              it.printStackTrace()
            }
        }
    }
}