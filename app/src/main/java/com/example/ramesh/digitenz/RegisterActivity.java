package com.example.ramesh.digitenz;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static com.example.ramesh.digitenz.Constants.requestUrl;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etPassword, etUserName;
    Spinner etDesignation;
    Button btnSubmit;
    String name, userName, password, designation, macAddress;
    String[] spinnerArray = {"Teacher", "Admin", "Student"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.et_name);
        etDesignation = findViewById(R.id.et_designation);
        etUserName = findViewById(R.id.et_user_name);
        etPassword = findViewById(R.id.et_password);

        btnSubmit = findViewById(R.id.btn_submit_on_register);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        spinnerArray); //selected item will look like a spinner set from XML
        adapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        etDesignation.setAdapter(adapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitInfo();
            }
        });
    }

    private void submitInfo() {
        if (etName.getText().toString().isEmpty() ||
                etUserName.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please complete the form", Toast.LENGTH_SHORT).show();
        } else {
            name = etName.getText().toString().trim();
            designation = etDesignation.getSelectedItem().toString();
            userName = etUserName.getText().toString().trim();
            password = etPassword.getText().toString().trim();

            if (!Constants.isWifiEnabled(RegisterActivity.this)) {
                Toast.makeText(RegisterActivity.this, "Please turn on your wifi to proceed with the registration", Toast.LENGTH_SHORT).show();
            } else {
                macAddress = Constants.getWifiMacAddress(RegisterActivity.this);
                new ServerTask().execute();
            }
        }
    }

    String serverRequest() throws UnsupportedEncodingException {

        String text = "";
        String urlData = requestUrl;
        HttpURLConnection conn = null;
        InputStream stream = null;
        // Send data
        try {
            // Defined URL  where to send data
            URL url = new URL(urlData);
            // Send GET data request
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            String urlParameters = "name="+name+"&unm="+userName+"&pwd="+password+"&dn="+designation+"&macaddr="+macAddress;
            Log.d("wifi", "serverRequest: "+urlParameters);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();


            conn.connect();
            if (conn.getResponseCode() == 200) {
                stream = conn.getInputStream();
                Log.d("Get Request", "inside try -if");
                if (stream != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder builder = new StringBuilder();
                    Log.d("click", "inside tr4y");
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        builder.append(line + "\n");
                        Log.d("line", line);
                    }
                    text = builder.toString();
                    Log.d("Response: ",text);
                }
            }
        }
        catch (Exception ex) {
            Log.d("ServerRequest Error", "error occurred" + ex);
        }
        finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return text;
    }

    class ServerTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String response = "";
            try {

                response = serverRequest();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.e("ServerTask Error", "Exception Occurred");
            }
            return response;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("click", "inside on pre execute");
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("click", "inside on post execute" + "the resulted string is" + s);
        }

    }
}
