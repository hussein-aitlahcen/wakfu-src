package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.types.resources.selectors.*;
import org.apache.tools.ant.types.resources.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.*;
import java.io.*;
import org.apache.tools.ant.util.*;

public class ContainsSelector extends BaseExtendSelector implements ResourceSelector
{
    private String contains;
    private boolean casesensitive;
    private boolean ignorewhitespace;
    private String encoding;
    public static final String EXPRESSION_KEY = "expression";
    public static final String CONTAINS_KEY = "text";
    public static final String CASE_KEY = "casesensitive";
    public static final String WHITESPACE_KEY = "ignorewhitespace";
    
    public ContainsSelector() {
        super();
        this.contains = null;
        this.casesensitive = true;
        this.ignorewhitespace = false;
        this.encoding = null;
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{containsselector text: ");
        buf.append('\"').append(this.contains).append('\"');
        buf.append(" casesensitive: ");
        buf.append(this.casesensitive ? "true" : "false");
        buf.append(" ignorewhitespace: ");
        buf.append(this.ignorewhitespace ? "true" : "false");
        buf.append("}");
        return buf.toString();
    }
    
    public void setText(final String contains) {
        this.contains = contains;
    }
    
    public void setEncoding(final String encoding) {
        this.encoding = encoding;
    }
    
    public void setCasesensitive(final boolean casesensitive) {
        this.casesensitive = casesensitive;
    }
    
    public void setIgnorewhitespace(final boolean ignorewhitespace) {
        this.ignorewhitespace = ignorewhitespace;
    }
    
    public void setParameters(final Parameter[] parameters) {
        super.setParameters(parameters);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; ++i) {
                final String paramname = parameters[i].getName();
                if ("text".equalsIgnoreCase(paramname)) {
                    this.setText(parameters[i].getValue());
                }
                else if ("casesensitive".equalsIgnoreCase(paramname)) {
                    this.setCasesensitive(Project.toBoolean(parameters[i].getValue()));
                }
                else if ("ignorewhitespace".equalsIgnoreCase(paramname)) {
                    this.setIgnorewhitespace(Project.toBoolean(parameters[i].getValue()));
                }
                else {
                    this.setError("Invalid parameter " + paramname);
                }
            }
        }
    }
    
    public void verifySettings() {
        if (this.contains == null) {
            this.setError("The text attribute is required");
        }
    }
    
    public boolean isSelected(final File basedir, final String filename, final File file) {
        return this.isSelected(new FileResource(file));
    }
    
    public boolean isSelected(final Resource r) {
        this.validate();
        if (r.isDirectory() || this.contains.length() == 0) {
            return true;
        }
        String userstr = this.contains;
        if (!this.casesensitive) {
            userstr = this.contains.toLowerCase();
        }
        if (this.ignorewhitespace) {
            userstr = SelectorUtils.removeWhitespace(userstr);
        }
        BufferedReader in = null;
        try {
            if (this.encoding != null) {
                in = new BufferedReader(new InputStreamReader(r.getInputStream(), this.encoding));
            }
            else {
                in = new BufferedReader(new InputStreamReader(r.getInputStream()));
            }
        }
        catch (Exception e) {
            throw new BuildException("Could not get InputStream from " + r.toLongString(), e);
        }
        try {
            for (String teststr = in.readLine(); teststr != null; teststr = in.readLine()) {
                if (!this.casesensitive) {
                    teststr = teststr.toLowerCase();
                }
                if (this.ignorewhitespace) {
                    teststr = SelectorUtils.removeWhitespace(teststr);
                }
                if (teststr.indexOf(userstr) > -1) {
                    return true;
                }
            }
            return false;
        }
        catch (IOException ioe) {
            throw new BuildException("Could not read " + r.toLongString());
        }
        finally {
            FileUtils.close(in);
        }
    }
}
