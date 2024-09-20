package com.example.car;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;

public class Oil  extends DataItem {
    private DataType type;
    private String name;

    private int count;

    private int buyPrice;
    private int  sellPrice;
    private String category;

    public Oil() {
        this.type = DataType.OIL;
        this.count = -1;
        this.buyPrice = -1;
        this.sellPrice = -1;
        this.category =null;
    }


}
