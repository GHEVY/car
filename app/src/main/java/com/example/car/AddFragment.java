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

import com.example.car.data.DataItem;
import com.example.car.databinding.FragmentAddBinding;
import com.example.car.utils.AppTextSeparatedWatcher;
import com.example.car.utils.SharedViewModel;

import java.util.ArrayList;


public class AddFragment extends Fragment implements categoryAdd.OnDialogResultListener {
    private static final String ARG_KEY = "args";
    private static final String ITEM_KEY = "ITEM";
    private FragmentAddBinding binding;
    private DataItem dataItem;
    private ArrayAdapter<String> adapter;
    private SharedViewModel model;

    @Override
    public void onDestroy() {
        assert getArguments() != null;
        MainActivity.update(getArguments().getInt(ITEM_KEY));
        super.onDestroy();
    }

    public static AddFragment newInstance(String type, int a) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_KEY, type);
        bundle.putInt(ITEM_KEY ,a);
        AddFragment fragment = new AddFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static AddFragment newInstance2(String type, String productId) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_KEY, type);
        bundle.putString("ID", productId);
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

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        dataItem = new DataItem();
        dataItem.setType(model.getType());
        String id = getArguments().getString("ID");
        if (id != null) {
            dataItem.setProductId(id);
        }
        FragmentResultListener fragmentResultListener = (requestKey, result) -> {
            ArrayList<String> list = new ArrayList<>();
            list.add(result.getString("category"));
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
            dialogFragment.show(getParentFragmentManager(), "TAG");
            dialogFragment.setTargetFragment(this, 0);
            getParentFragmentManager().setFragmentResultListener("category", getViewLifecycleOwner(), fragmentResultListener);
        });
        adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, model.getCategoryList());
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
        ArrayList<String> list = new ArrayList<>(model.getCategoryList());
        list.add(result);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, list);
        binding.category.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
    }
}