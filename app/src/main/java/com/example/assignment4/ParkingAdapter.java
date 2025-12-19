package com.example.assignment4;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ParkingViewHolder> {

    private Context context;
    private List<ParkingSlot> parkingSlotList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ParkingSlot slot);

        void onEditClick(ParkingSlot slot);

        void onDeleteClick(ParkingSlot slot);
    }

    public ParkingAdapter(Context context, List<ParkingSlot> parkingSlotList, OnItemClickListener listener) {
        this.context = context;
        this.parkingSlotList = parkingSlotList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_parking_slot, parent, false);
        return new ParkingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingViewHolder holder, int position) {
        final ParkingSlot slot = parkingSlotList.get(position);

        holder.tvSlotName.setText(slot.getSlotName());
        holder.tvLocation.setText("Location: " + slot.getLocation());
        holder.tvPrice.setText(String.format("$%.2f/hr", slot.getPricePerHour()));

        if (slot.isOccupied()) {
            holder.tvStatus.setText("Occupied");
            holder.tvStatus.setTextColor(android.graphics.Color.RED);
        } else {
            holder.tvStatus.setText("Available");
            holder.tvStatus.setTextColor(android.graphics.Color.GREEN);
        }

        // Click Listener for Details
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemClick(slot);
            }
        });

        // Long Click for Popup Menu
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopupMenu(v, slot);
                return true;
            }
        });
    }

    private void showPopupMenu(View view, final ParkingSlot slot) {
        PopupMenu popup = new PopupMenu(context, view);
        // We will inflate via code or resources. Let's assume a simple menu resource
        // later or add items directly
        popup.getMenu().add("Edit");
        popup.getMenu().add("Delete");

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().equals("Edit")) {
                    listener.onEditClick(slot);
                } else if (item.getTitle().equals("Delete")) {
                    listener.onDeleteClick(slot);
                }
                return true;
            }
        });
        popup.show();
    }

    @Override
    public int getItemCount() {
        return parkingSlotList.size();
    }

    public void updateData(List<ParkingSlot> newSlots) {
        this.parkingSlotList = newSlots;
        notifyDataSetChanged();
    }

    public static class ParkingViewHolder extends RecyclerView.ViewHolder {
        TextView tvSlotName, tvLocation, tvStatus, tvPrice;

        public ParkingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSlotName = itemView.findViewById(R.id.tvSlotName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
