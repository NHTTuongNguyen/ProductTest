package com.example.producttest.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.producttest.R
import com.example.producttest.model.Product
import java.text.NumberFormat
import java.util.*

class ProductDetailActivity : AppCompatActivity() {
    private var txtNameDetail: TextView? = null
    private var txtPriceDetail: TextView? = null
    private var txtDescriptionDetail: TextView? = null
    private var product: Product? = null
    private var toolbar_ProductDetail: Toolbar? = null
    private val imgDetail: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        inTent
        initView()
        actionToolBar()
    }

    private fun actionToolBar() {
        toolbar_ProductDetail = findViewById(R.id.toolbar_ProductDetail)
        setSupportActionBar(toolbar_ProductDetail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar_ProductDetail!!.setNavigationOnClickListener(View.OnClickListener { finish() })
    }

    private val inTent: Unit
        private get() {
            val intent = intent
            if (intent != null) {
                if (intent.hasExtra("ProductKey")) {
                    product = intent.getSerializableExtra("ProductKey") as Product?
                }
            }
        }

    private fun initView() {
        txtNameDetail = findViewById(R.id.txtNameDetail)
        txtDescriptionDetail = findViewById(R.id.txtDescriptionDetail)
        txtPriceDetail = findViewById(R.id.txtPriceDetail)
        toolbar_ProductDetail = findViewById(R.id.toolbar_ProductDetail)
        toolbar_ProductDetail!!.title = product!!.name
        //setText
        txtNameDetail!!.text = product!!.name
        val locale = Locale("vi", "VN")
        val fmt = NumberFormat.getCurrencyInstance(locale)
        txtPriceDetail!!.text = fmt.format(product!!.price.toLong())
        txtDescriptionDetail!!.text = product!!.des
    }
}