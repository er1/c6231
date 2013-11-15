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

        if (opName.equals("createCRecord")) {
                return _invoke_createCRecord(_is, handler);
        } else if (opName.equals("createMRecord")) {
                return _invoke_createMRecord(_is, handler);
        } else if (opName.equals("editCRecord")) {
                return _invoke_editCRecord(_is, handler);
        } else if (opName.equals("getRecordCounts")) {
                return _invoke_getRecordCounts(_is, handler);
        } else if (opName.equals("transfer")) {
                return _invoke_transfer(_is, handler);
        } else if (opName.equals("transferRecord")) {
                return _invoke_transferRecord(_is, handler);
        } else {
            throw new org.omg.CORBA.BAD_OPERATION(opName);
        }
    }

    // helper methods
    private org.omg.CORBA.portable.OutputStream _invoke_createCRecord(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();
        String arg2_in = _is.read_string();
        String arg3_in = _is.read_string();
        String arg4_in = _is.read_string();

        boolean _arg_result = createCRecord(arg0_in, arg1_in, arg2_in, arg3_in, arg4_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_createMRecord(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();
        String arg2_in = _is.read_string();
        String arg3_in = _is.read_string();
        String arg4_in = _is.read_string();
        String arg5_in = _is.read_string();
        String arg6_in = _is.read_string();

        boolean _arg_result = createMRecord(arg0_in, arg1_in, arg2_in, arg3_in, arg4_in, arg5_in, arg6_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getRecordCounts(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();

        String _arg_result = getRecordCounts(arg0_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_editCRecord(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();
        String arg2_in = _is.read_string();
        String arg3_in = _is.read_string();

        boolean _arg_result = editRecord(arg0_in, arg1_in, arg2_in, arg3_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_transferRecord(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();
        String arg2_in = _is.read_string();

        boolean _arg_result = transferRecord(arg0_in, arg1_in, arg2_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_transfer(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        stationServerIDL.Upcasted_Record arg0_in = stationServerIDL.Upcasted_RecordHelper.read(_is);

        boolean _arg_result = transfer(arg0_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

}
