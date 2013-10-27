package servlets;

import data.ConverterToMessageFormat;
import data.Data;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;
import javax.servlet.http.HttpServlet;

// -------------------------------------------------------------------------
/**
 * Servlet that takes care of adding a picture for a person or modifying
 * the running stats
 *
 * @author Bishwamoy Sinha Roy
 * @version Oct 19, 2013
 */
public class ModifyServlet
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
        context.setContextPath("/modify/*");
        server.setHandler(context);

        server.start();
        server.join();
    }


    // to modify a certain runner or add a picture for a certain person
    // client used GET
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        // chekc if needed parameters are present
        if (req.getParameterMap().size() == 0
            || req.getParameter("name") == null
            || req.getParameter("dist") == null
            || req.getParameter("time") == null
            || req.getParameter("fileLoc") == null
            || req.getParameter("takenLoc") == null
            || req.getParameter("description") == null)
        {
            converter.writeError(
                "Not proper message format!",
                resp.getOutputStream());
            return;
        }

        String name = req.getParameter("name");
        if( name == "" )
        {
            converter.writeError(
                "No name provided",
                resp.getOutputStream());
            return;
        }
        if (req.getParameter("dist") != "" && req.getParameter("time") != "")
        { // want to add new dist and time
            double dist = Double.parseDouble(req.getParameter("dist"));
            long time = Long.parseLong(req.getParameter("time"));
            // modify the correct runner
            Data.getRunners().updatePerson(name, time, dist);
        }
        else if(req.getParameter("fileLoc") != "" &&
                req.getParameter("takenLoc") != "" &&
                req.getParameter("description") != "")
        { // want to add pic
            String fileLoc = req.getParameter("fileLoc");
            String takenLoc = req.getParameter("takenLoc");
            String desc = req.getParameter("description");
            // create a representative picture
            data.Picture pic = new data.Picture();
            pic.setDescription(desc);
            pic.setFileLoc(fileLoc);
            pic.setLocationTaken(takenLoc);
            pic.setOwner(name);
            // add to the person
            Data.getRunners().addPicture(pic);
        }

        // send the modified person through protocol buffers
        converter.writePerson(
            Data.getRunners().getRegisteredPeople().get(name),
            resp.getOutputStream());
    }

    // does the same thing as doGet but if the client posted instead of get
    // client used POST
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        if (req.getParameterMap().size() == 0
            || req.getParameter("name") == null
            || req.getParameter("dist") == null
            || req.getParameter("time") == null
            || req.getParameter("fileLoc") == null
            || req.getParameter("takenLoc") == null
            || req.getParameter("description") == null)
        {
            converter.writeError(
                "Not proper message format!",
                resp.getOutputStream());
            return;
        }

        String name = req.getParameter("name");
        if( name == "" )
        {
            converter.writeError(
                "No name provided",
                resp.getOutputStream());
            return;
        }
        if (req.getParameter("dist") != "" && req.getParameter("time") != "")
        { // want to add new dist and time
            double dist = Double.parseDouble(req.getParameter("dist"));
            long time = Long.parseLong(req.getParameter("time"));
            // modify the correct runner
            Data.getRunners().updatePerson(name, time, dist);
        }
        else if(req.getParameter("fileLoc") != "" &&
                req.getParameter("takenLoc") != "" &&
                req.getParameter("description") != "")
        { // want to add pic
            String fileLoc = req.getParameter("fileLoc");
            String takenLoc = req.getParameter("takenLoc");
            String desc = req.getParameter("description");
            // create a representative picture
            data.Picture pic = new data.Picture();
            pic.setDescription(desc);
            pic.setFileLoc(fileLoc);
            pic.setLocationTaken(takenLoc);
            pic.setOwner(name);
            // add to the person
            Data.getRunners().addPicture(pic);
        }

        // send the modified person through protocol buffers
        converter.writePerson(
            Data.getRunners().getRegisteredPeople().get(name),
            resp.getOutputStream());
    }
}
