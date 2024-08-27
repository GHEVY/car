package com.example.car;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;
import com.example.car.databinding.FragmentItemBinding;
import com.example.car.utils.SharedViewModel;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;

public class FragmentItem extends Fragment {
    public SharedViewModel model;
    public FragmentItemBinding binding;
    private ArrayAdapter<String> adapter;
    private AdapterView.OnItemSelectedListener onItemSelectedListener;

    private final ActivityResultLauncher<Intent> qrScannerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String resultContents = data.getStringExtra("SCAN_RESULT");
                        if (resultContents != null) {
                            Toast.makeText(requireContext(), resultContents, Toast.LENGTH_SHORT).show();
                            binding.count.setText(resultContents);
//                            Toast.makeText(requireContext(), "Scanned: " + resultContents, Toast.LENGTH_LONG).show();
//                            String[] res = resultContents.split("\\r?\\n");
//                            DataItem item = new DataItem();
//                            item.setType(model.getType());
//                            item.setName(res[0]);
//                            item.setCount(res[1]);
//                            item.setBuyPrice(res[2]);
//                            item.setSellPrice(res[3]);
//                            model.addItem(item);
//                            updateAdapter();
                        } else {
                            Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "No scan data received!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


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
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DataItem newItem = model.getItems().valueAt(position);
                if (newItem != null) {
                    if (model.getType() == DataType.OIL || model.getType() == DataType.МАСЛО) {
                        binding.count.setText(newItem.getCount() + "litr");
                    } else {
                        binding.count.setText(newItem.getCount() + "hat");
                    }
                    binding.price.setText(newItem.getSellPrice() + "$");
                } else {
                    binding.count.setText("");
                    binding.price.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, find());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        binding.add.setOnClickListener(v -> {
            Fragment fragment = AddFragment.newInstance(model.getType().toString());
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            getParentFragmentManager().setFragmentResultListener("ADD", getViewLifecycleOwner(), fragmentResultListener);
        });
        binding.addByQr.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(requireActivity());
            integrator.setPrompt("SCAN");
            integrator.setRequestCode(111);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setCameraId(0);
            Intent scanIntent = integrator.createScanIntent();
            qrScannerLauncher.launch(scanIntent);
        });
    }

    private void updateAdapter() {
        if (find().isEmpty()) {
            adapter.clear();
        } else {
            adapter.clear();
            adapter.addAll(find());
            adapter.notifyDataSetChanged();
        }
        binding.spinner.setOnItemSelectedListener(onItemSelectedListener);
    }

    private ArrayList<String> find() {
        if (model.getType() != null) {
            if (model.getItems() != null) {
                return new ArrayList<>(model.getItems().keySet());
            }
        }
        return new ArrayList<>();
    }

    private final FragmentResultListener fragmentResultListener = (requestKey, result) -> {
        DataItem dataItem = result.getParcelable("REQUEST_ADD");
        assert dataItem != null;
        model.addItem(dataItem);
        updateAdapter();
    };
}
