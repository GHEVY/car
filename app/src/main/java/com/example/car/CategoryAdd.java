package com.example.car;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.car.database.DBSchema;
import com.example.car.databinding.FragmentCategoryAddBinding;
import com.example.car.utils.AppTextSeparatedWatcher;

public class CategoryAdd extends DialogFragment {
    private String category = null;

    public interface OnDialogResultListener {
        void onDialogResult(String result);
    }

    private OnDialogResultListener listener;

    private void sendResult(String result) {
        if (listener != null) {
            listener.onDialogResult(result);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCategoryAddBinding binding = FragmentCategoryAddBinding.inflate(getLayoutInflater());
        listener = (OnDialogResultListener) getTargetFragment();
        binding.categoryWrite.addTextChangedListener(new AppTextSeparatedWatcher(new AppTextSeparatedWatcher(s -> category = s.toString())));
        binding.save.setOnClickListener(v -> {
            if(category!=null) {
                sendResult(category);
                dismiss();
            }
            else {
                binding.categoryWrite.animate().translationX(-10).setDuration(70).withEndAction(() -> binding.categoryWrite.animate().translationX(20).setDuration(70).withEndAction(() -> binding.categoryWrite.animate().translationX(0).setDuration(70).start()));
            }
        });
        return binding.getRoot();
    }

}
