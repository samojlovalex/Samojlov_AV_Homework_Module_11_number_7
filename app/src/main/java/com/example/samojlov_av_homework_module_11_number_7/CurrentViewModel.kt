package com.example.samojlov_av_homework_module_11_number_7

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrentViewModel : ViewModel() {

    var productName = ""
    var productPrice = ""
    var productImage: Bitmap? = null
    var productImageResource : Int = R.drawable.shop_photo
    var productList: MutableList<Product> = mutableListOf()

    val currentProductImageResource: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val currentProductImage: MutableLiveData<Bitmap?> by lazy { MutableLiveData<Bitmap?>() }
    val currentProductList: MutableLiveData<MutableList<Product>> by
    lazy { MutableLiveData<MutableList<Product>>() }

}