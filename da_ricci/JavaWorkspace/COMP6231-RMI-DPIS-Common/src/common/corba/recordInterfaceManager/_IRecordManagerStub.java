package common.corba.recordInterfaceManager;

/**
 * Interface definition for the remote functionality being given by the server to the client
 * 
 * @author  Daniel Ricci <thedanny09 * @gmail .com>
 * 
 */
public class _IRecordManagerStub extends org.omg.CORBA.portable.ObjectImpl
        implements IRecordManager
{
    static final String[] _ids_list =
    {
        "IDL:common/corba/recordInterfaceManager/IRecordManager:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = common.corba.recordInterfaceManager.IRecordManagerOperations.class;

    /**
     * Operation createCRecord
     */
    public String createCRecord(String badgeId, String firstName, String lastName, String description, common.corba.recordInterfaceManager.ReportStatus status)
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
                    common.corba.recordInterfaceManager.ReportStatusHelper.write(_output,status);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("createCRecord",_opsClass);
                if (_so == null)
                   continue;
                common.corba.recordInterfaceManager.IRecordManagerOperations _self = (common.corba.recordInterfaceManager.IRecordManagerOperations) _so.servant;
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
    public String createMRecord(String badgeId, String firstName, String lastName, String address, long lastDate, String lastLocation, common.corba.recordInterfaceManager.ReportStatus status)
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
                    _output.write_longlong(lastDate);
                    _output.write_string(lastLocation);
                    common.corba.recordInterfaceManager.ReportStatusHelper.write(_output,status);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("createMRecord",_opsClass);
                if (_so == null)
                   continue;
                common.corba.recordInterfaceManager.IRecordManagerOperations _self = (common.corba.recordInterfaceManager.IRecordManagerOperations) _so.servant;
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
     * Operation editRecord
     */
    public boolean editRecord(String badgeId, String lastName, String recordId, common.corba.recordInterfaceManager.ReportStatus status)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("editRecord",true);
                    _output.write_string(badgeId);
                    _output.write_string(lastName);
                    _output.write_string(recordId);
                    common.corba.recordInterfaceManager.ReportStatusHelper.write(_output,status);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("editRecord",_opsClass);
                if (_so == null)
                   continue;
                common.corba.recordInterfaceManager.IRecordManagerOperations _self = (common.corba.recordInterfaceManager.IRecordManagerOperations) _so.servant;
                try
                {
                    return _self.editRecord( badgeId,  lastName,  recordId,  status);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getRecordCount
     */
    public String getRecordCount(String badgeId)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getRecordCount",true);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getRecordCount",_opsClass);
                if (_so == null)
                   continue;
                common.corba.recordInterfaceManager.IRecordManagerOperations _self = (common.corba.recordInterfaceManager.IRecordManagerOperations) _so.servant;
                try
                {
                    return _self.getRecordCount( badgeId);
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
    public String transferRecord(String badgeId, String recordId, String stationName)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("transferRecord",true);
                    _output.write_string(badgeId);
                    _output.write_string(recordId);
                    _output.write_string(stationName);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("transferRecord",_opsClass);
                if (_so == null)
                   continue;
                common.corba.recordInterfaceManager.IRecordManagerOperations _self = (common.corba.recordInterfaceManager.IRecordManagerOperations) _so.servant;
                try
                {
                    return _self.transferRecord( badgeId,  recordId,  stationName);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

}
