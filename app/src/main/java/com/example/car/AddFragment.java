package com.example.car;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        adapter.add(result.getString(CATEGORY));
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
        adapter = new SpinnerAdapter(requireContext(), R.layout.spinner_item_container);
        adapter.addAll(model.getCategoryList());
        adapter.setDropDownViewResource(R.layout.item_with_gradient);
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
            int count = 0;
            boolean[] booleans = {
                    dataItem.getName() == null,
                    dataItem.getCount() == -1,
                    dataItem.getBuyPrice() == -1,
                    dataItem.getSellPrice() == -1,
                    binding.category.getSelectedItem().equals(" ")
            };
            View[] views = {
                    binding.name,
                    binding.count,
                    binding.buy,
                    binding.sell,
                    binding.category
            };
            for (int i = 0; i < booleans.length; i++) {
                if (booleans[i]) {
                    showError(views[i]);
                } else count++;
            }
            if (count == 5) {
                dataItem.setCategory(binding.category.getSelectedItem().toString());
                model.addToDB(dataItem);
                getParentFragmentManager().popBackStack();
            } else {
                Toast.makeText(requireContext(), getString(R.string.all_required), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @SuppressLint("ResourceAsColor")
    private void showError(View view) {
        view.animate().translationX(-10).setDuration(70).withEndAction(() -> view.animate().translationX(20).setDuration(70).withEndAction(() -> view.animate().translationX(0).setDuration(70).start()));
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