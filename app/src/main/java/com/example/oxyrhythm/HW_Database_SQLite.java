//Class to store hardware generated data using SQLite

package com.example.oxyrhythm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;

class HW_Database_SQLite extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "HW_Data.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "hw_data";
    private static final String COLUMN_ID = "_id";
    static final String COLUMN_HEART_RATE = "heart_rate";
    static final String COLUMN_BLOOD_OXYGEN = "blood_oxygen";
    static final String COLUMN_TEMPERATURE = "temperature";
    static final String COLUMN_TIMESTAMP = "timestamp";


    HW_Database_SQLite(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HEART_RATE + " INTEGER, " +
                COLUMN_BLOOD_OXYGEN + " TEXT, " +
                COLUMN_TEMPERATURE + " TEXT, " +
                COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP);";
        db.execSQL(query);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addHWData(int heartRate, String bloodOxygen, String temperature) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_HEART_RATE, heartRate);
        cv.put(COLUMN_BLOOD_OXYGEN, bloodOxygen);
        cv.put(COLUMN_TEMPERATURE, temperature);

        long result = db.insert(TABLE_NAME, null, cv);

        if (result == -1) {
            //Toast.makeText(context, "Failed to insert data", Toast.LENGTH_SHORT).show();
            Log.e("HW_Database_SQLite", "Failed to insert data");
        } else {
            //Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
            Log.d("HW_Database_SQLite", "Data inserted successfully");
        }

        // Close the database to avoid memory leaks
        db.close();
    }


    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        try {
            if (db != null) {
                cursor = db.rawQuery(query, null);

                if (cursor != null && cursor.moveToFirst()) {
                    return cursor; // Return the cursor when data is available and cursor is positioned correctly
                } else {
                    Log.d("HW_Database_SQLite", "Cursor is null or empty.");
                }
            } else {
                Log.d("HW_Database_SQLite", "Database is null.");
            }
        } catch (Exception e) {
            Log.e("HW_Database_SQLite", "Error reading data from the database", e);
        }

        return null; // Return null if there's an issue
    }



    void updateData(long rowId, int heartRate, String bloodOxygen, String temperature) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_HEART_RATE, heartRate);
        cv.put(COLUMN_BLOOD_OXYGEN, bloodOxygen);
        cv.put(COLUMN_TEMPERATURE, temperature);

        long result = db.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[]{String.valueOf(rowId)});

        if (result == -1) {
            Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }


    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
