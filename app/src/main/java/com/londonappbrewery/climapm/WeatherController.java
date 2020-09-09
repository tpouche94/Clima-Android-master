package com.londonappbrewery.climapm;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class WeatherController extends AppCompatActivity {

    // Constants:
    final int REQUEST_CODE = 123;
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    // App ID to use OpenWeather data
    final String APP_ID = "303d3ad538a9ab22d2a36c4f0c74d478";
    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;

    // TODO: Set LOCATION_PROVIDER here:

    //since we are using an emulator, we need to use GPS_PROVIDER here
    // for physical device we would use NETWORK_PROVIDER
    String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;


    // Member Variables:
    TextView mCityLabel;
    ImageView mWeatherImage;
    TextView mTemperatureLabel;

    // TODO: Declare a LocationManager and a LocationListener here:
    LocationManager mLocationManager;
    LocationListener mLocationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);

        // Linking the elements in the layout to Java code
        mCityLabel = (TextView) findViewById(R.id.locationTV);
        mWeatherImage = (ImageView) findViewById(R.id.weatherSymbolIV);
        mTemperatureLabel = (TextView) findViewById(R.id.tempTV);
        ImageButton changeCityButton = (ImageButton) findViewById(R.id.changeCityButton);


        // TODO: Add an OnClickListener to the changeCityButton here:

    }


    // TODO: Add onResume() here:
    @Override
    protected void onResume() {

        super.onResume();
        Log.d("Clima", "onResume() called");
        Log.d("Clima", "Getting weather for current location");
        getWeatherForCurrentLocation();
    }


    // TODO: Add getWeatherForNewCity(String city) here:


    // TODO: Add getWeatherForCurrentLocation() here:
    private void getWeatherForCurrentLocation() {
        //gets hold of a location manager and
        // assigns it to our mLocationManager instance variable
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //check for update device location
        mLocationListener = new LocationListener() {
            @Override
            //will trigger when location of device changes
            public void onLocationChanged(Location location) {
                Log.d("Clima", "onLocationChanged() callback received");
                String longitude = String.valueOf(location.getLongitude());

                String latitude = String.valueOf(location.getLatitude());

                Log.d("Clima", "Longitude is:" + longitude);
                Log.d("Clima", "Latitude is:" + latitude);

                //parameters for openweatherapp
                RequestParams params = new RequestParams();
                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("appid", APP_ID);

                letsDoSomeNetworking(params);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.d("Clima", "onStatusChanged callback received");
            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            //will get trigger when gps/network is disabled
            public void onProviderDisabled(String s) {
                Log.d("Clima", "onProviderDisabled() callback received");


            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            //request permission from user
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

            return;
        }
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);
        }
    // run this block of code if the user grant location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //check if request on the call back matches
        // the request code we supplied when we made the request
        if(requestCode==REQUEST_CODE){

            //check user response, if user granted the app access
            //only if those 2 conditions meet we can access user's location
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
            Log.d("Clima", "onRequestPermissionResult() Permission granted!");
            //if user grant permission, then call method to get user location
            getWeatherForCurrentLocation();

            //if either of the conditions above doesn't meet, then permission deny



        }
        else {
            Log.d("Clima", "Permission denied");
        }
    }
    // TODO: Add letsDoSomeNetworking(RequestParams params) here:

    private void letsDoSomeNetworking(RequestParams params)
    {   //allow app to stay responsive while fetching data
        //from openweathermap
        AsyncHttpClient client = new AsyncHttpClient();

        //the JsonHttpResponseHandler() will receive two messages
        //on success or on failure, depending if the get request is
        //successful or not
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("Clima", "Fail " + throwable.toString());
            }
        });
    }



    // TODO: Add updateUI() here:



    // TODO: Add onPause() here:



}
