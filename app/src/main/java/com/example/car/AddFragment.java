package com.example.car;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;
import com.example.car.databinding.FragmentAddBinding;
import com.example.car.utils.AppTextSeparatedWatcher;
import com.example.car.utils.SharedViewModel;

import java.util.ArrayList;


public class AddFragment extends Fragment implements Dialog.OnDialogResultListener {
    private static final String ARG_KEY = "args";
    private FragmentAddBinding binding;
    private DataItem dataItem;
    private ArrayAdapter<String> adapter;
    private SharedViewModel model;

    public static AddFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_KEY, type);
        AddFragment fragment = new AddFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    public static AddFragment newInstance2(String type,String productId) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_KEY, type);
        bundle.putString("ID",productId);
        AddFragment fragment = new AddFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        model = MainActivity.getModel();
        binding = FragmentAddBinding.inflate(getLayoutInflater());
        adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, model.getCategoryList());
        binding.category.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        dataItem = new DataItem();
        dataItem.setType(DataType.valueOf(getArguments().getString(ARG_KEY)));
        String id = getArguments().getString("ID");
        if(id != null){
            dataItem.setProductId(id);
        }
        FragmentResultListener fragmentResultListener = (requestKey, result) -> {
            ArrayList<String> list = new ArrayList<>();
            list.add(result.getString("category"));
            ArrayAdapter<String> a = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, list);
            binding.category.setAdapter(a);
            a.notifyDataSetChanged();
        };

        binding.name.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setName(s.toString())));

        if (dataItem.getType().equals(DataType.OIL)) {
            binding.count.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setCount(s.toString() + "litre")));
        } else {
            binding.count.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setCount(s.toString() + "hat")));
        }

        binding.buy.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setBuyPrice(s.toString() + "$")));
        binding.sell.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setSellPrice(s.toString() + "$")));
        binding.addCategory.setOnClickListener(v -> {
            DialogFragment dialogFragment = new Dialog();
            dialogFragment.show(getParentFragmentManager(), "TAG");
            dialogFragment.setTargetFragment(this, 0);
            getParentFragmentManager().setFragmentResultListener("category", getViewLifecycleOwner(), fragmentResultListener);
        });
        adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, model.getCategoryList());
        binding.category.setAdapter(adapter);
        binding.save.setOnClickListener(v -> {
            Bundle args1 = new Bundle();
            if(binding.category.getSelectedItem() != null){
                dataItem.setCategory(binding.category.getSelectedItem().toString());
            }
            args1.putParcelable("REQUEST_ADD", dataItem);
            getParentFragmentManager().setFragmentResult("ADD", args1);
            getParentFragmentManager().popBackStack();
        });
    }

    @Override
    public void onDialogResult(String result) {
        ArrayList<String> a = new ArrayList<>(model.getCategoryList());
        a.add(result);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(requireContext(),R.layout.spinner_item,a);
        binding.category.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
    }
}