package c6231.Client;

import java.util.Date;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author chanman
 */
public class OfficerMenuClient {

    static Scanner in;

    /**
     *
     * @param query
     * @return
     */
    protected static String ask(String query) {
        System.out.print(query);
        return in.nextLine();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        String officerId = "SPVM7851";

        in = new Scanner(System.in);

        System.out.print("BadgeID: ");
        officerId = in.nextLine();

        OfficerClient client = new OfficerClient(officerId);
        client.connect();

        System.out.println(officerId);

        int response;

        do {

            System.out.print("\n\nOfficer Menu for " + officerId + "\n"
                    + "1. Create Criminal Record\n"
                    + "2. Create Missing Record\n"
                    + "3. Get Record Count\n"
                    + "4. Edit Criminal Record\n"
                    + "5. Transfer Record to Another Station\n"
                    + "0. Quit\n"
                    + "\n"
                    + "Enter Selection: ");

            try {
                String responseLine = in.nextLine();
                response = Integer.parseInt(responseLine.trim());
            } catch (Exception ex) {
                response = -1;
            }

            String retval;

            switch (response) {
                case 1:
                    try {
                        String name[] = (ask("Name: ") + " BADNAME BADNAME").split(" ");
                        String description = ask("Description: ");
                        String status = ask("Status: ");

                        retval = client.createCRecord(name[0], name[1], description, status);
                        System.out.println(retval);
                    } catch (Exception ex) {
                        System.out.println(ex.toString() + ex.getMessage());
                    }
                    break;
                case 2:
                    try {
                        String name[] = (ask("Name: ") + " BADNAME BADNAME").split(" ");
                        String address = ask("Address: ");
                        Date lastSeen = new Date(1230000000 + new Random().nextLong() % 150000000);
                        System.out.println(lastSeen.toString());
                        String lastLocation = ask("Last Location: ");
                        String status = ask("Status: ");

                        retval = client.createMRecord(name[0], name[1], address, lastSeen, lastLocation, status);
                        System.out.println(retval);
                    } catch (Exception ex) {
                        System.out.println(ex.toString() + ex.getMessage());
                    }
                    break;
                case 3:
                    retval = client.getRecordCounts();
                    System.out.println(retval);
                    break;
                case 4:
                    String lastName = ask("Last Name: ");
                    String recordId = ask("Record ID: ");
                    String status = ask("New Status: ");
                    retval = client.editCRecord(lastName, recordId, status);
                    System.out.println(retval);
                    break;
                case 5:
                    String trecordId = ask("Record ID: ");
                    String destination = ask("Station: ");
                    retval = client.transferRecord(officerId, trecordId, destination);
                    System.out.println(retval);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Bad response... ):");
            }

        } while (response != 0);

    }
}
