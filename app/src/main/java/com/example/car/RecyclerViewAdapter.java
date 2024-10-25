package com.example.car;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car.data.DataItem;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(DataItem dataItem);
    }


    private final ArrayList<DataItem> dataItemList;
    private final OnItemClickListener listener;

    public RecyclerViewAdapter(ArrayList<DataItem> items, OnItemClickListener listener) {
        this.dataItemList = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_sheet, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataItem item = dataItemList.get(position);
        holder.textViewModel.setText(item.getName());
        if (position == 0) {
            holder.textViewModel.setBackgroundResource(R.drawable.without_gradient);
        } else {
            holder.textViewModel.setBackgroundResource(R.drawable.with_gradient);
        }
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(dataItemList.get(position));
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataItemList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewModel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewModel = itemView.findViewById(R.id.text);

        }
    }


}