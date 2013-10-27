package com.example.nikeplusplus;

import java.util.ArrayList;

// -------------------------------------------------------------------------
/**
 *  Interface for any activity that wants to use the async tasks
 *  mainly needed so that the tasks can display
 *
 *  @author Bishwamoy Sinha Roy
 *  @version Oct 23, 2013
 */
public interface ScreenInterface
{
    // ----------------------------------------------------------
    /**
     * called by pares task to display on GUI
     * @param in
     */
    public abstract void display(ArrayList<String> in);
}
