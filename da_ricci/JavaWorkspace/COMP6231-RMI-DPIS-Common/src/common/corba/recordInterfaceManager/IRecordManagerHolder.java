package common.corba.recordInterfaceManager;

/**
 * Holder class for : IRecordManager
 * 
 * @author OpenORB Compiler
 */
final public class IRecordManagerHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal IRecordManager value
     */
    public common.corba.recordInterfaceManager.IRecordManager value;

    /**
     * Default constructor
     */
    public IRecordManagerHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public IRecordManagerHolder(common.corba.recordInterfaceManager.IRecordManager initial)
    {
        value = initial;
    }

    /**
     * Read IRecordManager from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = IRecordManagerHelper.read(istream);
    }

    /**
     * Write IRecordManager into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        IRecordManagerHelper.write(ostream,value);
    }

    /**
     * Return the IRecordManager TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return IRecordManagerHelper.type();
    }

}
