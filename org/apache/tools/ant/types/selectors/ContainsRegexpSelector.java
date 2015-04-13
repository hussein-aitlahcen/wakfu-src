package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.types.resources.selectors.*;
import org.apache.tools.ant.types.resources.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.util.regexp.*;
import java.io.*;

public class ContainsRegexpSelector extends BaseExtendSelector implements ResourceSelector
{
    private String userProvidedExpression;
    private RegularExpression myRegExp;
    private Regexp myExpression;
    private boolean caseSensitive;
    private boolean multiLine;
    private boolean singleLine;
    public static final String EXPRESSION_KEY = "expression";
    private static final String CS_KEY = "casesensitive";
    private static final String ML_KEY = "multiline";
    private static final String SL_KEY = "singleline";
    
    public ContainsRegexpSelector() {
        super();
        this.userProvidedExpression = null;
        this.myRegExp = null;
        this.myExpression = null;
        this.caseSensitive = true;
        this.multiLine = false;
        this.singleLine = false;
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{containsregexpselector expression: ");
        buf.append(this.userProvidedExpression);
        buf.append("}");
        return buf.toString();
    }
    
    public void setExpression(final String theexpression) {
        this.userProvidedExpression = theexpression;
    }
    
    public void setCaseSensitive(final boolean b) {
        this.caseSensitive = b;
    }
    
    public void setMultiLine(final boolean b) {
        this.multiLine = b;
    }
    
    public void setSingleLine(final boolean b) {
        this.singleLine = b;
    }
    
    public void setParameters(final Parameter[] parameters) {
        super.setParameters(parameters);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; ++i) {
                final String paramname = parameters[i].getName();
                if ("expression".equalsIgnoreCase(paramname)) {
                    this.setExpression(parameters[i].getValue());
                }
                else if ("casesensitive".equalsIgnoreCase(paramname)) {
                    this.setCaseSensitive(Project.toBoolean(parameters[i].getValue()));
                }
                else if ("multiline".equalsIgnoreCase(paramname)) {
                    this.setMultiLine(Project.toBoolean(parameters[i].getValue()));
                }
                else if ("singleline".equalsIgnoreCase(paramname)) {
                    this.setSingleLine(Project.toBoolean(parameters[i].getValue()));
                }
                else {
                    this.setError("Invalid parameter " + paramname);
                }
            }
        }
    }
    
    public void verifySettings() {
        if (this.userProvidedExpression == null) {
            this.setError("The expression attribute is required");
        }
    }
    
    public boolean isSelected(final File basedir, final String filename, final File file) {
        return this.isSelected(new FileResource(file));
    }
    
    public boolean isSelected(final Resource r) {
        String teststr = null;
        BufferedReader in = null;
        this.validate();
        if (r.isDirectory()) {
            return true;
        }
        if (this.myRegExp == null) {
            (this.myRegExp = new RegularExpression()).setPattern(this.userProvidedExpression);
            this.myExpression = this.myRegExp.getRegexp(this.getProject());
        }
        try {
            in = new BufferedReader(new InputStreamReader(r.getInputStream()));
        }
        catch (Exception e) {
            throw new BuildException("Could not get InputStream from " + r.toLongString(), e);
        }
        try {
            for (teststr = in.readLine(); teststr != null; teststr = in.readLine()) {
                if (this.myExpression.matches(teststr, RegexpUtil.asOptions(this.caseSensitive, this.multiLine, this.singleLine))) {
                    return true;
                }
            }
            return false;
        }
        catch (IOException ioe) {
            throw new BuildException("Could not read " + r.toLongString());
        }
        finally {
            try {
                in.close();
            }
            catch (Exception e2) {
                throw new BuildException("Could not close " + r.toLongString());
            }
        }
    }
}
