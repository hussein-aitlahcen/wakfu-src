package org.apache.tools.ant.util.regexp;

import java.util.regex.*;
import org.apache.tools.ant.*;

public class Jdk14RegexpRegexp extends Jdk14RegexpMatcher implements Regexp
{
    private static final int DECIMAL = 10;
    
    protected int getSubsOptions(final int options) {
        int subsOptions = 1;
        if (RegexpUtil.hasFlag(options, 16)) {
            subsOptions = 16;
        }
        return subsOptions;
    }
    
    public String substitute(final String input, String argument, final int options) throws BuildException {
        final StringBuffer subst = new StringBuffer();
        for (int i = 0; i < argument.length(); ++i) {
            char c = argument.charAt(i);
            if (c == '$') {
                subst.append('\\');
                subst.append('$');
            }
            else if (c == '\\') {
                if (++i < argument.length()) {
                    c = argument.charAt(i);
                    final int value = Character.digit(c, 10);
                    if (value > -1) {
                        subst.append("$").append(value);
                    }
                    else {
                        subst.append(c);
                    }
                }
                else {
                    subst.append('\\');
                }
            }
            else {
                subst.append(c);
            }
        }
        argument = subst.toString();
        final int sOptions = this.getSubsOptions(options);
        final Pattern p = this.getCompiledPattern(options);
        final StringBuffer sb = new StringBuffer();
        final Matcher m = p.matcher(input);
        if (RegexpUtil.hasFlag(sOptions, 16)) {
            sb.append(m.replaceAll(argument));
        }
        else {
            final boolean res = m.find();
            if (res) {
                m.appendReplacement(sb, argument);
                m.appendTail(sb);
            }
            else {
                sb.append(input);
            }
        }
        return sb.toString();
    }
}
