package com.example.car;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.car.data.DataType;
import com.example.car.databinding.ActivityMainBinding;
import com.example.car.utils.SharedViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String[] names;
    private DataType[] name;
    private Drawable[] drawables;
    private static SharedViewModel model;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new SharedViewModel(this);
        if(model.getType() == null){
            model.setType(DataType.OIL);
        }
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        names = new String[]{
                getString(R.string.oil),
                getString(R.string.filter),
                getString(R.string.parts),
        };
        name = new DataType[]{
                DataType.OIL,
                DataType.FILTER,
                DataType.AUTOPARTS
        };
        drawables = new Drawable[]{
                getDrawable(R.drawable.oil),
                getDrawable(R.drawable.filter),
                getDrawable(R.drawable.parts)
        };
        List<Fragment> list = new ArrayList<>();
        list.add(FragmentItem.newInstance());
        list.add(FragmentItem.newInstance());
        list.add(FragmentItem.newInstance());
        PagerAdapter adapter = new PagerAdapter(list, getSupportFragmentManager(), getLifecycle());
        binding.viewPager.setAdapter(adapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            tab.setText(names[position]);
            tab.setIcon(drawables[position]);
        }
        );
        tabLayoutMediator.attach();
        binding.viewPager.setOffscreenPageLimit(2);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                model.setType((name[position]));
            }
        });
    }

    public static SharedViewModel getModel() {
        return model;
    }
}
