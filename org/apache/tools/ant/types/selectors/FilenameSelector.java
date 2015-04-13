package org.apache.tools.ant.types.selectors;

import java.io.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.util.regexp.*;

public class FilenameSelector extends BaseExtendSelector
{
    private String pattern;
    private String regex;
    private boolean casesensitive;
    private boolean negated;
    public static final String NAME_KEY = "name";
    public static final String CASE_KEY = "casesensitive";
    public static final String NEGATE_KEY = "negate";
    public static final String REGEX_KEY = "regex";
    private RegularExpression reg;
    private Regexp expression;
    
    public FilenameSelector() {
        super();
        this.pattern = null;
        this.regex = null;
        this.casesensitive = true;
        this.negated = false;
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{filenameselector name: ");
        if (this.pattern != null) {
            buf.append(this.pattern);
        }
        if (this.regex != null) {
            buf.append(this.regex).append(" [as regular expression]");
        }
        buf.append(" negate: ").append(this.negated);
        buf.append(" casesensitive: ").append(this.casesensitive);
        buf.append("}");
        return buf.toString();
    }
    
    public void setName(String pattern) {
        pattern = pattern.replace('/', File.separatorChar).replace('\\', File.separatorChar);
        if (pattern.endsWith(File.separator)) {
            pattern += "**";
        }
        this.pattern = pattern;
    }
    
    public void setRegex(final String pattern) {
        this.regex = pattern;
        this.reg = null;
    }
    
    public void setCasesensitive(final boolean casesensitive) {
        this.casesensitive = casesensitive;
    }
    
    public void setNegate(final boolean negated) {
        this.negated = negated;
    }
    
    public void setParameters(final Parameter[] parameters) {
        super.setParameters(parameters);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; ++i) {
                final String paramname = parameters[i].getName();
                if ("name".equalsIgnoreCase(paramname)) {
                    this.setName(parameters[i].getValue());
                }
                else if ("casesensitive".equalsIgnoreCase(paramname)) {
                    this.setCasesensitive(Project.toBoolean(parameters[i].getValue()));
                }
                else if ("negate".equalsIgnoreCase(paramname)) {
                    this.setNegate(Project.toBoolean(parameters[i].getValue()));
                }
                else if ("regex".equalsIgnoreCase(paramname)) {
                    this.setRegex(parameters[i].getValue());
                }
                else {
                    this.setError("Invalid parameter " + paramname);
                }
            }
        }
    }
    
    public void verifySettings() {
        if (this.pattern == null && this.regex == null) {
            this.setError("The name or regex attribute is required");
        }
        else if (this.pattern != null && this.regex != null) {
            this.setError("Only one of name and regex attribute is allowed");
        }
    }
    
    public boolean isSelected(final File basedir, final String filename, final File file) {
        this.validate();
        if (this.pattern != null) {
            return SelectorUtils.matchPath(this.pattern, filename, this.casesensitive) == !this.negated;
        }
        if (this.reg == null) {
            (this.reg = new RegularExpression()).setPattern(this.regex);
            this.expression = this.reg.getRegexp(this.getProject());
        }
        final int options = RegexpUtil.asOptions(this.casesensitive);
        return this.expression.matches(filename, options) == !this.negated;
    }
}
