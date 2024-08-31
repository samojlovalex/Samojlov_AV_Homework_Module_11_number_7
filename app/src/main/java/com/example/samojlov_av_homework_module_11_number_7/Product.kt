package com.example.samojlov_av_homework_module_11_number_7

import android.graphics.Bitmap

class Product {
    var name: String = ""
    var price: String = ""
        set(value) {
            field = "$value RUB"
        }
    var description: String = ""
    var image: String? = null
}