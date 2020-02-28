package com.example.myapplication;

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
    }

    public void onClickRetrieveStudents(View view) {
        // Retrieve student records
        String URL = "content://com.example.myapplication.StudentsProvider";

        Uri students = Uri.parse(URL);
        Cursor c = managedQuery(students, null, null, null, "name");

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
    public void onClickDeleteStudent(View view){
        String URL = "content://com.example.myapplication.StudentsProvider";

        Uri students = Uri.parse(URL);

        StudentsProvider student = new StudentsProvider();
        String[] whereArgs = new String[] { ((EditText)findViewById(R.id.editText2)).getText().toString()};
        student.delete(students,StudentsProvider.NAME,whereArgs);
    }
}