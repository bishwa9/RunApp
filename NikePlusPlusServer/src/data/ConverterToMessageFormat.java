package data;

import java.io.IOException;
import java.io.OutputStream;

// -------------------------------------------------------------------------
/**
 * Objects of this class can be used to send persons and database through Google
 * Protocol Buffer Message
 *
 * @author Bishwamoy Sinha Roy
 * @version Oct 24, 2013
 */
public class ConverterToMessageFormat
{
    // ----------------------------------------------------------
    /**
     * Searilize the data and send to client
     *
     * @param persons
     *            - database to convert to message
     * @param stream
     *            - stream through which to send info
     */
    public void writeDatabase(DataBase persons, OutputStream stream)
    {
        // wrap database object with a response message
        PersonProto.response response =
            this.getResponse("successDB", null, null, persons);
        try
        {
            response.writeTo(stream);
            stream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    // ----------------------------------------------------------
    /**
     * write a person to the output stream
     *
     * @param person
     *            - person to convert to message
     * @param stream
     *            - stream through which to send info
     */
    public void writePerson(Person person, OutputStream stream)
    {
        // wrap person object with a response message
        PersonProto.response response =
            this.getResponse("successPerson", null, person, Data.getRunners());
        try
        {
            response.writeTo(stream);
            stream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    // ----------------------------------------------------------
    /**
     * To write an error to the client
     *
     * @param errorMessage
     *            - description of the error
     * @param stream
     *            - stream through which to send info
     */
    public void writeError(String errorMessage, OutputStream stream)
    {
        // wrap error descrition with a response message
        PersonProto.response response =
            this.getResponse("error", errorMessage, null, null);
        try
        {
            response.writeTo(stream);
            stream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    // ----------------------------------------------------------
    /**
     * convert the database to a message
     *
     * @param db
     * @return
     */
    private data.PersonProto.allRegisteredPersons convertDBToMessage(
        data.DataBase db)
    {
        // get a builder from google generated code
        data.PersonProto.allRegisteredPersons.Builder persons =
            data.PersonProto.allRegisteredPersons.newBuilder();
        // go through database object to add all persons to the list
        for (String name : db.getRegisteredPeople().keySet())
        {
            persons.addPerson(convertPersonToMessage(db.getRegisteredPeople()
                .get(name)));
        }
        return persons.build();
    }


    // ----------------------------------------------------------
    /**
     * Converts the person to a person message to send
     *
     * @param newPerson
     * @return
     */
    private PersonProto.Person convertPersonToMessage(Person newPerson)
    {
        // get a builder from google generated code
        data.PersonProto.Person.Builder messagePerson =
            data.PersonProto.Person.newBuilder();

        // add all variables
        messagePerson.setName(newPerson.getName());
        messagePerson.setMaxDist(newPerson.getMaxDist());
        messagePerson.setTotalDist(newPerson.getTotalDist());
        messagePerson.setMaxSpeed(newPerson.getMaxSpeed());
        messagePerson.setMaxTime(newPerson.getMaxTime());
        messagePerson.setTotalTime(newPerson.getTotalTime());
        messagePerson.setAvgSpeed(newPerson.getAvgSpeed());
        // add all of the pictures
        if (newPerson.getPhotos().size() != 0)
        {
            for (Picture pic : newPerson.getPhotos())
            {
                data.PersonProto.picture.Builder photo =
                    data.PersonProto.picture.newBuilder();
                photo.setFileLoc(pic.getFileLoc());
                photo.setLocationTaken(pic.getLocationTaken());
                photo.setOwner(pic.getOwner());
                photo.setDescription(pic.getDescription());
                messagePerson.addPhoto(photo);
            }
        }

        return messagePerson.build();
    }


    // ----------------------------------------------------------
    /**
     * Wraps with a response message
     */
    private PersonProto.response getResponse(
        String type,
        String error,
        Person person,
        data.DataBase dB)
    {
        data.PersonProto.response.Builder resp =
            data.PersonProto.response.newBuilder();

        switch (type)
        {
            case "error": // error response
            {
                resp.setMessageStatus("ERROR: " + error);
                break;
            }
            case "successPerson": // send only one person
            {
                resp.setMessageStatus("OK");
                resp.setPerson(this.convertPersonToMessage(person));
                resp.setNumOfPersons(dB.getRegisteredPeople().size());
                break;
            }
            case "successDB": // send the whole database
            {
                resp.setMessageStatus("OK");
                resp.setPersons(this.convertDBToMessage(dB));
                resp.setNumOfPersons(dB.getRegisteredPeople().size());
                break;
            }
        }

        return resp.build();
    }
}
