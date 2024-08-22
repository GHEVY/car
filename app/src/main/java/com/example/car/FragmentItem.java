package com.example.car;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.example.car.data.DataItem;
import com.example.car.databinding.FragmentItemBinding;
import com.example.car.utils.SharedViewModel;

import java.util.ArrayList;

public class FragmentItem extends Fragment {
    public FragmentItemBinding binding;
    private ArrayAdapter<String> adapter;
    public SharedViewModel model;

    private static final String KEY_TYPE = "key";

    public static FragmentItem newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TYPE, type);
        FragmentItem fragment = new FragmentItem();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        assert getArguments() != null;
        Log.e("TAG", "resume   " + model.getType());
        updateAdapter();

    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, find());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner1.setAdapter(adapter);
        binding.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DataItem newItem = null;
                if (model.getType() != null) {
                    newItem = model.getItems().valueAt(position);
                }if (newItem != null) {
                    binding.count.setText(newItem.getCount());
                    binding.price.setText(newItem.getSellPrice());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.add.setOnClickListener(v -> {
            Fragment fragment = AddFragment.newInstance(model.getType().toString());
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        getParentFragmentManager().setFragmentResultListener("ADD", getViewLifecycleOwner(), fragmentResultListener);
    }

    private void updateAdapter() {
        if (find().isEmpty()) {
            adapter.clear();
        } else {
            adapter.clear();
            adapter.addAll(find());
            adapter.notifyDataSetChanged();
            Log.e("TAG", "updateAdapter");
        }
    }

    private ArrayList<String> find() {
        if (model.getType() != null) {
            if (model.getItems() != null) {
                return new ArrayList<>(model.getItems().keySet());
            }
        }
        return new ArrayList<>();
    }

    private final FragmentResultListener fragmentResultListener = (requestKey, result) -> {
        DataItem dataItem = result.getParcelable("REQUEST_ADD");
        assert dataItem != null;
        model.addItem(dataItem);
        updateAdapter();
    };
}
