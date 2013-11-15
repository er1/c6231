package c6231.Server;

import c6231.Log;
import java.util.HashMap;

/**
 *
 * @author chanman
 */
public class RecordContainer {

    /**
     *
     */
    protected long nextId = 10000;
    /**
     *
     */
    protected final Object idlock = new Object();
    final HashMap<String, Record>[] records = new HashMap[26];
    final Object recordsLock = new Object();

    static RecordContainer getRecordContainer(String stationName) {
        RecordContainer container = new RecordContainer();
        container.createRecordContainer();
        return container;
    }

    /**
     *
     * @return
     */
    public long getNextFreeId() {
        synchronized (idlock) {
            return nextId++;
        }
    }

    /**
     *
     * @param id
     * @param lastName
     * @return
     */
    public Record getRecord(String id, String lastName) {
        Record record = null;
        return record;
    }

    /**
     *
     * @param record
     */
    public void addRecord(Record record) {
        String lastName = record.getLastName();

        // get the lower five bits representing which letter we are after
        // minus one since letters start at 1

        //int entry = (Character.getNumericValue(lastName.charAt(0)) & 0x1f) - 1;

        int entry = lastName.toLowerCase().codePointAt(0) - "a".codePointAt(0);

        if (entry < 0 || entry >= 26) {
            throw new RuntimeException(lastName + " is a bad last name");
        }

        HashMap<String, Record> entries = records[entry];

        synchronized (entries) {
            entries.put(record.getId(), record);
        }
    }

    /**
     *
     */
    protected void createRecordContainer() {
        for (int i = 0; i < 26; i++) {
            records[i] = new HashMap<String, Record>();
        }
    }

    /**
     *
     * @return
     */
    public int getRecordCount() {
        int count = 0;
        synchronized (records) {
            for (HashMap map : records) {
                count += map.size();
            }
        }
        return count;
    }

    void dump(Log log) {
        synchronized (records) {
            for (HashMap<String, Record> map : records) {
                for (String id : map.keySet()) {
                    log.log(map.get(id).toString());
                }
            }
        }
    }
}
