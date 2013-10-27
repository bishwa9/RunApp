package tasks;

import com.example.nikeplusplus.ScreenInterface;
import java.util.ArrayList;
import com.google.protobuf.Descriptors.FieldDescriptor;
import android.app.Activity;
import data.PersonProto.response;
import android.os.AsyncTask;

// -------------------------------------------------------------------------
/**
 * Used to parse the Google Protocol Buffer message to String List and display
 * on any gui that implements the ScreenInterface interface
 *
 * @author Bishwamoy Sinha Roy
 * @version Oct 24, 2013
 */
public class ParseTask
    extends AsyncTask<data.PersonProto.response, Void, ArrayList<String>>
{
    private ScreenInterface gui;


    // ----------------------------------------------------------
    /**
     * Create a new ParseTask object.
     *
     * @param gui_
     *            : gui on which to post updates on
     */
    public ParseTask(ScreenInterface gui_)
    {
        gui = gui_;
    }


    @Override
    protected ArrayList<String> doInBackground(response... param)
    {
        ArrayList<String> out = new ArrayList<String>(0);

        data.PersonProto.response response = param[0];

        String mStatus = response.getMessageStatus();

        if (mStatus.contains("OK"))
        {
            if (response.hasPerson()) // returned a person
            {
                data.PersonProto.Person person = response.getPerson();
                out.add("Currently Registered Users: "
                    + response.getNumOfPersons() + "\n"
                    + this.convPersonToString(person));
            }
            else
            // returned a database of persons
            {
                out.add("Currently Registered Users: "
                    + response.getNumOfPersons());
                for (data.PersonProto.Person person : response.getPersons()
                    .getPersonList())
                {
                    out.add(this.convPersonToString(person));
                }
            }
        }
        else if (mStatus.contains("ERROR"))
        {
            out.add(response.getMessageStatus());
        }
        else
        { // would happen if the server changed message statuses
            out.add("Server's message status is unrecognizable!");
        }

        return out;
    }


    @Override
    protected void onPostExecute(ArrayList<String> result)
    {
        gui.display(result); // display onto the GUI
    }


    // ----------------------------------------------------------
    /**
     * unwrap the person message into a string
     *
     * @param person
     *            : person message sent by the server
     * @return : string representation of the person
     */
    public String convPersonToString(data.PersonProto.Person person)
    {
        String resp = "";
        // running statistics
        resp =
            "Name: " + person.getName() + "\n" + "Total Distance Ran: "
                + person.getTotalDist() + "m\n" + "Total Time Ran: "
                + person.getTotalTime() + "s\n" + "Average Speed: "
                + person.getAvgSpeed() + "m/s\n" + "Max Speed: "
                + person.getMaxSpeed() + "m/s\n" + "Longest Run(time): "
                + person.getMaxTime() + "s\n" + "Longet Run(distance): "
                + person.getMaxDist() + "m";
        // about pictures
        for (data.PersonProto.picture pic : person.getPhotoList())
        {
            resp += "\nPhoto Descriptions: " + pic.getDescription();
            resp += "\nPhoto Location: " + pic.getLocationTaken();
        }

        return resp;
    }

}
