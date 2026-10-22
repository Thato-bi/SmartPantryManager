package com.richfield.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.richfield.smartpantrymanager.db.DatabaseHelper;
import com.richfield.smartpantrymanager.model.Recipe;
import com.richfield.smartpantrymanager.model.RecipeIngredient;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.recipe_details);
        }

        long recipeId = getIntent().getLongExtra("recipe_id", -1);
        if (recipeId == -1) {
            finish();
            return;
        }

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        Recipe recipe = null;
        List<Recipe> allWithStatus = dbHelper.getAllRecipesWithAvailability();
        for (Recipe r : allWithStatus) {
            if (r.getId() == recipeId) {
                recipe = r;
                break;
            }
        }
        if (recipe == null) {
            finish();
            return;
        }

        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);
        TextView tvCategory = findViewById(R.id.tvDetailCategory);
        TextView tvServings = findViewById(R.id.tvDetailServings);
        TextView tvPrep = findViewById(R.id.tvDetailPrep);
        TextView tvCook = findViewById(R.id.tvDetailCook);
        TextView tvStatus = findViewById(R.id.tvDetailStatus);
        TextView tvInstructions = findViewById(R.id.tvInstructions);
        LinearLayout layoutIngredients = findViewById(R.id.layoutIngredients);
        LinearLayout layoutMissing = findViewById(R.id.layoutMissing);
        TextView tvMissingLabel = findViewById(R.id.tvMissingLabel);

        tvName.setText(recipe.getName());
        tvDesc.setText(recipe.getDescription());
        tvCategory.setText(getString(R.string.category, recipe.getCategory()));
        tvServings.setText(getString(R.string.servings, recipe.getServings()));
        tvPrep.setText(getString(R.string.prep_time, recipe.getPrepTime()));
        tvCook.setText(getString(R.string.cook_time, recipe.getCookTime()));
        tvInstructions.setText(recipe.getInstructions());

        if (recipe.isAvailable()) {
            tvStatus.setText(R.string.available);
            tvStatus.setTextColor(getResources().getColor(R.color.success));
            layoutMissing.setVisibility(View.GONE);
            tvMissingLabel.setVisibility(View.GONE);
        } else {
            tvStatus.setText(R.string.not_available);
            tvStatus.setTextColor(getResources().getColor(R.color.error));
            layoutMissing.setVisibility(View.VISIBLE);
            tvMissingLabel.setVisibility(View.VISIBLE);
            if (recipe.getMissingItems() != null) {
                for (String m : recipe.getMissingItems()) {
                    TextView tv = new TextView(this);
                    tv.setText("• " + m);
                    tv.setTextSize(14);
                    tv.setPadding(0, 4, 0, 4);
                    layoutMissing.addView(tv);
                }
            }
        }

        for (RecipeIngredient ri : recipe.getIngredients()) {
            TextView tv = new TextView(this);
            tv.setText("• " + ri.getDisplay());
            tv.setTextSize(15);
            tv.setPadding(0, 6, 0, 6);
            layoutIngredients.addView(tv);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
