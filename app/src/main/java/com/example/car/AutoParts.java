package com.example.car;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;

public class AutoParts extends DataItem {

    public AutoParts() {
        this.setType(DataType.AUTOPARTS);
        this.setCount(-1);
        this.setBuyPrice(-1);
        this.setSellPrice(-1);
        this.setCategory(null);
    }
}
