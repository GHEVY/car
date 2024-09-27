package com.example.car;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;
import com.example.car.databinding.FragmentAddBinding;
import com.example.car.utils.AppTextSeparatedWatcher;
import com.example.car.utils.SharedViewModel;

import java.util.ArrayList;

public class AddFragment extends Fragment implements categoryAdd.OnDialogResultListener {
    private static final String ARG_KEY = "args";
    private static final String ID_KEY = "ID";
    private static final String CATEGORY = "CATEGORY";
    private FragmentAddBinding binding;
    private DataItem dataItem;
    private ArrayAdapter<String> adapter;
    private SharedViewModel model;


    public static AddFragment newInstance(String type, @Nullable String productId) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_KEY, type);
        bundle.putString(ID_KEY, productId);
        AddFragment fragment = new AddFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        binding = FragmentAddBinding.inflate(getLayoutInflater());
        adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, model.getCategoryList(DataType.valueOf(getArguments().getString(ARG_KEY))));
        binding.category.setAdapter(adapter);
        return binding.getRoot();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        dataItem = new DataItem();
        dataItem.setType(DataType.valueOf(getArguments().getString(ARG_KEY)));
        String id = getArguments().getString(ID_KEY);
        if (id != null) {
            dataItem.setProductId(id);
        }
        FragmentResultListener fragmentResultListener = (requestKey, result) -> {
            ArrayList<String> list = new ArrayList<>();
            list.add(result.getString(CATEGORY));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, list);
            binding.category.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        };
        binding.name.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setName(s.toString())));
        binding.count.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setCount(Integer.parseInt(s.toString()))));
        binding.buy.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setBuyPrice(Integer.parseInt(s.toString()))));
        binding.sell.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setSellPrice(Integer.parseInt(s.toString()))));
        binding.addCategory.setOnClickListener(v -> {
            DialogFragment dialogFragment = new categoryAdd();
            dialogFragment.show(getParentFragmentManager(), null);
            dialogFragment.setTargetFragment(this, 0);
            getParentFragmentManager().setFragmentResultListener(CATEGORY, getViewLifecycleOwner(), fragmentResultListener);
        });
        adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, model.getCategoryList(DataType.valueOf(getArguments().getString(ARG_KEY))));
        binding.category.setAdapter(adapter);
        binding.save.setOnClickListener(v -> {
            if (dataItem.getName() == null) {
                Toast.makeText(requireContext(), getString(R.string.name), Toast.LENGTH_SHORT).show();
            } else if (dataItem.getCount() == -1) {
                Toast.makeText(requireContext(), getString(R.string.count), Toast.LENGTH_SHORT).show();
            } else if (dataItem.getBuyPrice() == -1) {
                Toast.makeText(requireContext(), getString(R.string.buy_price), Toast.LENGTH_SHORT).show();
            } else if (dataItem.getSellPrice() == -1) {
                Toast.makeText(requireContext(), getString(R.string.sell_price), Toast.LENGTH_SHORT).show();
            } else if (binding.category.getSelectedItem() == null) {
                Toast.makeText(requireContext(), getString(R.string.write_category), Toast.LENGTH_SHORT).show();
            } else {
                dataItem.setCategory(binding.category.getSelectedItem().toString());
                model.addToDB(dataItem);
                getParentFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onDialogResult(String result) {
        ArrayList<String> list = new ArrayList<>(model.getCategoryList(DataType.valueOf(getArguments().getString(ARG_KEY))));
        list.add(result);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, list);
        binding.category.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.newUpdate.setValue(true);
    }
}