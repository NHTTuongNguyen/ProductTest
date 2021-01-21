package com.example.producttest.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.producttest.R
import com.example.producttest.Share.SharedPreferences_Utils
import com.example.producttest.adapter.ProductAdapter
import com.example.producttest.database.DatabaseHelper
import com.example.producttest.model.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.ByteArrayOutputStream

import java.io.FileNotFoundException
import java.io.InputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var recyclerViewProduct: RecyclerView? = null
    private val floatAddProduct: FloatingActionButton? = null
    private val PICK_IMAGE_REQUEST = 71
    private var imageViewAdd: ImageView? = null
    private var databaseHelper: DatabaseHelper? = null
    private var alertDialog: AlertDialog? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var productAdapter: ProductAdapter? = null
    private var productList: MutableList<Product>? = null
    private var txtNoData: TextView? = null
    private var txtTotalProduct: TextView? = null
    private var search_barView: SearchView? = null
    private val toolbar_Product: Toolbar? = null
    private val name: String? = null
    private val price: String? = null
    private val des: String? = null
    private var edtName: EditText? = null
    private var edtPrice: EditText? = null
    private var edtDescription: EditText? = null
    private var edtCount: EditText? = null
    private val btnHistory: Button? = null
    private var btnProduct: Button? = null
    private var sharedPreferences_utils: SharedPreferences_Utils? = null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        databaseHelper = DatabaseHelper(this)
        sharedPreferences_utils = SharedPreferences_Utils(this@MainActivity)
        initView()
        hasdatainlist()
        searchProduct()
        setTotalTextView()
    }

    fun setTotalTextView() {
        val total = totalProduct()
        val locale = Locale("vi", "VN")
        val fmt = NumberFormat.getCurrencyInstance(locale)
        txtTotalProduct!!.text = fmt.format(total.toLong())
    }

    private fun totalProduct(): Int {
        var total = 0
        for (product in productList!!) total += product.price * product.count
        return total
    }

    private fun initView() {
        search_barView = findViewById<SearchView>(R.id.search_barView)
        txtTotalProduct = findViewById<TextView>(R.id.txtTotalProduct)
        recyclerViewProduct = findViewById<RecyclerView>(R.id.recyclerViewProduct)
        txtNoData = findViewById<TextView>(R.id.txtNoData)
        btnProduct = findViewById<Button>(R.id.btnProduct)
        findViewById<View>(R.id.btnHistory).setOnClickListener(this)
        findViewById<View>(R.id.floatAddProduct).setOnClickListener(this)
        btnProduct!!.setOnClickListener {
            if (productList!!.isNotEmpty()) {
                val total = totalProduct()
                val dt = Date()
                val hours = dt.hours
                val minutes = dt.minutes
                val seconds = dt.seconds
                val calendar = Calendar.getInstance()
                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = simpleDateFormat.format(calendar.time)
                val curTime = hours.toString() + "h" + ":" + minutes + "m" + ":" + seconds + "s" + " - " + date
                sharedPreferences_utils!!.setSaveCartProduct(this@MainActivity, total, curTime)
                databaseHelper!!.removeAll()
                finish()
                startActivity(intent)
                startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
            } else {
                Toast.makeText(this@MainActivity, "Your Cart Empty !!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun hasdatainlist() {
        productList = databaseHelper!!.allProduct
        if (productList!!.isNotEmpty()) {
            setAdapter()
            recyclerViewProduct!!.visibility = View.VISIBLE
            txtNoData!!.visibility = View.GONE
        } else {
            recyclerViewProduct!!.visibility = View.GONE
            txtNoData!!.visibility = View.VISIBLE
        }
    }

    private fun setAdapter() {
        productAdapter = ProductAdapter(this, productList!!)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerViewProduct!!.layoutManager = linearLayoutManager
        recyclerViewProduct!!.setHasFixedSize(true)
        recyclerViewProduct!!.adapter = productAdapter
        productAdapter!!.notifyDataSetChanged()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.floatAddProduct -> diaLogAddProduct()
            R.id.selectImageview -> chooseImage()
            R.id.btnHistory -> startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
        }
    }

    private fun diaLogAddProduct() {
        val builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = this.getLayoutInflater()
        val viewLayout: View = inflater.inflate(R.layout.dialog_addproduct, null)
        viewLayout.findViewById<View>(R.id.selectImageview).setOnClickListener(this)
        viewLayout.findViewById<View>(R.id.btnAddProduct).setOnClickListener(this)
        initViewLayoutDiaLog(viewLayout)
        builder.setView(viewLayout)
        builder.setCancelable(true)
        alertDialog = builder.create()
        alertDialog!!.show()
    }

    private fun initViewLayoutDiaLog(viewLayout: View) {
        edtName = viewLayout.findViewById<EditText>(R.id.edtName)
        edtPrice = viewLayout.findViewById<EditText>(R.id.edtPrice)
        edtCount = viewLayout.findViewById<EditText>(R.id.edtCount)
        edtDescription = viewLayout.findViewById<EditText>(R.id.edtDescription)
        val btnAddProducts = viewLayout.findViewById<Button>(R.id.btnAddProduct)
        imageViewAdd = viewLayout.findViewById(R.id.selectImageview)
        btnAddProducts.setOnClickListener {
            val name: String = edtName!!.text.toString().trim({ it <= ' ' })
            val price: String = edtPrice!!.text.toString().trim({ it <= ' ' })
            val count: String = edtCount!!.text.toString().trim({ it <= ' ' })
            val des: String = edtDescription!!.text.toString().trim({ it <= ' ' })
            // byte [] image =imageViewToByte(imageViewAdd);
            if (TextUtils.isEmpty(name)) {
                edtName!!.error = "Please input name"
            } else if (TextUtils.isEmpty(des)) {
                edtDescription!!.error = "Please input description"
            }
            var priceInt = 0
            if (TextUtils.isEmpty(price)) {
                edtPrice!!.error = "Please input price"
            } else {
                try {
                    priceInt = price.toInt()
                } catch (e: NumberFormatException) {
                    edtPrice!!.error = "Please input again"
                }
            }
            var countInt = 0
            if (TextUtils.isEmpty(count)) {
                edtCount!!.error = "Please input count"
            } else {
                try {
                    countInt = count.toInt()
                } catch (e: NumberFormatException) {
                }
            }
            if (priceInt > 0 && countInt > 0) {
                val product = Product(name, priceInt, countInt, des)
                if (!containss(product)) {
                    databaseHelper!!.insertProduct(product)
                    hasdatainlist()
                    setTotalTextView()
                    alertDialog!!.dismiss()
                } else {
                    Toast.makeText(this@MainActivity, "Please input name orther", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@MainActivity, "number than 0", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private operator fun contains(product: Product): Boolean {
        for (i in productList!!) {
            if (i.name == product.name) {
                return true
            }
        }
        return false
    }

    private fun containss(product: Product): Boolean {
        for (i in productList!!.indices) {
            if (productList!![i].name == product.name) {
                return true
            }
        }
        return false
    }

    private fun index(productArrayList: List<Product>, stringname: String): Int {
        for (i in productArrayList.indices) {
            if (productArrayList[i].name === stringname) {
                return i
            }
        }
        return -1
    }

    private fun hasInList(productArrayList: List<Product>, stringname: String): Boolean {
        for (i in productArrayList.indices) {
            if (productArrayList[i].name === stringname) {
                return true
            }
        }
        return false
    }

    private fun imageViewToByte(imageView: ImageView): ByteArray {
        val bitmap: Bitmap = (imageView.drawable as BitmapDrawable).getBitmap()
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun chooseImage() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri: Uri = data.data!!
            try {
                val inputStream: InputStream = contentResolver.openInputStream(uri)!!
                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                imageViewAdd!!.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun searchProduct() {
        search_barView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                var newText = newText
                newText = newText.toLowerCase()
                val newList: ArrayList<Product> = ArrayList<Product>()
                for (product in productList!!) {
                    val name: String = product.name!!.toLowerCase()
                    val des: String = product.des!!.toLowerCase()
                    val price: String = product.price.toString().toLowerCase()
                    val result = name + price + des
                    if (result.contains(newText)) {
                        newList.add(product)
                    }
                }
                setAdapter()
                productAdapter!!.setFilter(newList)
                return true
            }
        })
    }
}