package com.example.car;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.example.car.data.DataItem;
import com.example.car.databinding.FragmentItemBinding;
import com.example.car.utils.SharedViewModel;
import com.google.zxing.integration.android.IntentIntegrator;

public class FragmentItem extends Fragment {
    public SharedViewModel model;
    public FragmentItemBinding binding;
    private ArrayAdapter<String> adapter;
    private AdapterView.OnItemSelectedListener onItemSelectedListener;

    public static FragmentItem newInstance() {
        return new FragmentItem();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdapter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = MainActivity.getModel();
        onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DataItem newItem = model.getItem().get(position);
                if (newItem != null) {
                    if(newItem.getCount()!=null && newItem.getSellPrice()!=null){
                        binding.count.setText(newItem.getCount());
                        binding.price.setText(newItem.getSellPrice());
                    }else {
                        binding.count.setText("");
                        binding.price.setText("");
                    }
                    if(newItem.getProductId() != null) {
                        Toast.makeText(requireContext(), newItem.getProductId(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(requireContext(),"null",Toast.LENGTH_SHORT).show();
                    }
                    }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.count.setVisibility(View.INVISIBLE);
                binding.price.setVisibility(View.INVISIBLE);
            }
        };
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, model.getNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        closeMenu();

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
            Fragment fragment = AddFragment.newInstance(model.getType().toString());
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            getParentFragmentManager().setFragmentResultListener("ADD", getViewLifecycleOwner(), fragmentResultListener);
        });
        binding.addByQRFAB.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(requireActivity());
            integrator.setPrompt("SCAN");
            integrator.setRequestCode(111);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setCameraId(0);
            Intent scanIntent = integrator.createScanIntent();
            qrScannerLauncher.launch(scanIntent);
        });
        binding.findByQRFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(requireActivity());
                integrator.setPrompt("SCAN");
                integrator.setRequestCode(111);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setCameraId(0);
                Intent scanIntent = integrator.createScanIntent();
                qrScannerLauncher2.launch(scanIntent);
            }
        });
    }

    private void updateAdapter() {
        if (model.getNames().isEmpty()) {
            adapter.clear();
        } else {
            adapter.clear();
            adapter.addAll(model.getNames());
            adapter.notifyDataSetChanged();
        }
        binding.spinner.setOnItemSelectedListener(onItemSelectedListener);
    }

    private final FragmentResultListener fragmentResultListener = (requestKey, result) -> {
        DataItem dataItem = result.getParcelable("REQUEST_ADD");
        model.addToDB(dataItem);
        updateAdapter();
    };

    private final ActivityResultLauncher<Intent> qrScannerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String resultContents = data.getStringExtra("SCAN_RESULT");
                        if (resultContents != null) {
                            Fragment fragment = AddFragment.newInstance2(model.getType().toString(),resultContents);
                            getParentFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fragment)
                                    .addToBackStack(null)
                                    .commit();
                            getParentFragmentManager().setFragmentResultListener("ADD", getViewLifecycleOwner(), fragmentResultListener);
                        } else {
                            Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "No scan data received!", Toast.LENGTH_SHORT).show();
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
                            model.getItemByProductID(resultContents);
                            DialogFragment dialogFragment = Dialog2.newInstance(resultContents);
                            dialogFragment.show(getParentFragmentManager(), "TAG");
                            dialogFragment.setTargetFragment(this, 0);
                            getParentFragmentManager().setFragmentResultListener("category", getViewLifecycleOwner(), fragmentResultListener);
                        } else {
                            Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "No scan data received!", Toast.LENGTH_SHORT).show();
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
        binding.findFAB.animate().translationX(0).start();
        binding.findByQRFAB.animate().translationX(0).start();
    }

    private void closeMenu() {
        binding.addFAB.animate().translationY(-150).withEndAction(() -> binding.addFAB.setVisibility(View.GONE)).start();
        binding.addByQRFAB.animate().translationY(-350).withEndAction(() -> binding.addByQRFAB.setVisibility(View.GONE)).start();
        binding.findFAB.animate().translationX(150).withEndAction(()-> binding.findFAB.setVisibility(View.GONE)).start();
        binding.findByQRFAB.animate().translationX(350).withEndAction(()-> binding.findByQRFAB.setVisibility(View.GONE)).start();
    }

}