package com.example.car;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.car.data.DataType;
import com.example.car.databinding.ActivityMainBinding;
import com.example.car.utils.SharedViewModel;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> names;

    private final DataType[] name = new DataType[]{
            DataType.OIL,
            DataType.FILTER,
            DataType.AUTO_PARTS
    };

    private final int[] drawables = new int[]{
            R.drawable.oil,
            R.drawable.filter,
            R.drawable.parts
    };

    private SharedViewModel model;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(SharedViewModel.class);
        model.init(this);
        model.setAdapter(new PagerAdapter(getSupportFragmentManager(),getLifecycle()));
        if (model.getType() == null) {
            model.setType(DataType.OIL);
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        model.setPager2(binding.viewPager);
        setContentView(binding.getRoot());
        closeMenu();
        names = new ArrayList<String>() {
        };
        names.add(getString(R.string.oil));
        names.add(getString(R.string.filter));
        names.add(getString(R.string.parts));
        if(binding.viewPager.getAdapter()==null){
            binding.viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(),getLifecycle()));
        }
        @SuppressLint("UseCompatLoadingForDrawables")
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            tab.setText(names.get(position));
            tab.setIcon(getDrawable(drawables[position]));
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
            binding.viewPager.setAdapter(null);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        binding.addByQRFAB.setOnClickListener(v -> {
            closeMenu();
            qrScannerLauncher.launch(createIntegratorIntent());

        });
        binding.findByQRFAB.setOnClickListener(v -> {
            closeMenu();
            qrScannerLauncher2.launch(createIntegratorIntent());

        });
        binding.findFAB.setOnClickListener(v -> {
            closeMenu();
            DialogFragment dialogFragment = SearchFilter.newInstance(binding.viewPager.getCurrentItem());
            dialogFragment.show(getSupportFragmentManager(), "TAG");
        });

        model.viewState.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){

                }
            }
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
                            binding.viewPager.setVisibility(View.INVISIBLE);
                            binding.viewPager.setAdapter(null);
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
                            DialogFragment dialogFragment = FindDialog.newInstance(resultContents, binding.viewPager.getCurrentItem());
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
        binding.addFAB.animate().translationY(0).withEndAction(() -> binding.addFAB.setClickable(true)).start();
        binding.addByQRFAB.animate().translationY(0).withEndAction(() -> binding.addByQRFAB.setClickable(true)).start();
        binding.findFAB.animate().translationY(0).withEndAction(() -> binding.findFAB.setClickable(true)).start();
        binding.findByQRFAB.animate().translationY(0).withEndAction(() -> binding.findByQRFAB.setClickable(true)).start();
    }

    private void closeMenu() {
        binding.addFAB.animate().translationY(200).withEndAction(() -> binding.addFAB.setClickable(false)).start();
        binding.addByQRFAB.animate().translationY(380).withEndAction(() -> binding.addByQRFAB.setClickable(false)).start();
        binding.findFAB.animate().translationY(570).withEndAction(() -> binding.findFAB.setClickable(false)).start();
        binding.findByQRFAB.animate().translationY(760).withEndAction(() -> binding.findByQRFAB.setClickable(false)).start();
    }

//    public void updateActivity(int a) {
//        binding.viewPager.setAdapter(adapter);
//        binding.viewPager.setCurrentItem(a);
//        binding.viewPager.setVisibility(View.VISIBLE);
//    }

}
