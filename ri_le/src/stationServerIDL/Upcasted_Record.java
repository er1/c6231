package stationServerIDL;

/**
 * Struct definition: Upcasted_Record.
 * 
 * @author OpenORB Compiler
*/
public final class Upcasted_Record implements org.omg.CORBA.portable.IDLEntity
{
    /**
     * Struct member RecordID
     */
    public String RecordID;

    /**
     * Struct member lastName
     */
    public String lastName;

    /**
     * Struct member firstName
     */
    public String firstName;

    /**
     * Struct member status
     */
    public String status;

    /**
     * Struct member description
     */
    public String description;

    /**
     * Struct member address
     */
    public String address;

    /**
     * Struct member lastDate
     */
    public String lastDate;

    /**
     * Struct member lastLocation
     */
    public String lastLocation;

    /**
     * Default constructor
     */
    public Upcasted_Record()
    { }

    /**
     * Constructor with fields initialization
     * @param RecordID RecordID struct member
     * @param lastName lastName struct member
     * @param firstName firstName struct member
     * @param status status struct member
     * @param description description struct member
     * @param address address struct member
     * @param lastDate lastDate struct member
     * @param lastLocation lastLocation struct member
     */
    public Upcasted_Record(String RecordID, String lastName, String firstName, String status, String description, String address, String lastDate, String lastLocation)
    {
        this.RecordID = RecordID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.status = status;
        this.description = description;
        this.address = address;
        this.lastDate = lastDate;
        this.lastLocation = lastLocation;
    }

}
