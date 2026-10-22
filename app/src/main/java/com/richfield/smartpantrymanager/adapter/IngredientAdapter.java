package com.richfield.smartpantrymanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.richfield.smartpantrymanager.R;
import com.richfield.smartpantrymanager.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onEdit(Ingredient ingredient);
        void onDelete(Ingredient ingredient);
    }

    private List<Ingredient> items = new ArrayList<>();
    private final OnItemClickListener listener;

    public IngredientAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Ingredient> newItems) {
        items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ing = items.get(position);
        holder.name.setText(ing.getName());
        holder.quantity.setText(ing.getDisplayQuantity());
        if (ing.getExpiryDate() != null && !ing.getExpiryDate().isEmpty()) {
            holder.expiry.setVisibility(View.VISIBLE);
            holder.expiry.setText("Expires: " + ing.getExpiryDate());
        } else {
            holder.expiry.setVisibility(View.GONE);
        }
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(ing);
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(ing);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, quantity, expiry;
        ImageButton btnEdit, btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvIngredientName);
            quantity = itemView.findViewById(R.id.tvQuantity);
            expiry = itemView.findViewById(R.id.tvExpiry);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
