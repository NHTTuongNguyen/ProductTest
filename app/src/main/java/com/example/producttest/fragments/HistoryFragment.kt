package com.example.producttest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.producttest.R
import com.example.producttest.Share.SharedPreferences_Utils
import com.example.producttest.activity.HomeActivity
import com.example.producttest.adapter.HistoryAdapter
import com.example.producttest.databinding.FragmentHistoryBinding
import com.example.producttest.model.Cart
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {


    private val toolbar_History: Toolbar? = null
    private var linearLayoutManager: LinearLayoutManager? = null
//    private var recyclerViewHistory: RecyclerView? = null
    private var cartArrayList: MutableList<Cart>? = null
    private var sharedPreferences_utils: SharedPreferences_Utils? = null
    private var adapter: HistoryAdapter? = null
//    private var txtNoData: TextView? = null
//    private var btnRemove: Button? = null
    private var views: View? = null
    private lateinit var  binding :FragmentHistoryBinding;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater,container,false)
        sharedPreferences_utils = SharedPreferences_Utils(requireActivity())
        initView()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        buildList()
    }

    private fun initView() {
        binding.btnRemoveFr.setOnClickListener(View.OnClickListener {
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

     public fun buildList() {
        cartArrayList = sharedPreferences_utils!!.getSaveCartProduct(requireActivity())
        if (cartArrayList!!.size > 0) {
            adapter = HistoryAdapter(requireContext(), cartArrayList!!)
            linearLayoutManager = LinearLayoutManager(activity)
            binding.recyclerViewHistoryFr.layoutManager = linearLayoutManager
            binding.recyclerViewHistoryFr.setHasFixedSize(true)
            binding.recyclerViewHistoryFr.adapter = adapter
            adapter!!.notifyDataSetChanged()
            binding.recyclerViewHistoryFr.visibility = View.VISIBLE
            txtNoDataHistoryFr.visibility = View.GONE
        } else {
            binding.recyclerViewHistoryFr.visibility = View.GONE
            txtNoDataHistoryFr.visibility = View.VISIBLE
        }
    }
    fun ToastNe(){
        Toast.makeText(activity, "Lo", Toast.LENGTH_SHORT).show()

    }
}