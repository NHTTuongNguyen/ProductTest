package com.example.producttest.Share

import android.content.Context
import android.content.SharedPreferences
import com.example.producttest.model.Cart
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class SharedPreferences_Utils(var context: Context) {
    private val MyPREFERENCES = "MyPrefsSetting1111"
    private val Total = "total_Key"
    private val CART_KEY = "Cart_Key"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)

    var saveTotal: Int
        get() = sharedPreferences.getInt(Total, 0)
        set(saveTotal) {
            val editor = sharedPreferences.edit()
            editor.putInt(Total, saveTotal)
            editor.commit()
        }

    fun setSaveCartProduct(context: Context?, total: Int, currentTime: String?) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val jsonStoryWatched = sharedPreferences.getString(CART_KEY, null)
        var chartsModelArrayList = ArrayList<Cart?>()
        if (jsonStoryWatched != null) {
            val type = object : TypeToken<ArrayList<Cart?>?>() {}.type /////luu mang
            chartsModelArrayList = gson.fromJson(jsonStoryWatched, type)
        }
        chartsModelArrayList.add(0, Cart(total, currentTime))
        val json = gson.toJson(chartsModelArrayList)
        editor.putString(CART_KEY, json)
        editor.commit()
    }

    fun getSaveCartProduct(context: Context): MutableList<Cart> {
        val gson = Gson()
        val json = sharedPreferences.getString(CART_KEY, null)
        val type = object : TypeToken<ArrayList<Cart>>() {}.type
        var chartsList = gson.fromJson<ArrayList<Cart>>(json, type)
        if (chartsList == null) {
            chartsList = ArrayList()
        }
        return chartsList
    }

    fun removeSaveCartProduct() {
        val editor = sharedPreferences.edit()
        editor.remove(CART_KEY)
        editor.commit()
    }




}