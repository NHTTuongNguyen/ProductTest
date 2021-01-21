package com.example.producttest.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.producttest.R
import com.example.producttest.adapter.ViewPagerHomeAdapter
import com.example.producttest.fragments.CartFragment
import com.example.producttest.fragments.HistoryFragment
import com.example.producttest.fragments.ProductListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private var bottomNavigationView: BottomNavigationView? = null
    private val fragmentTransaction: FragmentTransaction? = null
    private var productListFragment: ProductListFragment? = null
    private val cartFragment: CartFragment? = null
    private var historyFragment: HistoryFragment? = null
    var menuItem: MenuItem? = null
    private var viewPager: ViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bottomNavigationView = findViewById(R.id.nav_views)
        bottomNavigationView!!.setOnNavigationItemSelectedListener(this)
        viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        viewPager!!.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if (menuItem != null) {
                    menuItem!!.isChecked = false
                } else {
                    bottomNavigationView!!.menu.getItem(0).isChecked = false
                }
                Log.d("pagess", "onPageSelected: $position")
                bottomNavigationView!!.menu.getItem(position).isChecked = true
                menuItem = bottomNavigationView!!.menu.getItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        setupViewPager(viewPager)
    }

    fun getViewPager(): ViewPager? {
        if (null == viewPager) {
            viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        }
        return viewPager
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerHomeAdapter(supportFragmentManager)
        productListFragment = ProductListFragment()
        historyFragment = HistoryFragment()
        adapter.addFragment(productListFragment!!)
        adapter.addFragment(historyFragment!!)
        viewPager!!.adapter = adapter
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                viewPager!!.currentItem = 0
                return true
            }
            R.id.navigation_history -> {
                viewPager!!.currentItem = 1
                return true
            }
        }
        return false
    }
}