package c6231.Server;

/**
 *
 * @author chanman
 */
public abstract class Record {

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
    public String getFirstName() {
        return firstName;
    }
    /**
     *
     * @return
     */
    public String getId() {
        return getRecordType() + id;
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
     * @return
     */
    protected abstract String getRecordType();
    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
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
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
     * @param status
     * @return
     */
    protected boolean validStatus(String status) {
        // FIXME: all statuses are good statuses
        return true;
    }
}
