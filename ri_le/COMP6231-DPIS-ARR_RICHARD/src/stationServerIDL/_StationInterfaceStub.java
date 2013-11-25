package stationServerIDL;

/**
 * Interface definition: StationInterface.
 * 
 * @author OpenORB Compiler
 */
public class _StationInterfaceStub extends org.omg.CORBA.portable.ObjectImpl
        implements StationInterface
{
    static final String[] _ids_list =
    {
        "IDL:stationServerIDL/StationInterface:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = stationServerIDL.StationInterfaceOperations.class;

    /**
     * Operation transfer
     */
    public String transfer(stationServerIDL.Upcasted_Record theRecord)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("transfer",true);
                    stationServerIDL.Upcasted_RecordHelper.write(_output,theRecord);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("transfer",_opsClass);
                if (_so == null)
                   continue;
                stationServerIDL.StationInterfaceOperations _self = (stationServerIDL.StationInterfaceOperations) _so.servant;
                try
                {
                    return _self.transfer( theRecord);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

}
