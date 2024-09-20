package com.example.car;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.car.data.DataType;
import com.example.car.databinding.ActivityMainBinding;
import com.example.car.utils.SharedViewModel;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> names;
    private DataType[] name;
    private Drawable[] drawables;
    private static SharedViewModel model;
    private static ActivityMainBinding binding;
    public static PagerAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new SharedViewModel(this);
        if (model.getType() == null) {
            model.setType(DataType.OIL);
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        closeMenu();
        names = new ArrayList<String>() {
        };
        names.add(getString(R.string.oil));
        names.add(getString(R.string.filter));
        names.add(getString(R.string.parts));
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
        adapter = new PagerAdapter(list, getSupportFragmentManager(), getLifecycle());
        binding.viewPager.setAdapter(adapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            tab.setText(names.get(position));
            tab.setIcon(drawables[position]);
        });
        tabLayoutMediator.attach();
        binding.viewPager.setOffscreenPageLimit(2);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                model.setType((name[position]));
                binding.viewPager.setCurrentItem(position);
            }
        });
        binding.menuFAB.setOnClickListener(new View.OnClickListener() {
            private boolean isOpened = false;

            @Override
            public void onClick(View v) {
                if (isOpened) {
                    closeMenu();
                } else {
                    openMenu();
                }
                isOpened = !isOpened;
            }
        });
        binding.addFAB.setOnClickListener(v -> {
            closeMenu();
            Fragment fragment = AddFragment.newInstance(model.getType().toString(), binding.viewPager.getCurrentItem());
            binding.viewPager.setVisibility(View.INVISIBLE);
            binding.viewPager.setAdapter(null);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        binding.addByQRFAB.setOnClickListener(v -> {
            closeMenu();
            binding.viewPager.setVisibility(View.INVISIBLE);
            binding.viewPager.setAdapter(null);
            qrScannerLauncher.launch(createIntegratorIntent());
        });
        binding.findByQRFAB.setOnClickListener(v -> {
            closeMenu();
            qrScannerLauncher2.launch(createIntegratorIntent());
        });
        binding.findFAB.setOnClickListener(v -> {
            closeMenu();
            DialogFragment dialogFragment = SearchFilter.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "TAG");
        });
    }

    private Intent createIntegratorIntent() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("SCAN");
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setCameraId(0);
        return integrator.createScanIntent();
    }

    private final ActivityResultLauncher<Intent> qrScannerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String resultContents = data.getStringExtra("SCAN_RESULT");
                        if (resultContents != null) {
                            Fragment fragment = AddFragment.newInstance2(model.getType().toString(), resultContents);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "No scan data received!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private final ActivityResultLauncher<Intent> qrScannerLauncher2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String resultContents = data.getStringExtra("SCAN_RESULT");
                        if (resultContents != null) {
                            DialogFragment dialogFragment = Found.newInstance(resultContents);
                            dialogFragment.show(getSupportFragmentManager(), "TAG");
                        } else {
                            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "No scan data received!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void openMenu() {
        binding.addFAB.setVisibility(View.VISIBLE);
        binding.addByQRFAB.setVisibility(View.VISIBLE);
        binding.findFAB.setVisibility(View.VISIBLE);
        binding.findByQRFAB.setVisibility(View.VISIBLE);
        binding.addFAB.animate().translationY(0).start();
        binding.addByQRFAB.animate().translationY(0).start();
        binding.findFAB.animate().translationY(0).start();
        binding.findByQRFAB.animate().translationY(0).start();
    }

    private void closeMenu() {
        binding.addFAB.animate().translationY(150).withEndAction(() -> binding.addFAB.setVisibility(View.GONE)).start();
        binding.addByQRFAB.animate().translationY(350).withEndAction(() -> binding.addByQRFAB.setVisibility(View.GONE)).start();
        binding.findFAB.animate().translationY(550).withEndAction(() -> binding.findFAB.setVisibility(View.GONE)).start();
        binding.findByQRFAB.animate().translationY(750).withEndAction(() -> binding.findByQRFAB.setVisibility(View.GONE)).start();
    }

    public static SharedViewModel getModel() {
        return model;
    }


    public static void update(int a) {
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setCurrentItem(a);
        binding.viewPager.setVisibility(View.VISIBLE);
    }

}
