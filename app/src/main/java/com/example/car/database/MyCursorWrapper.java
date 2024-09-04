package com.example.car.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;

public class MyCursorWrapper extends CursorWrapper {
    public MyCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public DataItem getItem() {
        String Type = getString(getColumnIndex(DBSchema.Table.Cols.Type));
        String Name = getString(getColumnIndex(DBSchema.Table.Cols.Name));
        String Count = getString(getColumnIndex(DBSchema.Table.Cols.Count));
        String Buy_price = getString(getColumnIndex(DBSchema.Table.Cols.Buy_price));
        String Sell_price = getString(getColumnIndex(DBSchema.Table.Cols.Sell_price));
        String Category = getString(getColumnIndex(DBSchema.Table.Cols.Category));
        DataItem dataItem = new DataItem();
        dataItem.setType(DataType.valueOf(Type));
        dataItem.setName(Name);
        dataItem.setCount(Count);
        dataItem.setBuyPrice(Buy_price);
        dataItem.setSellPrice(Sell_price);
        dataItem.setCategory(Category);
        if(getString(getColumnIndex(DBSchema.Table.Cols.ProductId)) != null){
            dataItem.setProductId(getString(getColumnIndex(DBSchema.Table.Cols.ProductId)));
        }
        return dataItem;
    }
}