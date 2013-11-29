package stationServerIDL;

/**
 * Interface definition: StationInterface.
 * 
 * @author OpenORB Compiler
 */
public abstract class StationInterfacePOA extends org.omg.PortableServer.Servant
        implements StationInterfaceOperations, org.omg.CORBA.portable.InvokeHandler
{
    public StationInterface _this()
    {
        return StationInterfaceHelper.narrow(_this_object());
    }

    public StationInterface _this(org.omg.CORBA.ORB orb)
    {
        return StationInterfaceHelper.narrow(_this_object(orb));
    }

    private static String [] _ids_list =
    {
        "IDL:stationServerIDL/StationInterface:1.0"
    };

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte [] objectId)
    {
        return _ids_list;
    }

    public final org.omg.CORBA.portable.OutputStream _invoke(final String opName,
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler)
    {

        if (opName.equals("transfer")) {
                return _invoke_transfer(_is, handler);
        } else {
            throw new org.omg.CORBA.BAD_OPERATION(opName);
        }
    }

    // helper methods
    private org.omg.CORBA.portable.OutputStream _invoke_transfer(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        stationServerIDL.Upcasted_Record arg0_in = stationServerIDL.Upcasted_RecordHelper.read(_is);

        String _arg_result = transfer(arg0_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

}
