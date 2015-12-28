package me.bitfrom.weatherapp.database;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import static me.bitfrom.weatherapp.database.WeatherContract.CONTENT_AUTHORITY;
import static me.bitfrom.weatherapp.database.WeatherContract.PATH_WEATHER;
import static me.bitfrom.weatherapp.database.WeatherContract.WeatherEntry;

public class WeatherProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private WeatherDBHelper mOpenHelper;

    private static final String UNKNOWN_URI = "Unknown uri: ";

    public static final int WEATHER = 1000;
    public static final int WEATHER_ID = 1001;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        matcher.addURI(authority, PATH_WEATHER, WEATHER);
        matcher.addURI(authority, PATH_WEATHER + "/#", WEATHER_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new WeatherDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case WEATHER:
                return WeatherEntry.CONTENT_TYPE;
            case WEATHER_ID:
                return WeatherEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(WeatherEntry.TABLE_NAME);

        switch (sUriMatcher.match(uri)) {
            case WEATHER_ID:
            {
                queryBuilder.appendWhere(WeatherEntry._ID + "=" + uri.getLastPathSegment());
                break;
            }
            case WEATHER:
            {
                break;
            }
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        retCursor = queryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case WEATHER: {
                long _id = db.insert(WeatherEntry.TABLE_NAME, null, values);

                if (_id > 0)
                    returnUri = WeatherEntry.buildHamstersUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);

                break;
            }
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsDeleted;

        switch (match) {
            case WEATHER:
                rowsDeleted = db.delete(WeatherEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case WEATHER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(WeatherEntry.TABLE_NAME,
                            WeatherEntry._ID + "=" + id, null);
                } else {
                    rowsDeleted = db.delete(WeatherEntry.TABLE_NAME,
                            WeatherEntry._ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        if (rowsDeleted != 0) getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case WEATHER:
                rowsUpdated = db.update(WeatherEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case WEATHER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(WeatherEntry.TABLE_NAME,
                            values,
                            WeatherEntry._ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = db.update(WeatherEntry.TABLE_NAME,
                            values,
                            WeatherEntry._ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        if (rowsUpdated != 0) getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WEATHER:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value: values) {
                        long _id = db.insert(WeatherEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
