package com.example.producttest.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.producttest.R;
import com.example.producttest.adapter.ViewPagerHomeAdapter;
import com.example.producttest.fragments.CartFragment;
import com.example.producttest.fragments.HistoryFragment;
import com.example.producttest.fragments.ProductListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction fragmentTransaction;
    private ProductListFragment productListFragment;
    private CartFragment cartFragment;
    private HistoryFragment historyFragment;
    MenuItem menuItem;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.nav_views);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("pagess", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                menuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);
    }

    public ViewPager getViewPager() {
        if (null == viewPager) {
            viewPager = (ViewPager) findViewById(R.id.viewpager);
        }
        return viewPager;
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerHomeAdapter adapter = new ViewPagerHomeAdapter(getSupportFragmentManager());
        productListFragment = new ProductListFragment();
        historyFragment = new HistoryFragment();
        adapter.addFragment(productListFragment);
        adapter.addFragment(historyFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.navigation_home:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.navigation_history:
                viewPager.setCurrentItem(1);
                return true;
        }
        return false;
    }



}