package com.ucl.epl.lfsab1509.groupe20.meetinghaters.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final String TAG = "MyDBHandler";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "meetingHaterInner.db";

    private static final String TABLE_USER = "user";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_TOKEN = "token";

    private static final int COLUMN_ID_INDEX = 0;
    private static final int COLUMN_LOCATION_INDEX = 1;
    private static final int COLUMN_TOKEN_INDEX = 2;

    public MyDBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_ID + " TEXT PRIMARY KEY, " + COLUMN_LOCATION
                + " TEXT," + COLUMN_TOKEN + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void addUser(String user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, user);
        values.put(COLUMN_LOCATION, "none");
        values.put(COLUMN_TOKEN, "none");

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public String isRegistered() {
        String query = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String returned;

        if (cursor.moveToFirst()) {
            returned = cursor.getString(COLUMN_ID_INDEX);
            cursor.close();
        } else {
            returned = null;
        }

        db.close();
        return returned;
    }

    public String getUser() {
        String query = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String user;

        cursor.moveToFirst();
        user = cursor.getString(COLUMN_ID_INDEX);
        cursor.close();
        db.close();
        
        return user;
    }

    public void deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, null, null);
        db.close();
    }

    public void updateLocation(String location) {
        String user = this.getUser();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_LOCATION, location);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_USER, contentValues, COLUMN_ID + "=?", new String[] {user});
        db.close();
    }

    public String getToken() {
        String query = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String token;

        cursor.moveToFirst();
        token = cursor.getString(COLUMN_TOKEN_INDEX);
        cursor.close();
        db.close();
        
        return token;
    }
    
    public void setToken(String token) {
        String user = this.getUser();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COLUMN_TOKEN, token);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_USER, contentValues, COLUMN_ID + "=?", new String[] {user});
        db.close();
    }

}
