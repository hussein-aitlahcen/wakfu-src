package org.apache.tools.ant.types;

import org.apache.tools.ant.*;

public class Substitution extends DataType
{
    public static final String DATA_TYPE_NAME = "substitution";
    private String expression;
    
    public Substitution() {
        super();
        this.expression = null;
    }
    
    public void setExpression(final String expression) {
        this.expression = expression;
    }
    
    public String getExpression(final Project p) {
        if (this.isReference()) {
            return this.getRef(p).getExpression(p);
        }
        return this.expression;
    }
    
    public Substitution getRef(final Project p) {
        return (Substitution)this.getCheckedRef(p);
    }
}
