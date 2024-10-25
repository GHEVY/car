package com.example.car;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;
import com.example.car.databinding.FoundDialogBinding;
import com.example.car.utils.SharedViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Objects;

public class FoundDialog extends BottomSheetDialogFragment {
    private static final String ID_KEY = "ID";
    private static final String LIST_KEY = "list";
    private static final String KEY = "key";
    private FoundDialogBinding binding;
    private SharedViewModel model;
    private int count = 1;
    private int item_count;

    public static FoundDialog newInstance(String id) {
        Bundle args = new Bundle();
        args.putString(ID_KEY, id);
        args.putString(KEY,"id");
        FoundDialog fragment = new FoundDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static FoundDialog newInstance(ArrayList<DataItem> list) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(LIST_KEY, list);
        args.putString(KEY,"list");
        FoundDialog fragment = new FoundDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FoundDialogBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        ArrayList<DataItem> list = getDataItems();
        ArrayList<String> nameList = new ArrayList<>();
        assert list != null;
        for (DataItem item : list) {
            nameList.add(item.getName());
        }
        updateCount();
        SpinnerAdapter adapter = new SpinnerAdapter(requireContext(), R.layout.spinner_item_container);
        adapter.setDropDownViewResource(R.layout.item_with_gradient);
        binding.spinner.setAdapter(adapter);
        adapter.addAll(nameList);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DataItem newItem;
                newItem = list.get(position);
                if (newItem != null) {
                    if (newItem.getCount() != -1 && newItem.getSellPrice() != -1) {
                        if (newItem.getType() == DataType.OIL) {
                            binding.count.setText(getString(R.string.left) +" " +newItem.getCount() + " " + getString(R.string.liters));
                        } else {
                            binding.count.setText(getString(R.string.left) + " " + newItem.getCount() +" " + getString(R.string.piece));
                        }
                        binding.price.setText(getString(R.string.price )+ " " + newItem.getSellPrice() + "$");
                        item_count = newItem.getCount();
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
        binding.add1.setOnClickListener(v -> {
            if(count<item_count){
                count++;
                updateCount();
            }
        });
        binding.sub1.setOnClickListener(v -> {
            if(count>1){
                count--;
                updateCount();
            }
        });
        binding.buy.setOnClickListener(v ->
                Toast.makeText(requireContext(), "You bought " + count + " ", Toast.LENGTH_SHORT).show());

    }

    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            WindowManager.LayoutParams params = Objects.requireNonNull(getDialog().getWindow()).getAttributes();
            getDialog().getWindow().setAttributes(params);
        }
    }
    public void updateCount() {
        binding.buyCount.setText(String.valueOf(count));
    }
    @Nullable
    private ArrayList<DataItem> getDataItems() {

        ArrayList<DataItem> list = new ArrayList<>();

        assert getArguments() != null;
        if (Objects.equals(getArguments().getString(KEY), "id") && getArguments().getString(ID_KEY) != null) {
            if (model.getItemByProductID(getArguments().getString(ID_KEY)) != null) {
                list = model.getItemByProductID(getArguments().getString(ID_KEY));
            } else {
                binding.spinner.setVisibility(View.GONE);
                binding.price.setVisibility(View.GONE);
                binding.count.setText(R.string.no_item);
            }
        } else if (Objects.equals(getArguments().getString(KEY), "list") && getArguments().getParcelableArrayList(LIST_KEY) != null) {
            list = getArguments().getParcelableArrayList(LIST_KEY);
        } else {
            binding.spinner.setVisibility(View.GONE);
            binding.price.setVisibility(View.GONE);
            binding.count.setText(R.string.no_item);
        }
        return list;
    }


}
