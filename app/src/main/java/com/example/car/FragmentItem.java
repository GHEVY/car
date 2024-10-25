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
    private FragmentItemBinding binding;
    private final String KEY = "Key";
    private final String ITEM = "Item";
    private int count = 1;
    private int item_count;

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
        binding.buyCount.setText("1");
        model.viewState.observe(getViewLifecycleOwner(), type1 -> {
            if (model.getItem(type1).isEmpty()) {
                binding.count.setVisibility(View.INVISIBLE);
                binding.price.setVisibility(View.INVISIBLE);
                binding.add1.setVisibility(View.INVISIBLE);
                binding.sub1.setVisibility(View.INVISIBLE);
                binding.buyCount.setVisibility(View.INVISIBLE);
                binding.buy.setVisibility(View.INVISIBLE);
                binding.name.setText(R.string.no_item);
            } else {
                updateAdapter(model.getItem(type1).get(0));
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

    public void updateCount() {
        binding.buyCount.setText(String.valueOf(count));
    }

    @SuppressLint("SetTextI18n")
    public void updateAdapter(DataItem item) {
        if (item == null) {
            binding.count.setText(R.string.no_item);
        } else {
            binding.count.setVisibility(View.VISIBLE);
            binding.price.setVisibility(View.VISIBLE);
            binding.add1.setVisibility(View.VISIBLE);
            binding.sub1.setVisibility(View.VISIBLE);
            binding.buyCount.setVisibility(View.VISIBLE);
            binding.buy.setVisibility(View.VISIBLE);
            binding.name.setText(item.getName());
            if (item.getType() == DataType.OIL) {
                binding.count.setText(getString(R.string.left) + " " + item.getCount() + " " + getString(R.string.liters));
            } else {
                binding.count.setText(getString(R.string.left) + " " + item.getCount() + " " + getString(R.string.piece));
            }
            item_count = item.getCount();
            binding.price.setText(getString(R.string.price) + " " + item.getSellPrice() + "$");
            count=1;
            updateCount();
        }
    }
}


