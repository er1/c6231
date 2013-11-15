package c6231.Server;

/**
 *
 * @author chanman
 */
public class CriminalRecord extends Record {

    String description;

    CriminalRecord(long recordId, String firstName, String lastName, String description, String status) {
        super(recordId, firstName, lastName);
        this.description = description;
        this.status = status;
    }

    /**
     *
     * @return
     */
    @Override
    public String getRecordType() {
        return "CR";
    }

    /**
     *
     * @return
     */
    public String toString() {
        return "[" + getId() + "] " + firstName + " " + lastName + " " + description + " [" + status + "]";
    }
}
