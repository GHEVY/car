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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;
import com.example.car.databinding.FragmentItemBinding;
import com.example.car.utils.SharedViewModel;

public class FragmentItem extends Fragment {
    private static final String TYPE_KEY = "TYPE";
    private SharedViewModel model;
    private FragmentItemBinding binding;
    public ArrayAdapter<String> adapter;
    private AdapterView.OnItemSelectedListener onItemSelectedListener;

    public static FragmentItem newInstance(DataType type) {
        FragmentItem fragmentItem = new FragmentItem();
        Bundle args = new Bundle();
        args.putString(TYPE_KEY,type.toString());
        fragmentItem.setArguments(args);
        return fragmentItem;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdapter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DataItem newItem = model.getItem(DataType.valueOf(getArguments().getString(TYPE_KEY))).get(position);
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
        adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(onItemSelectedListener);
        if(model.getNames(DataType.valueOf(getArguments().getString(TYPE_KEY)))!=null) {
            adapter.addAll(model.getNames(DataType.valueOf(getArguments().getString(TYPE_KEY))));
        }
        adapter.notifyDataSetChanged();
        model.newUpdate.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    updateAdapter();
                    model.newUpdate.setValue(false);
                }
            }

        });
        binding.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] list= new String[1];
                int a = binding.spinner.getSelectedItemPosition();
                list[0] = model.getItem(model.viewState.getValue()).get(a).getName();
//                model.buy();
            }
        });
    }

    public void updateAdapter() {
        if (model.getNames(DataType.valueOf(getArguments().getString(TYPE_KEY))).isEmpty()) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        } else {
            adapter.clear();
            adapter.addAll(model.getNames(DataType.valueOf(getArguments().getString(TYPE_KEY))));
            adapter.notifyDataSetChanged();
        }
    }
}
