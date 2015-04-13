package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.*;

public class Contains implements Condition
{
    private String string;
    private String subString;
    private boolean caseSensitive;
    
    public Contains() {
        super();
        this.caseSensitive = true;
    }
    
    public void setString(final String string) {
        this.string = string;
    }
    
    public void setSubstring(final String subString) {
        this.subString = subString;
    }
    
    public void setCasesensitive(final boolean b) {
        this.caseSensitive = b;
    }
    
    public boolean eval() throws BuildException {
        if (this.string == null || this.subString == null) {
            throw new BuildException("both string and substring are required in contains");
        }
        return this.caseSensitive ? (this.string.indexOf(this.subString) > -1) : (this.string.toLowerCase().indexOf(this.subString.toLowerCase()) > -1);
    }
}
