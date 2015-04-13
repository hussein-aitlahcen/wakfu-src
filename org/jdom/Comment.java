package org.jdom;

import org.jdom.output.*;

public class Comment extends Content
{
    private static final String CVS_ID = "@(#) $RCSfile: Comment.java,v $ $Revision: 1.32 $ $Date: 2004/02/11 21:12:43 $ $Name: jdom_1_0 $";
    protected String text;
    
    protected Comment() {
        super();
    }
    
    public Comment(final String text) {
        super();
        this.setText(text);
    }
    
    public String getText() {
        return this.text;
    }
    
    public String getValue() {
        return this.text;
    }
    
    public Comment setText(final String text) {
        final String reason;
        if ((reason = Verifier.checkCommentData(text)) != null) {
            throw new IllegalDataException(text, "comment", reason);
        }
        this.text = text;
        return this;
    }
    
    public String toString() {
        return "[Comment: " + new XMLOutputter().outputString(this) + "]";
    }
}
