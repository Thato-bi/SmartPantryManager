package com.richfield.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS = "smart_pantry_prefs";
    private static final String KEY_EXPIRING = "expiring_alerts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.settings);
        }

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        SwitchCompat switchExpiring = findViewById(R.id.switchExpiring);
        TextView tvAbout = findViewById(R.id.tvAbout);
        TextView tvVersion = findViewById(R.id.tvVersion);

        switchExpiring.setChecked(prefs.getBoolean(KEY_EXPIRING, true));
        switchExpiring.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            prefs.edit().putBoolean(KEY_EXPIRING, isChecked).apply();
        });

        tvAbout.setText(R.string.about_text);
        tvVersion.setText(R.string.version);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
