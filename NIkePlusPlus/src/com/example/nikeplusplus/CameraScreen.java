package com.example.nikeplusplus;

import android.location.Location;
import com.google.android.gms.location.LocationClient;
import android.widget.Toast;
import android.content.IntentSender;
import com.google.android.gms.common.GooglePlayServicesClient;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import java.util.ArrayList;
import tasks.ModifyTask;
import android.net.Uri;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Environment;
import java.io.File;
import android.widget.Button;
import android.widget.EditText;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

// -------------------------------------------------------------------------
/**
 * Camera activity of the application
 *
 * @author Bishwamoy Sinha Roy
 * @version Oct 21, 2013
 */
public class CameraScreen
    extends FragmentActivity
    implements ScreenInterface, GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener
{
    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private EditText         textInput;
    private TextView         textOut;
    private Button           donePicture;
    private Uri              fileURI                               = null;
    private String           PictureDescription                    = "";
    private String           owner                                 = "";
    private String           LocationTaken                         = "";
    private boolean          sendToServer                          = false;
    private boolean          hasGoogleAPK                          = false;
    private LocationClient   LocClient                             = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_screen);

        donePicture = (Button)findViewById(R.id.saveP);
        textInput = (EditText)findViewById(R.id.input);
        textOut = (TextView)findViewById(R.id.outC);

        hasGoogleAPK = this.servicesConnected();

        String hint = "Tap Camera on Action Bar to take Picture first!";
        if (hasGoogleAPK)
        { // notify the user if the device can save locations
            hint +=
                "\nYou have the Google Service you can save "
                    + "the geographical location of taken pictures!\n";
        }

        /*
         * Create a new location client, using the enclosing class to handle
         * callbacks.
         */
        LocClient = new LocationClient(this, this, this);

        textOut.setText(hint);

        setupActionBar();
    }


    /*
     * Called when the Activity becomes visible. Override to only start the
     * location client when needed
     */
    @Override
    protected void onRestart()
    {
        super.onRestart();
        LocClient.connect();
    }


    /*
     * Called when the Activity is no longer visible. To save battery
     */
    @Override
    protected void onStop()
    {
        // Disconnecting the client invalidates it.
        LocClient.disconnect();
        super.onStop();
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
        getMenuInflater().inflate(R.menu.camera_screen, menu);
        return true;
    }


    // overriden from ScreenInterface
    // so that async tasks can display on the GUI
    public void display(ArrayList<String> in)
    {
        this.textOut.setText(in.get(0));
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
                /*
                 * calls a camera application which returns when picture was
                 * taken
                 */
            case R.id.action_takePicture:
                Intent cameraIntent =
                    new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // create a file to save the video
                fileURI = getOutputMediaFileUri();
                // set the image file name
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileURI);

                this.startActivityForResult(cameraIntent, 0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // -------------------------------------------------------------------------
    // CAMERA STUFF

    /*
     * Called when the camera application exits with the picture data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            textOut.setText("Enter a picture description and hit done!\n");
            textOut.append("Image saved at: ");
            textOut.append(fileURI.toString());
            this.sendToServer = true;
            this.owner = HomeScreen.currentPerson; // person logged in
        }
        else if (resultCode == RESULT_CANCELED)
        {
            // User cancelled the image capture
            textOut.setText("Press Done to go back!");
        }
        else
        {
            // image capture failed
            textOut.setText("Image Capture Failed!!!\n"
                + "Press Done to go back and try again :(");
        }
    }


    /*
     * Called when the doneButton is clicked
     */
    public void done(View view)
    {
        //post to server in doneButton's listener only if it is okay
        //so only after a picture was taken
        if (this.sendToServer)
        {
            //Get Location
            if (this.hasGoogleAPK)
            {
                Location CurrentLocation;
                CurrentLocation = LocClient.getLastLocation();
                this.LocationTaken =
                    "Latitude: " + CurrentLocation.getLatitude()
                        + " Longitude: " + CurrentLocation.getLongitude();
                textOut.append("\n" + this.LocationTaken);
            }
            this.PictureDescription = this.textInput.getText().toString();
            // create a task to post data to modify servlet
            ModifyTask task = new ModifyTask(this);
            task.execute(
                HomeScreen.ip,
                HomeScreen.port,
                HomeScreen.currentPerson,
                "",
                "",
                this.fileURI.toString(),
                this.LocationTaken,
                this.PictureDescription);
            // reset picture descriptor
            this.owner = "";
            this.fileURI = null;
            this.LocationTaken = "";
            this.PictureDescription = "";
        }
        this.sendToServer = false; // reset
    }


    /** Create a file Uri for saving an image or video */
    // From Android Website
    private static Uri getOutputMediaFileUri()
    {
        return Uri.fromFile(getOutputMediaFile());
    }


    /** Create a File for saving an image or video */
    // From Android Website
    private static File getOutputMediaFile()
    {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir =
            new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "NikePlusPlus");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp =
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile =
            new File(mediaStorageDir.getPath() + File.separator + "IMG_"
                + timeStamp + ".jpg");

        return mediaFile;
    }


    // -------------------------------------------------------------------------
    // GPS Stuff

    // -------------------------------------------------------------------------
    /**
     * Define a DialogFragment that displays the error dialog shown only if the
     * device doesn't have google play services
     *
     * From Android Website
     *
     * @author User1
     * @version Oct 26, 2013
     */
    public static class ErrorDialogFragment
        extends DialogFragment
    {
        // Global field to contain the error dialog
        private Dialog mDialog;


        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment()
        {
            super();
            mDialog = null;
        }


        // Set the dialog to display
        public void setDialog(Dialog dialog)
        {
            mDialog = dialog;
        }


        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            return mDialog;
        }
    }


    // Method that checks whether the device has the google services apk
    // From Android Website
    private boolean servicesConnected()
    {
        // Check that Google Play services is available
        int resultCode =
            GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode)
        {
            // Continue
            return true;
            // Google Play services was not available for some reason
        }
        else
        {
            // Get the error code
            int errorCode = resultCode;
            // Get the error dialog from Google Play services
            Dialog errorDialog =
                GooglePlayServicesUtil.getErrorDialog(
                    errorCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null)
            {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(
                    getSupportFragmentManager(),
                    "Location Updates");
            }
            return false;
        }
    }


    /*
     * Called by Location Services when the request to connect the client
     * finishes successfully. At this point, you can request the current
     * location or start periodic updates
     */
    // From Android Website
    public void onConnected(Bundle dataBundle)
    {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        boolean c = LocClient.isConnected();
    }


    /*
     * Called by Location Services if the connection to the location client
     * drops because of an error.
     */
    // From Android Website
    public void onDisconnected()
    {
        // Display the connection status
        Toast.makeText(
            this,
            "Disconnected. Please re-connect.",
            Toast.LENGTH_SHORT).show();
    }


    /*
     * Called by Location Services if the attempt to Location Services fails.
     */
    // From Android Website
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        /*
         * Google Play services can resolve some errors it detects. If the error
         * has a resolution, try sending an Intent to start a Google Play
         * services activity that can resolve error.
         */
        if (connectionResult.hasResolution())
        {
            try
            {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            }
            catch (IntentSender.SendIntentException e)
            {
                // Log the error
                e.printStackTrace();
            }
        }
        else
        {
            /*
             * If no resolution is available, display a dialog to the user with
             * the error.
             */
        }
    }
}
