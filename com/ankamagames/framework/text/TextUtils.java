package com.ankamagames.framework.text;

import com.ankamagames.framework.java.util.*;
import java.util.regex.*;

public class TextUtils
{
    private static final Pattern HTML_UNESCAPE_PATTERN;
    
    public static String unescapeHTML(final String text) {
        final StringBuffer sb = new StringBuffer();
        final Matcher matcher = TextUtils.HTML_UNESCAPE_PATTERN.matcher(text);
        int lastMatch = 0;
        while (matcher.find()) {
            final String charValue = matcher.group(1);
            final char character = (char)PrimitiveConverter.getInteger(charValue);
            matcher.appendReplacement(sb, String.valueOf(character));
            lastMatch = matcher.end();
        }
        sb.append(text.substring(lastMatch));
        return sb.toString();
    }
    
    static {
        HTML_UNESCAPE_PATTERN = Pattern.compile("&#([0-9]+);");
    }
}
