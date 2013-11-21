package c6231.Server;

public abstract class Record {

    protected String firstName;
    protected String lastName;
    protected String status;
    protected long id;

    public Record(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return getRecordType() + id;
    }

    public String getLastName() {
        return lastName;
    }

    protected abstract String getRecordType();

    public String getStatus() {
        return status;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setStatus(String status) {
        if (validStatus(status)) {
            this.status = status;
        }
    }

    protected boolean validStatus(String status) {
        // FIXME: all statuses are good statuses
        return true;
    }
}
