package com.example.ramesh.digitenz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.ramesh.digitenz.Constants.CLGWIFI;
import static com.example.ramesh.digitenz.Constants.MYSHAREDPREFERENCES;

public class MainActivity extends AppCompatActivity {

    EditText userName, password;
    Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btn_register);

        btnLogin = findViewById(R.id.btn_login);

        Log.d("wifi", "onCreate: "+getSharedPreferences(MYSHAREDPREFERENCES, MODE_PRIVATE).getString(CLGWIFI, ""));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userName.getText().toString().equals("superadmin") && password.getText().toString().equals("superadmin")){
                    startActivity(new Intent(MainActivity.this, SuperAdminActivity.class));
                } else if(userName.getText().toString().equals("teacher")){
                    startActivity(new Intent(MainActivity.this, TeacherActivity.class));
                }
                else {
                    Toast.makeText(MainActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

    }
}
