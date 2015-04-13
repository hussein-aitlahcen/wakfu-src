package org.jdom;

import org.jdom.output.*;

public class DocType extends Content
{
    private static final String CVS_ID = "@(#) $RCSfile: DocType.java,v $ $Revision: 1.31 $ $Date: 2004/02/27 11:32:57 $ $Name: jdom_1_0 $";
    protected String elementName;
    protected String publicID;
    protected String systemID;
    protected String internalSubset;
    
    protected DocType() {
        super();
    }
    
    public DocType(final String elementName) {
        this(elementName, null, null);
    }
    
    public DocType(final String elementName, final String systemID) {
        this(elementName, null, systemID);
    }
    
    public DocType(final String elementName, final String publicID, final String systemID) {
        super();
        this.setElementName(elementName);
        this.setPublicID(publicID);
        this.setSystemID(systemID);
    }
    
    public String getElementName() {
        return this.elementName;
    }
    
    public String getInternalSubset() {
        return this.internalSubset;
    }
    
    public String getPublicID() {
        return this.publicID;
    }
    
    public String getSystemID() {
        return this.systemID;
    }
    
    public String getValue() {
        return "";
    }
    
    public DocType setElementName(final String elementName) {
        final String reason = Verifier.checkXMLName(elementName);
        if (reason != null) {
            throw new IllegalNameException(elementName, "DocType", reason);
        }
        this.elementName = elementName;
        return this;
    }
    
    public void setInternalSubset(final String newData) {
        this.internalSubset = newData;
    }
    
    public DocType setPublicID(final String publicID) {
        final String reason = Verifier.checkPublicID(publicID);
        if (reason != null) {
            throw new IllegalDataException(publicID, "DocType", reason);
        }
        this.publicID = publicID;
        return this;
    }
    
    public DocType setSystemID(final String systemID) {
        final String reason = Verifier.checkSystemLiteral(systemID);
        if (reason != null) {
            throw new IllegalDataException(systemID, "DocType", reason);
        }
        this.systemID = systemID;
        return this;
    }
    
    public String toString() {
        return "[DocType: " + new XMLOutputter().outputString(this) + "]";
    }
}
