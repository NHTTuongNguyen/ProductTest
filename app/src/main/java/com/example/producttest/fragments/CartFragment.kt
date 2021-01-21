package com.example.producttest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.producttest.R

class CartFragment : Fragment() {
    var views: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        views = inflater.inflate(R.layout.fragment_cart, container, false)
        return views
    }
}