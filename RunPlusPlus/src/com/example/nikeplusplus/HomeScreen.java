package com.example.nikeplusplus;

import java.util.ArrayList;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

// -------------------------------------------------------------------------
/**
 * Home screen of the application
 *
 * @author Bishwamoy Sinha Roy
 * @version Oct 23, 2013
 */
public class HomeScreen
    extends FragmentActivity
    implements ScreenInterface
{
    private Button       cameraB;
    private Button       runB;
    private Button       recordB;
    private Button       registerB;
    private TextView     out;
    private EditText     name;

    public static String ip            = "172.31.221.125"; // initial ip
    public static String port          = "8080"; // initial port
    public static String currentPerson = ""; // person currently logged in


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        setupActionBar();

        cameraB = (Button)findViewById(R.id.camActivity);
        runB = (Button)findViewById(R.id.runActivity);
        recordB = (Button)findViewById(R.id.recordActivity);
        registerB = (Button)findViewById(R.id.registerPerson);
        out = (TextView)findViewById(R.id.outH);
        name = (EditText)findViewById(R.id.personName);
        String hint =
            "Wanna login or register? Enter "
                + "name and click Register/Change\n\n" + "Current IP: " + ip
                + "\n" + "Current port: " + port;
        out.setText(hint);
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        String hint =
            "Wanna login or register? Enter "
                + "name and click Register/Change\n" + "Current IP: " + ip
                + "\n" + "Current port: " + port + "\n" + "Logged In as: "
                + currentPerson;
        out.setText(hint);
    }


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
        getMenuInflater().inflate(R.menu.home_screen, menu);
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
         * Shows a dialog to the user to change the ip and port
         */
            case R.id.action_changeIPPort:
                //start dialogue
                DialogFragment newFragment = new IPPortDialog();
                newFragment.show(getSupportFragmentManager(), "dialog");
        }
        return super.onOptionsItemSelected(item);
    }


    // ----------------------------------------------------------
    /**
     * To start the camera activity
     *
     * @param view
     */
    public void startCameraActivity(View view)
    {
        Intent myIntent = new Intent(this, CameraScreen.class);
        this.startActivity(myIntent);
    }


    // ----------------------------------------------------------
    /**
     * To start the Run activity
     *
     * @param view
     */
    public void startRunActivity(View view)
    {
        Intent myIntent = new Intent(this, RunScreen.class);
        this.startActivity(myIntent);
    }


    // ----------------------------------------------------------
    /**
     * To start the record activity
     *
     * @param view
     */
    public void startRecordActivity(View view)
    {
        Intent myIntent = new Intent(this, RecordScreen.class);
        this.startActivity(myIntent);
    }


    // ----------------------------------------------------------
    /**
     * To change the currently logged in person
     *
     * @param view
     */
    public void regChange(View view)
    {
        HomeScreen.currentPerson = name.getText().toString();
        // initiate task to retrieve info
        tasks.RegisterTask task = new tasks.RegisterTask(this);
        task.execute(HomeScreen.ip, HomeScreen.port, name.getText().toString());
    }


    // ----------------------------------------------------------
    /**
     * Implement this function from the interface to display
     * onto the TextView
     * Can only be a single person and current registered members
     */
    public void display(ArrayList<String> in)
    {
        out.setText(in.get(0));
    }
}
