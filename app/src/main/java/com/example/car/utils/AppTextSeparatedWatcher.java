package com.example.car.utils;

import android.text.Editable;
import android.text.TextWatcher;

public class AppTextSeparatedWatcher implements TextWatcher {
    public interface BeforeTextChanged {
        void textChanged(CharSequence s, int start, int count, int after);
    }

    public interface OnTextChanged {
        void textChanged(CharSequence s, int start, int before, int count);
    }

    public interface AfterTextChanged {
        void textChanged(Editable s);
    }

    private TextWatcher textWatcher;
    private BeforeTextChanged beforeTextChanged;
    private OnTextChanged onTextChanged;
    private AfterTextChanged afterTextChanged;

    public AppTextSeparatedWatcher(TextWatcher textWatcher) {
        this.textWatcher = textWatcher;
    }

    public AppTextSeparatedWatcher(BeforeTextChanged beforeTextChanged) {
        this.beforeTextChanged = beforeTextChanged;
    }

    public AppTextSeparatedWatcher(OnTextChanged onTextChanged) {
        this.onTextChanged = onTextChanged;
    }

    public AppTextSeparatedWatcher(AfterTextChanged afterTextChanged) {
        this.afterTextChanged = afterTextChanged;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (textWatcher != null) {
            textWatcher.beforeTextChanged(s, start, count, after);
        }

        if (beforeTextChanged != null) {
            beforeTextChanged.textChanged(s, start, count, after);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (textWatcher != null) {
            textWatcher.onTextChanged(s, start, before, count);
        }

        if (onTextChanged != null) {
            onTextChanged.textChanged(s, start, before, count);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (textWatcher != null) {
            textWatcher.afterTextChanged(s);
        }

        if (afterTextChanged != null) {
            afterTextChanged.textChanged(s);
        }
    }
}
