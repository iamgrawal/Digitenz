package com.example.ramesh.digitenz;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.multidots.fingerprintauth.AuthErrorCodes;
import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;
import com.multidots.fingerprintauth.FingerPrintUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.ramesh.digitenz.Constants.requestUrl;

public class TeacherActivity extends AppCompatActivity implements FingerPrintAuthCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Button btnMarkAttendance;
    FingerPrintAuthHelper mFingerPrintAuthHelper;
    String macAddr;
    protected Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    String address = "";
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        buildGoogleApiClient();

        time = new SimpleDateFormat(Constants.DateTimeFormat).format(new Date());

        mFingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(this, this);

        startService(new Intent(this, SettingAlarmService.class));
        btnMarkAttendance = findViewById(R.id.btn_mark_attendance);

        btnMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Constants.isWifiEnabled(TeacherActivity.this)) {
                    Toast.makeText(TeacherActivity.this, "Please turn on your wifi", Toast.LENGTH_SHORT).show();
                } else {
                    mFingerPrintAuthHelper.startAuth();
                    Toast.makeText(TeacherActivity.this, "Place your finger to complete authentication", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFingerPrintAuthHelper.stopAuth();
    }

    @Override

    public void onNoFingerPrintHardwareFound() {

        //Device does not have finger print scanner.

    }


    @Override

    public void onNoFingerPrintRegistered() {

        //There are no finger prints registered on this device.
        Toast.makeText(this, "Register your finger print first", Toast.LENGTH_SHORT).show();
        FingerPrintUtils.openSecuritySettings(TeacherActivity.this);
    }


    @Override

    public void onBelowMarshmallow() {

        //Device running below API 23 version of android that does not support finger print authentication.

    }


    @Override

    public void onAuthSuccess(FingerprintManager.CryptoObject cryptoObject) {

        //Authentication sucessful.
        Toast.makeText(this, "Authenticated", Toast.LENGTH_SHORT).show();
        authenticateWithMacAddress();

    }


    @Override

    public void onAuthFailed(int errorCode, String errorMessage) {

        switch (errorCode) {    //Parse the error code for recoverable/non recoverable error.

            case AuthErrorCodes.CANNOT_RECOGNIZE_ERROR:

                //Cannot recognize the fingerprint scanned.
                Toast.makeText(this, "Cannot authenticate", Toast.LENGTH_SHORT).show();
                break;

            case AuthErrorCodes.NON_RECOVERABLE_ERROR:

                //This is not recoverable error. Try other options for user authentication. like pin, password.

                break;

            case AuthErrorCodes.RECOVERABLE_ERROR:

                //Any recoverable error. Display message to the user.

                break;

        }

    }

    private void authenticateWithMacAddress() {
        macAddr = Constants.getWifiMacAddress(TeacherActivity.this);
        accessLocation();
        //new ServerTask().execute();
    }

    String serverRequest() throws UnsupportedEncodingException {

        String text = "";
        String urlData = requestUrl;          //API
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

            String urlParameters = "macaddr=" + macAddr;
            Log.d("wifi", "serverRequest: " + urlParameters);

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
                    Log.d("Response: ", text);
                }
            }
        } catch (Exception ex) {
            Log.d("ServerRequest Error", "error occurred" + ex);
        } finally {
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
            if (s.equals("true")) {
                accessLocation();
            } else {
                Toast.makeText(TeacherActivity.this, "Your device is not registered in the app", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void accessLocation() {
        if (address.contains("Maharishi Rd")) {
            Toast.makeText(this, "Attendance Marked", Toast.LENGTH_SHORT).show();
            new MarkAttendance().execute();
        } else {
            Toast.makeText(this, "Seems Like you are not in the college", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public boolean isGpsEnabled() {
        LocationManager locationManager;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return GpsStatus;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(TeacherActivity.this, "Sorry", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(TeacherActivity.this, "Sorry", Toast.LENGTH_LONG).show();

    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(TeacherActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            Log.d("addr", "getCompleteAddressString: " + addresses);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                Log.d("addr", "getCompleteAddressString: " + returnedAddress);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    Log.d("addr", "getCompleteAddressString: " + returnedAddress.getAddressLine(i));
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.d("addr", "" + strReturnedAddress.toString());
            } else {
                Log.d("addr", "no address");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("addr", "Cannot get Address!");
        }
        return strAdd;
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(TeacherActivity.this)

                .addConnectionCallbacks(this)

                .addOnConnectionFailedListener(this)

                .addApi(LocationServices.API)

                .build();

    }


    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        action_button();
    }

    private void action_button() {

        if (ActivityCompat.checkSelfPermission(TeacherActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TeacherActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                }, 100);
            }
            return;
        }

        if (isOnline() == true) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }


        if (mLastLocation != null) {
            address = getCompleteAddressString(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            Log.d("addr", "action_button: " + mLastLocation.getLatitude() + " " + address);
        } else {

            if (isGpsEnabled() == false) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
            } else if (isOnline() == false) {
                Toast.makeText(TeacherActivity.this, "Make sure you are connected to the internet", Toast.LENGTH_LONG).show();
            } else if (isOnline() == true) {
                action_button();
            }
        }
        }

    class MarkAttendance extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String response = "";
            try {
                response = serverRequestForMarkingAttendance();
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
    String serverRequestForMarkingAttendance() throws UnsupportedEncodingException {

        String text = "";
        String urlData = requestUrl;          //API
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

            String urlParameters = "macaddr=" + macAddr+"&time="+time+"&status=P";
            Log.d("wifi", "serverRequest: " + urlParameters);

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
                    Log.d("Response: ", text);
                }
            }
        } catch (Exception ex) {
            Log.d("ServerRequest Error", "error occurred" + ex);
        } finally {
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
}