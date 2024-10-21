package com.example.car;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;
import com.example.car.databinding.FragmentItemBinding;
import com.example.car.utils.SharedViewModel;

public class FragmentItem extends Fragment {
    private static final String TYPE_KEY = "TYPE";
    private FragmentItemBinding binding;
    private final String KEY = "Key";
    private final String ITEM = "Item";

    public static FragmentItem newInstance(DataType type) {
        FragmentItem fragmentItem = new FragmentItem();
        Bundle args = new Bundle();
        args.putString(TYPE_KEY, type.toString());
        fragmentItem.setArguments(args);
        return fragmentItem;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        DataType type = DataType.valueOf(getArguments().getString(TYPE_KEY));
        model.viewState.observe(getViewLifecycleOwner(), new Observer<DataType>() {
            @Override
            public void onChanged(DataType type) {
                if(model.getItem(type).isEmpty()) {
                    binding.count.setVisibility(View.INVISIBLE);
                    binding.price.setVisibility(View.INVISIBLE);
                    binding.name.setText(R.string.no_item);
                }
                else {
                    updateAdapter(model.getItem(type).get(0));
                }
            }
        });
        binding.name.setOnClickListener(v -> {
            BottomSheetList.newInstance(type).show(getParentFragmentManager(), "s");
            getParentFragmentManager().setFragmentResultListener(KEY, getViewLifecycleOwner(), (requestKey, result) -> {
                DataItem dataItem = result.getParcelable(ITEM);
                assert dataItem != null;
                updateAdapter(dataItem);
            });
        });
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    public void updateAdapter(DataItem item) {
        if (item == null) {
            binding.count.setText(R.string.no_item);
        } else {
            binding.count.setVisibility(View.VISIBLE);
            binding.price.setVisibility(View.VISIBLE);
            binding.name.setText(item.getName());
            if (item.getType() == DataType.OIL) {
                binding.count.setText(getString(R.string.left) +" " +item.getCount() + " " + getString(R.string.liters));
            } else {
                binding.count.setText(getString(R.string.left) + " " + item.getCount() +" " + getString(R.string.piece));
            }
            binding.price.setText(getString(R.string.price )+ " " + item.getSellPrice() + "$");
        }
    }
}

//        binding.buy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String[] list= new String[1];
//                int a = binding.spinner.getSelectedItemPosition();
//                list[0] = model.getItem(model.viewState.getValue()).get(a).getName();
//                model.buy();
//            }
//        });
