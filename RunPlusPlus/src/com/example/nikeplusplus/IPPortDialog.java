package com.example.nikeplusplus;

import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.view.LayoutInflater;
import android.content.DialogInterface;
import android.app.Dialog;
import android.app.AlertDialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

// -------------------------------------------------------------------------
/**
 *  Dialog to change the IP and Port to which the application is connecting
 *  to
 *
 *  @author Bishwamoy Sinha Roy
 *  @version Oct 21, 2013
 */
public class IPPortDialog
    extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View input = inflater.inflate(R.layout.activity_ipport_dialog, null);

        final EditText IPin = (EditText) input
            .findViewById(R.id.ip);
        final EditText Portin = (EditText) input
            .findViewById(R.id.port);

        builder.setView(input)
               .setPositiveButton(R.string.pos, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Change the IP Address
                       HomeScreen.ip = IPin.getText().toString();
                       // Change port
                       HomeScreen.port = Portin.getText().toString();
                   }
               })
               .setNegativeButton(R.string.neg, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
