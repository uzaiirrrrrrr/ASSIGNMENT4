package com.example.assignment4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ParkingAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ParkingAdapter adapter;
    private ParkingDatabaseHelper dbHelper;
    private List<ParkingSlot> parkingSlots = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "ParkingPrefs";
    private static final String KEY_THEME = "theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply saved theme
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        applyTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new ParkingDatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ParkingDetailActivity for adding
                Intent intent = new Intent(MainActivity.this, ParkingDetailActivity.class);
                startActivity(intent);
            }
        });

        adapter = new ParkingAdapter(this, parkingSlots, this);
        recyclerView.setAdapter(adapter);

        loadData();
    }

    private void applyTheme() {
        int theme = sharedPreferences.getInt(KEY_THEME, 0);
        switch (theme) {
            case 1:
                setTheme(R.style.Theme_ASSIGNMENT4_Light);
                break;
            case 2:
                setTheme(R.style.Theme_ASSIGNMENT4_Dark);
                break;
            case 3:
                setTheme(R.style.Theme_ASSIGNMENT4_Custom);
                break;
            default:
                setTheme(R.style.Theme_ASSIGNMENT4);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // Reload data in case of changes from DetailActivity
    }

    private void loadData() {
        parkingSlots.clear();
        parkingSlots.addAll(dbHelper.getAllParkingSlots());

        if (parkingSlots.isEmpty() && isNetworkAvailable()) {
            Toast.makeText(this, "Fetching data from API...", Toast.LENGTH_SHORT).show();
            NetworkUtils.fetchParkingSlots(new NetworkUtils.ParkingDataListener() {
                @Override
                public void onDataFetched(final List<ParkingSlot> slots) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (ParkingSlot slot : slots) {
                                dbHelper.insertParkingSlot(slot);
                            }
                            parkingSlots.clear();
                            parkingSlots.addAll(dbHelper.getAllParkingSlots());
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Data Sync Complete", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(final String error) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            adapter.notifyDataSetChanged();
            if (parkingSlots.isEmpty()) {
                Toast.makeText(this, "No valid data available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean themeChanged = false;

        if (item.getItemId() == R.id.action_theme_light) {
            editor.putInt(KEY_THEME, 1);
            themeChanged = true;
        } else if (item.getItemId() == R.id.action_theme_dark) {
            editor.putInt(KEY_THEME, 2);
            themeChanged = true;
        } else if (item.getItemId() == R.id.action_theme_custom) {
            editor.putInt(KEY_THEME, 3);
            themeChanged = true;
        } else if (item.getItemId() == R.id.action_logout) {
            editor.putBoolean("isLoggedIn", false);
            editor.apply();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        if (themeChanged) {
            editor.apply();
            recreate(); // Restart activity to apply theme
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Callbacks from Adapter implementation
    @Override
    public void onItemClick(ParkingSlot slot) {
        // View Details / WebView
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("slotName", slot.getSlotName());
        startActivity(intent);
    }

    @Override
    public void onEditClick(ParkingSlot slot) {
        Intent intent = new Intent(this, ParkingDetailActivity.class);
        intent.putExtra("parkingId", slot.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(ParkingSlot slot) {
        dbHelper.deleteParkingSlot(slot.getId());
        loadData(); // Refresh list
        Toast.makeText(this, "Deleted " + slot.getSlotName(), Toast.LENGTH_SHORT).show();
    }
}