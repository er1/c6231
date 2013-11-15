import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.junit.Test;

import server.report.concrete.RecordFactory;
import server.report.interfaces.IRecord;

/**
 * Tests against records
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 * @version Build 1
 *
 */
public class RecordTests {

	/**
	 * Tests that when creating a record it requires a non null first name and non empty first name
	 */
	@Test
	public void InvalidFirstNameInRecord() {
		IRecord record1 = RecordFactory.getCriminalRecord(null, "Bar", "Description", common.corba.recordInterfaceManager.ReportStatus.CAPTURED); // Null first name
		IRecord record2 = RecordFactory.getCriminalRecord("", "Bar", "Description", common.corba.recordInterfaceManager.ReportStatus.CAPTURED); // Empty first name
		IRecord record3 = RecordFactory.getCriminalRecord("Foo", "Bar", "Description", common.corba.recordInterfaceManager.ReportStatus.CAPTURED); // What it should look like
		assertTrue(record1 == null && record2 == null && record3 != null);
	}
	
	/**
	 * Tests that the last name is a valid last name when creating a record
	 */
	@Test
	public void InvalidLastNameInRecord() {
		IRecord record1 = RecordFactory.getCriminalRecord("Foo", null, "Description", common.corba.recordInterfaceManager.ReportStatus.CAPTURED); // Null first name
		IRecord record2 = RecordFactory.getCriminalRecord("Foo", "", "Description", common.corba.recordInterfaceManager.ReportStatus.CAPTURED); // Empty first name
		IRecord record3 = RecordFactory.getCriminalRecord("Foo", "1Whatever", "Description", common.corba.recordInterfaceManager.ReportStatus.CAPTURED); // Number is the first character
		IRecord record4 = RecordFactory.getCriminalRecord("Foo", "Whatever", "Description", common.corba.recordInterfaceManager.ReportStatus.CAPTURED); // What it should look like
		assertTrue(record1 == null && record2 == null && record3 == null && record4 != null);
	}
	
	/**
	 * Tests that when creating a record it requires a correct status based on the record being created
	 */
	@Test
	public void InvalidRecordStatus() {
		
		// Criminal Record (Invalid)
		IRecord record1 = RecordFactory.getCriminalRecord("Foo", "Bar", "Description", common.corba.recordInterfaceManager.ReportStatus.MISSING);
		IRecord record2 = RecordFactory.getCriminalRecord("Foo", "Bar", "Description", common.corba.recordInterfaceManager.ReportStatus.FOUND);
		
		// Criminal Record (Valid)
		IRecord record3 = RecordFactory.getCriminalRecord("Foo", "Bar", "Description", common.corba.recordInterfaceManager.ReportStatus.ON_THE_RUN);
		IRecord record4 = RecordFactory.getCriminalRecord("Foo", "Bar", "Description", common.corba.recordInterfaceManager.ReportStatus.CAPTURED);
		
		// Missing Record (Invalid)
		IRecord record5 = RecordFactory.getMissingRecord("Foo", "Bar", "Description", new Date().getTime(), "Wherever", common.corba.recordInterfaceManager.ReportStatus.ON_THE_RUN);
		IRecord record6 = RecordFactory.getMissingRecord("Foo", "Bar", "Description", new Date().getTime(), "Wherever", common.corba.recordInterfaceManager.ReportStatus.CAPTURED);
		
		// Missing Record (valid)
		IRecord record7 = RecordFactory.getMissingRecord("Foo", "Bar", "Description", new Date().getTime(), "Wherever", common.corba.recordInterfaceManager.ReportStatus.MISSING);
		IRecord record8 = RecordFactory.getMissingRecord("Foo", "Bar", "Description", new Date().getTime(), "Wherever", common.corba.recordInterfaceManager.ReportStatus.FOUND);
		
		assertTrue(record1 == null && record2 == null && record3 != null && record4 != null && record5 == null && record6 == null && record7 != null && record8 != null);
	}
	
	/**
	 * Test that verifies that all records created are unique
	 */
	@Test
	public void UniqueRecords() {
		List<IRecord> records = new ArrayList<IRecord>();
		Boolean isUnique = true;
		
		for(int i = 0; i < 100; ++i) {
			records.add(RecordFactory.getCriminalRecord("Foo", "Bar", "Description", common.corba.recordInterfaceManager.ReportStatus.CAPTURED));
		}
		
		for(int i = 0; i < records.size(); ++i) {
			try {
				System.out.println(records.get(i).getNumericalID());
				int identifier = records.get(i).getNumericalID();
				for(int j = i + 1; j < records.size(); ++j) {
					if(identifier == records.get(j).getNumericalID()) {
						isUnique = false;
						break;
					}
				}
				if(!isUnique) {
					break;
				}
			} catch (InvalidAttributeValueException e) {
				e.printStackTrace();
			}
		}
		assertTrue(isUnique);
	}
}
