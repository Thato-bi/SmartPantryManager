package com.richfield.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.richfield.smartpantrymanager.db.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private TextView tvIngredientCount;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = DatabaseHelper.getInstance(this);
        tvIngredientCount = findViewById(R.id.tvIngredientCount);

        Button btnPantry = findViewById(R.id.btnViewPantry);
        Button btnSuggestions = findViewById(R.id.btnGetSuggestions);
        Button btnAllRecipes = findViewById(R.id.btnBrowseRecipes);
        Button btnSettings = findViewById(R.id.btnSettings);

        btnPantry.setOnClickListener(v -> startActivity(new Intent(this, PantryListActivity.class)));
        btnSuggestions.setOnClickListener(v -> startActivity(new Intent(this, SuggestedRecipesActivity.class)));
        btnAllRecipes.setOnClickListener(v -> startActivity(new Intent(this, AllRecipesActivity.class)));
        btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_pantry) {
                startActivity(new Intent(this, PantryListActivity.class));
                return true;
            } else if (id == R.id.nav_suggestions) {
                startActivity(new Intent(this, SuggestedRecipesActivity.class));
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int count = dbHelper.getIngredientCount();
        tvIngredientCount.setText(getString(R.string.ingredients_count, count));
    }
}
