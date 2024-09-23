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
import androidx.lifecycle.ViewModelProvider;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;
import com.example.car.databinding.FoundBinding;
import com.example.car.utils.SharedViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class FindDialog extends DialogFragment {
    private static String flag = " ";
    private static final String ITEM_KEY = "ITEM";
    private static final String ID_KEY = "ID";
    private static final String LIST_KEY = "list";

    public static FindDialog newInstance(String id, int a) {
        Bundle args = new Bundle();
        args.putString(ID_KEY, id);
        args.putInt(ITEM_KEY, a);
        FindDialog fragment = new FindDialog();
        fragment.setArguments(args);
        flag = "id";
        return fragment;
    }

    public static FindDialog newInstance2(ArrayList<DataItem> list, int a) {

        Bundle args = new Bundle();
        args.putParcelableArrayList(LIST_KEY, list);
        args.putInt(ITEM_KEY, a);
        FindDialog fragment = new FindDialog();
        fragment.setArguments(args);
        flag = "list";
        return fragment;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FoundBinding binding = FoundBinding.inflate(getLayoutInflater());
        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        ArrayList<DataItem> list = null;
        assert getArguments() != null;
        if (Objects.equals(flag, "id") && getArguments().getString(ID_KEY) != null) {
            if (model.getItemByProductID(getArguments().getString(ID_KEY)) != null) {
                list = model.getItemByProductID(getArguments().getString(ID_KEY));
            } else {
                binding.spinner.setVisibility(View.GONE);
                binding.price.setVisibility(View.GONE);
                binding.count.setText(R.string.noitem);

            }
        } else if (Objects.equals(flag, "list") && getArguments().getParcelableArrayList(LIST_KEY) != null) {
            list = getArguments().getParcelableArrayList(LIST_KEY);
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
                assert finalList != null;
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
