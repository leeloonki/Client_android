package com.example.tool.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.tool.Adapter.ViewPagerFragmentStateAdapter;
import com.example.tool.Fragment.Mainfragment;
import com.example.tool.Fragment.SelfFragment;
import com.example.tool.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    BottomNavigationView bnView;
    ViewPager2 viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
        onClick();
    }

    private void onClick() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new Mainfragment());
        fragments.add(new SelfFragment());

        ViewPagerFragmentStateAdapter adapter=new ViewPagerFragmentStateAdapter(fragments, this);
        viewPager.setAdapter(adapter);

        //BottomNavigationView 点击事件监听
        bnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuId = item.getItemId();
                // 跳转指定页面：Fragment
                switch (menuId) {
                    case R.id.tab_one:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_two:
                        viewPager.setCurrentItem(1);
                        break;
                }
                return false;

            }
        });
        //页面滑动监听
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bnView.getMenu().getItem(position).setChecked(true);
            }
        });

    }

    private void initView() {
        bnView=findViewById(R.id.bottom_nav_view);
        viewPager=findViewById(R.id.view_pager);
    }
}
