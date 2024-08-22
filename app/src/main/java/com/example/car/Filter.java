package com.example.car;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;

public class Filter extends DataItem {
    private DataType type;
    private String name;

    private String count;

    private String buyPrice;
    private  String sellPrice;
    private String category;

    public Filter() {
        this.type = DataType.FILTER;
        this.count = null;
        this.buyPrice = null;
        this.sellPrice = null;
        this.category =null;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }
}
