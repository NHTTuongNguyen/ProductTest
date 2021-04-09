package com.example.producttest.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.producttest.R
import com.example.producttest.Share.SharedPreferences_Utils
import com.example.producttest.activity.HomeActivity
import com.example.producttest.adapter.ProductAdapter
import com.example.producttest.database.DatabaseHelper
import com.example.producttest.model.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.ByteArrayOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ProductListFragment : Fragment(), View.OnClickListener {
    private var views: View? = null
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
    private val suggesList: List<Product> = ArrayList<Product>()
    private var fragmentTransaction: FragmentTransaction? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        views = inflater.inflate(R.layout.fragment_product_list, container, false)
        databaseHelper = DatabaseHelper(activity)
        sharedPreferences_utils = SharedPreferences_Utils(requireActivity())
        initView()
        hasdatainlist()
        searchProduct()
        setTotalTextView()
        return views
    }

    fun setTotalTextView() {
        val total = totalProduct()
        val locale = Locale("vi", "VN")
        val fmt = NumberFormat.getCurrencyInstance(locale)
        txtTotalProduct!!.setText(fmt.format(total.toLong()))
    }

    private fun totalProduct(): Int {
        var total = 0
        for (product in productList!!) total += product.price * product.count
        return total
    }

    private fun initView() {
        search_barView = views!!.findViewById(R.id.search_barView)
        txtTotalProduct = views!!.findViewById<TextView>(R.id.txtTotalProduct)
        recyclerViewProduct = views!!.findViewById(R.id.recyclerViewProduct)
        txtNoData = views!!.findViewById<TextView>(R.id.txtNoData)
        views!!.findViewById<View>(R.id.txtNoData).setOnClickListener(this)
        btnProduct = views!!.findViewById(R.id.btnProduct)
        views!!.findViewById<View>(R.id.btnHistory).setOnClickListener(this)
        btnProduct!!.setOnClickListener(View.OnClickListener {
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
                sharedPreferences_utils!!.setSaveCartProduct(activity, total, curTime)
                databaseHelper!!.removeAll()
                (activity as HomeActivity?)!!.getViewPager()!!.currentItem = 1
                //                    setFragment(new TestFragment());
//                    Fragment fragment = new HistoryFragment();
//                    getChildFragmentManager().beginTransaction()
//                            .replace(R.id.container, fragment)
//                            .addToBackStack(null)
//                            .commit();
            } else {
                Toast.makeText(activity, "Your Cart Empty !!!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setFragment(fragment: Fragment) {
        fragmentTransaction = requireFragmentManager().beginTransaction()
        fragmentTransaction!!.replace(R.id.frame_container, fragment)
        //        fragmentTransaction.addToBackStack(null);
        fragmentTransaction!!.commit()
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
        productAdapter = ProductAdapter(requireActivity(), productList!!, this@ProductListFragment)
        linearLayoutManager = LinearLayoutManager(activity)
        recyclerViewProduct!!.layoutManager = linearLayoutManager
        recyclerViewProduct!!.setHasFixedSize(true)
        recyclerViewProduct!!.adapter = productAdapter
        productAdapter!!.notifyDataSetChanged()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.selectImageview -> chooseImage()
            R.id.btnHistory -> diaLogAddProduct()
            R.id.txtNoData -> diaLogAddProduct()
        }
    }

    private fun diaLogAddProduct() {
        val builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = this.layoutInflater
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
            val name: String = edtName!!.text.toString().trim { it <= ' ' }
            val price: String = edtPrice!!.text.toString().trim { it <= ' ' }
            val count: String = edtCount!!.text.toString().trim { it <= ' ' }
            val des: String = edtDescription!!.text.toString().trim { it <= ' ' }
            if (name != "" && price != "" && count != "" && des != "") {
                var priceInt = 0
                priceInt = price.toInt()
                var countInt = 0
                countInt = count.toInt()
                if (priceInt >= 500 && countInt > 0) {
                    val product = Product(name, priceInt, countInt, des)
                    if (!containss(product)) {
                        databaseHelper!!.insertProduct(product)
                        hasdatainlist()
                        setTotalTextView()
                        alertDialog!!.dismiss()
                    } else {
                        Toast.makeText(activity, "Vui lòng nhập tên khác", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (priceInt < 500) {
                        edtPrice!!.error = "Tối thiểu là 500đ"
                    }
                    if (countInt <= 0) {
                        edtCount!!.error = "Nhập đúng số lượng"
                    }
                }
            } else {
                if (name == "") {
                    edtName!!.error = resources.getString(R.string.data_not_null)
                }
                if (price == "") {
                    edtPrice!!.error = resources.getString(R.string.data_not_null)
                }
                if (count == "") {
                    edtCount!!.error = resources.getString(R.string.data_not_null)
                }
                if (des == "") {
                    edtDescription!!.error = resources.getString(R.string.data_not_null)
                }
            }
        }
    }

    private operator fun contains(product: Product): Boolean {
        for (i in productList!!) {
            if (i.name!! == product.name!!) {
                return true
            }
        }
        return false
    }

    private fun containss(product: Product): Boolean {
        for (i in productList!!.indices) {
            if (productList!![i].name!! == product.name!!) {
                return true
            }
        }
        return false
    }

    private fun index(productArrayList: List<Product>, stringname: String): Int {
        for (i in productArrayList.indices) {
            if (productArrayList[i].name!! === stringname) {
                return i
            }
        }
        return -1
    }

    private fun hasInList(productArrayList: List<Product>, stringname: String): Boolean {
        for (i in productArrayList.indices) {
            if (productArrayList[i].name!! === stringname) {
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
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
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
                    val price: String = product.price!!.toString().toLowerCase()
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