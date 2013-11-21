package c6231.Server;

public class CriminalRecord extends Record {

    String description;

    CriminalRecord(long recordId, String firstName, String lastName, String description, String status) {
        super(recordId, firstName, lastName);
        this.description = description;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getRecordType() {
        return "CR";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "[" + getId() + "] " + firstName + " " + lastName + " " + description + " [" + status + "]";
    }
}
