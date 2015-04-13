package org.jdom;

public class IllegalNameException extends IllegalArgumentException
{
    private static final String CVS_ID = "@(#) $RCSfile: IllegalNameException.java,v $ $Revision: 1.13 $ $Date: 2004/02/06 09:28:30 $ $Name: jdom_1_0 $";
    
    public IllegalNameException(final String reason) {
        super(reason);
    }
    
    IllegalNameException(final String name, final String construct) {
        super("The name \"" + name + "\" is not legal for JDOM/XML " + construct + "s.");
    }
    
    IllegalNameException(final String name, final String construct, final String reason) {
        super("The name \"" + name + "\" is not legal for JDOM/XML " + construct + "s: " + reason + ".");
    }
}
