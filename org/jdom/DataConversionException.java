package org.jdom;

public class DataConversionException extends JDOMException
{
    private static final String CVS_ID = "@(#) $RCSfile: DataConversionException.java,v $ $Revision: 1.13 $ $Date: 2004/02/06 09:28:30 $ $Name: jdom_1_0 $";
    
    public DataConversionException(final String name, final String dataType) {
        super("The XML construct " + name + " could not be converted to a " + dataType);
    }
}
