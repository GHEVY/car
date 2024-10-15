package com.example.car;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;
import com.example.car.databinding.FragmentItemBinding;
import com.example.car.utils.SharedViewModel;

public class FragmentItem extends Fragment {
    private static final String TYPE_KEY = "TYPE";
    private SharedViewModel model;
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
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        assert getArguments() != null;
        DataType type = DataType.valueOf(getArguments().getString(TYPE_KEY));
        binding.name.setOnClickListener(v ->
                BottomSheetList.newInstance(type).show(getParentFragmentManager(), "s")
        );

        getParentFragmentManager().setFragmentResultListener(KEY, this, (requestKey, result) -> {
            DataItem dataItem = result.getParcelable(ITEM);
            Toast.makeText(requireContext(), "g", Toast.LENGTH_SHORT).show();
            assert dataItem != null;
            binding.name.setText( "iiii");
            binding.count.setText("aaaa");
            binding.price.setText("a");
            updateAdapter();
        });

    }

    public void updateAdapter(DataItem item) {
        if (item == null) {
            binding.price.setVisibility(View.INVISIBLE);
            binding.count.setText(R.string.noitem);
        } else {
            binding.name.setText(item.getName());
//            binding.count.setText(item.getCount());
//            binding.price.setText(item.getSellPrice());
        }
    }

    public void updateAdapter() {
        binding.name.setText( "iiii");
        binding.count.setText("aaaa");
        binding.price.setText("a");
        Toast.makeText(requireContext(), "update", Toast.LENGTH_SHORT).show();
    }
}


//        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                assert getArguments() != null;
//                DataItem newItem = model.getItem(DataType.valueOf(getArguments().getString(TYPE_KEY))).get(position);
//                if (newItem != null) {
//                    if (newItem.getCount() != -1 && newItem.getSellPrice() != -1) {
//                        binding.price.setVisibility(View.VISIBLE);
//                        if (newItem.getType() == DataType.OIL) {
//                            binding.count.setText(newItem.getCount() + " liters left");
//                        } else {
//                            binding.count.setText(newItem.getCount() + " left");
//                        }
//                        binding.price.setText(newItem.getSellPrice() + "$");
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                binding.price.setVisibility(View.INVISIBLE);
//                binding.count.setText(R.string.noitem);
//            }
//        };

//        binding.buy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String[] list= new String[1];
//                int a = binding.spinner.getSelectedItemPosition();
//                list[0] = model.getItem(model.viewState.getValue()).get(a).getName();
//                model.buy();
//            }
//        });
