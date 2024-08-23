package com.example.car;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.car.data.DataType;
import com.example.car.databinding.ActivityMainBinding;
import com.example.car.utils.SharedViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String[] names;
    private ActivityMainBinding binding;
    private SharedViewModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(SharedViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        names = new String[]{
                getString(R.string.oil),
                getString(R.string.filter),
                getString(R.string.parts),
        };
        List<Fragment> list = new ArrayList<>();
        list.add(FragmentItem.newInstance(getString(R.string.oil)));
        list.add(FragmentItem.newInstance(getString(R.string.filter)));
        list.add(FragmentItem.newInstance(getString(R.string.parts)));
        PagerAdapter adapter = new PagerAdapter(list, getSupportFragmentManager(), getLifecycle());
        binding.viewPager.setAdapter(adapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                binding.tabLayout,
                binding.viewPager,
                (tab, position) -> tab.setText(names[position])
        );
        tabLayoutMediator.attach();
        binding.viewPager.setOffscreenPageLimit(2);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                model.setType(DataType.valueOf(names[position]));
            }
        });
    }
}
