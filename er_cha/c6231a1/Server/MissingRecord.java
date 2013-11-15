package c6231.Server;

import java.util.Date;

/**
 *
 * @author chanman
 */
public class MissingRecord extends Record {

    /**
     *
     */
    protected String address;
    /**
     *
     */
    protected Date lastDate;
    /**
     *
     */
    protected String lastLocation;

    MissingRecord(long recordId, String firstName, String lastName, String address, Date lastDate, String lastLocation, String status) {
        super(recordId, firstName, lastName);
        this.address = address;
        this.lastDate = lastDate;
        this.lastLocation = lastLocation;
        this.status = status;
    }

    /**
     *
     * @return
     */
    @Override
    public String getRecordType() {
        return "MR";
    }

    /**
     *
     * @return
     */
    public String toString() {
        return "[" + getId() + "] " + firstName + " " + lastName + " " + address + " " + lastDate + " @" + lastLocation + " [" + status + "]";
    }
}
