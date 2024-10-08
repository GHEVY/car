package com.example.car;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.car.data.DataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragments = new ArrayList<>(Arrays.asList(
            FragmentItem.newInstance(DataType.OIL),
            FragmentItem.newInstance(DataType.FILTER),
            FragmentItem.newInstance(DataType.AUTO_PARTS)
            ));


    public PagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }


}