package com.example.car;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;

public class Filter extends DataItem {


    public Filter() {
        this.setType(DataType.FILTER);
        this.setCount(-1);
        this.setBuyPrice(-1);
        this.setSellPrice(-1);
        this.setCategory(null);
    }
    }