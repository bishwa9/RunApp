package com.example.nikeplusplus;

import android.widget.ProgressBar;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import java.util.ArrayList;
import tasks.ModifyTask;
import android.widget.Button;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.content.Context;
import android.location.LocationManager;
import android.widget.ToggleButton;
import android.view.View;
import android.view.MenuItem;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

// -------------------------------------------------------------------------
/**
 *  Screen that is displayed when the player is running
 *
 *  @author Bishwamoy Sinha Roy
 *  @version Oct 24, 2013
 */
public class RunScreen
    extends Activity
    implements ScreenInterface
{

    private long         timeElapsed = 0;    // time person was running
    private long         timeBegan   = 0;
    private double       distanceRan = 0.0;  // total distance covered
    private boolean      isPaused    = false;
    private boolean      first       = true;
    private boolean      done        = false;
    // location at previous location update
    private Location     prevLoc     = null;
    private ToggleButton runPauseB;
    private Button       doneB;
    private ProgressBar  lightMeter;
    private TextView     lightUpdate;
    private TextView     output;
    private boolean start = true;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        setupActionBar();

        runPauseB = (ToggleButton)findViewById(R.id.pause);
        doneB = (Button)findViewById(R.id.doneB);

        // at first the toggle button is set off (running)
        runPauseB.setChecked(false);

        lightMeter = (ProgressBar)findViewById(R.id.lightmeter);

        output = (TextView)findViewById(R.id.update);
        lightUpdate = (TextView)findViewById(R.id.lightUpdate);


        // -------------------------------------------------------------------
        // GPS
        // From Android Website
        // Acquire a reference to the system Location Manager
        LocationManager locationManager =
            (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location)
            {
                // Called when a new location is found by the network location
                // provider.
                makeUseOfnewLocation(location);
            }


            public void onStatusChanged(
                String provider,
                int status,
                Bundle extras)
            {
            }


            public void onProviderEnabled(String provider)
            {
            }


            public void onProviderDisabled(String provider)
            {
            }
        };

        // Register the listener with the Location Manager to receive location
        // updates
        // minimum time 3 seconds, minimum distance : 2 meters
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0,
            0,
            locationListener);

        // ---------------------------------------------------------------------
        // Light Sensor
        SensorManager sensorManager =
            (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null)
        {
            Toast.makeText(this, "No Light Sensor! quit-", Toast.LENGTH_LONG)
                .show();
        }
        else
        {
            float max = lightSensor.getMaximumRange();
            lightMeter.setMax((int)max); // set range for progress bar
            Toast.makeText(this, "Light Sensor present!", Toast.LENGTH_LONG)
            .show();
            sensorManager.registerListener(
                lightSensorEventListener,
                lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        }
    }


    // ----------------------------------------------------------
    /**
     * called when new location is available
     * @param loc : new location
     */
    public void makeUseOfnewLocation(Location loc)
    {
        // update time and position only when app isn't paused
        if (!isPaused && !done)
        {
            if (first)
            {
                prevLoc = loc;
                timeBegan = System.nanoTime();
                timeElapsed = 0;
                distanceRan = 0.0;
                first = false;
            }
            else
            {
                distanceRan += loc.distanceTo(prevLoc); // update distance
                timeElapsed = System.nanoTime() - timeBegan; // update
                // time
                prevLoc = loc; // update loc
            }

            // update UI with new info
            output.setText("");
            output.append("Accuracy: " + loc.getAccuracy() + "\n\n");
            output.append("distance : " // round to two decimal places
                + (double)Math.round((distanceRan) * 100) / 100 + "\n\n");
            output // conver to seconds
                .append("time elapsed : " + timeElapsed / 1000000000 + "\n\n");
        }
    }

    /**
     * listens to new light sensor updates
     */
    SensorEventListener lightSensorEventListener
    = new SensorEventListener()
    {
        public void onAccuracyChanged(
            Sensor sensor,
            int accuracy)
        {
        }


        public void onSensorChanged(
            SensorEvent event)
        {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT)
            { // get new luminosity
                float currentReading =
                    event.values[0];
                lightMeter // update meter
                    .setProgress((int)currentReading);
                if( currentReading < SensorManager.LIGHT_SHADE )
                {
                    lightUpdate.setText("Its getting dark!!!");
                }
                else
                {
                    lightUpdate.setText("Its bright!!!");
                }
            }
        }
    };

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.run, menu);
        return true;
    }


    @Override
    /*
     * Called when a button is clicked on the action bar
     */
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        /*
         * Kills this activity and goes back to previous thread
         */
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // ----------------------------------------------------------
    /**
     * Called when the toggle button is pressed
     *
     * @param view
     *            : clicked button
     */
    public void runPause(View view)
    {
        if (runPauseB.isChecked())
        {
            this.isPaused = true;
        }
        else
        {
            this.isPaused = false;
        }
    }


    // ----------------------------------------------------------
    /**
     * When done button is clicked
     * run statistics is sent to the server
     *
     * @param view
     */
    public void doneRun(View view)
    {
        // task to modify the runner with the newly run statistics
        tasks.ModifyTask task = new ModifyTask(this);
        task.execute(
            HomeScreen.ip,
            HomeScreen.port,
            HomeScreen.currentPerson,
            String.valueOf(this.timeElapsed / 1000000000), //seconds
            // round to two decimal places
            String.valueOf((double)Math.round((this.distanceRan) * 100) / 100),
            "", // not adding a picture
            "",
            "");
        // reset
        this.distanceRan = 0.0;
        this.done = false;
        this.first = true;
        this.prevLoc = null;
        this.timeBegan = 0;
        this.timeElapsed = 0;
        this.done = true;
    }

    // overriden to display of textView
    public void display(ArrayList<String> in)
    {
        output.setText(in.get(0));
        output.append("\n\n\n"
            + "Go back to the main screen to being another Run!!!");
    }

}
