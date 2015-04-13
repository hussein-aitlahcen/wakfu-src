package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.types.*;
import org.apache.tools.ant.*;

public class IsReference extends ProjectComponent implements Condition
{
    private Reference ref;
    private String type;
    
    public void setRefid(final Reference r) {
        this.ref = r;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public boolean eval() throws BuildException {
        if (this.ref == null) {
            throw new BuildException("No reference specified for isreference condition");
        }
        final String key = this.ref.getRefId();
        if (!this.getProject().hasReference(key)) {
            return false;
        }
        if (this.type == null) {
            return true;
        }
        final Object o = this.getProject().getReference(key);
        Class typeClass = this.getProject().getDataTypeDefinitions().get(this.type);
        if (typeClass == null) {
            typeClass = this.getProject().getTaskDefinitions().get(this.type);
        }
        return typeClass != null && typeClass.isAssignableFrom(o.getClass());
    }
}
