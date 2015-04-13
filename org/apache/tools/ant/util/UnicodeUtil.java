package org.apache.tools.ant.util;

public class UnicodeUtil
{
    public static StringBuffer EscapeUnicode(final char ch) {
        final StringBuffer unicodeBuf = new StringBuffer("u0000");
        final String s = Integer.toHexString(ch);
        for (int i = 0; i < s.length(); ++i) {
            unicodeBuf.setCharAt(unicodeBuf.length() - s.length() + i, s.charAt(i));
        }
        return unicodeBuf;
    }
}
