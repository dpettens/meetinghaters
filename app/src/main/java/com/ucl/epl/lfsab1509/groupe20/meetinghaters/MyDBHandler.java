package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "meetingHaterInner.db";
    private static final String TABLE_USER = "user";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LOCATION = "location";

    public MyDBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " +
                TABLE_USER + "("
                + COLUMN_ID + " TEXT PRIMARY KEY," + COLUMN_LOCATION
                + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void addUser(String user) {

        ContentValues values = new ContentValues();
        Log.e("VALUES :",values.toString());
        values.put(COLUMN_ID, user);
        values.put(COLUMN_LOCATION, "none");
        Log.e("VALUES :", values.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        Long test = db.insert(TABLE_USER, null, values);
        Log.e("SQLITE INSERT :", Long.toString(test));
        db.close();
    }

    public String isRegistered() {

        String query = "Select * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        String returned = null;

        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            returned = cursor.getString(0);
            cursor.close();
        } else {
            returned = null;
        }
        db.close();
        return returned;
    }

    public void deleteUser(String user) {

        String query = "DROP TABLE " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        db.close();
    }

}
