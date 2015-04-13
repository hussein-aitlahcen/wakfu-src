package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.*;

public class Equals implements Condition
{
    private static final int REQUIRED = 3;
    private Object arg1;
    private Object arg2;
    private boolean trim;
    private boolean caseSensitive;
    private int args;
    private boolean forcestring;
    
    public Equals() {
        super();
        this.trim = false;
        this.caseSensitive = true;
        this.forcestring = false;
    }
    
    public void setArg1(final Object arg1) {
        if (arg1 instanceof String) {
            this.setArg1((String)arg1);
        }
        else {
            this.setArg1Internal(arg1);
        }
    }
    
    public void setArg1(final String a1) {
        this.setArg1Internal(a1);
    }
    
    private void setArg1Internal(final Object arg1) {
        this.arg1 = arg1;
        this.args |= 0x1;
    }
    
    public void setArg2(final Object arg2) {
        if (arg2 instanceof String) {
            this.setArg2((String)arg2);
        }
        else {
            this.setArg2Internal(arg2);
        }
    }
    
    public void setArg2(final String a2) {
        this.setArg2Internal(a2);
    }
    
    private void setArg2Internal(final Object arg2) {
        this.arg2 = arg2;
        this.args |= 0x2;
    }
    
    public void setTrim(final boolean b) {
        this.trim = b;
    }
    
    public void setCasesensitive(final boolean b) {
        this.caseSensitive = b;
    }
    
    public void setForcestring(final boolean forcestring) {
        this.forcestring = forcestring;
    }
    
    public boolean eval() throws BuildException {
        if ((this.args & 0x3) != 0x3) {
            throw new BuildException("both arg1 and arg2 are required in equals");
        }
        if (this.arg1 == this.arg2 || (this.arg1 != null && this.arg1.equals(this.arg2))) {
            return true;
        }
        if (this.forcestring) {
            this.arg1 = ((this.arg1 == null || this.arg1 instanceof String) ? this.arg1 : this.arg1.toString());
            this.arg2 = ((this.arg2 == null || this.arg2 instanceof String) ? this.arg2 : this.arg2.toString());
        }
        if (this.arg1 instanceof String && this.trim) {
            this.arg1 = ((String)this.arg1).trim();
        }
        if (this.arg2 instanceof String && this.trim) {
            this.arg2 = ((String)this.arg2).trim();
        }
        if (this.arg1 instanceof String && this.arg2 instanceof String) {
            final String s1 = (String)this.arg1;
            final String s2 = (String)this.arg2;
            return this.caseSensitive ? s1.equals(s2) : s1.equalsIgnoreCase(s2);
        }
        return false;
    }
}
