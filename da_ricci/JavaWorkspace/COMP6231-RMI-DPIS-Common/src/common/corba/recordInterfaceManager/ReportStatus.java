package common.corba.recordInterfaceManager;

/**
 * Enum functionality that is used as reference to the report status
 * 
 * @author  Daniel Ricci <thedanny09 * @gmail .com>
 * 
 */
public final class ReportStatus implements org.omg.CORBA.portable.IDLEntity
{
    /**
     * Enum member FOUND value 
     */
    public static final int _FOUND = 0;

    /**
     * Enum member FOUND
     */
    public static final ReportStatus FOUND = new ReportStatus(_FOUND);

    /**
     * Enum member MISSING value 
     */
    public static final int _MISSING = 1;

    /**
     * Enum member MISSING
     */
    public static final ReportStatus MISSING = new ReportStatus(_MISSING);

    /**
     * Enum member CAPTURED value 
     */
    public static final int _CAPTURED = 2;

    /**
     * Enum member CAPTURED
     */
    public static final ReportStatus CAPTURED = new ReportStatus(_CAPTURED);

    /**
     * Enum member ON_THE_RUN value 
     */
    public static final int _ON_THE_RUN = 3;

    /**
     * Enum member ON_THE_RUN
     */
    public static final ReportStatus ON_THE_RUN = new ReportStatus(_ON_THE_RUN);

    /**
     * Internal member value 
     */
    private final int _ReportStatus_value;

    /**
     * Private constructor
     * @param  the enum value for this new member
     */
    private ReportStatus( final int value )
    {
        _ReportStatus_value = value;
    }

    /**
     * Maintains singleton property for serialized enums.
     * Issue 4271: IDL/Java issue, Mapping for IDL enum.
     */
    public java.lang.Object readResolve() throws java.io.ObjectStreamException
    {
        return from_int( value() );
    }

    /**
     * Return the internal member value
     * @return the member value
     */
    public int value()
    {
        return _ReportStatus_value;
    }

    /**
     * Return a enum member from its value.
     * @param value An enum value
     * @return An enum member
         */
    public static ReportStatus from_int( int value )
    {
        switch ( value )
        {
        case 0:
            return FOUND;
        case 1:
            return MISSING;
        case 2:
            return CAPTURED;
        case 3:
            return ON_THE_RUN;
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }

    /**
     * Return a string representation
     * @return a string representation of the enumeration
     */
    public java.lang.String toString()
    {
        switch ( _ReportStatus_value )
        {
        case 0:
            return "FOUND";
        case 1:
            return "MISSING";
        case 2:
            return "CAPTURED";
        case 3:
            return "ON_THE_RUN";
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }

}
