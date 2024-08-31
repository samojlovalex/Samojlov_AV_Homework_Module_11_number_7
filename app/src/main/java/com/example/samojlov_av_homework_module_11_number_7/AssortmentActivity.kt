package com.example.samojlov_av_homework_module_11_number_7

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
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
import com.google.gson.Gson
import kotlin.reflect.typeOf
import kotlin.reflect.javaType


@OptIn(ExperimentalStdlibApi::class)
@Suppress("DEPRECATION", "DEPRECATED_IDENTITY_EQUALS")
class AssortmentActivity : AppCompatActivity() {

    private val GALLERY_REQUEST = 25
    private var photoUri: Uri? = null
    private var productList: MutableList<Product> = mutableListOf()

    private lateinit var currentViewModel: CurrentViewModel
    private var listAdapter: ArrayAdapter<Product>? = null

    private lateinit var binding: ActivityAssortmentBinding
    private lateinit var toolbarAssortment: androidx.appcompat.widget.Toolbar
    private lateinit var editImageIV: ImageView
    private lateinit var productNameET: EditText
    private lateinit var productPriceET: EditText
    private lateinit var productDescriptionET: EditText
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
        productDescriptionET = binding.productDescriptionET
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

        listProductLV.onItemClickListener =listVieWCheck()

    }

    private fun listVieWCheck() =
        AdapterView.OnItemClickListener { _, _, position, _ ->
            val product = productList[position]
            val type = typeOf<Product>().javaType
            val gson = Gson().toJson(product, type)
            val intent = Intent(this, ProductDescriptionActivity::class.java)
            intent.putExtra("product", gson)
            startActivity(intent)
        }

    private fun saveDataAssortmentActivity() {
        currentViewModel.currentProductList.observe(this) {
            productList = it
            listAdapter = ListAdapter(this@AssortmentActivity, it)
            listProductLV.adapter = listAdapter
            listAdapter!!.notifyDataSetChanged()
        }

        currentViewModel.currentProductImage.observe(this) {
            photoUri = it
            editImageIV.setImageURI(it)
        }

        currentViewModel.currentProductImageResource.observe(this) {
            editImageIV.setImageResource(it)
        }

    }

    private fun clearEditFields() {
        productNameET.text.clear()
        productPriceET.text.clear()
        productDescriptionET.text.clear()
        val resource = R.drawable.shop_photo
        photoUri = null
        currentViewModel.currentProductImage.value =
            (photoUri.also { currentViewModel.productImage = it })
        editImageIV.setImageResource(resource)
        currentViewModel.currentProductImageResource.value =
            (resource.also { currentViewModel.productImageResource = it })
    }

    private fun greatProduct() {
        currentViewModel.productName = productNameET.text.toString()
        currentViewModel.productPrice = productPriceET.text.toString()
        currentViewModel.productDescription = productDescriptionET.text.toString()
        currentViewModel.currentProductImage.value =
            (photoUri.also { currentViewModel.productImage = it })
        val product = Product()
        product.name = currentViewModel.productName
        product.price = currentViewModel.productPrice
        product.description = currentViewModel.productDescription
        product.image = photoUri.toString()
        productList.add(product)
        currentViewModel.currentProductList.value =
            (productList.also { currentViewModel.productList = it })
        listAdapter!!.notifyDataSetChanged()
        Toast.makeText(this, "${product.name} добавлен", Toast.LENGTH_LONG).show()

    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        editImageIV = binding.editImageIV
        when (requestCode) {
            GALLERY_REQUEST -> if (resultCode === RESULT_OK) {
                photoUri = data?.data
                editImageIV.setImageURI(photoUri)

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