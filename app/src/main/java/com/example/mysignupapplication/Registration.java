package com.example.mysignupapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.nio.Buffer;

public class Registration extends AppCompatActivity {
    EditText name,email,phone;
    Button save,view,delete,update;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        name=findViewById(R.id.etName);
        email=findViewById(R.id.etEmail);
        phone=findViewById(R.id.etPhone);
        save=findViewById(R.id.btnSave);
        view=findViewById(R.id.btnView);
        delete=findViewById(R.id.btnDelete);
        update=findViewById(R.id.btnUpdate);

        //create database
        db=openOrCreateDatabase("Emobilis_Trainings",MODE_PRIVATE,null);
        //CREATE TABLE
        db.execSQL("CREATE TABLE IF NOT EXISTS students(Name VARCHAR, Email VARCHAR, Phone VARCHAR)");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Uname, Uemail, Uphone;
                Uname = name.getText().toString().trim();
                Uemail = email.getText().toString().trim();
                Uphone = phone.getText().toString().trim();

                if(Uname.isEmpty()){
                    name.setError("Please enter ur name");
                }
                else if(Uemail.isEmpty()){
                    email.setError("Please enter ur email");
                }
                else if(Uphone.isEmpty()){
                    phone.setError("Please enter ur phone");
                }
                else{
                //proceed to save the data
                    db.execSQL("INSERT INTO students VALUES('"+Uname+"','"+Uemail+"','"+Uphone+"')");
                    messages("SUCCESS!!!","Record saved successfully");
                }

            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FETCHING DTA FROM DB USING CURSOR
                Cursor cursor=db.rawQuery("SELECT * FROM students",null);
                if(cursor.getCount()==0){
                    messages("EMPTY TABLE","Sorry we founf no that data in this table");
                }
                else{
                    //write the loop to display the record one by one use Stringbuffer to attend the record
                    StringBuffer buffer = new StringBuffer();
                    while(cursor.moveToNext()){
                        buffer.append(cursor.getString(0));
                        buffer.append("\n");
                        buffer.append(cursor.getString(1));
                        buffer.append("\n");
                        buffer.append(cursor.getString(2));
                        buffer.append("\n");
                        buffer.append("\n");

                    }
                    messages("CURRENTS RECORDS",buffer.toString());
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Uphone;
                Uphone = phone.getText().toString().trim();
                if(Uphone.isEmpty()){
                    phone.setError("Please enter phone");
                }
                else{
                    Cursor cursor=db.rawQuery("SELECT * FROM students WHERE Phone='"+Uphone+"'",null);
                    if(cursor.getCount()==0){
                        messages("EMPTY DATA","Sorry we couldn't find that phone number");
                    }
                    else{

                        AlertDialog.Builder builder= new AlertDialog.Builder(Registration.this);
                        builder.setTitle("DELETING");
                        builder.setMessage("Are you sure u want to delete this person");
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                phone.setText("");
                            }
                        });
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.execSQL("DELETE FROM students WHERE Phone='"+Uphone+"'");
                                messages("SUCCESS!!!","Record Deleted successfully");
                            }
                        });
                        builder.create().show();

                        }
                }
            }
        });
    }
    public void messages(String title,String body){
        AlertDialog.Builder builder= new AlertDialog.Builder(Registration.this);
        builder.setTitle(title);
        builder.setMessage(body);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name.setText("");
                email.setText("");
                phone.setText("");
            }
        });
        builder.create().show();
    }
}