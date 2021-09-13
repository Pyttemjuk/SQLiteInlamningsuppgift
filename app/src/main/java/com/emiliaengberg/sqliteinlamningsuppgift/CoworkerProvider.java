package com.emiliaengberg.sqliteinlamningsuppgift;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CoworkerProvider extends ContentProvider {

    //Strings that are used for representing queries for whole table or specific row
    private static final int COWORKER = 100;
    private static final int COWORKER_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private CoworkerDBHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new CoworkerDBHelper(getContext());
        return true;
    }

    //Builds a UriMatcher that is used to determine witch database request is being made.
    public static UriMatcher buildUriMatcher(){
        String content = CoworkerContract.CONTENT_AUTHORITY;

        //The UriMatcher has paths that correspond to return when there is a match
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, CoworkerContract.PATH_COWORKERS, COWORKER);
        matcher.addURI(content, CoworkerContract.PATH_COWORKERS + "/#", COWORKER_ID);

        return matcher;
    }

    //Get MIME type of results. Directory or individual item.
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch(sUriMatcher.match(uri)){
            case COWORKER:
                return CoworkerContract.CoworkerEntry.CONTENT_TYPE;
            case COWORKER_ID:
                return CoworkerContract.CoworkerEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    //Method to query the database. Returns a cursor with the retrieved data. Takes a string array
    //with column names to be returned, a selection criteria, an argument to be checked against and
    //a sort order as in parameters.
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor retCursor;

        //Checks if there is match for the URI
        switch(sUriMatcher.match(uri)){
            case COWORKER:
                retCursor = db.query(
                        CoworkerContract.CoworkerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case COWORKER_ID:
                long _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        CoworkerContract.CoworkerEntry.TABLE_NAME,
                        projection,
                        CoworkerContract.CoworkerEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set the notification URI for the cursor to the one passed into the function. This
        // causes the cursor to register a content observer to watch for changes that happen to
        // this URI and any URI that begins with this path.
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    //Uses content values for inserting a new row in the table
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        //Uses SQLite method insert to add a new row to the database
        _id = db.insert(CoworkerContract.CoworkerEntry.TABLE_NAME, null, values);

        if(_id > 0){
            returnUri = CoworkerContract.CoworkerEntry.buildCoworkerUri(_id);
        } else{
            throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
        }

        //URI passed into the function to notify any observers that the uri has changed.
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    //Uses a set of string and arguments to determine which row that should be deleted
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows; // Number of rows effected

        //Uses SQLite method delete to delete a row from the database
        rows = db.delete(CoworkerContract.CoworkerEntry.TABLE_NAME, selection, selectionArgs);

        // Because null could delete all rows:
        if(selection == null || rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    //Uses a set of string and arguments to determine which row that should be updated. The
    //content values are the new values for that row
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows;

        //Uses SQLite method update to update a row in the database
        rows = db.update(CoworkerContract.CoworkerEntry.TABLE_NAME, values, selection, selectionArgs);

        if(rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }
}
