package com.example.car;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.car.databinding.FragmentCategoryAddBinding;

public class Dialog extends DialogFragment{
    String category;

    public interface OnDialogResultListener {
        void onDialogResult(String result);
    }
    private OnDialogResultListener listener;
    private void sendResult(String result) {
        if (listener != null) {
            listener.onDialogResult(result);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCategoryAddBinding binding = FragmentCategoryAddBinding.inflate(getLayoutInflater());
        listener = (OnDialogResultListener) getTargetFragment();
        binding.categoryWrite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                category = s.toString();
            }
        });
        binding.save.setOnClickListener(v -> {
            sendResult(category);
            dismiss();
        });
        return binding.getRoot();
    }
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
