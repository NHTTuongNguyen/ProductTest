package com.example.producttest.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.producttest.R
import com.example.producttest.adapter.ViewPagerAdapter
import com.example.producttest.databinding.ActivityHomeBinding
import com.example.producttest.fragments.CartFragment
import com.example.producttest.fragments.HistoryFragment
import com.example.producttest.fragments.ProductListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding : ActivityHomeBinding
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPager = findViewById(R.id.viewpager)
        binding.navViews.setOnNavigationItemSelectedListener(this)
        setupViewPager(viewPager)

    }

    fun getViewPager(): ViewPager? {
        if (null == viewPager) {
            viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        }
        return viewPager
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        viewPager!!.adapter = viewPagerAdapter
        viewPager!!.offscreenPageLimit = 1
        viewPager!!.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.navViews.menu.findItem(R.id.navigation_home).isChecked = true
                    1 -> binding.navViews.menu.findItem(R.id.navigation_history).isChecked = true
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                viewPager!!.currentItem = 0

                return true
            }
            R.id.navigation_history -> {
                viewPager!!.currentItem = 1
//                val storyFragment: HistoryFragment = viewPager!!.adapter!!.instantiateItem(viewPager!!, 1) as HistoryFragment
//                storyFragment.ToastNe()
                return true
            }
        }
        return false
    }
}