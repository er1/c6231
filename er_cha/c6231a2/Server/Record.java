package c6231.Server;

/**
 *
 * @author chanman
 */
public abstract class Record {

    /**
     *
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        if (validStatus(status)) {
            this.status = status;
        }
    }
    /**
     *
     */
    protected String firstName;
    /**
     *
     */
    protected String lastName;
    /**
     *
     */
    protected String status;
    /**
     *
     */
    protected long id;

    /**
     *
     * @param id
     * @param firstName
     * @param lastName
     */
    public Record(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     *
     * @return
     */
    protected abstract String getRecordType();

    /**
     *
     * @param status
     * @return
     */
    protected boolean validStatus(String status) {
        // FIXME: all statuses are good statuses
        return true;
    }

    /**
     *
     * @return
     */
    public String getId() {
        return getRecordType() + id;
    }
}
