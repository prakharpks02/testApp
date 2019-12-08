package com.example.waterapp.mylocation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    Context context = this;
    Button startTrackButton;                                   // To be renamed to start_button
    Button end_button;
    TextView user_location;
    Button switch_button;

    private ViewFlipper viewFlipper ;
    private ViewFlipper viewFlipper2 ;

    private volatile boolean stopTracking = false;

    private FusedLocationProviderClient mFusedLocationClient;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_location = findViewById(R.id.location_textView);
        startTrackButton = findViewById(R.id.start_button);
        end_button = findViewById(R.id.end_button);
        switch_button = findViewById(R.id.switch_button);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        viewFlipper = findViewById(R.id.view_flipper);
        viewFlipper2 = findViewById(R.id.view_flipper_2);
        viewFlipper2.setFlipInterval(2000);
        viewFlipper2.startFlipping();

        /*
        startTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fetchLocation();

            }
        });
        */

    }

    public void fetchLocation() {

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Permission Denied", LENGTH_SHORT).show();

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to acess this feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();

                Toast.makeText(this, "Request the dialog box", LENGTH_SHORT).show();


            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                Toast.makeText(this, "Request the permission ", LENGTH_SHORT).show();
            }
        } else {
            // Permission has already been granted
            //Toast.makeText(this, "Permission Granted", LENGTH_SHORT).show();
            user_location.setText("Hooray");
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                Double latitude = location.getLatitude();
                                Double longitude = location.getLongitude();

                                user_location.setText("Latitude = "+latitude + "\nLongitude = " + longitude);

                            } else {
                                user_location.setText("Coordinates are null");
                            }

                        }
                    });

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //abc
            } else {

            }
        }
    }

    class ExampleRunnable implements Runnable {
        int seconds ;

        ExampleRunnable(int seconds) {
            this.seconds = seconds ;
        }

        @Override
        public void run() {
            for (int i = 0; i < seconds; i++ ) {
                if (stopTracking)
                    return;
                /*
                Instead of buttonStartThread.post(), we can also use runOnUiThread() [Both takes Runnable as argument]
                 */
                fetchLocation();
                Log.d(TAG, "startThread: "+ i);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public void startLocationTrack(View view) {
        stopTracking = false;
        Toast.makeText(this, "Started Tracking", LENGTH_SHORT).show();
        /*
        ExampleRunnable runnable = new ExampleRunnable(10);
        new Thread(runnable).start();
        */
        fetchLocation();

    }

    public void stopLocationTracking(View view) {
        stopTracking = true;
        Toast.makeText(this, "Stopped Tracking", LENGTH_SHORT).show();
        user_location.setText("Tracking Finished");

    }

    public void nextView(View v){
        viewFlipper.showNext();
    }

    public void previousView(View v){
        viewFlipper.showPrevious();
    }

    public void imageForward(View v){
        viewFlipper2.showNext();
    }

    public void imageBackward(View v){
        viewFlipper2.showPrevious();
    }


}
