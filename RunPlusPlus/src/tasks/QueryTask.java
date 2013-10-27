package tasks;

import com.example.nikeplusplus.ScreenInterface;
import android.app.Activity;
import data.PersonProto.response;
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
import org.apache.http.message.BasicNameValuePair;
import android.os.AsyncTask;

// -------------------------------------------------------------------------
/**
 *  This task queries for all registered person on the server
 *
 *  @author Bishwamoy Sinha Roy
 *  @version Oct 25, 2013
 */
public class QueryTask
    extends AsyncTask<String, Void, data.PersonProto.response>
{
    private ScreenInterface gui;

    // ----------------------------------------------------------
    /**
     * Create a new QueryTask object.
     * @param gui_
     */
    public QueryTask(ScreenInterface gui_)
    {
        gui = gui_;
    }


    @Override
    // param[0] = ipaddress
    // param[1] = port
    protected response doInBackground(String... param)
    {
        if (param.length != 2)
        {
            return null;
        }

        String server = param[0]; // ip Address
        String port = param[1]; // port number

        // post to /modify servlet on the server end to modify the person
        String url = "http://" + server + ":" + port + "/";

        // create key-value pairs (none)
        ArrayList<NameValuePair> nameValuePairs =
            new ArrayList<NameValuePair>(0);

        // post to server
        data.PersonProto.response resp = makeHTTPPost(url, nameValuePairs);
        return resp;
    }


    @Override
    protected void onPostExecute(data.PersonProto.response result)
    {
        if (result == null)
        { // no runners registered
            ArrayList<String> in = new ArrayList<String>(0);
            in.add("No runners!");
            gui.display(in);
            return;
        }
        else
        { // initiated parser task to parse server's Google Protocol Buffer message
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
     * @param pairs
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
