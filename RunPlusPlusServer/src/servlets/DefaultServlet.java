package servlets;

import data.ConverterToMessageFormat;
import java.io.OutputStream;
import java.util.Iterator;
import data.Person;
import data.PersonProto;
import data.Data;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;
import javax.servlet.http.HttpServlet;

// -------------------------------------------------------------------------
/**
 * The default servlet that returns info about all registered people
 *
 * @author Bishwamoy Sinha Roy
 * @version Oct 19, 2013
 */
public class DefaultServlet
    extends HttpServlet
{
    // used to convert objects to Google Protocol Buffer Messages
    // class defined in data package
    private ConverterToMessageFormat converter = new ConverterToMessageFormat();

    // ----------------------------------------------------------
    /**
     * Establishes the mapping
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
        context.setContextPath("/");
        server.setHandler(context);

        server.start();
        server.join();
    }

    // if the client used get
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        converter.writeDatabase(Data.getRunners(), resp.getOutputStream());
    }

    // if the client used post
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        converter.writeDatabase(Data.getRunners(), resp.getOutputStream());
    }
}
