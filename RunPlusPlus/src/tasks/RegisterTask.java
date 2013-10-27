package tasks;

import com.example.nikeplusplus.ScreenInterface;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import java.util.List;
import org.apache.http.client.methods.HttpPost;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.os.AsyncTask;

// -------------------------------------------------------------------------
/**
 *  Task to register a person to the server
 *
 *  @author Bishwamoy Sinha Roy
 *  @version Oct 25, 2013
 */
public class RegisterTask
    extends AsyncTask<String, Void, data.PersonProto.response>
{

    private ScreenInterface gui; // stores the activity that created this

    // ----------------------------------------------------------
    /**
     * Create a new RegisterTask object.
     * @param gui_
     */
    public RegisterTask(ScreenInterface gui_)
    {
        gui = gui_;
    }

    @Override
    // ipAddress is parameter 1
    // port number is parameter 2
    // keyvalue pairs is parameter 3
    protected data.PersonProto.response doInBackground(String... param)
    {
        if( param.length != 3 )
        {
            return null;
        }
        String server = param[0]; // ip Address
        String port = param[1]; // port number
        String name = param[2]; // name of the person being registered
        // post to /register servlet on the server end
        String url = "http://" + server + ":" + port + "/register";
        // create key-value pairs
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("name", name));
        data.PersonProto.response resp =
            makeHTTPPost(url, nameValuePairs);
        return resp;
    }

    @Override
    protected void onPostExecute(data.PersonProto.response result)
    {
        if( result == null )
        { // problem
            ArrayList<String> out = new ArrayList<String>(0);
            out.add("Returned nothing!");
            gui.display(out);
            return;
        }
        else
        { // initiate task to parse server's response
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
