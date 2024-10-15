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

public class AddFragment extends Fragment implements CategoryAdd.OnDialogResultListener {
    private static final String ARG_KEY = "args";
    private static final String ID_KEY = "ID";
    private static final String CATEGORY = "CATEGORY";
    private FragmentAddBinding binding;
    private DataItem dataItem;
    private SharedViewModel model;
    private SpinnerAdapter adapter;

    private final FragmentResultListener fragmentResultListener = (requestKey, result) -> {
        ArrayList<String> list = new ArrayList<>();
        list.add(result.getString(CATEGORY));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, list);
        binding.category.setAdapter(adapter1);
        adapter.notifyDataSetChanged();
    };

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
        binding = FragmentAddBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        adapter = new SpinnerAdapter(requireContext(), R.layout.spinner_item);
        adapter.addAll(model.getCategoryList());
        adapter.setDropDownViewResource(R.layout.spinner_menu);
        binding.category.setAdapter(adapter);

        assert getArguments() != null;
        dataItem = new DataItem();
        dataItem.setType(DataType.valueOf(getArguments().getString(ARG_KEY)));
        if (getArguments().getString(ID_KEY) != null) {
            dataItem.setProductId(getArguments().getString(ID_KEY));
        }

        binding.addCategory.setOnClickListener(v -> {
            DialogFragment dialogFragment = new CategoryAdd();
            dialogFragment.show(getParentFragmentManager(), null);
            dialogFragment.setTargetFragment(this, 0);
            getParentFragmentManager().setFragmentResultListener(CATEGORY, getViewLifecycleOwner(), fragmentResultListener);
        });

        addTextWatchers();

        binding.save.setOnClickListener(v -> {
            boolean[] arrBool = new boolean[]{
                    dataItem.getName() == null,
                    dataItem.getCount() == -1,
                    dataItem.getBuyPrice() == -1,
                    dataItem.getSellPrice() == -1,
                    binding.category.getSelectedItem() == null
            };

            int[] arrInt = new  int[]{
                    R.string.name,
                    R.string.count,
                    R.string.buy_price,
                    R.string.sell_price,
                    R.string.write_category

            };

            for(int i =0;i<5;i++){
                if(arrBool[i]){
                    showToast(arrInt[i]);
                    break;
                } else if (i!=4) {
                    continue;
                }
                dataItem.setCategory(binding.category.getSelectedItem().toString());
                model.addToDB(dataItem);
                getParentFragmentManager().popBackStack();

            }
//            /// TODO simplify
//            if (dataItem.getName() == null) {
//                showToast(R.string.name);
//            } else if (dataItem.getCount() == -1) {
//                showToast(R.string.count);
//            } else if (dataItem.getBuyPrice() == -1) {
//                showToast(R.string.buy_price);
//            } else if (dataItem.getSellPrice() == -1) {
//                showToast(R.string.sell_price);
//            } else if (binding.category.getSelectedItem() == null) {
//                showToast(R.string.write_category);
//            } else {
//                dataItem.setCategory(binding.category.getSelectedItem().toString());
//                model.addToDB(dataItem);
//                getParentFragmentManager().popBackStack();
//            }

        });
    }

    private void showToast(int messageId ) {
        Toast.makeText(requireContext(), getString(messageId), Toast.LENGTH_SHORT).show();
    }

    private void addTextWatchers() {
        binding.name.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setName(s.toString())));
        binding.count.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setCount(Integer.parseInt(s.toString()))));
        binding.buy.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setBuyPrice(Integer.parseInt(s.toString()))));
        binding.sell.addTextChangedListener(new AppTextSeparatedWatcher(s -> dataItem.setSellPrice(Integer.parseInt(s.toString()))));
    }

    @Override
    public void onDialogResult(String result) {
        adapter.clear();
        ArrayList<String> list = model.getCategoryList();
        list.add(result);
        adapter.addAll(list);
        binding.category.setSelection(adapter.getCount() - 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.viewState.setValue(model.viewState.getValue());
    }
}