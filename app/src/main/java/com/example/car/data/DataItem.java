package com.example.car.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.UUID;

public class DataItem implements Parcelable {

    private String productId;
    private DataType type;
    private String name;
    private int count;
    private int buyPrice;
    private int sellPrice;
    private String category;

    public DataItem() {
        this.type = null;
        this.count = -1;
        this.buyPrice = -1;
        this.sellPrice = -1;
        this.category =null;
    }

    protected DataItem(Parcel in) {
        name = in.readString();
        count = Integer.parseInt(in.readString());
        buyPrice = Integer.parseInt(in.readString());
        sellPrice = Integer.parseInt(in.readString());
        category = in.readString();
    }

    public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
        @Override
        public DataItem createFromParcel(Parcel in) {
            return new DataItem(in);
        }

        @Override
        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public int getCount() {return count;}

    public void setCount(int count) {
        this.count = count;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(String.valueOf(count));
        dest.writeString(String.valueOf(buyPrice));
        dest.writeString(String.valueOf(sellPrice));
        dest.writeString(category);
    }
}
