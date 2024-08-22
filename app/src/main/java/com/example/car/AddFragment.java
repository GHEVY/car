package com.example.car;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.car.data.DataItem;
import com.example.car.databinding.FragmentAddBinding;
import com.example.car.utils.AppTextSeparatedWatcher;

import java.util.Objects;


public class AddFragment extends Fragment {
    private static final String ARG_KEY = "args";
    private FragmentAddBinding binding;
    private DataItem dataItem;

    public static AddFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_KEY, type);
        AddFragment fragment = new AddFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        if (Objects.equals(getArguments().getString(ARG_KEY), "OIL")) {
            dataItem = new Oil();
        } else if (Objects.equals(getArguments().getString(ARG_KEY), "FILTER")) {
            dataItem = new Filter();
        } else if(Objects.equals(getArguments().getString(ARG_KEY), "AUTOPARTS")) {
            dataItem = new AutoParts();
        }
        binding.name.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setName(s.toString())));
        binding.count.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setCount(s.toString() + "l")));
        binding.buy.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setBuyPrice(s.toString() + "$")));
        binding.sell.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setSellPrice(s.toString() + "$")));
        binding.category.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setCategory(s.toString())));
        binding.save.setOnClickListener(v -> {
            Bundle args1 = new Bundle();
            args1.putParcelable("REQUEST_ADD", dataItem);
            getParentFragmentManager().setFragmentResult("ADD", args1);
            getParentFragmentManager().popBackStack();
        });
    }
}