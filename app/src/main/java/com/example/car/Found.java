package com.example.car;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;
import com.example.car.databinding.FindedBinding;
import com.example.car.utils.SharedViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class Found extends DialogFragment {
    private static String flag = " ";


    public static Found newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        Found fragment = new Found();
        fragment.setArguments(args);
        flag = "id";
        return fragment;
    }

    public static Found newInstance2(ArrayList<DataItem> list) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("list", list);
        Found fragment = new Found();
        fragment.setArguments(args);
        flag = "list";
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FindedBinding binding = FindedBinding.inflate(getLayoutInflater());
        SharedViewModel model = MainActivity.getModel();
        ArrayList<DataItem> list = null;
        assert getArguments() != null;
        if (Objects.equals(flag, "id") && getArguments().getString("id") != null) {
            if (model.getItemByProductID(getArguments().getString("id")) != null) {
                list = model.getItemByProductID(getArguments().getString("id"));
            }
            else {
                binding.spinner.setVisibility(View.GONE);
                binding.price.setVisibility(View.GONE);
                binding.count.setText(R.string.noitem);

            }
        } else if (Objects.equals(flag, "list") && getArguments().getParcelableArrayList("list") != null) {
            list = getArguments().getParcelableArrayList("list");
        } else {
            binding.spinner.setVisibility(View.GONE);
            binding.price.setVisibility(View.GONE);
            binding.count.setText(R.string.noitem);
        }
        ArrayList<String> nameList = new ArrayList<>();
        if (list != null) {
            for (DataItem a : list) {
                nameList.add(a.getName());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, nameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        ArrayList<DataItem> finalList = list;
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DataItem newItem = finalList.get(position);
                if (newItem != null) {
                    if (newItem.getCount() != -1 && newItem.getSellPrice() != -1) {
                        if (newItem.getType() == DataType.OIL) {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.count.setVisibility(View.INVISIBLE);
                binding.price.setVisibility(View.INVISIBLE);
            }
        });
        return binding.getRoot();
    }

    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            WindowManager.LayoutParams params = Objects.requireNonNull(getDialog().getWindow()).getAttributes();
            params.height = 1700;
            params.width = 1000;
            getDialog().getWindow().setAttributes(params);
        }
    }
}
