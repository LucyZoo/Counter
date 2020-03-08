package com.example.myapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;


public class CounterProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.myapplication.CounterProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/";
    static final Uri CONTENT_URI = Uri.parse(PROVIDER_NAME);

    static final String _ID = "_id";
    static final String NAME = "name";
    static final String FUNDS = "FundsAvailable";



    static final int USER_ID = 1;
    static final int CATEGORY_COUNTER = 2;
    static final int ADD_CATEGORY = 3;


    private static HashMap<String, String> STUDENTS_PROJECTION_MAP;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "#", USER_ID);
        uriMatcher.addURI(PROVIDER_NAME, "*/*", CATEGORY_COUNTER);
        uriMatcher.addURI(PROVIDER_NAME, "*", ADD_CATEGORY);
    }

    /**
     * Database specific constant declarations
     */

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "FundsAvailable";
    static final String USER_TABLE_NAME = "Users";
    static final String FUNDS_TABLE_NAME = "Funds";

    static final int DATABASE_VERSION = 1;
    static final String CREATE_USER_TABLE =
            " CREATE TABLE " + USER_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " name TEXT NOT NULL);";

    static final String CREATE_FUNDS_TABLE =
            " CREATE TABLE " + FUNDS_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY, " +
                    " Category TEXT NOT NULL, " +
                    " FundsAvailable NUM NOT NULL," +
                    " LastOpenedDate TEXT NOT NULL," +
                    " FOREIGN KEY (_id) REFERENCES " + USER_TABLE_NAME + "(_id));";

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_USER_TABLE);
            db.execSQL(CREATE_FUNDS_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  USER_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " +  FUNDS_TABLE_NAME);

            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        CounterProvider.DatabaseHelper dbHelper = new CounterProvider.DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add new user or new funds category
         */

        switch (uriMatcher.match(uri)) {
            case USER_ID:
                long rowID = db.insert(USER_TABLE_NAME, "", values);

                /**
                 * If record is added successfully
                 */
                if (rowID > 0) {
                    Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                throw new SQLException("Failed to add a record into " + uri);

            case CATEGORY_COUNTER:
                long insertResult = db.insert(FUNDS_TABLE_NAME, "", values);

                /**
                 * If record is added successfully
                 */
                if (insertResult > 0) {
                    Uri _uri = ContentUris.withAppendedId(CONTENT_URI, insertResult);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                throw new SQLException("Failed to add a record into " + uri);

            case ADD_CATEGORY:
                long categoryAdd = db.insert(FUNDS_TABLE_NAME, "", values);
                if (categoryAdd > 0){
                    Uri _uriCat = ContentUris.withAppendedId(CONTENT_URI, categoryAdd);
                    getContext().getContentResolver().notifyChange(_uriCat, null);
                    return _uriCat;
                }
                throw new SQLException("Failed to add a record into " + uri);

            default:
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(FUNDS_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case USER_ID:
                qb.setProjectionMap(STUDENTS_PROJECTION_MAP);
                break;

            case CATEGORY_COUNTER:
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, null);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        System.out.println(uri);
        System.out.println(selection);
        System.out.println(selectionArgs.length);
        int count = 0;

        String id = uri.getPathSegments().get(2);
        count = db.delete(FUNDS_TABLE_NAME, NAME +  " = ?" +
                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int count = 0;

        count = db.update(FUNDS_TABLE_NAME, values,
                _ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);


        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case CATEGORY_COUNTER:
                return "vnd.android.cursor.dir/vnd.example.students";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
