package com.example.assignment4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ParkingDetailActivity extends AppCompatActivity {

    private EditText etSlotName, etLocation, etPrice;
    private Switch swOccupied;
    private Button btnSave, btnDelete;

    private ParkingDatabaseHelper dbHelper;
    private int parkingId = -1;
    private ParkingSlot currentSlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_detail);

        etSlotName = findViewById(R.id.etSlotName);
        etLocation = findViewById(R.id.etLocation);
        etPrice = findViewById(R.id.etPrice);
        swOccupied = findViewById(R.id.swOccupied);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        dbHelper = new ParkingDatabaseHelper(this);

        if (getIntent().hasExtra("parkingId")) {
            parkingId = getIntent().getIntExtra("parkingId", -1);
            loadParkingData(parkingId);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parkingId != -1) {
                    dbHelper.deleteParkingSlot(parkingId);
                    Toast.makeText(ParkingDetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void loadParkingData(int id) {
        // In a real app we might fetch just single item by ID, here we reuse getAll for
        // simplicity or we can add getById
        // Let's iterate for now since list is small, or strictly I should add getById
        // in Helper.
        // For efficiency in big apps, getById is better.
        // I will do a quick raw query here or add to helper.
        // Or simply, I'll filter the list.
        List<ParkingSlot> list = dbHelper.getAllParkingSlots();
        for (ParkingSlot slot : list) {
            if (slot.getId() == id) {
                currentSlot = slot;
                etSlotName.setText(slot.getSlotName());
                etLocation.setText(slot.getLocation());
                etPrice.setText(String.valueOf(slot.getPricePerHour()));
                swOccupied.setChecked(slot.isOccupied());
                break;
            }
        }
    }

    private void saveData() {
        String name = etSlotName.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        if (name.isEmpty() || location.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        boolean isOccupied = swOccupied.isChecked();

        if (parkingId != -1 && currentSlot != null) {
            // Update
            currentSlot.setSlotName(name);
            currentSlot.setLocation(location);
            currentSlot.setPricePerHour(price);
            currentSlot.setOccupied(isOccupied);
            dbHelper.updateParkingSlot(currentSlot);
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        } else {
            // Insert
            ParkingSlot newSlot = new ParkingSlot(name, location, isOccupied, price);
            dbHelper.insertParkingSlot(newSlot);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
