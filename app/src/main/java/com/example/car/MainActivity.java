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
    private static final int SCAN_FOR_ADD_CODE = 100;
    private static final int SCAN_FOR_FIND_CODE = 100;

    private final ArrayList<String> names = new ArrayList<>();

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
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        closeMenu();
        names.add(getString(R.string.oil));
        names.add(getString(R.string.filter));
        names.add(getString(R.string.parts));
        if (binding.viewPager.getAdapter() == null) {
            binding.viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), getLifecycle()));
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
                model.viewState.setValue(name[position]);
            }
        });

        addFabListeners();
        binding.viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), getLifecycle()));

        model.newUpdate.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(MainActivity.this, "change", Toast.LENGTH_SHORT).show();
                    updateActivity(model.viewState.getValue());
                }
            }
        });

    }

    private void addFabListeners() {
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
            handleFabAction(FabAction.ADD_PRODUCT);
            binding.menuFAB.setVisibility(View.GONE);
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

    }

    private Intent createIntegratorIntent() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("SCAN");
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setCameraId(0);
        integrator.setRequestCode(SCAN_FOR_ADD_CODE);
        return integrator.createScanIntent();
    }

    private final ActivityResultLauncher<Intent> qrScannerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String resultContents = data.getStringExtra("SCAN_RESULT");
                        int code = data.getIntExtra("REQUEST_CODE", 0);
                        if(code == SCAN_FOR_ADD_CODE){
                            Fragment fragment = AddFragment.newInstance(model.viewState.getValue().toString(), resultContents);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                        else if (code == SCAN_FOR_FIND_CODE ){
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

    private void handleFabAction(FabAction action){
        switch (action){
            case EMPTY:
                break;
            case ADD_PRODUCT: {
                Fragment fragment = AddFragment.newInstance(model.viewState.getValue().toString(), null);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
                break;
            }
        }
    }

    public void updateActivity(DataType type) {
        if (type == DataType.OIL) {
            binding.viewPager.setCurrentItem(0);
        } else if (type == DataType.FILTER) {
            binding.viewPager.setCurrentItem(1);
        } else {
            binding.viewPager.setCurrentItem(2);
        }
        binding.menuFAB.setVisibility(View.VISIBLE);
    }

    private enum FabAction {
        EMPTY,
        ADD_PRODUCT,
        FIND_BY_QR,
        ADD_BY_QR
    }
}
