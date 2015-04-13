package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.types.*;
import org.apache.tools.ant.*;
import java.io.*;

public abstract class BaseSelector extends DataType implements FileSelector
{
    private String errmsg;
    
    public BaseSelector() {
        super();
        this.errmsg = null;
    }
    
    public void setError(final String msg) {
        if (this.errmsg == null) {
            this.errmsg = msg;
        }
    }
    
    public String getError() {
        return this.errmsg;
    }
    
    public void verifySettings() {
        if (this.isReference()) {
            ((BaseSelector)this.getCheckedRef()).verifySettings();
        }
    }
    
    public void validate() {
        if (this.getError() == null) {
            this.verifySettings();
        }
        if (this.getError() != null) {
            throw new BuildException(this.errmsg);
        }
        if (!this.isReference()) {
            this.dieOnCircularReference();
        }
    }
    
    public abstract boolean isSelected(final File p0, final String p1, final File p2);
}
