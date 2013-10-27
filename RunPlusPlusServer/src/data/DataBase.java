package data;

import java.util.HashMap;
import java.util.Map;

// -------------------------------------------------------------------------
/**
 * Database of persons

 * @author Bishwamoy Sinha Roy
 * @version Oct 19, 2013
 */
public class DataBase
{
    // map of all the registered persons
    private Map<String, Person> registeredPeople_;


    // ----------------------------------------------------------
    /**
     * Create a new DataBase object.
     */
    public DataBase()
    {
        registeredPeople_ = new HashMap<String, Person>();
    }


    // ----------------------------------------------------------
    /**
     * @return : map of the registered persons
     */
    public Map<String, Person> getRegisteredPeople()
    {
        return registeredPeople_;
    }


    // ----------------------------------------------------------
    /**
     * Only adds a single player with a name
     *
     * @param newPerson : new person to register
     */
    public void addPerson(Person newPerson)
    {
        if (!this.registeredPeople_.containsKey(newPerson.getName()))
        {
            this.registeredPeople_.put(newPerson.getName(), newPerson);
        }
    }


    // ----------------------------------------------------------
    /**
     * Delete a person
     *
     * @param person : person to delete
     */
    public void removePerson(Person person)
    {
        this.registeredPeople_.remove(person.getName());
    }


    // ----------------------------------------------------------
    /**
     * get a person's total distance ran
     *
     * @param name : name of person
     * @return : total dist
     */
    public double getDist(String name)
    {
        return this.registeredPeople_.get(name).getTotalDist();
    }


    // ----------------------------------------------------------
    /**
     * get the maximum distance ran
     *
     * @param name : name of person
     * @return : maximum distance
     */
    public double getMaxDist(String name)
    {
        return this.registeredPeople_.get(name).getMaxDist();
    }


    // ----------------------------------------------------------
    /**
     * get person's average speed
     *
     * @param name : name of person
     * @return : average speed
     */
    public double getAvgSpeed(String name)
    {
        return this.registeredPeople_.get(name).getAvgSpeed();
    }


    // ----------------------------------------------------------
    /**
     * get person's max speed
     *
     * @param name : name of person
     * @return : maximum speed
     */
    public double getMaxSpeed(String name)
    {
        return this.registeredPeople_.get(name).getMaxSpeed();
    }


    // ----------------------------------------------------------
    /**
     * get person's total time ran
     *
     * @param name : name of person
     * @return : total time
     */
    public double getTotalTime(String name)
    {
        return this.registeredPeople_.get(name).getTotalTime();
    }


    // ----------------------------------------------------------
    /**
     * get person's max time ran
     *
     * @param name : name of person
     * @return : maximum time
     */
    public double getMaxTime(String name)
    {
        return this.registeredPeople_.get(name).getMaxTime();
    }


    // ----------------------------------------------------------
    /**
     * Updates the totalMiles
     *
     * @param name : name of person
     * @param newTime : newly run time
     * @param correspondingDistance : newly run distance
     * @return : updated person
     */
    public Person updatePerson(
        String name,
        long newTime,
        double correspondingDistance)
    {
        Person personOfInterest = this.registeredPeople_.get(name);

        // add recent distance to total distance
        personOfInterest.setTotalDist(personOfInterest.getTotalDist()
            + correspondingDistance);
        // add recent time to total time
        personOfInterest
            .setTotalTime(personOfInterest.getTotalTime() + newTime);

        // check if recent time was the longest time
        if (newTime > personOfInterest.getMaxTime())
        {
            personOfInterest.setMaxTime(newTime);
        }
        // check if recent distance was the longest distance
        if (correspondingDistance > personOfInterest.getMaxDist())
        {
            personOfInterest.setMaxDist(correspondingDistance);
        }

        // check if recent speed was high than previous max speed
        double potentialMaxSpeed = correspondingDistance / newTime;
        potentialMaxSpeed = (double)Math.round((potentialMaxSpeed) * 100) / 100;
        if (potentialMaxSpeed > personOfInterest.getMaxSpeed())
        {
            personOfInterest.setMaxSpeed(potentialMaxSpeed);
        }

        // updated average speed
        double newAvgSpeed =
            personOfInterest.getTotalDist() / personOfInterest.getTotalTime();
        newAvgSpeed = (double)Math.round((newAvgSpeed) * 100) / 100;
        personOfInterest.setAvgSpeed(newAvgSpeed);

        // replace the old person with the updated version
        this.registeredPeople_.put(name, personOfInterest);

        return personOfInterest;
    }


    // ----------------------------------------------------------
    /**
     * add a picture ( picture's owner is used to add )
     * owner has to be registered
     *
     * @param pic : picture to be added
     */
    public void addPicture(Picture pic)
    {
        if (this.registeredPeople_.containsKey(pic.getOwner()))
        {
            this.registeredPeople_.get(pic.getOwner()).getPhotos().add(pic);
        }
    }
}
