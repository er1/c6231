package stationServerIDL;

/**
 * Holder class for : StationInterface
 * 
 * @author OpenORB Compiler
 */
final public class StationInterfaceHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal StationInterface value
     */
    public stationServerIDL.StationInterface value;

    /**
     * Default constructor
     */
    public StationInterfaceHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public StationInterfaceHolder(stationServerIDL.StationInterface initial)
    {
        value = initial;
    }

    /**
     * Read StationInterface from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = StationInterfaceHelper.read(istream);
    }

    /**
     * Write StationInterface into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        StationInterfaceHelper.write(ostream,value);
    }

    /**
     * Return the StationInterface TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return StationInterfaceHelper.type();
    }

}
