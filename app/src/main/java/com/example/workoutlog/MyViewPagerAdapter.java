package com.example.workoutlog;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new NoviTreningFragment();
            case 1:
                return new PrijedloziTreningaFragment();
            case 2:
                return new HistoryFragment();
            default:
                return new NoviTreningFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
