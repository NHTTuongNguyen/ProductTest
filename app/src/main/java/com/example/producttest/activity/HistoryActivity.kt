package com.example.producttest.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.producttest.R
import com.example.producttest.Share.SharedPreferences_Utils
import com.example.producttest.adapter.HistoryAdapter
import com.example.producttest.model.Cart
import java.util.*

class HistoryActivity : AppCompatActivity() {
    private var toolbar_History: Toolbar? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var recyclerViewHistory: RecyclerView? = null
    private var cartArrayList: MutableList<Cart>? = null
    private var sharedPreferences_utils: SharedPreferences_Utils? = null
    private var adapter: HistoryAdapter? = null
    private var txtNoData: TextView? = null
    private var btnRemove: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        actionToolBar()
        sharedPreferences_utils = SharedPreferences_Utils(this)
        initView()
        cartArrayList = sharedPreferences_utils!!.getSaveCartProduct(this)
        buidRecyclerView()
    }

    private fun initView() {
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory)
        txtNoData = findViewById(R.id.txtNoDataHistory)
        btnRemove = findViewById(R.id.btnRemove)
        btnRemove!!.setOnClickListener(View.OnClickListener {
            if (cartArrayList!!.size > 0) {
                sharedPreferences_utils!!.removeSaveCartProduct()
                finish()
                startActivity(intent)
            } else {
                Toast.makeText(this@HistoryActivity,
                        "Not Data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun buidRecyclerView() {
        if (cartArrayList!!.size > 0) {
            adapter = HistoryAdapter(this@HistoryActivity, cartArrayList!!)
            linearLayoutManager = LinearLayoutManager(this)
            recyclerViewHistory!!.layoutManager = linearLayoutManager
            recyclerViewHistory!!.setHasFixedSize(true)
            recyclerViewHistory!!.adapter = adapter
            adapter!!.notifyDataSetChanged()
            recyclerViewHistory!!.visibility = View.VISIBLE
            txtNoData!!.visibility = View.GONE
        } else {
            recyclerViewHistory!!.visibility = View.GONE
            txtNoData!!.visibility = View.VISIBLE
        }
    }

    private fun actionToolBar() {
        toolbar_History = findViewById(R.id.toolbar_History)
        setSupportActionBar(toolbar_History)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar_History!!.setNavigationOnClickListener(View.OnClickListener { finish() })
    }
}