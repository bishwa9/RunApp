package data;

// -------------------------------------------------------------------------
/**
 *  Objects representing a picture
 *
 *  @author Bishwamoy Sinha Roy
 *  @version Oct 26, 2013
 */
public class Picture
{
    private String fileLoc;
    private String description;
    private String locationTaken;
    private String owner;

    public String getFileLoc()
    {
        return fileLoc;
    }
    public void setFileLoc(String fileLocation)
    {
        this.fileLoc = fileLocation;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description_)
    {
        this.description = description_;
    }
    public String getLocationTaken()
    {
        return locationTaken;
    }
    public void setLocationTaken(String locationTaken_)
    {
        this.locationTaken = locationTaken_;
    }
    public String getOwner()
    {
        return owner;
    }
    public void setOwner(String owner_)
    {
        this.owner = owner_;
    }
}
