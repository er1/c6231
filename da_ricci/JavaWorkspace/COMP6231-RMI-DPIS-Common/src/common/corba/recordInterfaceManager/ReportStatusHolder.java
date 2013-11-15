package common.corba.recordInterfaceManager;

/**
 * Holder class for : ReportStatus
 * 
 * @author OpenORB Compiler
 */
final public class ReportStatusHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal ReportStatus value
     */
    public common.corba.recordInterfaceManager.ReportStatus value;

    /**
     * Default constructor
     */
    public ReportStatusHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public ReportStatusHolder(common.corba.recordInterfaceManager.ReportStatus initial)
    {
        value = initial;
    }

    /**
     * Read ReportStatus from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = ReportStatusHelper.read(istream);
    }

    /**
     * Write ReportStatus into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        ReportStatusHelper.write(ostream,value);
    }

    /**
     * Return the ReportStatus TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return ReportStatusHelper.type();
    }

}
