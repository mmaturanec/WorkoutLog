package com.example.workoutlog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainMenuActivity extends AppCompatActivity  {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ImageView ivSettings, infoButtonMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);
        ivSettings = findViewById(R.id.ivSettings);
        infoButtonMainMenu = findViewById(R.id.infoButtonMainMenu);
        int initialFragmentPosition = getIntent().getIntExtra("INITIAL_FRAGMENT", 0);

        if( initialFragmentPosition != 0)
        {
            viewPager2.post(() -> viewPager2.setCurrentItem(initialFragmentPosition, false));

        }

        infoButtonMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentTabPosition = tabLayout.getSelectedTabPosition();
                if(currentTabPosition == 0)
                {
                    InfoODodavanjuVjezbiDialog.showInfoDialog(MainMenuActivity.this);

                }
                if(currentTabPosition == 1)
                {
                    InfoODodavanjuVjezbiDialog.showInfoDialog(MainMenuActivity.this);

                }
                if(currentTabPosition == 2)
                {

                }
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SettingsIntent = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(SettingsIntent);
                overridePendingTransition(0, 0);

            }
        });

    }
}