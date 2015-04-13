package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.*;
import java.util.*;
import java.util.regex.*;

public class Jdk14RegexpMatcher implements RegexpMatcher
{
    private String pattern;
    
    public void setPattern(final String pattern) {
        this.pattern = pattern;
    }
    
    public String getPattern() {
        return this.pattern;
    }
    
    protected Pattern getCompiledPattern(final int options) throws BuildException {
        final int cOptions = this.getCompilerOptions(options);
        try {
            final Pattern p = Pattern.compile(this.pattern, cOptions);
            return p;
        }
        catch (PatternSyntaxException e) {
            throw new BuildException(e);
        }
    }
    
    public boolean matches(final String argument) throws BuildException {
        return this.matches(argument, 0);
    }
    
    public boolean matches(final String input, final int options) throws BuildException {
        try {
            final Pattern p = this.getCompiledPattern(options);
            return p.matcher(input).find();
        }
        catch (Exception e) {
            throw new BuildException(e);
        }
    }
    
    public Vector getGroups(final String argument) throws BuildException {
        return this.getGroups(argument, 0);
    }
    
    public Vector getGroups(final String input, final int options) throws BuildException {
        final Pattern p = this.getCompiledPattern(options);
        final Matcher matcher = p.matcher(input);
        if (!matcher.find()) {
            return null;
        }
        final Vector v = new Vector();
        for (int cnt = matcher.groupCount(), i = 0; i <= cnt; ++i) {
            String match = matcher.group(i);
            if (match == null) {
                match = "";
            }
            v.addElement(match);
        }
        return v;
    }
    
    protected int getCompilerOptions(final int options) {
        int cOptions = 1;
        if (RegexpUtil.hasFlag(options, 256)) {
            cOptions |= 0x2;
        }
        if (RegexpUtil.hasFlag(options, 4096)) {
            cOptions |= 0x8;
        }
        if (RegexpUtil.hasFlag(options, 65536)) {
            cOptions |= 0x20;
        }
        return cOptions;
    }
}
