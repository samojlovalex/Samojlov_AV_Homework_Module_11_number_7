package com.example.samojlov_av_homework_module_11_number_7

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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

@Suppress("DEPRECATION", "DEPRECATED_IDENTITY_EQUALS")
@OptIn(ExperimentalStdlibApi::class)
class ProductDescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDescriptionBinding
    private lateinit var toolbarDescription: androidx.appcompat.widget.Toolbar
    private lateinit var currentViewModel: CurrentViewModel
    private lateinit var editImageDescriptionIV: ImageView
    private lateinit var nameDescriptionTW: TextView
    private lateinit var priceDescriptionTW: TextView
    private lateinit var descriptionDescriptionTV: TextView
    private var product: Product? = null
    private var productList: MutableList<Product>? = null
    private val GALLERY_REQUEST = 26
    private var photoUri: Uri? = null

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

        editImageDescriptionIV.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
        }

        currentViewModel = ViewModelProvider(this)[CurrentViewModel::class.java]

        currentViewModel.currentProductImageDescription.observe(this) {
            photoUri = it.toUri()
            editImageDescriptionIV.setImageURI(it.toUri())
        }

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
        val type = typeOf<MutableList<Product>?>().javaType
        val productListString: String? = intent.getStringExtra("productList")
        productList = Gson().fromJson(productListString, type)
        currentViewModel.position = intent.extras?.getInt("position")
        product = productList!![currentViewModel.position!!.toInt()]
        currentViewModel.checkDescription = intent.extras?.getBoolean("checkOut")

        currentViewModel.productNameDescription = product!!.name
        currentViewModel.productPriceDescription = product!!.price
        currentViewModel.productDescriptionDescription = product?.description ?: ""
        currentViewModel.productImageDescription = product?.image!!

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

            R.id.backMenuAssortment -> {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.toast_back),
                    Toast.LENGTH_LONG
                ).show()
                if (currentViewModel.position != null) {
                    productList!!.add(currentViewModel.position!! + 1, product!!)
                    productList!!.removeAt(currentViewModel.position!!)
                }
                val intent = Intent(this, AssortmentActivity::class.java)
                val type = typeOf<MutableList<Product>>().javaType
                val gson = Gson().toJson(productList, type)
                currentViewModel.checkDescription = false
                intent.putExtra("checkBack", currentViewModel.checkDescription)
                intent.putExtra("productListBack", gson)
                currentViewModel.currentProductImageDescription.removeObservers(this)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        editImageDescriptionIV = binding.editImageDescriptionIV
        when (requestCode) {
            GALLERY_REQUEST -> if (resultCode === RESULT_OK) {
                photoUri = data?.data
                editImageDescriptionIV.setImageURI(photoUri)
                currentViewModel.currentProductImageDescription.value =
                    (photoUri.toString().also { currentViewModel.productImageDescription = it })
                product?.image = currentViewModel.productImageDescription
            }
        }
    }
}