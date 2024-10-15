package com.example.car;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;
import com.example.car.databinding.FoundBinding;
import com.example.car.utils.SharedViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class FindDialog extends DialogFragment {
    private static final String ID_KEY = "ID";
    private static final String LIST_KEY = "list";
    private static final String KEY = "key";
    private FoundBinding binding;
    private SharedViewModel model;

    public static FindDialog newInstance(String id) {
        Bundle args = new Bundle();
        args.putString(ID_KEY, id);
        args.putString(KEY,"id");
        FindDialog fragment = new FindDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static FindDialog newInstance(ArrayList<DataItem> list) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(LIST_KEY, list);
        args.putString(KEY,"list");
        FindDialog fragment = new FindDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FoundBinding.inflate(getLayoutInflater());
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
        SpinnerAdapter adapter = new SpinnerAdapter(requireContext(), R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_menu);
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
                            binding.count.setText(newItem.getCount() + "liter");
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
    }

    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            WindowManager.LayoutParams params = Objects.requireNonNull(getDialog().getWindow()).getAttributes();
            params.height = 1600;
            params.width = 900;
            getDialog().getWindow().setAttributes(params);
        }
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
                binding.count.setText(R.string.noitem);
            }
        } else if (Objects.equals(getArguments().getString(KEY), "list") && getArguments().getParcelableArrayList(LIST_KEY) != null) {
            list = getArguments().getParcelableArrayList(LIST_KEY);
        } else {
            binding.spinner.setVisibility(View.GONE);
            binding.price.setVisibility(View.GONE);
            binding.count.setText(R.string.noitem);
        }
        return list;
    }


}
