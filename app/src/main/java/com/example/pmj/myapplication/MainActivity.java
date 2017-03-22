package com.example.pmj.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    public Button btMap, btLo;
    public LocationManager mLocationMan;
    public MyLocationListener myLocationLx;
    protected Geocoder geocoder;
    protected TextToSpeech tts;
    protected Button a;

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.KOREAN);
            tts.setPitch(1.0f);
            tts.setSpeechRate(1.5f);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btMap = (Button) findViewById(R.id.button2);
        btMap.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:36.321609,127.337957?z=20"));
                startActivity(mIntent);

            }

        });

        btLo = (Button) findViewById(R.id.button);
        btLo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double altitude = myLocationLx.altitude;
                double latitude = myLocationLx.latitude;
                double longitude = myLocationLx.longitude;

                List<Address> lsAddress;
              try{
                lsAddress = geocoder.getFromLocation(latitude, longitude, 1);
                    String address = lsAddress.get(0).getAddressLine(0);
                    String city = lsAddress.get(0).getLocality();
                    Toast.makeText(getApplicationContext(),address, Toast.LENGTH_SHORT).show();
                    tts.speak(address,TextToSpeech.QUEUE_FLUSH,null,null);
                } catch (IOException e) {
                }
            }


        });

        mLocationMan = (LocationManager) getSystemService(LOCATION_SERVICE);
        myLocationLx = new MyLocationListener();
        long minTime = 1000;   // minimum time interval between location updates, in milliseconds
        float minDistance = 0;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        mLocationMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, myLocationLx);
        mLocationMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, myLocationLx);

        geocoder = new Geocoder(this,Locale.KOREAN);
        tts = new TextToSpeech(this, this);
    }


}
