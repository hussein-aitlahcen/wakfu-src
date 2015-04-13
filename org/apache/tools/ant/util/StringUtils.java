package org.apache.tools.ant.util;

import java.util.*;
import java.io.*;
import org.apache.tools.ant.*;

public final class StringUtils
{
    private static final long KILOBYTE = 1024L;
    private static final long MEGABYTE = 1048576L;
    private static final long GIGABYTE = 1073741824L;
    private static final long TERABYTE = 1099511627776L;
    private static final long PETABYTE = 1125899906842624L;
    public static final String LINE_SEP;
    
    public static Vector<String> lineSplit(final String data) {
        return split(data, 10);
    }
    
    public static Vector<String> split(final String data, final int ch) {
        final Vector<String> elems = new Vector<String>();
        int pos;
        int i;
        for (pos = -1, i = 0; (pos = data.indexOf(ch, i)) != -1; i = pos + 1) {
            final String elem = data.substring(i, pos);
            elems.addElement(elem);
        }
        elems.addElement(data.substring(i));
        return elems;
    }
    
    public static String replace(final String data, final String from, final String to) {
        return data.replace(from, to);
    }
    
    public static String getStackTrace(final Throwable t) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        pw.close();
        return sw.toString();
    }
    
    public static boolean endsWith(final StringBuffer buffer, final String suffix) {
        if (suffix.length() > buffer.length()) {
            return false;
        }
        int endIndex = suffix.length() - 1;
        int bufferIndex = buffer.length() - 1;
        while (endIndex >= 0) {
            if (buffer.charAt(bufferIndex) != suffix.charAt(endIndex)) {
                return false;
            }
            --bufferIndex;
            --endIndex;
        }
        return true;
    }
    
    public static String resolveBackSlash(final String input) {
        final StringBuffer b = new StringBuffer();
        boolean backSlashSeen = false;
        for (int i = 0; i < input.length(); ++i) {
            final char c = input.charAt(i);
            if (!backSlashSeen) {
                if (c == '\\') {
                    backSlashSeen = true;
                }
                else {
                    b.append(c);
                }
            }
            else {
                switch (c) {
                    case '\\': {
                        b.append('\\');
                        break;
                    }
                    case 'n': {
                        b.append('\n');
                        break;
                    }
                    case 'r': {
                        b.append('\r');
                        break;
                    }
                    case 't': {
                        b.append('\t');
                        break;
                    }
                    case 'f': {
                        b.append('\f');
                        break;
                    }
                    case 's': {
                        b.append(" \t\n\r\f");
                        break;
                    }
                    default: {
                        b.append(c);
                        break;
                    }
                }
                backSlashSeen = false;
            }
        }
        return b.toString();
    }
    
    public static long parseHumanSizes(String humanSize) throws Exception {
        long factor = 1L;
        final char s = humanSize.charAt(0);
        switch (s) {
            case '+': {
                humanSize = humanSize.substring(1);
                break;
            }
            case '-': {
                factor = -1L;
                humanSize = humanSize.substring(1);
                break;
            }
        }
        final char c = humanSize.charAt(humanSize.length() - 1);
        if (!Character.isDigit(c)) {
            int trim = 1;
            switch (c) {
                case 'K': {
                    factor *= 1024L;
                    break;
                }
                case 'M': {
                    factor *= 1048576L;
                    break;
                }
                case 'G': {
                    factor *= 1073741824L;
                    break;
                }
                case 'T': {
                    factor *= 1099511627776L;
                    break;
                }
                case 'P': {
                    factor *= 1125899906842624L;
                    break;
                }
                default: {
                    trim = 0;
                    break;
                }
            }
            humanSize = humanSize.substring(0, humanSize.length() - trim);
        }
        try {
            return factor * Long.parseLong(humanSize);
        }
        catch (NumberFormatException e) {
            throw new BuildException("Failed to parse \"" + humanSize + "\"", e);
        }
    }
    
    public static String removeSuffix(final String string, final String suffix) {
        if (string.endsWith(suffix)) {
            return string.substring(0, string.length() - suffix.length());
        }
        return string;
    }
    
    public static String removePrefix(final String string, final String prefix) {
        if (string.startsWith(prefix)) {
            return string.substring(prefix.length());
        }
        return string;
    }
    
    static {
        LINE_SEP = System.getProperty("line.separator");
    }
}
