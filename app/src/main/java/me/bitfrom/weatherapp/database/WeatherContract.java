package me.bitfrom.weatherapp.database;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class WeatherContract {

    public static final String CONTENT_AUTHORITY = "me.bitfrom.weatherapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_WEATHER = "weather";

    /**
     * Inner class that defines the table contents of the weather table
     * **/

    public static final class WeatherEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_WEATHER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER;

        public static final String TABLE_NAME = "weather";

        public static final String COLUMN_DATESTAMP = "date_stamp";

        public static final String COLUMN_CITY = "city";

        public static final String COLUMN_DAYTEMPERATURE = "day_temperature";

        public static final String COLUMN_MAXTEMPERATURE = "max_temperature";

        public static final String COLUMN_MINTEMPERATURE = "min_temperature";

        public static final String COLUMN_HUMIDITY = "humidity";

        public static final String COLUMN_DESCRIPTION = "description";

        public static final String COLUMN_WINDSPEED= "wind_speed";

        //Need this for caching data
        public static final String COLUMN_CURRENT_DATESTAMP = "current_date_stamp";

        public static final String[] PROJECTION = {
                TABLE_NAME + "." + _ID,
                COLUMN_CITY,
                COLUMN_DAYTEMPERATURE,
                COLUMN_MAXTEMPERATURE,
                COLUMN_MINTEMPERATURE,
                COLUMN_HUMIDITY,
                COLUMN_WINDSPEED,
                COLUMN_DESCRIPTION
        };

        public static final String[] SHARE_PROJECTION = {
                TABLE_NAME + "." + _ID,
                COLUMN_DATESTAMP,
                COLUMN_DAYTEMPERATURE,
                COLUMN_DESCRIPTION
        };

        public static Uri buildHamstersUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
