package com.example.car;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.car.data.DataItem;
import com.example.car.databinding.FindedBinding;
import com.example.car.utils.SharedViewModel;

import java.util.ArrayList;

public class Dialog2 extends DialogFragment {
    private ArrayAdapter<String> adapter;
    private SharedViewModel model;
    private AdapterView.OnItemSelectedListener onItemSelectedListener;




    public static Dialog2 newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id",id);
        Dialog2 fragment = new Dialog2();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FindedBinding binding = FindedBinding.inflate(getLayoutInflater());
        model = MainActivity.getModel();
        ArrayList<String> list = new ArrayList<>();
        //list.add(model.getItemByProductID(getArguments().getString("id")).getName());
        adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(onItemSelectedListener);
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
        return binding.getRoot();
    }
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = 3000;
            params.height = 7000;
            getDialog().getWindow().setAttributes(params);
        }
    }
}
