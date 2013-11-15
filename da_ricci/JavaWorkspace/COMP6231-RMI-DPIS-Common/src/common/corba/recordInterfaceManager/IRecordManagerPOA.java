package common.corba.recordInterfaceManager;

/**
 * Interface definition for the remote functionality being given by the server to the client
 * 
 * @author  Daniel Ricci <thedanny09 * @gmail .com>
 * 
 */
public abstract class IRecordManagerPOA extends org.omg.PortableServer.Servant
        implements IRecordManagerOperations, org.omg.CORBA.portable.InvokeHandler
{
    public IRecordManager _this()
    {
        return IRecordManagerHelper.narrow(_this_object());
    }

    public IRecordManager _this(org.omg.CORBA.ORB orb)
    {
        return IRecordManagerHelper.narrow(_this_object(orb));
    }

    private static String [] _ids_list =
    {
        "IDL:common/corba/recordInterfaceManager/IRecordManager:1.0"
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
        } else if (opName.equals("editRecord")) {
                return _invoke_editRecord(_is, handler);
        } else if (opName.equals("getRecordCount")) {
                return _invoke_getRecordCount(_is, handler);
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
        common.corba.recordInterfaceManager.ReportStatus arg4_in = common.corba.recordInterfaceManager.ReportStatusHelper.read(_is);

        String _arg_result = createCRecord(arg0_in, arg1_in, arg2_in, arg3_in, arg4_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

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
        long arg4_in = _is.read_longlong();
        String arg5_in = _is.read_string();
        common.corba.recordInterfaceManager.ReportStatus arg6_in = common.corba.recordInterfaceManager.ReportStatusHelper.read(_is);

        String _arg_result = createMRecord(arg0_in, arg1_in, arg2_in, arg3_in, arg4_in, arg5_in, arg6_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_editRecord(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();
        String arg2_in = _is.read_string();
        common.corba.recordInterfaceManager.ReportStatus arg3_in = common.corba.recordInterfaceManager.ReportStatusHelper.read(_is);

        boolean _arg_result = editRecord(arg0_in, arg1_in, arg2_in, arg3_in);

        _output = handler.createReply();
        _output.write_boolean(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_getRecordCount(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();

        String _arg_result = getRecordCount(arg0_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_transferRecord(
            final org.omg.CORBA.portable.InputStream _is,
            final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = _is.read_string();
        String arg1_in = _is.read_string();
        String arg2_in = _is.read_string();

        String _arg_result = transferRecord(arg0_in, arg1_in, arg2_in);

        _output = handler.createReply();
        _output.write_string(_arg_result);

        return _output;
    }

}
