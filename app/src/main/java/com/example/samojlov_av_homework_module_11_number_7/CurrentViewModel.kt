package com.example.samojlov_av_homework_module_11_number_7

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrentViewModel : ViewModel() {

    var productName = ""
    var productPrice = ""
    var productDescription = ""
    var productImage: Uri? = null
    var productImageResource : Int = R.drawable.shop_photo
    var productList: MutableList<Product> = mutableListOf()
    var productNameDescription = ""
    var productPriceDescription = ""
    var productDescriptionDescription = ""
    var productImageDescription = ""

    val currentProductImageResource: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val currentProductImage: MutableLiveData<Uri?> by lazy { MutableLiveData<Uri?>() }
    val currentProductList: MutableLiveData<MutableList<Product>> by
    lazy { MutableLiveData<MutableList<Product>>() }

}