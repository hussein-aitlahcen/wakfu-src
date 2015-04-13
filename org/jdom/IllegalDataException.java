package org.jdom;

public class IllegalDataException extends IllegalArgumentException
{
    private static final String CVS_ID = "@(#) $RCSfile: IllegalDataException.java,v $ $Revision: 1.13 $ $Date: 2004/02/06 09:28:30 $ $Name: jdom_1_0 $";
    
    public IllegalDataException(final String reason) {
        super(reason);
    }
    
    IllegalDataException(final String data, final String construct) {
        super("The data \"" + data + "\" is not legal for a JDOM " + construct + ".");
    }
    
    IllegalDataException(final String data, final String construct, final String reason) {
        super("The data \"" + data + "\" is not legal for a JDOM " + construct + ": " + reason + ".");
    }
}
