package com.example.car.utils;

import android.util.ArrayMap;

import androidx.lifecycle.ViewModel;

import com.example.car.data.DataItem;
import com.example.car.data.DataType;

public class SharedViewModel extends ViewModel {
    private final ArrayMap<String, DataItem> oilList = new ArrayMap<>();
    private final ArrayMap<String, DataItem> filterList = new ArrayMap<>();
    private final ArrayMap<String, DataItem> partsList = new ArrayMap<>();
    private DataType type;


    public void addItem(DataItem dataItem) {
        if (dataItem.getType().equals(DataType.OIL) || dataItem.getType().equals(DataType.МАСЛО)) {
            oilList.put(dataItem.getName(), dataItem);
        } else if (dataItem.getType().equals(DataType.FILTER) || dataItem.getType().equals(DataType.ФИЛЬТР)) {
            filterList.put(dataItem.getName(), dataItem);
        } else if (dataItem.getType().equals(DataType.AUTOPARTS) || dataItem.getType().equals(DataType.АВТОЗАПЧАСТИ)) {
            partsList.put(dataItem.getName(), dataItem);
        }
    }

    public ArrayMap<String, DataItem> getItems() {
        if (type.equals(DataType.OIL) || type.equals(DataType.МАСЛО)) {
            return oilList;
        } else if (type.equals(DataType.FILTER) || type.equals(DataType.ФИЛЬТР)) {
            return filterList;
        } else if (type.equals(DataType.AUTOPARTS) || type.equals(DataType.АВТОЗАПЧАСТИ) ){
            return partsList;
        }
        return  null;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public DataType getType() {
        return type;
    }
}
