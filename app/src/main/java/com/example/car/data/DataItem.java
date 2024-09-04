package com.example.car.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.UUID;

public class DataItem implements Parcelable {

    private String productId;
    private DataType type;
    private String name;
    private String count;
    private String buyPrice;
    private String sellPrice;
    private String category;

    public DataItem() {
        this.type = null;
        this.count = null;
        this.buyPrice = null;
        this.sellPrice = null;
        this.category =null;
    }

    protected DataItem(Parcel in) {
        name = in.readString();
        count = in.readString();
        buyPrice = in.readString();
        sellPrice = in.readString();
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

    public String getCount() {return count;}

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
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(count);
        dest.writeString(buyPrice);
        dest.writeString(sellPrice);
        dest.writeString(category);
    }
}
