package com.example.car;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.car.data.DataType;
import com.example.car.databinding.SearchFilterBinding;
import com.example.car.utils.AppTextSeparatedWatcher;
import com.example.car.utils.SharedViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class SearchFilter extends BottomSheetDialogFragment {
    private String name = null;
    private int start = -1;
    private int end = -1;
    private String category = null;
    private SearchFilterBinding binding;
    private SharedViewModel model;

    public static SearchFilter newInstance() {
        return new SearchFilter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchFilterBinding.inflate(getLayoutInflater());
        model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        addTextChangedListeners();
        SpinnerAdapter adapter = new SpinnerAdapter(requireContext(), R.layout.spinner_item);
        adapter.addAll(getStrings());
        adapter.setDropDownViewResource(R.layout.spinner_menu);
        binding.type.setAdapter(adapter);
        binding.search.setOnClickListener(v -> {
            DialogFragment dialogFragment = FoundDialog.newInstance(model.findItem(name, start, end, category, (String) binding.type.getSelectedItem()));
            dialogFragment.show(getParentFragmentManager(), SearchFilter.class.getSimpleName());
            dialogFragment.setTargetFragment(getParentFragment(), 0);
            dismiss();
        });
        return binding.getRoot();
    }

    @NonNull
    private static ArrayList<String> getStrings() {
        ArrayList<String> list = new ArrayList<>();
        list.add(null);
        list.add(DataType.AUTO_PARTS.toString());
        list.add(DataType.FILTER.toString());
        list.add(DataType.OIL.toString());
        return list;
    }

    private void addTextChangedListeners() {
        binding.name.addTextChangedListener(new AppTextSeparatedWatcher(s -> name = s.toString()));
        binding.category.addTextChangedListener(new AppTextSeparatedWatcher(s -> category = s.toString()));
        binding.start.addTextChangedListener(new AppTextSeparatedWatcher(s -> {
            if(!s.toString().isEmpty()){
                start = Integer.parseInt(s.toString());
            }
        }));
        binding.end.addTextChangedListener(new AppTextSeparatedWatcher(s -> {
            if(!s.toString().isEmpty()){
                end= Integer.parseInt(s.toString());
            }
        }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.viewState.setValue(model.viewState.getValue());
    }
}

