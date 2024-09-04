package com.example.car.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {
        super(context, "database", null, 1);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DBSchema.Table.NAME + " (" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBSchema.Table.Cols.ProductId + " TEXT," +
                DBSchema.Table.Cols.Type + " TEXT, " +
                DBSchema.Table.Cols.Name + " TEXT, " +
                DBSchema.Table.Cols.Count + " INTEGER, " +
                DBSchema.Table.Cols.Buy_price + " REAL, " +
                DBSchema.Table.Cols.Sell_price + " REAL, " +
                DBSchema.Table.Cols.Category + " TEXT" +
                ")");
    }
}
