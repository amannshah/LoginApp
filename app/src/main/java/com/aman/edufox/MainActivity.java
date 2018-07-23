package com.aman.edufox;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText usernameET,passwordET;
    TextView signUpModeTextView;
    Button signUpButton;
    SQLiteDatabase db;
    boolean signUpModeActive = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameET = findViewById(R.id.userName);
        passwordET = findViewById(R.id.passWord);
        signUpModeTextView = findViewById(R.id.signUpModeTextView);
        signUpButton = findViewById(R.id.SignUpButton);

        signUpModeTextView.setOnClickListener(this);
        db=openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Student(username varchar,password varchar)");
    }

    public void onClick(View view) {

        if (view.getId() == R.id.signUpModeTextView) {

            if (signUpModeActive) {

                signUpModeActive = false;
                signUpButton.setText("Login");
                signUpModeTextView.setText("Or Sign Up");

            } else {

                signUpModeActive = true;
                signUpButton.setText("Sign up");
                signUpModeTextView.setText("Or Login");

            }
        }
    }

    public void SignUp(View view) {
        if (usernameET.getText().toString().matches("") || passwordET.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(),"A username and a password required",Toast.LENGTH_LONG).show();
        } else {
            if (signUpModeActive) {
                String un = usernameET.getText().toString().trim();
                String pw = passwordET.getText().toString().trim();
                String query = "INSERT INTO Student(username,password) VALUES(?,?)";
                SQLiteStatement statement = db.compileStatement(query);
                statement.bindString(1,un);
                statement.bindString(2,pw);
                statement.execute();
                Toast.makeText(getApplicationContext(),"You have been successfully registered",Toast.LENGTH_LONG).show();
            } else {
                Log.i("Here","The prob is not re");
                String un = usernameET.getText().toString().trim();
                String pw = passwordET.getText().toString().trim();
                Log.i("Here",un);
                Log.i("Here",pw);
                String query = "SELECT * FROM Student WHERE username = '"+ un + "' AND password = '" + pw + "'";
                Log.i("Print",query);
                Cursor cr = db.rawQuery(query,null);
                Log.i("Here","The prob is not here");
                if (cr != null) {
                    if (cr.moveToFirst()) {
                        Intent intent = new Intent(MainActivity.this, FinalActivity.class);
                        intent.putExtra("Username", un);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),"INVALID USERNAME/PASSWORD",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"INVALID USERNAME/PASSWORD",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
