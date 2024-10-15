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
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.car.data.DataType;
import com.example.car.databinding.ActivityMainBinding;
import com.example.car.utils.MainActivityViewModel;
import com.example.car.utils.SharedViewModel;
import com.google.android.material.tabs.TabLayoutMediator;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int SCAN_FOR_ADD_CODE = 100;
    private static final int SCAN_FOR_FIND_CODE = 120;
    private int currentRequest;
    private SharedViewModel model;
    private MainActivityViewModel mainModel;
    private ActivityMainBinding binding;

    //    @StringRes
//    private final int[] pageNameResArr = new int[]{
//           R.string.oil,
//           R.string.filter,
//           R.string.parts
//    };
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(SharedViewModel.class);
        model.init(this);
        mainModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        closeMenu();

        binding.viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), getLifecycle()));
        @SuppressLint("UseCompatLoadingForDrawables")
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            //tab.setText(getString(pageNameResArr[position]));
            tab.setText(getString(mainModel.getString(position)));
            tab.setIcon(mainModel.getDrawable(position));
        });
        tabLayoutMediator.attach();

        binding.viewPager.setOffscreenPageLimit(2);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                model.viewState.setValue(mainModel.getName(position));
            }
        });

        binding.viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), getLifecycle()));
        model.viewState.observe(this, type ->
                updateActivity(model.viewState.getValue())
        );

        addFabListeners();
    }


    private void addFabListeners() {
        binding.menuFAB.setOnClickListener(new View.OnClickListener() {
            public boolean isOpened = false;

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
        binding.addFAB.setOnClickListener(v -> handleFabAction(FabAction.ADD_PRODUCT));
        binding.addByQRFAB.setOnClickListener(v -> handleFabAction(FabAction.ADD_BY_QR));
        binding.findByQRFAB.setOnClickListener(v -> handleFabAction(FabAction.FIND_BY_QR));
        binding.findFAB.setOnClickListener(v -> handleFabAction(FabAction.FIND));

    }

    private void handleFabAction(FabAction action) {
        binding.menuFAB.performClick();
        switch (action) {
            case ADD_PRODUCT: {
                setVisibilityOfFABs(View.INVISIBLE);
                Fragment fragment = AddFragment.newInstance(Objects.requireNonNull(model.viewState.getValue()).toString(), null);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
                break;
            }
            case ADD_BY_QR: {
                qrScannerLauncher.launch(createScanIntent(SCAN_FOR_ADD_CODE));
                break;
            }
            case FIND_BY_QR: {
                qrScannerLauncher.launch(createScanIntent(SCAN_FOR_FIND_CODE));
                break;
            }
            case FIND: {
                SearchFilter.newInstance().show(getSupportFragmentManager(), "TAG");
                break;
            }
        }
    }

    private Intent createScanIntent(int code) {
        currentRequest = code;
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setPrompt("SCANNING");
        scanOptions.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
        scanOptions.setCameraId(0);
        return scanOptions.createScanIntent(this);
    }

    private final ActivityResultLauncher<Intent> qrScannerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String resultContents = data.getStringExtra("SCAN_RESULT");
                        if (currentRequest == SCAN_FOR_ADD_CODE) {
                            Fragment fragment = AddFragment.newInstance(Objects.requireNonNull(model.viewState.getValue()).toString(), resultContents);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        } else if (currentRequest == SCAN_FOR_FIND_CODE) {
                            DialogFragment dialogFragment = FindDialog.newInstance(resultContents);
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


    private void updateActivity(DataType type) {
        if (type == DataType.OIL) {
            binding.viewPager.setCurrentItem(0);
        } else if (type == DataType.FILTER) {
            binding.viewPager.setCurrentItem(1);
        } else {
            binding.viewPager.setCurrentItem(2);
        }
        setVisibilityOfFABs(View.VISIBLE);
    }

    private enum FabAction {
        ADD_PRODUCT,
        FIND_BY_QR,
        ADD_BY_QR,
        FIND
    }

    private void setVisibilityOfFABs(int visibility) {
        binding.menuFAB.setVisibility(visibility);
        binding.addFAB.setVisibility(visibility);
        binding.addByQRFAB.setVisibility(visibility);
        binding.findByQRFAB.setVisibility(visibility);
        binding.findFAB.setVisibility(visibility);
    }

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
}
