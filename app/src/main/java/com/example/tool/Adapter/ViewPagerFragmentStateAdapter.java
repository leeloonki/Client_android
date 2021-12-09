package com.example.tool.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;

public class ViewPagerFragmentStateAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> fragmentSparseArray;

    public ViewPagerFragmentStateAdapter(ArrayList<Fragment> fragmentSparseArray, @NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.fragmentSparseArray=fragmentSparseArray;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentSparseArray.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentSparseArray.size();
    }

}
