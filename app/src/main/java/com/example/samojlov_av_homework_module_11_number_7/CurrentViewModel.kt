package com.example.samojlov_av_homework_module_11_number_7

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CurrentViewModel : ViewModel() {
    //TODO AssortmentActivity
    var productName = ""
    var productPrice = ""
    var productDescription = ""
    var productImage: Uri? = null
    var productImageResource: Int = R.drawable.shop_photo
    var productList: MutableList<Product> = mutableListOf()
    var check = true

    val currentProductImageResource: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val currentProductImage: MutableLiveData<Uri?> by lazy { MutableLiveData<Uri?>() }
    val currentProductList: MutableLiveData<MutableList<Product>> by
    lazy { MutableLiveData<MutableList<Product>>() }

    //TODO ProductDescriptionActivity
    var productNameDescription = ""
    var productPriceDescription = ""
    var productImageDescription: String = ""
    var productDescriptionDescription = ""
    var position: Int? = null
    var checkDescription: Boolean? = null

    val currentProductImageDescription: MutableLiveData<String> by lazy { MutableLiveData<String>() }

}