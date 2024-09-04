package com.example.car.database;

public class DBSchema {
    public static final class Table {

        public static final String NAME = "auto_parts";
        public static final class Cols {
            public static final String ProductId = "productId";
            public static final String Type = "type";
            public static final String Name = "name";
            public static final String Count = "count";
            public static final String Buy_price = "buy_price";
            public static final String Sell_price = "sell_price";
            public static final String Category = "category";
        }
    }
}
