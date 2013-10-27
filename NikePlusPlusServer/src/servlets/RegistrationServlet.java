package servlets;

import data.ConverterToMessageFormat;
import java.io.OutputStream;
import data.PersonProto.Person.Builder;
import data.Data;
import data.Person;
import data.PersonProto;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

// -------------------------------------------------------------------------
/**
 * servlet to register a person to the server
 *
 * @author Bishwamoy Sinha Roy
 * @version Oct 19, 2013
 */
public class RegistrationServlet
    extends HttpServlet
{
    // used to convert objects to Google Protocol Buffer Messages
    // class defined in data package
    private ConverterToMessageFormat converter = new ConverterToMessageFormat();


    // ----------------------------------------------------------
    /**
     * Establishes a mapping to this servlet and gets it running
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args)
        throws Exception
    {
        Server server = new Server(8080);

        WebAppContext context = new WebAppContext();
        context.setWar("war");
        context.setContextPath("/register/*");
        server.setHandler(context);

        server.start();
        server.join();
    }


    // to register a new person
    // client used GET
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        // check if needed parameters have been included
        if (req.getParameterMap().size() == 0
            || req.getParameter("name") == null
            || req.getParameter("name") == "")
        {
            converter.writeError(
                "Not proper message format!",
                resp.getOutputStream());
            return;
        }

        String name = req.getParameter("name"); // retrieve name
        Person newPerson = new Person(name); // init person

        Data.getRunners().addPerson(newPerson); // add to database

        converter.writePerson(// reply with the person
            Data.getRunners().getRegisteredPeople().get(name),
            resp.getOutputStream());
    }

    // to register a new person
    // client used POST
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        // check if needed parameters have been included
        if (req.getParameterMap().size() == 0
            || req.getParameter("name") == null
            || req.getParameter("name") == "")
        {
            converter.writeError(
                "Not proper message format!",
                resp.getOutputStream());
            return;
        }

        String name = req.getParameter("name"); // retrieve name
        Person newPerson = new Person(name); // init person

        Data.getRunners().addPerson(newPerson); // add to database

        converter.writePerson(// reply with the person
            Data.getRunners().getRegisteredPeople().get(name),
            resp.getOutputStream());
    }
}
