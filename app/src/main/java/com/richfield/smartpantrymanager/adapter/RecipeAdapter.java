package com.richfield.smartpantrymanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.richfield.smartpantrymanager.R;
import com.richfield.smartpantrymanager.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    private List<Recipe> items = new ArrayList<>();
    private final OnRecipeClickListener listener;
    private boolean showAvailability;

    public RecipeAdapter(OnRecipeClickListener listener, boolean showAvailability) {
        this.listener = listener;
        this.showAvailability = showAvailability;
    }

    public void setItems(List<Recipe> newItems) {
        items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = items.get(position);
        holder.name.setText(recipe.getName());
        holder.category.setText(recipe.getCategory());
        holder.desc.setText(recipe.getDescription());
        if (showAvailability) {
            holder.status.setVisibility(View.VISIBLE);
            if (recipe.isAvailable()) {
                holder.status.setText(R.string.available);
                holder.status.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.success));
            } else {
                holder.status.setText(R.string.not_available);
                holder.status.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.error));
            }
        } else {
            holder.status.setVisibility(View.GONE);
        }
        holder.card.setOnClickListener(v -> {
            if (listener != null) listener.onRecipeClick(recipe);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView name, category, desc, status;

        ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.cardRecipe);
            name = itemView.findViewById(R.id.tvRecipeName);
            category = itemView.findViewById(R.id.tvCategory);
            desc = itemView.findViewById(R.id.tvDescription);
            status = itemView.findViewById(R.id.tvStatus);
        }
    }
}
