package me.bitfrom.weatherapp.ui.recyclerview;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FilterQueryProvider;
import android.widget.TextView;

import me.bitfrom.weatherapp.R;

import static me.bitfrom.weatherapp.database.WeatherContract.WeatherEntry;

public class ForecastRVAdapter extends RecyclerView.Adapter<ForecastRVAdapter.ViewHolder> {

    private static final String LOG_TAG = ForecastRVAdapter.class.getSimpleName();

    protected boolean mDataValid;
    protected boolean mAutoRequery;
    protected Cursor mCursor;
    protected Context mContext;
    protected int mRowIDColumn;
    protected ChangeObserver mChangeObserver;
    protected DataSetObserver mDataSetObserver;
    protected FilterQueryProvider mFilterQueryProvider;
    public static final int FLAG_AUTO_REQUERY = 0x01;
    public static final int FLAG_REGISTER_CONTENT_OBSERVER = 0x02;


    //Recommended
    public ForecastRVAdapter(Context context, Cursor c, int flags) {
        init(context, c, flags);
    }

    public ForecastRVAdapter(Context context, Cursor c) {
        init(context, c, FLAG_AUTO_REQUERY);
    }

    public ForecastRVAdapter(Context context, Cursor c, boolean autoRequery) {
        init(context, c, autoRequery ? FLAG_AUTO_REQUERY : FLAG_REGISTER_CONTENT_OBSERVER);
    }

    void init(Context context, Cursor c, int flags) {
        if ((flags & FLAG_AUTO_REQUERY) == FLAG_AUTO_REQUERY) {
            flags |= FLAG_REGISTER_CONTENT_OBSERVER;
            mAutoRequery = true;
        } else {
            mAutoRequery = false;
        }
        boolean cursorPresent = c != null;
        mCursor = c;
        mDataValid = cursorPresent;
        mContext = context;
        mRowIDColumn = cursorPresent ? c.getColumnIndexOrThrow("_id") : -1;
        if ((flags & FLAG_REGISTER_CONTENT_OBSERVER) == FLAG_REGISTER_CONTENT_OBSERVER) {
            mChangeObserver = new ChangeObserver();
            mDataSetObserver = new MyDataSetObserver();
        } else {
            mChangeObserver = null;
            mDataSetObserver = null;
        }

        if (cursorPresent) {
            if (mChangeObserver != null) c.registerContentObserver(mChangeObserver);
            if (mDataSetObserver != null) c.registerDataSetObserver(mDataSetObserver);
        }
    }

    public int getCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    public Cursor getItem(int position) {
        if (mDataValid && mCursor != null) {
            mCursor.moveToPosition(position);
            return mCursor;
        } else {
            return null;
        }
    }



    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null) {
            if (mCursor.moveToPosition(position)) {
                return mCursor.getLong(mRowIDColumn);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        if (oldCursor != null) {
            if (mChangeObserver != null) oldCursor.unregisterContentObserver(mChangeObserver);
            if (mDataSetObserver != null) oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (newCursor != null) {
            if (mChangeObserver != null) newCursor.registerContentObserver(mChangeObserver);
            if (mDataSetObserver != null) newCursor.registerDataSetObserver(mDataSetObserver);
            mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            mRowIDColumn = -1;
            mDataValid = false;
            // notify the observers about the lack of a data set
            notifyDataSetInvalidated();
        }
        return oldCursor;
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public CharSequence convertToString(Cursor cursor) {
        return cursor == null ? "" : cursor.toString();
    }

    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (mFilterQueryProvider != null) {
            return mFilterQueryProvider.runQuery(constraint);
        }
        return mCursor;
    }


    public FilterQueryProvider getFilterQueryProvider() {
        return mFilterQueryProvider;
    }

    public void setFilterQueryProvider(FilterQueryProvider filterQueryProvider) {
        mFilterQueryProvider = filterQueryProvider;
    }

    protected void onContentChanged() {
        if (mAutoRequery && mCursor != null && !mCursor.isClosed()) {
            mDataValid = mCursor.requery();
        }
    }

    private class ChangeObserver extends ContentObserver {
        public ChangeObserver() {
            super(new Handler());
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            onContentChanged();
        }
    }

    private class MyDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            mDataValid = false;
            notifyDataSetInvalidated();
        }
    }


    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.weather_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Cursor cursor = getItem(position);

        holder.dayStamp.setText(cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_DATESTAMP)));
        holder.dayTemp.setText(cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_DAYTEMPERATURE)));
        holder.maxTemp.setText(cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_MAXTEMPERATURE)));
        holder.minTemp.setText(cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_MINTEMPERATURE)));
        holder.humidity.setText(cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_HUMIDITY)));
        holder.windSpeed.setText(cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_WINDSPEED)));
        holder.description.setText(cursor.getString(cursor.getColumnIndex(WeatherEntry.COLUMN_DESCRIPTION)));
    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView dayStamp;
        public final TextView dayTemp;
        public final TextView maxTemp;
        public final TextView minTemp;
        public final TextView humidity;
        public final TextView windSpeed;
        public final TextView description;

        public ViewHolder(View itemView) {
            super(itemView);

            dayStamp = (TextView) itemView.findViewById(R.id.week_date_stamp);
            dayTemp = (TextView) itemView.findViewById(R.id.week_day_temp);
            maxTemp = (TextView) itemView.findViewById(R.id.week_max_temp);
            minTemp = (TextView) itemView.findViewById(R.id.week_min_temp);
            humidity = (TextView) itemView.findViewById(R.id.week_humidity);
            windSpeed = (TextView) itemView.findViewById(R.id.week_wind_speed);
            description = (TextView) itemView.findViewById(R.id.week_description);
        }
    }
}
