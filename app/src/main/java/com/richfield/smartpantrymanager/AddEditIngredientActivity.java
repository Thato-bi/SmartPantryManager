package com.richfield.smartpantrymanager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.richfield.smartpantrymanager.db.DatabaseHelper;
import com.richfield.smartpantrymanager.model.Ingredient;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText etName, etQuantity, etExpiry;
    private Spinner spinnerUnit;
    private DatabaseHelper dbHelper;
    private long ingredientId = -1;
    private boolean isEdit = false;

    private static final String[] UNITS = {"g", "kg", "ml", "l", "pcs"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = DatabaseHelper.getInstance(this);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiry = findViewById(R.id.etExpiry);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, UNITS);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(unitAdapter);

        if (getIntent().hasExtra("ingredient_id")) {
            ingredientId = getIntent().getLongExtra("ingredient_id", -1);
            isEdit = true;
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.edit_ingredient);
            }
            loadIngredient();
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.add_ingredient);
            }
        }

        btnSave.setOnClickListener(v -> saveIngredient());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadIngredient() {
        Ingredient ing = dbHelper.getIngredientById(ingredientId);
        if (ing != null) {
            etName.setText(ing.getName());
            etQuantity.setText(String.valueOf(ing.getQuantity()));
            etExpiry.setText(ing.getExpiryDate() != null ? ing.getExpiryDate() : "");
            for (int i = 0; i < UNITS.length; i++) {
                if (UNITS[i].equalsIgnoreCase(ing.getUnit())) {
                    spinnerUnit.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveIngredient() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();
        String expiry = etExpiry.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError(getString(R.string.validation_name));
            etName.requestFocus();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(qtyStr);
            if (quantity <= 0) {
                etQuantity.setError(getString(R.string.validation_quantity));
                etQuantity.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            etQuantity.setError(getString(R.string.validation_quantity));
            etQuantity.requestFocus();
            return;
        }

        Ingredient ingredient = new Ingredient(name, quantity, unit, expiry.isEmpty() ? null : expiry);

        if (isEdit) {
            ingredient.setId(ingredientId);
            dbHelper.updateIngredient(ingredient);
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.addIngredient(ingredient);
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
