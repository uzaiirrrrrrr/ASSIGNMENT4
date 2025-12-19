package com.example.assignment4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ParkingDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "parking_db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PARKING = "parking_slots";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_IS_OCCUPIED = "is_occupied";
    private static final String COLUMN_PRICE = "price";

    public ParkingDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_PARKING + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_IS_OCCUPIED + " INTEGER, " +
                COLUMN_PRICE + " REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARKING);
        onCreate(db);
    }

    // Insert
    public long insertParkingSlot(ParkingSlot slot) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, slot.getSlotName());
        values.put(COLUMN_LOCATION, slot.getLocation());
        values.put(COLUMN_IS_OCCUPIED, slot.isOccupied() ? 1 : 0);
        values.put(COLUMN_PRICE, slot.getPricePerHour());
        long id = db.insert(TABLE_PARKING, null, values);
        db.close();
        return id;
    }

    // Read All
    public List<ParkingSlot> getAllParkingSlots() {
        List<ParkingSlot> parkingSlots = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PARKING, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
                boolean isOccupied = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_OCCUPIED)) == 1;
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE));

                parkingSlots.add(new ParkingSlot(id, name, location, isOccupied, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return parkingSlots;
    }

    // Update
    public int updateParkingSlot(ParkingSlot slot) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, slot.getSlotName());
        values.put(COLUMN_LOCATION, slot.getLocation());
        values.put(COLUMN_IS_OCCUPIED, slot.isOccupied() ? 1 : 0);
        values.put(COLUMN_PRICE, slot.getPricePerHour());

        int count = db.update(TABLE_PARKING, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(slot.getId()) });
        db.close();
        return count;
    }

    // Delete
    public void deleteParkingSlot(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PARKING, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
    }
}
