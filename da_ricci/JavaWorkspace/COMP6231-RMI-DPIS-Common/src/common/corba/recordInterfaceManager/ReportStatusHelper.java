package common.corba.recordInterfaceManager;

/** 
 * Helper class for : ReportStatus
 *  
 * @author OpenORB Compiler
 */ 
public class ReportStatusHelper
{
    /**
     * Insert ReportStatus into an any
     * @param a an any
     * @param t ReportStatus value
     */
    public static void insert(org.omg.CORBA.Any a, common.corba.recordInterfaceManager.ReportStatus t)
    {
        a.type(type());
        write(a.create_output_stream(),t);
    }

    /**
     * Extract ReportStatus from an any
     *
     * @param a an any
     * @return the extracted ReportStatus value
     */
    public static common.corba.recordInterfaceManager.ReportStatus extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        return read( a.create_input_stream() );
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the ReportStatus TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            String []_members = new String[ 4 ];
            _members[ 0 ] = "FOUND";
            _members[ 1 ] = "MISSING";
            _members[ 2 ] = "CAPTURED";
            _members[ 3 ] = "ON_THE_RUN";
            _tc = orb.create_enum_tc( id(), "ReportStatus", _members );
        }
        return _tc;
    }

    /**
     * Return the ReportStatus IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:common/corba/recordInterfaceManager/ReportStatus:1.0";

    /**
     * Read ReportStatus from a marshalled stream
     * @param istream the input stream
     * @return the readed ReportStatus value
     */
    public static common.corba.recordInterfaceManager.ReportStatus read(org.omg.CORBA.portable.InputStream istream)
    {
        return ReportStatus.from_int(istream.read_ulong());
    }

    /**
     * Write ReportStatus into a marshalled stream
     * @param ostream the output stream
     * @param value ReportStatus value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, common.corba.recordInterfaceManager.ReportStatus value)
    {
        ostream.write_ulong(value.value());
    }

}
