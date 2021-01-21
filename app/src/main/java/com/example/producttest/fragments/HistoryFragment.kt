package com.example.producttest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.producttest.R
import com.example.producttest.Share.SharedPreferences_Utils
import com.example.producttest.activity.HomeActivity
import com.example.producttest.adapter.HistoryAdapter
import com.example.producttest.model.Cart
import kotlinx.android.synthetic.main.fragment_history.*
import java.util.*

class HistoryFragment : Fragment() {
    private val toolbar_History: Toolbar? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var recyclerViewHistory: RecyclerView? = null
    private var cartArrayList: MutableList<Cart>? = null
    private var sharedPreferences_utils: SharedPreferences_Utils? = null
    private var adapter: HistoryAdapter? = null
//    private var txtNoData: TextView? = null
//    private var btnRemove: Button? = null
    private var views: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_history, container, false)
        sharedPreferences_utils = SharedPreferences_Utils(requireActivity())
        initView()
        return view
    }

    override fun onResume() {
        super.onResume()
        buidRecyclerView()
    }

    private fun initView() {
//        btnRemove = views!!.findViewById(R.id.btnRemove)
        btnRemoveFr.setOnClickListener(View.OnClickListener {
            if (cartArrayList!!.size > 0) {
                sharedPreferences_utils!!.removeSaveCartProduct()
                //                    finish();

//                    startActivity(getIntent());
//                    getActivity().onBackPressed();
                (activity as HomeActivity?)!!.getViewPager()!!.currentItem = 0
            } else {
                Toast.makeText(activity,
                        "Not Data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun buidRecyclerView() {
        cartArrayList = sharedPreferences_utils!!.getSaveCartProduct(requireActivity())
        if (cartArrayList!!.size > 0) {
            adapter = HistoryAdapter(requireContext(), cartArrayList!!)
            linearLayoutManager = LinearLayoutManager(activity)
            recyclerViewHistory!!.layoutManager = linearLayoutManager
            recyclerViewHistory!!.setHasFixedSize(true)
            recyclerViewHistory!!.adapter = adapter
            adapter!!.notifyDataSetChanged()
            recyclerViewHistory!!.visibility = View.VISIBLE
            txtNoDataHistoryFr.visibility = View.GONE
        } else {
            recyclerViewHistory!!.visibility = View.GONE
            txtNoDataHistoryFr.visibility = View.VISIBLE
        }
    }
}