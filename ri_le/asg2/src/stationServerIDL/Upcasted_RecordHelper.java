package stationServerIDL;

/** 
 * Helper class for : Upcasted_Record
 *  
 * @author OpenORB Compiler
 */ 
public class Upcasted_RecordHelper
{
    private static final boolean HAS_OPENORB;
    static
    {
        boolean hasOpenORB = false;
        try
        {
            Thread.currentThread().getContextClassLoader().loadClass( "org.openorb.orb.core.Any" );
            hasOpenORB = true;
        }
        catch ( ClassNotFoundException ex )
        {
            // do nothing
        }
        HAS_OPENORB = hasOpenORB;
    }
    /**
     * Insert Upcasted_Record into an any
     * @param a an any
     * @param t Upcasted_Record value
     */
    public static void insert(org.omg.CORBA.Any a, stationServerIDL.Upcasted_Record t)
    {
        a.insert_Streamable(new stationServerIDL.Upcasted_RecordHolder(t));
    }

    /**
     * Extract Upcasted_Record from an any
     *
     * @param a an any
     * @return the extracted Upcasted_Record value
     */
    public static stationServerIDL.Upcasted_Record extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        // MANUAL MOFIDIFCATION
        // changed all org.openorb.orb.core.Any to org.omg.CORBA.Any
        if (HAS_OPENORB && a instanceof org.omg.CORBA.Any) {
            // streamable extraction. The jdk stubs incorrectly define the Any stub
        	org.omg.CORBA.Any any = (org.omg.CORBA.Any)a;
            try {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if ( s instanceof stationServerIDL.Upcasted_RecordHolder )
                    return ( ( stationServerIDL.Upcasted_RecordHolder ) s ).value;
            }
            catch ( org.omg.CORBA.BAD_INV_ORDER ex )
            {
            }
            stationServerIDL.Upcasted_RecordHolder h = new stationServerIDL.Upcasted_RecordHolder( read( a.create_input_stream() ) );
            a.insert_Streamable( h );
            return h.value;
        }
        return read( a.create_input_stream() );
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;
    private static boolean _working = false;

    /**
     * Return the Upcasted_Record TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            synchronized(org.omg.CORBA.TypeCode.class) {
                if (_tc != null)
                    return _tc;
                if (_working)
                    return org.omg.CORBA.ORB.init().create_recursive_tc(id());
                _working = true;
                org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
                org.omg.CORBA.StructMember _members[] = new org.omg.CORBA.StructMember[ 8 ];

                _members[ 0 ] = new org.omg.CORBA.StructMember();
                _members[ 0 ].name = "RecordID";
                _members[ 0 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 1 ] = new org.omg.CORBA.StructMember();
                _members[ 1 ].name = "lastName";
                _members[ 1 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 2 ] = new org.omg.CORBA.StructMember();
                _members[ 2 ].name = "firstName";
                _members[ 2 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 3 ] = new org.omg.CORBA.StructMember();
                _members[ 3 ].name = "status";
                _members[ 3 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 4 ] = new org.omg.CORBA.StructMember();
                _members[ 4 ].name = "description";
                _members[ 4 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 5 ] = new org.omg.CORBA.StructMember();
                _members[ 5 ].name = "address";
                _members[ 5 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 6 ] = new org.omg.CORBA.StructMember();
                _members[ 6 ].name = "lastDate";
                _members[ 6 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _members[ 7 ] = new org.omg.CORBA.StructMember();
                _members[ 7 ].name = "lastLocation";
                _members[ 7 ].type = orb.get_primitive_tc( org.omg.CORBA.TCKind.tk_string );
                _tc = orb.create_struct_tc( id(), "Upcasted_Record", _members );
                _working = false;
            }
        }
        return _tc;
    }

    /**
     * Return the Upcasted_Record IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:stationServerIDL/Upcasted_Record:1.0";

    /**
     * Read Upcasted_Record from a marshalled stream
     * @param istream the input stream
     * @return the readed Upcasted_Record value
     */
    public static stationServerIDL.Upcasted_Record read(org.omg.CORBA.portable.InputStream istream)
    {
        stationServerIDL.Upcasted_Record new_one = new stationServerIDL.Upcasted_Record();

        new_one.RecordID = istream.read_string();
        new_one.lastName = istream.read_string();
        new_one.firstName = istream.read_string();
        new_one.status = istream.read_string();
        new_one.description = istream.read_string();
        new_one.address = istream.read_string();
        new_one.lastDate = istream.read_string();
        new_one.lastLocation = istream.read_string();

        return new_one;
    }

    /**
     * Write Upcasted_Record into a marshalled stream
     * @param ostream the output stream
     * @param value Upcasted_Record value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, stationServerIDL.Upcasted_Record value)
    {
        ostream.write_string( value.RecordID );
        ostream.write_string( value.lastName );
        ostream.write_string( value.firstName );
        ostream.write_string( value.status );
        ostream.write_string( value.description );
        ostream.write_string( value.address );
        ostream.write_string( value.lastDate );
        ostream.write_string( value.lastLocation );
    }

}
