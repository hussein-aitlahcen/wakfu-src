package org.apache.tools.ant.filters;

import java.util.*;
import java.io.*;
import org.apache.tools.ant.util.regexp.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

public final class LineContainsRegExp extends BaseParamFilterReader implements ChainableReader
{
    private static final String REGEXP_KEY = "regexp";
    private static final String NEGATE_KEY = "negate";
    private static final String CS_KEY = "casesensitive";
    private Vector<RegularExpression> regexps;
    private String line;
    private boolean negate;
    private int regexpOptions;
    
    public LineContainsRegExp() {
        super();
        this.regexps = new Vector<RegularExpression>();
        this.line = null;
        this.negate = false;
        this.regexpOptions = 0;
    }
    
    public LineContainsRegExp(final Reader in) {
        super(in);
        this.regexps = new Vector<RegularExpression>();
        this.line = null;
        this.negate = false;
        this.regexpOptions = 0;
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
            final int regexpsSize = this.regexps.size();
            this.line = this.readLine();
            while (this.line != null) {
                boolean matches = true;
                Regexp re;
                for (int i = 0; matches && i < regexpsSize; matches = re.matches(this.line, this.regexpOptions), ++i) {
                    final RegularExpression regexp = this.regexps.elementAt(i);
                    re = regexp.getRegexp(this.getProject());
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
    
    public void addConfiguredRegexp(final RegularExpression regExp) {
        this.regexps.addElement(regExp);
    }
    
    private void setRegexps(final Vector<RegularExpression> regexps) {
        this.regexps = regexps;
    }
    
    private Vector<RegularExpression> getRegexps() {
        return this.regexps;
    }
    
    public Reader chain(final Reader rdr) {
        final LineContainsRegExp newFilter = new LineContainsRegExp(rdr);
        newFilter.setRegexps(this.getRegexps());
        newFilter.setNegate(this.isNegated());
        newFilter.setCaseSensitive(!RegexpUtil.hasFlag(this.regexpOptions, 256));
        return newFilter;
    }
    
    public void setNegate(final boolean b) {
        this.negate = b;
    }
    
    public void setCaseSensitive(final boolean b) {
        this.regexpOptions = RegexpUtil.asOptions(b);
    }
    
    public boolean isNegated() {
        return this.negate;
    }
    
    private void initialize() {
        final Parameter[] params = this.getParameters();
        if (params != null) {
            for (int i = 0; i < params.length; ++i) {
                if ("regexp".equals(params[i].getType())) {
                    final String pattern = params[i].getValue();
                    final RegularExpression regexp = new RegularExpression();
                    regexp.setPattern(pattern);
                    this.regexps.addElement(regexp);
                }
                else if ("negate".equals(params[i].getType())) {
                    this.setNegate(Project.toBoolean(params[i].getValue()));
                }
                else if ("casesensitive".equals(params[i].getType())) {
                    this.setCaseSensitive(Project.toBoolean(params[i].getValue()));
                }
            }
        }
    }
}
