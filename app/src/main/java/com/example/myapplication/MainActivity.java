package com.example.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;

import android.content.ContentValues;
import android.content.CursorLoader;

import android.database.Cursor;

import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;

import static com.example.myapplication.StudentsProvider.STUDENTS_TABLE_NAME;

public class MainActivity extends Activity {
    Button addStudentButton;
    Button deleteStudentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addStudentButton = (Button) findViewById(R.id.button2);

        addStudentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Add a new student record
                ContentValues values = new ContentValues();
                values.put(StudentsProvider.NAME,
                        ((EditText)findViewById(R.id.editText2)).getText().toString());

                values.put(StudentsProvider.GRADE,
                        ((EditText)findViewById(R.id.editText3)).getText().toString());

                Uri uri = getContentResolver().insert(
                        StudentsProvider.CONTENT_URI, values);

                Toast.makeText(getBaseContext(),
                        uri.toString(), Toast.LENGTH_LONG).show();
            }
        });

        deleteStudentButton = findViewById(R.id.button3);

        deleteStudentButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view){
                CharSequence text = "Deleted student ";
                String studentName = ((EditText)findViewById(R.id.editText2)).getText().toString();
                Context context = getApplicationContext();
                Uri nameUri = Uri.parse(StudentsProvider.URL + "/name");
                String[] whereArgs = new String[] {studentName};
                getContentResolver().delete(nameUri,null, whereArgs);
                Toast.makeText(context, text+studentName,Toast.LENGTH_LONG).show();
            }
        });


    }

    public void onClickRetrieveStudents(View view) {
        // Retrieve student records
        String URL = "content://com.example.myapplication.StudentsProvider";
        Uri students = Uri.parse(URL);
        Cursor c = getContentResolver().query(students, null, null, null, "name");

        if (c.moveToFirst()) {
            do{
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(StudentsProvider._ID)) +
                                ": \nName " +  c.getString(c.getColumnIndex(StudentsProvider.NAME)) +
                                "\nGrade " + c.getString(c.getColumnIndex( StudentsProvider.GRADE)),
                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }
    }

}