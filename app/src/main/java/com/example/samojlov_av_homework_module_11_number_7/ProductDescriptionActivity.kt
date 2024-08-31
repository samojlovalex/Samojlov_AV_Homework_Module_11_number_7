package com.example.samojlov_av_homework_module_11_number_7

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.samojlov_av_homework_module_11_number_7.databinding.ActivityProductDescriptionBinding
import com.google.gson.Gson
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

@OptIn(ExperimentalStdlibApi::class)
class ProductDescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDescriptionBinding
    private lateinit var toolbarDescription: androidx.appcompat.widget.Toolbar
    private lateinit var currentViewModel: CurrentViewModel
    private lateinit var editImageDescriptionIV: ImageView
    private lateinit var nameDescriptionTW: TextView
    private lateinit var priceDescriptionTW: TextView
    private lateinit var descriptionDescriptionTV: TextView
    private var productString: String? = null
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProductDescriptionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        setContentView(R.layout.activity_product_description)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init() {
        toolbarDescription = binding.toolbarDescription
        setSupportActionBar(toolbarDescription)
        title = getString(R.string.toolbar_title)
        toolbarDescription.subtitle = getString(R.string.toolbar_subtitle)
        toolbarDescription.setLogo(R.drawable.logo_products_toolbar)

        editImageDescriptionIV = binding.editImageDescriptionIV
        nameDescriptionTW = binding.nameDescriptionTW
        priceDescriptionTW = binding.priceDescriptionTW
        descriptionDescriptionTV = binding.descriptionDescriptionTV

        currentViewModel = ViewModelProvider(this)[CurrentViewModel::class.java]

        receivingData()

        printData()

    }

    private fun printData() {
        nameDescriptionTW.text = currentViewModel.productNameDescription
        priceDescriptionTW.text = currentViewModel.productPriceDescription
        descriptionDescriptionTV.text = currentViewModel.productDescriptionDescription

        if (currentViewModel.productImageDescription != "null") {
            editImageDescriptionIV.setImageURI(currentViewModel.productImageDescription.toUri())
        } else editImageDescriptionIV.setImageResource(R.drawable.shop_photo)
    }

    private fun receivingData() {
        val type = typeOf<Product>().javaType
        productString = intent.getStringExtra("product")
        product = Gson().fromJson(productString, type)

        currentViewModel.productNameDescription = product!!.name
        currentViewModel.productPriceDescription = product!!.price
        currentViewModel.productDescriptionDescription = product?.description ?: ""
        currentViewModel.productImageDescription = product?.image ?: ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_description, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("SetTextI18n", "ShowToast")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuAssortment -> {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.toast_exit),
                    Toast.LENGTH_LONG
                ).show()
                finishAffinity()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}