package tasks;

import com.example.nikeplusplus.ScreenInterface;
import org.xml.sax.helpers.ParserAdapter;
import org.apache.http.message.BasicNameValuePair;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import data.PersonProto.response;
import android.os.AsyncTask;

// -------------------------------------------------------------------------
/**
 *  AsyncTask that modifies a person or adds a person
 *
 *  @author Bishwamoy Sinha Roy
 *  @version Oct 24, 2013
 */
public class ModifyTask
    extends AsyncTask<String, Void, data.PersonProto.response>
{
    private ScreenInterface gui;


    // ----------------------------------------------------------
    /**
     * Create a new ModifyTask object.
     * @param gui_ : gui onto which otuput is printed
     */
    public ModifyTask(ScreenInterface gui_)
    {
        gui = gui_;
    }


    @Override
    // param[0] = ipaddress
    // param[1] = port
    // param[2] = name
    // param[3] = newTime
    // param[4] = newDist
    // param[5] = photoLoc
    // param[6] = takenLoc
    // param[7] = photoDescription
    protected response doInBackground(String... param)
    {
        if (param.length != 8)
        {
            return null;
        }

        String server = param[0]; // ip Address
        String port = param[1]; // port number
        String name = param[2]; // name of the person being registered
        String newTime = param[3]; // new Time entered
        String newDist = param[4]; // new Distance ran
        String photoLocation = param[5]; // file location of a photo
        String takenLocation = param[6]; // geolocation
        String description = param[7]; // photo's decription

        // post to /modify servlet on the server end to modify the person
        String url = "http://" + server + ":" + port + "/modify";

        // create key-value pairs
        ArrayList<NameValuePair> nameValuePairs =
            new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("name", name));
        nameValuePairs.add(new BasicNameValuePair("dist", newDist));
        nameValuePairs.add(new BasicNameValuePair("time", newTime));
        nameValuePairs.add(new BasicNameValuePair("fileLoc", photoLocation));
        nameValuePairs.add(new BasicNameValuePair("takenLoc", takenLocation));
        nameValuePairs
            .add(new BasicNameValuePair("description", description));

        // post to server
        data.PersonProto.response resp = makeHTTPPost(url, nameValuePairs);
        return resp;
    }


    @Override
    protected void onPostExecute(data.PersonProto.response result)
    {
        if (result == null)
        {
            return;
        }
        else
        {
            // parse server response
            // should be the person that was modified or an error
            ParseTask parser = new ParseTask(gui);
            parser.execute(result);
        }
    }


    // ----------------------------------------------------------
    /**
     * called to make a HttpRequest to the appropriate server
     *
     * @param url
     *            : url to request
     * @return : response from the server
     */
    public data.PersonProto.response makeHTTPPost(
        String url,
        ArrayList<NameValuePair> pairs)
    {
        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(pairs));
            // Execute HTTP Post Request
            HttpResponse response = client.execute(httppost);
            InputStream inFromServer = response.getEntity().getContent();
            // parse
            return data.PersonProto.response.parseFrom(inFromServer);
        }
        catch (UnknownHostException e)
        {
            ArrayList<String> out = new ArrayList<String>(0);
            out.add("Unknown Host");
            gui.display(out);
        }
        catch (IOException e)
        {
            ArrayList<String> out = new ArrayList<String>(0);
            out.add("Unknown Host");
            gui.display(out);
        }
        return null;
    }
}
