package com.example.producttest.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.producttest.activity.HomeActivity
import com.example.producttest.fragments.HistoryFragment
import com.example.producttest.fragments.ProductListFragment
import java.util.*

class ViewPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ProductListFragment()
            1 -> HistoryFragment()
            else -> ProductListFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }
}