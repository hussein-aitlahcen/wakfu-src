package org.jdom;

public class CDATA extends Text
{
    private static final String CVS_ID = "@(#) $RCSfile: CDATA.java,v $ $Revision: 1.30 $ $Date: 2004/02/27 11:32:57 $ $Name: jdom_1_0 $";
    
    protected CDATA() {
        super();
    }
    
    public CDATA(final String str) {
        super();
        this.setText(str);
    }
    
    public void append(final String str) {
        if (str == null) {
            return;
        }
        final String reason;
        if ((reason = Verifier.checkCDATASection(str)) != null) {
            throw new IllegalDataException(str, "CDATA section", reason);
        }
        if (super.value == "") {
            super.value = str;
        }
        else {
            super.value = String.valueOf(super.value) + str;
        }
    }
    
    public Text setText(final String str) {
        if (str == null) {
            super.value = "";
            return this;
        }
        final String reason;
        if ((reason = Verifier.checkCDATASection(str)) != null) {
            throw new IllegalDataException(str, "CDATA section", reason);
        }
        super.value = str;
        return this;
    }
    
    public String toString() {
        return new StringBuffer(64).append("[CDATA: ").append(this.getText()).append("]").toString();
    }
}
