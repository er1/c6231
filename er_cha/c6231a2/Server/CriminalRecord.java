package c6231.Server;

/**
 *
 * @author chanman
 */
public class CriminalRecord extends Record {

    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
    @Override
    public String toString() {
        return "[" + getId() + "] " + firstName + " " + lastName + " " + description + " [" + status + "]";
    }
}
