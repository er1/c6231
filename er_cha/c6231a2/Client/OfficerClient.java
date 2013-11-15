package c6231.Client;

import c6231.Log;
import c6231.StationInterface;
import c6231.StationInterfaceHelper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

/**
 * Officer Class to act against the RMI server logging events as they occur
 *
 * @author chanman
 */
public class OfficerClient {

    String badgeId;
    String station;
    StationInterface instance;
    Log log;

    /**
     * Create an Officer client and its corresponding log
     *
     * @param badgeId
     */
    public OfficerClient(String badgeId) {
        if (!badgeId.matches("[A-Z]+\\d\\d\\d\\d")) {
            throw new RuntimeException("Bad Badge ID");
        }
        this.log = new Log(badgeId);
        this.badgeId = badgeId;
        this.station = badgeId.substring(0, badgeId.length() - 4);
        this.log.log("Started!");
    }

    /**
     * Connect to the RMI server
     *
     * @throws RemoteException
     */
    public void connect() {
        try {
            // ghetto hardcode the parameters
            ORB orb = ORB.init(new String[]{"-ORBInitialHost", "localhost", "-ORBInitialPort", "8989"}, null);

            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContext ncRef = NamingContextHelper.narrow(objRef);

            NameComponent nc = new NameComponent(station, "");
            NameComponent path[] = {nc};
            instance = StationInterfaceHelper.narrow(ncRef.resolve(path));

            this.log.log("Connected!");
        } catch (Exception ex) {
            log.log(ex.toString() + ex.getMessage());
        }
    }

    String createCRecord(String firstName, String lastName, String description, String status) {
        if (firstName == null || lastName == null || description == null || status == null) {
            return null;
        }
        String retval;
        log.log("createCRecord (" + firstName + " " + lastName + ": " + description + " [" + status + "])");
        retval = instance.createCRecord(firstName, lastName, description, status);
        log.log("  createCRecord <OK> " + retval);
        return retval;
    }

    String createMRecord(String firstName, String lastName, String address, Date lastDate, String lastLocation, String status) {
        if (firstName == null || lastName == null || address == null || lastDate == null || lastLocation == null || status == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String retval;
        log.log("createMRecord (" + firstName + " " + lastName + ": " + address + " " + lastLocation + "@" + lastDate.toString() + " [" + status + "])");
        retval = instance.createMRecord(firstName, lastName, address, df.format(lastDate), lastLocation, status);
        log.log("  createMRecord <OK> " + retval);
        return retval;
    }

    String getRecordCounts() {
        String retval;
        log.log("getRecordCounts ()");
        retval = instance.getRecordCounts();
        log.log("  getRecordCounts <OK> " + retval);
        return retval;
    }

    String editCRecord(String lastName, String recordID, String newStatus) {
        if (lastName == null || recordID == null || newStatus == null) {
            return null;
        }
        String retval;
        log.log("editCRecord (" + recordID + "(" + lastName + ") --> " + newStatus + ")");
        retval = instance.editCRecord(lastName, recordID, newStatus);
        log.log("  editCRecord <OK> " + retval);
        return retval;
    }

    String transferRecord(String badgeID, String recordID, String remoteStationServerName) {
        if (badgeID == null || recordID == null || remoteStationServerName == null) {
            return null;
        }
        String retval;
        log.log("transferRecord (" + badgeID + " transfers " + recordID + " --> " + remoteStationServerName);
        retval = instance.transferRecord(badgeID, recordID, remoteStationServerName);
        log.log("  transferRecord <OK> " + retval);
        return retval;
    }
}
