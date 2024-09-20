package com.example.car;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.car.data.DataType;
import com.example.car.databinding.SearchFilterBinding;
import com.example.car.utils.AppTextSeparatedWatcher;
import com.example.car.utils.SharedViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFilter extends DialogFragment {
    private String name = null;
    private String start ="-1";
    private String end = "-1";
    private String category = null;


    public static SearchFilter newInstance() {
        Bundle args = new Bundle();
        SearchFilter fragment = new SearchFilter();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SearchFilterBinding binding = SearchFilterBinding.inflate(getLayoutInflater());
        SharedViewModel model = MainActivity.getModel();
        binding.name.addTextChangedListener(new AppTextSeparatedWatcher(s -> name = s.toString()));
        binding.start.addTextChangedListener(new AppTextSeparatedWatcher(s -> start = s.toString()));
        binding.end.addTextChangedListener(new AppTextSeparatedWatcher(s -> end = s.toString()));
        binding.category.addTextChangedListener(new AppTextSeparatedWatcher(s -> category = s.toString()));
        ArrayList<String> list = new ArrayList<>();
        list.add("");
        list.add(DataType.AUTOPARTS.toString());
        list.add(DataType.FILTER.toString());
        list.add(DataType.OIL.toString());
        binding.type.setAdapter(new ArrayAdapter<>(requireContext(),R.layout.spinner_item,list));

        binding.search.setOnClickListener(v -> {

            DialogFragment dialogFragment = Found.newInstance2(model.findItem(name,Integer.parseInt(start),Integer.parseInt(end),category,binding.type.getSelectedItem().toString()));
            dialogFragment.show(getParentFragmentManager(), "TAG");
            dialogFragment.setTargetFragment(getParentFragment(), 0);
            dismiss();
        });
        return binding.getRoot();
    }

    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            WindowManager.LayoutParams params = Objects.requireNonNull(getDialog().getWindow()).getAttributes();
            params.height = 1700;
            params.width = 1100;
            getDialog().getWindow().setAttributes(params);
        }
    }
}

