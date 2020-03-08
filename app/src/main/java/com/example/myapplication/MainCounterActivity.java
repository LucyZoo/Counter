package com.example.myapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainCounterActivity extends Activity {
    Button viewFunds;
    Button addCategory;
    boolean signInSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SignIn getUserSignIn = new SignIn();
        //signInSuccess = getUserSignIn.UserSignIn(); // deal with sign in status

        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_main);
        viewFunds = findViewById(R.id.fundsViewButton);
        addCategory = findViewById(R.id.addCategory);

        viewFunds.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String[] fundsProjection = new String[] {CounterProvider.FUNDS};
                String[] selectionArgs = new String[] {"TESTTABLE"};
                // Add a new student record
                Uri uriTest = Uri.parse(CounterProvider.URL + "/" + "TESTTABLE/" +"fetch");
                Cursor c = getContentResolver().query(uriTest, new String[]{"FundsAvailable"}, null, null, null);

                if (c.moveToFirst())
                    System.out.println(c.getString(c.getColumnIndex("FundsAvailable"))); // test funds values

                c.close();


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
