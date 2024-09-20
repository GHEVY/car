package com.example.car;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;
import com.example.car.databinding.FragmentItemBinding;
import com.example.car.utils.SharedViewModel;

public class FragmentItem extends Fragment {
    private SharedViewModel model;
    private FragmentItemBinding binding;
    public ArrayAdapter<String> adapter;
    private AdapterView.OnItemSelectedListener onItemSelectedListener;

    public static FragmentItem newInstance() {
        return new FragmentItem();
    }

    @Override
    public void onResume() {
        updateAdapter();
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
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
                    if (newItem.getCount() != -1 && newItem.getSellPrice() != -1) {
                        binding.spinner.setVisibility(View.VISIBLE);
                        binding.price.setVisibility(View.VISIBLE);
                        if (newItem.getType() == DataType.OIL) {
                            binding.count.setText(newItem.getCount() + " liters left");
                        } else {
                            binding.count.setText(newItem.getCount() + " left");
                        }
                        binding.price.setText(newItem.getSellPrice() + "$");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.spinner.setVisibility(View.INVISIBLE);
                binding.price.setVisibility(View.INVISIBLE);
                binding.count.setText(R.string.noitem);
            }
        };


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, model.getNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(onItemSelectedListener);
    }
    public void updateAdapter(){
        if (model.getNames().isEmpty()) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        } else {
            adapter.clear();
            adapter.addAll(model.getNames());
            adapter.notifyDataSetChanged();
        }
    }


}
