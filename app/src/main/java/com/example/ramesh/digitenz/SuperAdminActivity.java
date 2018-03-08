package com.example.ramesh.digitenz;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.ramesh.digitenz.Constants.CLGWIFI;
import static com.example.ramesh.digitenz.Constants.MYSHAREDPREFERENCES;

public class SuperAdminActivity extends AppCompatActivity {

    Button btnSetWifi;
    EditText wifiName;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin);

        sharedPreferences = getSharedPreferences(MYSHAREDPREFERENCES, MODE_PRIVATE);
        wifiName = findViewById(R.id.clg_wifi);
        btnSetWifi = findViewById(R.id.btn_set_wifi);

        btnSetWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!wifiName.getText().toString().isEmpty()){
                    editor = sharedPreferences.edit();
                    editor.putString(CLGWIFI, wifiName.getText().toString());
                    editor.apply();
                    Toast.makeText(SuperAdminActivity.this, "Wifi Name is Set", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SuperAdminActivity.this, "Empty fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
