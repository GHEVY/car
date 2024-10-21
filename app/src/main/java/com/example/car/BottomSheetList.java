package com.example.car;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;
import com.example.car.databinding.BottomSheetListBinding;
import com.example.car.utils.SharedViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetList extends BottomSheetDialogFragment{
    private BottomSheetListBinding binding;
    private SharedViewModel model;
    private RecyclerViewAdapter adapter;
    private final static String TYPE = "Type";
    private final String KEY = "Key";
    private final String ITEM = "Item";


    public static BottomSheetList newInstance(DataType type) {
        BottomSheetList bottomSheetList = new BottomSheetList();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE,type.toString());
        bottomSheetList.setArguments(bundle);
        return bottomSheetList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetListBinding.inflate(getLayoutInflater());
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        DataType type = DataType.valueOf(getArguments().getString(TYPE));
        adapter = new RecyclerViewAdapter(model.getItem(type), dataItem -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ITEM, dataItem);
            getParentFragmentManager().setFragmentResult(KEY, bundle);
            dismiss();
        });
        binding.recView.setAdapter(adapter);
        binding.recView.setLayoutManager(new LinearLayoutManager(requireContext()));
        return binding.getRoot();
    }
}
