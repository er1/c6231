package stationServerIDL;

/**
 * Holder class for : Upcasted_Record
 * 
 * @author OpenORB Compiler
 */
final public class Upcasted_RecordHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal Upcasted_Record value
     */
    public stationServerIDL.Upcasted_Record value;

    /**
     * Default constructor
     */
    public Upcasted_RecordHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public Upcasted_RecordHolder(stationServerIDL.Upcasted_Record initial)
    {
        value = initial;
    }

    /**
     * Read Upcasted_Record from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = Upcasted_RecordHelper.read(istream);
    }

    /**
     * Write Upcasted_Record into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        Upcasted_RecordHelper.write(ostream,value);
    }

    /**
     * Return the Upcasted_Record TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return Upcasted_RecordHelper.type();
    }

}
