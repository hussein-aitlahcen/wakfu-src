package org.apache.tools.ant.filters;

import java.util.*;
import java.io.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

public final class LineContains extends BaseParamFilterReader implements ChainableReader
{
    private static final String CONTAINS_KEY = "contains";
    private static final String NEGATE_KEY = "negate";
    private Vector<String> contains;
    private String line;
    private boolean negate;
    
    public LineContains() {
        super();
        this.contains = new Vector<String>();
        this.line = null;
        this.negate = false;
    }
    
    public LineContains(final Reader in) {
        super(in);
        this.contains = new Vector<String>();
        this.line = null;
        this.negate = false;
    }
    
    public int read() throws IOException {
        if (!this.getInitialized()) {
            this.initialize();
            this.setInitialized(true);
        }
        int ch = -1;
        if (this.line != null) {
            ch = this.line.charAt(0);
            if (this.line.length() == 1) {
                this.line = null;
            }
            else {
                this.line = this.line.substring(1);
            }
        }
        else {
            final int containsSize = this.contains.size();
            this.line = this.readLine();
            while (this.line != null) {
                boolean matches = true;
                String containsStr;
                for (int i = 0; matches && i < containsSize; matches = (this.line.indexOf(containsStr) >= 0), ++i) {
                    containsStr = this.contains.elementAt(i);
                }
                if (matches ^ this.isNegated()) {
                    break;
                }
                this.line = this.readLine();
            }
            if (this.line != null) {
                return this.read();
            }
        }
        return ch;
    }
    
    public void addConfiguredContains(final Contains contains) {
        this.contains.addElement(contains.getValue());
    }
    
    public void setNegate(final boolean b) {
        this.negate = b;
    }
    
    public boolean isNegated() {
        return this.negate;
    }
    
    private void setContains(final Vector<String> contains) {
        this.contains = contains;
    }
    
    private Vector<String> getContains() {
        return this.contains;
    }
    
    public Reader chain(final Reader rdr) {
        final LineContains newFilter = new LineContains(rdr);
        newFilter.setContains(this.getContains());
        newFilter.setNegate(this.isNegated());
        return newFilter;
    }
    
    private void initialize() {
        final Parameter[] params = this.getParameters();
        if (params != null) {
            for (int i = 0; i < params.length; ++i) {
                if ("contains".equals(params[i].getType())) {
                    this.contains.addElement(params[i].getValue());
                }
                else if ("negate".equals(params[i].getType())) {
                    this.setNegate(Project.toBoolean(params[i].getValue()));
                }
            }
        }
    }
    
    public static class Contains
    {
        private String value;
        
        public final void setValue(final String contains) {
            this.value = contains;
        }
        
        public final String getValue() {
            return this.value;
        }
    }
}
