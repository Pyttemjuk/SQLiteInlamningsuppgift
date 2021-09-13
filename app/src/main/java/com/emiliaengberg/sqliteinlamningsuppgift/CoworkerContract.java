package com.emiliaengberg.sqliteinlamningsuppgift;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class CoworkerContract {

    //Name for the content provider, a unique identifier for the database Here package name
    //for the app is used since it is unique.
    public static final String CONTENT_AUTHORITY = "com.emiliaengberg.sqliteinlamningsuppgift";

    //Used to create the base of all URIs which apps will use to contact this content provider.
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Path to table to be added to the base URI
    public static final String PATH_COWORKERS = "coworkers";

    public static final class CoworkerEntry implements BaseColumns {
        //Content URI represents the base location for the table. This is the final URI
        //that is used for this content provider
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COWORKERS).build();

        //Prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_COWORKERS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_COWORKERS;

        //Creates strings with table and column names.
        public static final String TABLE_NAME = "coworkerdetails";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SHIFT = "shift";
        public static final String COLUMN_NUMBER = "phone_number";

        //Defines a function to build a URI to find a specific coworker based on the id.
        public static Uri buildCoworkerUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
