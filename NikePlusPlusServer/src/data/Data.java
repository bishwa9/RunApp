package data;


// -------------------------------------------------------------------------
/**
 *  Instantiates a common database object for the servlets to work off of
 *
 *  @author Bishwamoy Sinha Roy
 *  @version Oct 19, 2013
 */
public class Data
{
    // common database object
    private static DataBase runners_ = new DataBase();

    // ----------------------------------------------------------
    /**
     * Retrieve the database to modify
     */
    public static DataBase getRunners()
    {
        return runners_;
    }
}
