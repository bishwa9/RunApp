package data;

import java.util.ArrayList;

// -------------------------------------------------------------------------
/**
 * Object representing a runner
 *
 * @author Bishwamoy Sinha Roy
 * @version Oct 19, 2013
 */
public class Person
{
    private String name_;
    private double totalDist_;
    private double maxDist_;
    private double avgSpeed_;
    private double maxSpeed_;
    private long   totalTime_;
    private long   maxTime_;
    private ArrayList<Picture> photos_;

    // ----------------------------------------------------------
    /**
     * Create a new Person object.
     * @param name : name of person
     */
    public Person(String name)
    {
        this.avgSpeed_ = 0.0;
        this.maxDist_ = 0.0;
        this.maxSpeed_ = 0.0;
        this.maxTime_ = 0;
        this.name_ = name;
        this.totalDist_ = 0.0;
        this.totalTime_ = 0;
        this.photos_ = new ArrayList<Picture>(0);
    }

    // ----------------------------------------------------------
    /**
     * @return : name
     */
    public String getName()
    {
        return name_;
    }


    // ----------------------------------------------------------
    /**
     * @param name : name to modify to
     */
    public void setName(String name)
    {
        this.name_ = name;
    }


    // ----------------------------------------------------------
    /**
     * @return : total distance run
     */
    public double getTotalDist()
    {
        return totalDist_;
    }


    // ----------------------------------------------------------
    /**
     * @param totalMiles : miles to modify to
     */
    public void setTotalDist(double totalMiles)
    {
        this.totalDist_ = totalMiles;
    }


    // ----------------------------------------------------------
    /**
     * @return : maximum distance run
     */
    public double getMaxDist()
    {
        return maxDist_;
    }


    // ----------------------------------------------------------
    /**
     * @param maxDist : maxDist to modify to
     */
    public void setMaxDist(double maxDist)
    {
        this.maxDist_ = maxDist;
    }


    // ----------------------------------------------------------
    /**
     * @return : average running speed
     */
    public double getAvgSpeed()
    {
        return avgSpeed_;
    }


    // ----------------------------------------------------------
    /**
     * @param avgSpeed : average speed to modify to
     */
    public void setAvgSpeed(double avgSpeed)
    {
        this.avgSpeed_ = avgSpeed;
    }


    // ----------------------------------------------------------
    /**
     * @return : maximum running speed
     */
    public double getMaxSpeed()
    {
        return maxSpeed_;
    }


    // ----------------------------------------------------------
    /**
     * @param maxSpeed : max speed to modify to
     */
    public void setMaxSpeed(double maxSpeed)
    {
        this.maxSpeed_ = maxSpeed;
    }


    // ----------------------------------------------------------
    /**
     * @return : total run time
     */
    public long getTotalTime()
    {
        return totalTime_;
    }


    // ----------------------------------------------------------
    /**
     * @param totalTime : time to modify to
     */
    public void setTotalTime(long totalTime)
    {
        this.totalTime_ = totalTime;
    }


    // ----------------------------------------------------------
    /**
     * @return : maximum time run
     */
    public long getMaxTime()
    {
        return maxTime_;
    }


    // ----------------------------------------------------------
    /**
     * @param maxTime : maxTime to modify to
     */
    public void setMaxTime(long maxTime)
    {
        this.maxTime_ = maxTime;
    }

    // ----------------------------------------------------------
    /**
     * @return : list of all pictures
     */
    public ArrayList<Picture> getPhotos()
    {
        return photos_;
    }
}
