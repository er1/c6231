package c6231.Server;

import java.util.Date;

public class MissingRecord extends Record {

    protected String address;
    protected Date lastDate;
    protected String lastLocation;

    MissingRecord(long recordId, String firstName, String lastName, String address, Date lastDate, String lastLocation, String status) {
        super(recordId, firstName, lastName);
        this.address = address;
        this.lastDate = lastDate;
        this.lastLocation = lastLocation;
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    @Override
    public String getRecordType() {
        return "MR";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    @Override
    public String toString() {
        return "[" + getId() + "] " + firstName + " " + lastName + " " + address + " " + lastDate + " @" + lastLocation + " [" + status + "]";
    }
}
