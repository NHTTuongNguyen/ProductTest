package com.example.producttest.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.producttest.R
import com.example.producttest.activity.MainActivity
import com.example.producttest.activity.ProductDetailActivity
import com.example.producttest.adapter.ProductAdapter.ViewHolderProduct
import com.example.producttest.database.DatabaseHelper
import com.example.producttest.fragments.ProductListFragment
import com.example.producttest.model.Product
import java.text.NumberFormat
import java.util.*


class ProductAdapter : RecyclerView.Adapter<ViewHolderProduct> {
    var context: Context
    var productList: MutableList<Product>
    private val PICK_IMAGE_REQUEST = 71
    var edtName: EditText? = null
    var edtPrice: EditText? = null
    var edtDes: EditText? = null
    var edtCount: EditText? = null
    var textViewId: TextView? = null
    var alertDialog: AlertDialog? = null
    var productListFragment: ProductListFragment? = null
    var mainActivity : MainActivity? = null

    constructor(context: Context, productList: MutableList<Product>) {
        this.context = context
        this.productList = productList
    }

    constructor(context: Context, productList: MutableList<Product>, productListFragment: ProductListFragment?) {
        this.context = context
        this.productList = productList
        this.productListFragment = productListFragment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderProduct {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ViewHolderProduct(view)
    }

    override fun onBindViewHolder(holder: ViewHolderProduct, position: Int) {
        val product = productList[position]
        //        holder.txtId.setText("Id "+product.getId());
        holder.txtName.text = product.name
        val locale = Locale("vi", "VN")
        val fmt = NumberFormat.getCurrencyInstance(locale)
        holder.txtPrice.text = fmt.format(product.price.toLong())
        holder.txtCount.text = product.count.toString() + ""
        holder.txtDes.text = product.des
        //
//        byte [] imageProduct = product.getImage();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(imageProduct,0,imageProduct.length);
//        holder.imgProduct.setImageBitmap(bitmap);
        holder.imgDelete.setOnClickListener {
            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            builder.setTitle("Delete Cart")
            builder.setMessage("Do you want to delete: " + productList[position].name + "")
            builder.setPositiveButton("Yes") { dialogInterface, positions ->
                removeProduct(position)
                //                        ((MainActivity)context).setTotalTextView();
//                productListFragment!!.setTotalTextView()
                (context as MainActivity).setTotalTextView()
            }
            builder.setNegativeButton("No") { dialogInterface, i -> }
            builder.show()
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    private fun totalProduct(): Int {
        var total = 0
        for (product in productList) total += product.price
        Log.d("sd", total.toString() + "")
        return total
    }

    inner class ViewHolderProduct(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtName: TextView
        var txtPrice: TextView
        var txtDes: TextView
        var txtId: TextView? = null
        var txtCount: TextView
//        var imgProduct: ImageView
        var imgDelete: ImageView
        var img_Productss: ImageView
        private val linearLayoutDelete: LinearLayout? = null

        init {
            img_Productss = itemView.findViewById(R.id.img_Productss)
            txtName = itemView.findViewById(R.id.txtName)
            txtPrice = itemView.findViewById(R.id.txtPrice)
            txtDes = itemView.findViewById(R.id.txtDes)
//            imgProduct = itemView.findViewById(R.id.imgProduct)
            imgDelete = itemView.findViewById(R.id.delete_cart)
            txtCount = itemView.findViewById(R.id.txtCount)
            itemView.setOnClickListener { //
                val intent = Intent(context, ProductDetailActivity::class.java)
                intent.putExtra("ProductKey", productList[position])
                context.startActivity(intent)
                Log.d("ASDAS", "SADAS")
            }
            ///
            itemView.setOnLongClickListener {
                val item = arrayOf<CharSequence>("Update", "Delete")
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Choose an action ")
                builder.setItems(item) { dialogInterface, i ->
                    if (i == 0) {
                        dialogUpdate(position)
                    } else {
                        removeProduct(position)
                    }
                }
                builder.show()
                true
            }
        }

    }

    private fun removeProduct(position: Int) {
        val product = productList[position]
        val id = product.id
        val databaseHelper = DatabaseHelper(context)
        databaseHelper.deleteProduct(id!!)
//        mainActivity!!.hasdatainlist();
//        mainActivity!!.setTotalTextView();
//        productListFragment!!.hasdatainlist()
//        productListFragment!!.setTotalTextView()
        (context as MainActivity).hasdatainlist()
        (context as MainActivity).setTotalTextView()
    }

    private fun dialogUpdate(position: Int) {
        val builder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewLayout = inflater.inflate(R.layout.dialog_addproduct, null)
        builder.setView(viewLayout)
        textViewId = viewLayout.findViewById(R.id.edtID)
        edtName = viewLayout.findViewById(R.id.edtName)
        edtPrice = viewLayout.findViewById(R.id.edtPrice)
        edtCount = viewLayout.findViewById(R.id.edtCount)
        edtDes = viewLayout.findViewById(R.id.edtDescription)
        val img = viewLayout.findViewById<ImageView>(R.id.selectImageviewUpdate)
        val btn = viewLayout.findViewById<Button>(R.id.btnAddProduct)
        btn.setOnClickListener(View.OnClickListener {
            val products = productList[position]
            edtName!!.setText(products.name)
            val name = edtName!!.text.toString()
            val priceString = edtPrice!!.text.toString()
            val count = edtCount!!.text.toString().trim { it <= ' ' }
            val des = edtDes!!.text.toString()
            if (TextUtils.isEmpty(name)) {
                edtName!!.error = "Please input name"
                return@OnClickListener
            } else if (TextUtils.isEmpty(des)) {
                edtDes!!.error = "Please input description"
                return@OnClickListener
            }
            var priceInteger = 0
            if (TextUtils.isEmpty(priceString)) {
                edtPrice!!.error = "Please input price"
            } else {
                try {
                    priceInteger = priceString.toInt()
                } catch (e: NumberFormatException) {
                    edtPrice!!.error = "Please input is number"
                }
            }
            var countss = 0
            if (TextUtils.isEmpty(count)) {
                edtCount!!.error = "Please input count"
            } else {
                try {
                    countss = count.toInt()
                } catch (e: NumberFormatException) {
                }
            }
            ////update
            if (priceInteger > 0 && countss > 0) {
                val product = productList[position]
                val databaseHelper = DatabaseHelper(context)
                databaseHelper.updateProducts(Product(product.id, name, priceInteger, countss, des))
                //                    ((MainActivity) context).hasdatainlist();
//                    ((MainActivity)context).setTotalTextView();
                productListFragment!!.hasdatainlist()
                productListFragment!!.setTotalTextView()
                alertDialog!!.dismiss()
            } else {
                Toast.makeText(context, "Please input again", Toast.LENGTH_SHORT).show()
            }
        })
        alertDialog = builder.create()
        alertDialog!!.show()
    }

    fun setFilter(newList: List<Product>) {
        productList = ArrayList()
        productList!!.addAll(newList)
        notifyDataSetChanged()
    }
}