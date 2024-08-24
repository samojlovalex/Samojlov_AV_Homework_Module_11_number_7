package com.example.samojlov_av_homework_module_11_number_7

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.samojlov_av_homework_module_11_number_7.databinding.ActivityAssortmentBinding
import java.io.IOException


class AssortmentActivity : AppCompatActivity() {

    private val GALLERY_REQUEST = 25
    var bitmap: Bitmap? = null
    private var productList: MutableList<Product> = mutableListOf()

    lateinit var currentViewModel: CurrentViewModel
    private var listAdapter: ArrayAdapter<Product>? = null

    private lateinit var binding: ActivityAssortmentBinding
    private lateinit var toolbarAssortment: androidx.appcompat.widget.Toolbar
    private lateinit var editImageIV: ImageView
    private lateinit var productNameET: EditText
    private lateinit var productPriceET: EditText
    private lateinit var addProductBT: Button
    private lateinit var listProductLV: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAssortmentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        setContentView(R.layout.activity_assortment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init() {
        toolbarAssortment = binding.toolbarAssortment
        setSupportActionBar(toolbarAssortment)
        title = getString(R.string.toolbar_title)
        toolbarAssortment.subtitle = getString(R.string.toolbar_subtitle)
        toolbarAssortment.setLogo(R.drawable.logo_products_toolbar)

        currentViewModel = ViewModelProvider(this)[CurrentViewModel::class.java]

        editImageIV = binding.editImageIV
        productNameET = binding.productNameET
        productPriceET = binding.productPriceET
        addProductBT = binding.addProductBT
        listProductLV = binding.listProductLV

        saveDataAssortmentActivity()

        editImageIV.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
        }

        addProductBT.setOnClickListener {
            if (productNameET.text.isEmpty() || productPriceET.text.isEmpty()) return@setOnClickListener
            greatProduct()
            clearEditFields()
        }
    }

    private fun saveDataAssortmentActivity() {
        currentViewModel.currentProductList.observe(this) {
            productList = it
            listAdapter = ListAdapter(this@AssortmentActivity, it)
            listProductLV.adapter = listAdapter
            listAdapter!!.notifyDataSetChanged()
        }

        currentViewModel.currentProductImage.observe(this) {
            bitmap = it
            editImageIV.setImageBitmap(it)
        }

//        currentViewModel.currentProductImageResource.observe(this){
//            editImageIV.setImageResource(it)
//        }

    }

    private fun clearEditFields() {
        productNameET.text.clear()
        productPriceET.text.clear()
        var resource = R.drawable.shop_photo
        editImageIV.setImageResource(resource)
//        currentViewModel.currentProductImageResource.value = (resource.also { currentViewModel.productImageResource = it })
        bitmap = null
    }

    private fun greatProduct() {
        currentViewModel.productName = productNameET.text.toString()
        currentViewModel.productPrice = productPriceET.text.toString()
        currentViewModel.currentProductImage.value = (bitmap.also { currentViewModel.productImage = it } )
        val product = Product()
        product.name = currentViewModel.productName
        product.price = currentViewModel.productPrice
        product.image = bitmap
        productList.add(product)
        currentViewModel.currentProductList.value = (productList.also { currentViewModel.productList = it } )
        listAdapter!!.notifyDataSetChanged()
        Toast.makeText(this,"${product.name} добавлен", Toast.LENGTH_LONG).show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        editImageIV = binding.editImageIV
        when (requestCode) {
            GALLERY_REQUEST -> if (resultCode === RESULT_OK) {
                val selectedImage: Uri? = data?.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                    currentViewModel.currentProductImage.value = (bitmap.also { currentViewModel.productImage = it } )
                } catch (
                    e: IOException
                ) {
                    e.printStackTrace()
                }
                editImageIV.setImageBitmap(bitmap)

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_assortment, menu)
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