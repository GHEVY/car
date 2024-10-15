package com.example.car.utils;

import androidx.lifecycle.ViewModel;

import com.example.car.R;
import com.example.car.data.DataType;

public class MainActivityViewModel extends ViewModel {
    private final DataType[] name = new DataType[]{
            DataType.OIL,
            DataType.FILTER,
            DataType.AUTO_PARTS
    };

    private final int[] drawables = new int[]{
            R.drawable.oil,
            R.drawable.filter,
            R.drawable.parts
    };
    private final int[] pageNameResArr = new int[]{
            R.string.oil,
            R.string.filter,
            R.string.parts
    };

    public DataType getName(int position){
        return name[position];
    }
    public int getDrawable(int position){
        return drawables[position];
    }
    public int getString(int position){
        return pageNameResArr[position];
    }

}
