package com.example.nikeplusplus;

import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.widget.ListView;
import android.widget.TextView;
import tasks.RegisterTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.annotation.TargetApi;
import android.os.Build;

// -------------------------------------------------------------------------
/**
 *  Display all registered players
 *
 *  @author Bishwamoy Sinha Roy
 *  @version Oct 25, 2013
 */
public class RecordScreen
    extends Activity implements ScreenInterface
{

    private ListView records;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_screen);
        // Show the Up button in the action bar.
        setupActionBar();

        records = (ListView)findViewById(R.id.recordList);
        // createy a task to retrieve all registered players
        tasks.QueryTask task = new tasks.QueryTask(this);
        task.execute(HomeScreen.ip, HomeScreen.port);
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
        getMenuInflater().inflate(R.menu.record_screen, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return true;
                // TODO: refresh button
        }
        return super.onOptionsItemSelected(item);
    }

    // over load function to display all players on a list view
    public void display(ArrayList<String> in)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, in);
        records.setAdapter(adapter);
    }

}
