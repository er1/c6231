package common.corba.recordInterfaceManager;

/** 
 * Helper class for : IRecordManager
 *  
 * @author OpenORB Compiler
 */ 
public class IRecordManagerHelper
{
    /**
     * Insert IRecordManager into an any
     * @param a an any
     * @param t IRecordManager value
     */
    public static void insert(org.omg.CORBA.Any a, common.corba.recordInterfaceManager.IRecordManager t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract IRecordManager from an any
     *
     * @param a an any
     * @return the extracted IRecordManager value
     */
    public static common.corba.recordInterfaceManager.IRecordManager extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return common.corba.recordInterfaceManager.IRecordManagerHelper.narrow( a.extract_Object() );
        }
        catch ( final org.omg.CORBA.BAD_PARAM e )
        {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the IRecordManager TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "IRecordManager" );
        }
        return _tc;
    }

    /**
     * Return the IRecordManager IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:common/corba/recordInterfaceManager/IRecordManager:1.0";

    /**
     * Read IRecordManager from a marshalled stream
     * @param istream the input stream
     * @return the readed IRecordManager value
     */
    public static common.corba.recordInterfaceManager.IRecordManager read(org.omg.CORBA.portable.InputStream istream)
    {
        return(common.corba.recordInterfaceManager.IRecordManager)istream.read_Object(common.corba.recordInterfaceManager._IRecordManagerStub.class);
    }

    /**
     * Write IRecordManager into a marshalled stream
     * @param ostream the output stream
     * @param value IRecordManager value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, common.corba.recordInterfaceManager.IRecordManager value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to IRecordManager
     * @param obj the CORBA Object
     * @return IRecordManager Object
     */
    public static IRecordManager narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof IRecordManager)
            return (IRecordManager)obj;

        if (obj._is_a(id()))
        {
            _IRecordManagerStub stub = new _IRecordManagerStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to IRecordManager
     * @param obj the CORBA Object
     * @return IRecordManager Object
     */
    public static IRecordManager unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof IRecordManager)
            return (IRecordManager)obj;

        _IRecordManagerStub stub = new _IRecordManagerStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
