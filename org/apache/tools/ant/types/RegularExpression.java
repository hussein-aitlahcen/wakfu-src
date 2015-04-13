package org.apache.tools.ant.types;

import org.apache.tools.ant.util.regexp.*;
import org.apache.tools.ant.*;

public class RegularExpression extends DataType
{
    public static final String DATA_TYPE_NAME = "regexp";
    private boolean alreadyInit;
    private static final RegexpFactory FACTORY;
    private Regexp regexp;
    private String myPattern;
    private boolean setPatternPending;
    
    public RegularExpression() {
        super();
        this.alreadyInit = false;
        this.regexp = null;
        this.setPatternPending = false;
    }
    
    private void init(final Project p) {
        if (!this.alreadyInit) {
            this.regexp = RegularExpression.FACTORY.newRegexp(p);
            this.alreadyInit = true;
        }
    }
    
    private void setPattern() {
        if (this.setPatternPending) {
            this.regexp.setPattern(this.myPattern);
            this.setPatternPending = false;
        }
    }
    
    public void setPattern(final String pattern) {
        if (this.regexp == null) {
            this.myPattern = pattern;
            this.setPatternPending = true;
        }
        else {
            this.regexp.setPattern(pattern);
        }
    }
    
    public String getPattern(final Project p) {
        this.init(p);
        if (this.isReference()) {
            return this.getRef(p).getPattern(p);
        }
        this.setPattern();
        return this.regexp.getPattern();
    }
    
    public Regexp getRegexp(final Project p) {
        this.init(p);
        if (this.isReference()) {
            return this.getRef(p).getRegexp(p);
        }
        this.setPattern();
        return this.regexp;
    }
    
    public RegularExpression getRef(final Project p) {
        return (RegularExpression)this.getCheckedRef(p);
    }
    
    static {
        FACTORY = new RegexpFactory();
    }
}
