package org.jdom;

public class Text extends Content
{
    private static final String CVS_ID = "@(#) $RCSfile: Text.java,v $ $Revision: 1.24 $ $Date: 2004/02/27 11:32:57 $ $Name: jdom_1_0 $";
    static final String EMPTY_STRING = "";
    protected String value;
    
    protected Text() {
        super();
    }
    
    public Text(final String str) {
        super();
        this.setText(str);
    }
    
    public void append(final String str) {
        if (str == null) {
            return;
        }
        final String reason;
        if ((reason = Verifier.checkCharacterData(str)) != null) {
            throw new IllegalDataException(str, "character content", reason);
        }
        if (str == "") {
            this.value = str;
        }
        else {
            this.value = String.valueOf(this.value) + str;
        }
    }
    
    public void append(final Text text) {
        if (text == null) {
            return;
        }
        this.value = String.valueOf(this.value) + text.getText();
    }
    
    public Object clone() {
        final Text text = (Text)super.clone();
        text.value = this.value;
        return text;
    }
    
    public String getText() {
        return this.value;
    }
    
    public String getTextNormalize() {
        return normalizeString(this.getText());
    }
    
    public String getTextTrim() {
        return this.getText().trim();
    }
    
    public String getValue() {
        return this.value;
    }
    
    public static String normalizeString(final String str) {
        if (str == null) {
            return "";
        }
        final char[] c = str.toCharArray();
        final char[] n = new char[c.length];
        boolean white = true;
        int pos = 0;
        for (int i = 0; i < c.length; ++i) {
            if (" \t\n\r".indexOf(c[i]) != -1) {
                if (!white) {
                    n[pos++] = ' ';
                    white = true;
                }
            }
            else {
                n[pos++] = c[i];
                white = false;
            }
        }
        if (white && pos > 0) {
            --pos;
        }
        return new String(n, 0, pos);
    }
    
    public Text setText(final String str) {
        if (str == null) {
            this.value = "";
            return this;
        }
        final String reason;
        if ((reason = Verifier.checkCharacterData(str)) != null) {
            throw new IllegalDataException(str, "character content", reason);
        }
        this.value = str;
        return this;
    }
    
    public String toString() {
        return new StringBuffer(64).append("[Text: ").append(this.getText()).append("]").toString();
    }
}
