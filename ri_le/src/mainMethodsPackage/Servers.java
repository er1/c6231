package mainMethodsPackage;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import distributedSystem.ServerStationImpl;

public class Servers {

	public static void main(String[] args) throws InvalidName, ServantAlreadyActive, WrongPolicy, ObjectNotActive, FileNotFoundException, AdapterInactive {

		ORB orb = ORB.init(args, null);
		POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		
		ServerStationImpl spvmServerStation = new ServerStationImpl("SPVM", 15140);
		byte[] id1 = rootPOA.activate_object(spvmServerStation);
		org.omg.CORBA.Object ref1 = rootPOA.id_to_reference(id1);
		String ior1 = orb.object_to_string(ref1);
		System.out.println(ior1);
		PrintWriter file1 = new PrintWriter("spvm_ior.txt");
		file1.println(ior1);
		file1.close();
		
		
		ServerStationImpl splServerStation = new ServerStationImpl("SPL", 14500);
		byte[] id2 = rootPOA.activate_object(splServerStation);
		org.omg.CORBA.Object ref2 = rootPOA.id_to_reference(id2);
		String ior2 = orb.object_to_string(ref2);
		System.out.println(ior2);
		PrintWriter file2 = new PrintWriter("spl_ior.txt");
		file2.println(ior2);
		file2.close();
		
		ServerStationImpl spbServerStation = new ServerStationImpl("SPB", 15790);
		byte[] id3 = rootPOA.activate_object(spbServerStation);
		org.omg.CORBA.Object ref3 = rootPOA.id_to_reference(id3);
		String ior3 = orb.object_to_string(ref3);
		System.out.println(ior3);
		PrintWriter file3 = new PrintWriter("spb_ior.txt");
		file3.println(ior3);
		file3.close();
		
		rootPOA.the_POAManager().activate();
		orb.run();

	}

}
