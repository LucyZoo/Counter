package com.example.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainCounterActivity extends Activity {
    Button EditFundsButton;
    Button addCategory;
    boolean signInSuccess = false;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SignIn getUserSignIn = new SignIn();
        //signInSuccess = getUserSignIn.UserSignIn(); // deal with sign in status

        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_main);
        EditFundsButton = findViewById(R.id.EditFundsButton);
        addCategory = findViewById(R.id.addCategory);



        Uri uriTest = Uri.parse(CounterProvider.URL + "/" + "TESTTABLE/" +"fetch");

        Cursor cursor = getContentResolver().query(uriTest, new String[]{"_id","Category","FundsAvailable"}, null, null, null);
        FundsCursorAdapter adapter = new FundsCursorAdapter(
                this, R.layout.counter_main, cursor, 0 );

        //findViewById(R.id.CategoryFundsLayout).setListAdapter(adapter);
//        listView = findViewById(R.id.CategoryFundsLayout);
        //adapter.bindView(findViewById(R.id.CategoryFundsLayout),this,cursor);


        EditFundsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                String categoryFundToChange = ((EditText)findViewById(R.id.addNewCategoryInput)).getText().toString();
                String newFundValue = ((EditText)findViewById(R.id.EditFunds)).getText().toString();
                values.put(CounterProvider.FUNDS, newFundValue);
                Uri uriTest = Uri.parse(CounterProvider.URL + "/" + "TESTTABLE/" +"fetch");
                int updateResult = getContentResolver().update(uriTest, values, null, null);

            }
        });

        addCategory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                String categoryInputValue = ((EditText)findViewById(R.id.addNewCategoryInput)).getText().toString();
                values.put(CounterProvider.CATEGORY, categoryInputValue);

                Uri CONTENT_URI = Uri.parse(CounterProvider.URL + "/" +categoryInputValue );

                Uri uri  = getContentResolver().insert(CONTENT_URI, values);

            }
        });


    }
}
