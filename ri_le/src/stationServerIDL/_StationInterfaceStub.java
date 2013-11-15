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
     * Operation createCRecord
     */
    public boolean createCRecord(String badgeId, String firstName, String lastName, String description, String status)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("createCRecord",true);
                    _output.write_string(badgeId);
                    _output.write_string(firstName);
                    _output.write_string(lastName);
                    _output.write_string(description);
                    _output.write_string(status);
                    _input = this._invoke(_output);
                    boolean _arg_ret = _input.read_boolean();
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("createCRecord",_opsClass);
                if (_so == null)
                   continue;
                stationServerIDL.StationInterfaceOperations _self = (stationServerIDL.StationInterfaceOperations) _so.servant;
                try
                {
                    return _self.createCRecord( badgeId,  firstName,  lastName,  description,  status);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation createMRecord
     */
    public boolean createMRecord(String badgeId, String firstName, String lastName, String address, String lastDate, String lastLocation, String status)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("createMRecord",true);
                    _output.write_string(badgeId);
                    _output.write_string(firstName);
                    _output.write_string(lastName);
                    _output.write_string(address);
                    _output.write_string(lastDate);
                    _output.write_string(lastLocation);
                    _output.write_string(status);
                    _input = this._invoke(_output);
                    boolean _arg_ret = _input.read_boolean();
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("createMRecord",_opsClass);
                if (_so == null)
                   continue;
                stationServerIDL.StationInterfaceOperations _self = (stationServerIDL.StationInterfaceOperations) _so.servant;
                try
                {
                    return _self.createMRecord( badgeId,  firstName,  lastName,  address,  lastDate,  lastLocation,  status);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getRecordCounts
     */
    public String getRecordCounts(String badgeId)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getRecordCounts",true);
                    _output.write_string(badgeId);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getRecordCounts",_opsClass);
                if (_so == null)
                   continue;
                stationServerIDL.StationInterfaceOperations _self = (stationServerIDL.StationInterfaceOperations) _so.servant;
                try
                {
                    return _self.getRecordCounts( badgeId);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation editCRecord
     */
    public boolean editRecord(String badgeId, String lastName, String recordID, String status)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("editCRecord",true);
                    _output.write_string(badgeId);
                    _output.write_string(lastName);
                    _output.write_string(recordID);
                    _output.write_string(status);
                    _input = this._invoke(_output);
                    boolean _arg_ret = _input.read_boolean();
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("editCRecord",_opsClass);
                if (_so == null)
                   continue;
                stationServerIDL.StationInterfaceOperations _self = (stationServerIDL.StationInterfaceOperations) _so.servant;
                try
                {
                    return _self.editRecord( badgeId,  lastName,  recordID,  status);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation transferRecord
     */
    public boolean transferRecord(String badgeID, String recordID, String remoteStationServerName)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("transferRecord",true);
                    _output.write_string(badgeID);
                    _output.write_string(recordID);
                    _output.write_string(remoteStationServerName);
                    _input = this._invoke(_output);
                    boolean _arg_ret = _input.read_boolean();
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("transferRecord",_opsClass);
                if (_so == null)
                   continue;
                stationServerIDL.StationInterfaceOperations _self = (stationServerIDL.StationInterfaceOperations) _so.servant;
                try
                {
                    return _self.transferRecord( badgeID,  recordID,  remoteStationServerName);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation transfer
     */
    public boolean transfer(stationServerIDL.Upcasted_Record theRecord)
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
                    boolean _arg_ret = _input.read_boolean();
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
