package stationServerIDL;

/** 
 * Helper class for : StationInterface
 *  
 * @author OpenORB Compiler
 */ 
public class StationInterfaceHelper
{
    /**
     * Insert StationInterface into an any
     * @param a an any
     * @param t StationInterface value
     */
    public static void insert(org.omg.CORBA.Any a, stationServerIDL.StationInterface t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract StationInterface from an any
     *
     * @param a an any
     * @return the extracted StationInterface value
     */
    public static stationServerIDL.StationInterface extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return stationServerIDL.StationInterfaceHelper.narrow( a.extract_Object() );
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
     * Return the StationInterface TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "StationInterface" );
        }
        return _tc;
    }

    /**
     * Return the StationInterface IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:stationServerIDL/StationInterface:1.0";

    /**
     * Read StationInterface from a marshalled stream
     * @param istream the input stream
     * @return the readed StationInterface value
     */
    public static stationServerIDL.StationInterface read(org.omg.CORBA.portable.InputStream istream)
    {
        return(stationServerIDL.StationInterface)istream.read_Object(stationServerIDL._StationInterfaceStub.class);
    }

    /**
     * Write StationInterface into a marshalled stream
     * @param ostream the output stream
     * @param value StationInterface value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, stationServerIDL.StationInterface value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to StationInterface
     * @param obj the CORBA Object
     * @return StationInterface Object
     */
    public static StationInterface narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof StationInterface)
            return (StationInterface)obj;

        if (obj._is_a(id()))
        {
            _StationInterfaceStub stub = new _StationInterfaceStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to StationInterface
     * @param obj the CORBA Object
     * @return StationInterface Object
     */
    public static StationInterface unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof StationInterface)
            return (StationInterface)obj;

        _StationInterfaceStub stub = new _StationInterfaceStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}
