package com.example.car.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import androidx.lifecycle.ViewModel;

import com.example.car.FragmentItem;
import com.example.car.data.DataItem;
import com.example.car.data.DataType;
import com.example.car.database.DBHelper;
import com.example.car.database.DBSchema;
import com.example.car.database.MyCursorWrapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SharedViewModel extends ViewModel {
    private DataType type;
    private final SQLiteDatabase database;


    public SharedViewModel(Context context) {
        this.database = new DBHelper(context).getWritableDatabase();
    }

    public void setType(DataType type) {
        if (type == DataType.OIL) {
            this.type = DataType.OIL;
        } else if (type == DataType.FILTER) {
            this.type = DataType.FILTER;
        }
        if (type == DataType.AUTOPARTS) {
            this.type = DataType.AUTOPARTS;
        }
    }

    public ArrayList<String> getCategoryList() {
        Set<String> list = new HashSet<>();
        if (getType() != null) {
            MyCursorWrapper cursor = query();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursor.getItem().getCategory());
                cursor.moveToNext();
            }
        }
        return new ArrayList<>(list);
    }


    public DataType getType() {
        return type;
    }

    public ArrayList<String> getNames() {
        ArrayList<String> list = new ArrayList<>();
        if (getType() != null) {
            try (MyCursorWrapper cursor = query()) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    if (cursor.getItem().getType().toString().equals(getType().toString())) {
                        list.add(cursor.getItem().getName());
                    }
                    cursor.moveToNext();
                }
            }
        }
        return list;
    }


    public ArrayList<DataItem> getItem() {
        ArrayList<DataItem> items = new ArrayList<>();
        try (MyCursorWrapper cursor = query()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (cursor.getItem().getType() == getType()) {
                    items.add(cursor.getItem());
                }
                cursor.moveToNext();
            }
        }
        return items;
    }

    public ArrayList<DataItem> getItemByProductID(String id) {
        ArrayList<DataItem> list = new ArrayList<>();
        try (MyCursorWrapper cursor = query()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (Objects.equals(cursor.getItem().getProductId(), id)) {
                    list.add(cursor.getItem());
                }
                cursor.moveToNext();
            }
            if (!list.isEmpty()) {
                return list;
            }
        }
        return null;
    }

    public ArrayList<DataItem> findItem(String name, int minPrice, int maxPrice, String category,String type) {
        ArrayList<DataItem> list = new ArrayList<>();
        try (MyCursorWrapper cursor = query()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if(name != null){
                    if (!cursor.getItem().getName().contains(name) && !name.contains(cursor.getItem().getName())) {
                        cursor.moveToNext();
                        continue;
                    }
                }
                if(maxPrice != -1 ){
                    if (cursor.getItem().getSellPrice() < minPrice || cursor.getItem().getSellPrice() > maxPrice) {
                        cursor.moveToNext();
                        continue;
                    }
                }
                if(maxPrice == -1 && minPrice != -1){
                    if (cursor.getItem().getSellPrice() < minPrice) {
                        cursor.moveToNext();
                        continue;
                    }
                }
                if(category != null){
                    if (!cursor.getItem().getCategory().contains(category) && !category.contains(cursor.getItem().getCategory())) {
                        cursor.moveToNext();
                        continue;
                    }
                }
                if(!Objects.equals(type, "")){
                    if(!cursor.getItem().getType().toString().equals(type)){
                        cursor.moveToNext();
                        continue;
                    }
                }
                list.add(cursor.getItem());
                cursor.moveToNext();
            }
            if (!list.isEmpty()) {
                return list;
            }
        }
        return null;
    }

    private static ContentValues getContentValues(DataItem item) {
        ContentValues values = new ContentValues();
        if (item.getProductId() != null) {
            values.put(DBSchema.Table.Cols.ProductId, item.getProductId());
        }
        values.put(DBSchema.Table.Cols.Name, item.getName());
        values.put(DBSchema.Table.Cols.Type, item.getType().toString());
        values.put(DBSchema.Table.Cols.Count, item.getCount());
        values.put(DBSchema.Table.Cols.Buy_price, item.getBuyPrice());
        values.put(DBSchema.Table.Cols.Sell_price, item.getSellPrice());
        values.put(DBSchema.Table.Cols.Category, item.getCategory());

        return values;
    }

    public void addToDB(DataItem dataItem) {
        ContentValues contentValues = getContentValues(dataItem);
        database.insert(DBSchema.Table.NAME, null, contentValues);
    }

    private MyCursorWrapper query() {
        Cursor cursor = database.query(
                DBSchema.Table.NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        return new MyCursorWrapper(cursor);
    }



}
