package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper {

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
        String CREATE_USER_TABLE = "CREATE TABLE " +
                TABLE_USER + "("
                + COLUMN_ID + " TEXT PRIMARY KEY," + COLUMN_LOCATION
                + " TEXT," + COLUMN_TOKEN + "TEXT" + ")";
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

        Long test = db.insert(TABLE_USER, null, values);
        db.close();
    }

    public String getUser() {

        String query = "Select * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        String returned = null;

        if (cursor.moveToFirst()) {
            //cursor.moveToFirst();
            returned = cursor.getString(COLUMN_ID_INDEX);
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

    public void updateLocation(String location){

        String user = this.getUser();
        String query = "UPDATE " + TABLE_USER
                        + " SET " + COLUMN_LOCATION + " = " + location
                        + " WHERE " + COLUMN_ID + " = " + user;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        db.close();
    }

    public String getToken(){
        
        String query = "Select * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        String token = null;

        cursor.moveToFirst();
        token = cursor.getString(COLUMN_TOKEN_INDEX);
        cursor.close();
        db.close();

        return token;
    }

    public void setToken(String token){
        String user = this.getUser();
        String query = "UPDATE " + TABLE_USER
                + " SET " + COLUMN_TOKEN + " = " + token
                + " WHERE " + COLUMN_ID + " = " + user;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        db.close();
    }

}
