package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class FundsCursorAdapter extends ResourceCursorAdapter {

    public FundsCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
        super(context, layout, cursor, flags);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView name = (TextView) view.findViewById(R.id.Category);
        name.setText(cursor.getString(cursor.getColumnIndex(CounterProvider.CATEGORY)));

        TextView phone = (TextView) view.findViewById(R.id.CategoryFunds);
        phone.setText(cursor.getString(cursor.getColumnIndex(CounterProvider.FUNDS)));
    }
}