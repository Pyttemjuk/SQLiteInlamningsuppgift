package com.emiliaengberg.sqliteinlamningsuppgift;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class CoworkerDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "coworkerdb";

    private ContentResolver mContentResolver;

    //Constructor that takes the context as in parameter and also initiates the ContentResolver
    public CoworkerDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String for creating table. Uses the CoworkerContract class to setup columns
        String CREATE_TABLE = "CREATE TABLE " + CoworkerContract.CoworkerEntry.TABLE_NAME + "("
                + CoworkerContract.CoworkerEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CoworkerContract.CoworkerEntry.COLUMN_NAME + " TEXT,"
                + CoworkerContract.CoworkerEntry.COLUMN_SHIFT + " TEXT,"
                + CoworkerContract.CoworkerEntry.COLUMN_NUMBER + " TEXT"+ ")";

        //Inserts the coworker table into the database
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + CoworkerContract.CoworkerEntry.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    //Add new user details
    public void addCoworker(Coworker coworker) {

        //Adds coworker data to the ContentValue object. Used for inserting into database
        ContentValues contentValues = new ContentValues();
        contentValues.put(CoworkerContract.CoworkerEntry.COLUMN_NAME, coworker.getName());
        contentValues.put(CoworkerContract.CoworkerEntry.COLUMN_SHIFT, coworker.getShiftNumber());
        contentValues.put(CoworkerContract.CoworkerEntry.COLUMN_NUMBER, coworker.getPhoneNumber());

        //Insert new row with coworker data into table using ContentResolver
        mContentResolver.insert(CoworkerContract.CoworkerEntry.CONTENT_URI, contentValues);
    }

    //Get user details based on coworker id.
    public Coworker getCoworkerById(int coworkerId) {

        //Sets up which columns we want to retrieve
        String[] projection = {CoworkerContract.CoworkerEntry.COLUMN_ID,
                CoworkerContract.CoworkerEntry.COLUMN_NAME,
                CoworkerContract.CoworkerEntry.COLUMN_SHIFT,
                CoworkerContract.CoworkerEntry.COLUMN_NUMBER };

        //uses a cursor object and ContentResolver to query the database and retrieve data based on
        //coworker id. Note: Not used in this project
        Cursor cursor = mContentResolver.query(CoworkerContract.CoworkerEntry.CONTENT_URI,
                projection,
                CoworkerContract.CoworkerEntry.COLUMN_ID + " =?",
                new String[]{String.valueOf(coworkerId)},
                null);

        //Gets coworker with chosen id and adds to cursor
        if(cursor != null) {
            cursor.moveToFirst();
        }

        //Adds information from cursor into a coworker object
        int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CoworkerContract.CoworkerEntry.COLUMN_ID)));
        String name = cursor.getString(cursor.getColumnIndex(CoworkerContract.CoworkerEntry.COLUMN_NAME));
        String shift = cursor.getString(cursor.getColumnIndex(CoworkerContract.CoworkerEntry.COLUMN_SHIFT));
        String phoneNumber = cursor.getString(cursor.getColumnIndex(CoworkerContract.CoworkerEntry.COLUMN_NUMBER));
        Coworker coworker = new Coworker(id, name, shift, phoneNumber);

        return coworker;
    }

    //Get user details based on coworker name
    public ArrayList<Coworker> getCoworkerByShift(String coworkerShift) {

        ArrayList<Coworker> coworkerList = new ArrayList<>();

        //Sets up which columns we want to retrieve
        String[] projection = {CoworkerContract.CoworkerEntry.COLUMN_ID,
                CoworkerContract.CoworkerEntry.COLUMN_NAME,
                CoworkerContract.CoworkerEntry.COLUMN_SHIFT,
                CoworkerContract.CoworkerEntry.COLUMN_NUMBER };

        //uses a cursor object and ContentResolver to query the database and retrieve data based on
        //coworker shift.
        Cursor cursor = mContentResolver.query(CoworkerContract.CoworkerEntry.CONTENT_URI,
                projection,
                CoworkerContract.CoworkerEntry.COLUMN_SHIFT + " =?",
                new String[]{String.valueOf(coworkerShift)},
                null);

        //Adds data about coworkers that matches chosen shift into the array
        if(cursor != null) {
            if (cursor.moveToFirst()){
                do {
                    Coworker coworker = new Coworker();
                    coworker.setId(Integer.parseInt(cursor.getString(0)));
                    coworker.setName(cursor.getString(1));
                    coworker.setShiftNumber(cursor.getString(2));
                    coworker.setPhoneNumber(cursor.getString(3));
                    coworkerList.add(coworker);
                } while (cursor.moveToNext());
            }
        }

        //Returns a list with coworkers that match chosen shift
        return coworkerList;
    }

    //Get user details based on coworker name
    public ArrayList<Coworker> getCoworkerByName(String coworkerName) {

        ArrayList<Coworker> coworkerList = new ArrayList<>();

        //Sets up which columns we want to retrieve
        String[] projection = {CoworkerContract.CoworkerEntry.COLUMN_ID,
                CoworkerContract.CoworkerEntry.COLUMN_NAME,
                CoworkerContract.CoworkerEntry.COLUMN_SHIFT,
                CoworkerContract.CoworkerEntry.COLUMN_NUMBER };

        //uses a cursor object and ContentResolver to query the database and retrieve data based on
        //coworker name.
        Cursor cursor = mContentResolver.query(CoworkerContract.CoworkerEntry.CONTENT_URI,
                projection,
                CoworkerContract.CoworkerEntry.COLUMN_NAME + " =? COLLATE NOCASE",
                new String[]{String.valueOf(coworkerName)},
                null);

        //Adds data about coworkers that matches name into the array
        if(cursor != null) {
            if (cursor.moveToFirst()){
                do {
                    Coworker coworker = new Coworker();
                    coworker.setId(Integer.parseInt(cursor.getString(0)));
                    coworker.setName(cursor.getString(1));
                    coworker.setShiftNumber(cursor.getString(2));
                    coworker.setPhoneNumber(cursor.getString(3));
                    coworkerList.add(coworker);
                } while (cursor.moveToNext());
            }
        }

        //Returns a list with coworkers that match user input of name
        return coworkerList;
    }

    //Get coworker details
    public ArrayList<Coworker> getAllCoworkers() {

        ArrayList<Coworker> coworkerList = new ArrayList<>();

        //uses a cursor object and ContentResolver to query the database and retrieve all data
        Cursor cursor = mContentResolver.query(CoworkerContract.CoworkerEntry.CONTENT_URI,
                null, null, null, null );

        if (cursor.moveToFirst()){
            do {
                Coworker coworker = new Coworker();
                coworker.setId(Integer.parseInt(cursor.getString(0)));
                coworker.setName(cursor.getString(1));
                coworker.setShiftNumber(cursor.getString(2));
                coworker.setPhoneNumber(cursor.getString(3));
                coworkerList.add(coworker);
            } while (cursor.moveToNext());
        }

        //Returns a list with all coworkers
        return coworkerList;
    }

    public boolean updateCoworker(Coworker coworker) {

        boolean result = false;

        //Adds coworker data to the ContentValue object. Used for updating data about a coworker
        //in the database
        ContentValues contentValues = new ContentValues();
        contentValues.put(CoworkerContract.CoworkerEntry.COLUMN_NAME, coworker.getName());
        contentValues.put(CoworkerContract.CoworkerEntry.COLUMN_SHIFT, coworker.getShiftNumber());
        contentValues.put(CoworkerContract.CoworkerEntry.COLUMN_NUMBER, coworker.getPhoneNumber());

        //Uses ContentResolver to update data about a specific coworker in the database using
        // the id for that coworker.
        int rowsUpdated = mContentResolver.update(CoworkerContract.CoworkerEntry.CONTENT_URI,
                contentValues,
                CoworkerContract.CoworkerEntry.COLUMN_ID + " =?",
                new String[]{String.valueOf(coworker.getId())});

        if (rowsUpdated > 0) {
            result = true;
        }


        return result;
    }

    public boolean deleteCoworker(Coworker coworker) {

        boolean result = false;

        //Uses ContentResolver to delete a specific coworker from the database using the id for that
        //coworker.
       int rowsDeleted =  mContentResolver.delete(CoworkerContract.CoworkerEntry.CONTENT_URI,
                CoworkerContract.CoworkerEntry.COLUMN_ID + " =?",
                new String[]{String.valueOf(coworker.getId())});

        if (rowsDeleted > 0)
            result = true;

        return result;
    }
}
