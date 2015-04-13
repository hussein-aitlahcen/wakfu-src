package com.ankamagames.framework.kernel.utils;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.io.*;
import org.apache.commons.lang3.*;
import java.util.regex.*;
import java.text.*;
import org.jetbrains.annotations.*;
import gnu.trove.*;
import java.util.*;

public abstract class StringUtils
{
    private static final DecimalFormat DECIMAL_FORMATTER_WITH_THOUSAND_SEPARATOR;
    private static final String HEX_DIGITS = "0123456789abcdef";
    private static final char DIESE = '\uff03';
    private static final char HORIZONTAL_ELLIPSIS = '\u2026';
    private static final char SPECIAL_QUOTE = '\u2018';
    private static final char INVERT_SPECIAL_QUOTE = '\u2019';
    private static final char SPECIAL_DOUBLE_QUOTE = '\u201c';
    private static final char INVERT_SPECIAL_DOUBLE_QUOTE = '\u201d';
    private static final char OPENING_HORIZONTAL_DOUBLE_QUOTE = '«';
    private static final char CLOSING_HORIZONTAL_DOUBLE_QUOTE = '»';
    private static final char HARD_SPACE = ' ';
    private static final String[] REPLACEMENT;
    private static final Pattern[] PATTERNS;
    
    public static byte[] toUTF8(final String s) {
        if (s == null) {
            return PrimitiveArrays.EMPTY_BYTE_ARRAY;
        }
        try {
            return s.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            return s.getBytes();
        }
    }
    
    public static String fromUTF8(final byte[] b) {
        return fromUTF8(b, 0, b.length);
    }
    
    @Nullable
    public static String fromUTF8(final byte[] b, final int offset, final int length) {
        if (b == null) {
            return null;
        }
        if (length == 0) {
            return "";
        }
        try {
            return new String(b, offset, length, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            return new String(b);
        }
    }
    
    public static String capitalize(final String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
    
    public static String capitalizeWords(final String s) {
        final char[] chars = s.trim().toLowerCase().toCharArray();
        boolean found = false;
        for (int charsCount = chars.length, i = 0; i < charsCount; ++i) {
            if (Character.isLetter(chars[i])) {
                if (!found) {
                    chars[i] = Character.toUpperCase(chars[i]);
                    found = true;
                }
            }
            else {
                found = false;
            }
        }
        return String.valueOf(chars);
    }
    
    public static String lowerCamelCase(final String s) {
        final StringBuilder sb = new StringBuilder();
        boolean continueWord = true;
        for (final char c : s.toLowerCase().toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                if (continueWord) {
                    sb.append(c);
                }
                else {
                    sb.append(Character.toUpperCase(c));
                    continueWord = true;
                }
            }
            else {
                continueWord = false;
            }
        }
        return sb.toString();
    }
    
    @Nullable
    public static String cleanSentence(final String sentence) {
        if (sentence == null) {
            return null;
        }
        String result = sentence.toLowerCase();
        result = org.apache.commons.lang3.StringUtils.stripAccents(result);
        return result.trim();
    }
    
    public static String intToStr(final int value, final int length) {
        return intToStr(String.valueOf(value), length);
    }
    
    public static String intToStr(final String value, final int length) {
        final StringBuffer buff = new StringBuffer(length);
        for (int i = 0; i < length - value.length(); ++i) {
            buff.append("0");
        }
        buff.append(value);
        return buff.toString();
    }
    
    public static String long2dateStr(final long timestamp) {
        return long2dateStr(timestamp, true);
    }
    
    public static String long2dateStr(final long timestamp, final boolean hours) {
        final Date date = new Date(timestamp);
        final String jour = date.getDate() + "/" + (date.getMonth() + 1) + "/" + (date.getYear() + 1900);
        if (!hours) {
            return jour;
        }
        final String heures = ((date.getHours() < 10) ? ("0" + date.getHours()) : date.getHours()) + ":" + ((date.getMinutes() < 10) ? ("0" + date.getMinutes()) : date.getMinutes());
        return jour + " \u00e0 " + heures;
    }
    
    public static boolean isEmptyOrNull(final String value) {
        return value == null || value.trim().length() == 0 || value.equals("null");
    }
    
    public static boolean isInteger(final String value) {
        return !isEmptyOrNull(value) && Pattern.matches("^[-]?\\d+$", value);
    }
    
    public static boolean isFloat(final String value) {
        return !isEmptyOrNull(value) && Pattern.matches("^[-]?[0-9.]+$", value);
    }
    
    public static int count(final char c, final String sentence) {
        int count = 0;
        for (int length = sentence.length(), i = 0; i < length; ++i) {
            if (sentence.charAt(i) == c) {
                ++count;
            }
        }
        return count;
    }
    
    public static int count(final char c, final char[] sentence) {
        int count = 0;
        for (int length = sentence.length, i = 0; i < length; ++i) {
            if (sentence[i] == c) {
                ++count;
            }
        }
        return count;
    }
    
    public static String incrementName(final String name) {
        final Pattern p = Pattern.compile("(.*)([0-9]+)");
        final Matcher matcher = p.matcher(name);
        if (!matcher.matches()) {
            return name + " 0";
        }
        final String n = matcher.group(2);
        final int value = Integer.parseInt(n);
        return matcher.group(1) + Integer.toString(value + 1);
    }
    
    public static boolean equals(final String s1, final String s2, final boolean emptyEqualsNull) {
        if (emptyEqualsNull) {
            if (isEmptyOrNull(s1)) {
                return isEmptyOrNull(s2);
            }
        }
        else {
            if (s1 == null) {
                return s2 == null;
            }
            if (s2 == null) {
                return false;
            }
        }
        return s1.equals(s2);
    }
    
    public static int compare(final String s1, final String s2) {
        if (s1 == null) {
            return (s2 == null) ? 0 : "".compareTo(s2);
        }
        return s1.compareTo((s2 == null) ? "" : s2);
    }
    
    public static int alphanumericCompare(final String s1, final String s2) {
        return AlphanumericComparator.INSTANCE.compare((CharSequence)s1, (CharSequence)s2);
    }
    
    public static String forXML(final String aText) {
        final StringBuilder result = new StringBuilder();
        final StringCharacterIterator iterator = new StringCharacterIterator(aText);
        for (char character = iterator.current(); character != '\uffff'; character = iterator.next()) {
            if (character == '<') {
                result.append("&lt;");
            }
            else if (character == '>') {
                result.append("&gt;");
            }
            else if (character == '\"') {
                result.append("&quot;");
            }
            else if (character == '\'') {
                result.append("&#039;");
            }
            else if (character == '&') {
                result.append("&amp;");
            }
            else {
                result.append(character);
            }
        }
        return result.toString();
    }
    
    public static String toSQLHexString(final byte[] v) {
        final StringBuffer sb = new StringBuffer(v.length * 2);
        for (int i = 0; i < v.length; ++i) {
            final int b = v[i] & 0xFF;
            sb.append("0123456789abcdef".charAt(b >>> 4)).append("0123456789abcdef".charAt(b & 0xF));
        }
        return sb.toString();
    }
    
    public static void appendSQLHexString(final StringBuilder sb, final byte[] v) {
        for (int i = 0; i < v.length; ++i) {
            final int b = v[i] & 0xFF;
            sb.append("0123456789abcdef".charAt(b >>> 4)).append("0123456789abcdef".charAt(b & 0xF));
        }
    }
    
    public static String withThousandSeparator(final long value) {
        return StringUtils.DECIMAL_FORMATTER_WITH_THOUSAND_SEPARATOR.format(value);
    }
    
    public static String withThousandSeparator(final int value) {
        return StringUtils.DECIMAL_FORMATTER_WITH_THOUSAND_SEPARATOR.format(value);
    }
    
    public static String withThousandSeparator(final short value) {
        return StringUtils.DECIMAL_FORMATTER_WITH_THOUSAND_SEPARATOR.format(value);
    }
    
    public static String toString(final TIntHashSet value) {
        if (value.size() == 0) {
            return "";
        }
        return toString(value.toArray());
    }
    
    public static String toString(final int[] value) {
        if (value.length == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length - 1; ++i) {
            sb.append(value[i]).append(",");
        }
        sb.append(value[value.length - 1]);
        return sb.toString();
    }
    
    public static String toString(final String[] value, final char separator) {
        if (value.length == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length - 1; ++i) {
            sb.append(value[i]).append(separator);
        }
        sb.append(value[value.length - 1]);
        return sb.toString();
    }
    
    public static int[] distinctIntArrayFromString(@NotNull final String list) {
        return new TIntHashSet(intArrayFromString(list)).toArray();
    }
    
    public static int[] intArrayFromString(@NotNull final String list) {
        final TIntArrayList result = new TIntArrayList();
        if (list.length() == 0) {
            return PrimitiveArrays.EMPTY_INT_ARRAY;
        }
        final String[] arr$;
        final String[] ids = arr$ = split(list, ',');
        for (final String id : arr$) {
            result.add(Integer.parseInt(id.trim()));
        }
        return result.toNativeArray();
    }
    
    public static String replaceVar(final String text, final String varName, final Object value) {
        final int index = text.indexOf(varName);
        return text.substring(0, index) + value + text.substring(index + varName.length());
    }
    
    public static String[] split(final String text, final String separator) {
        assert text != null;
        if (text.length() == 0) {
            return new String[] { "" };
        }
        return org.apache.commons.lang3.StringUtils.split(text, separator);
    }
    
    public static String[] split(final String text, final char separator) {
        assert text != null;
        if (text.length() == 0) {
            return new String[] { "" };
        }
        return org.apache.commons.lang3.StringUtils.split(text, separator);
    }
    
    public static String correctWordChars(final String text) {
        if (text == null) {
            return null;
        }
        String corrected = text;
        for (int i = 0; i < StringUtils.PATTERNS.length; ++i) {
            corrected = StringUtils.PATTERNS[i].matcher(corrected).replaceAll(StringUtils.REPLACEMENT[i]);
        }
        return corrected;
    }
    
    public static String truncate(final String inputString, final int encodedSize) {
        String truncatedString = inputString;
        do {
            final byte[] tmp = toUTF8(truncatedString);
            if (tmp.length <= encodedSize) {
                return truncatedString;
            }
            truncatedString = truncatedString.substring(0, truncatedString.length() - 1);
        } while (!truncatedString.isEmpty());
        return "";
    }
    
    public static void main(final String[] args) {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("x400");
        list.add("x15");
        list.add("x200a");
        list.add("x200");
        list.add("x20");
        list.add("x20b");
        list.add("x200a56");
        list.add("12");
        list.add("34");
        list.add("32f");
        list.add("ad21");
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(final String o1, final String o2) {
                return StringUtils.alphanumericCompare(o1, o2);
            }
        });
        for (final String s : list) {
            System.out.println(s);
        }
    }
    
    static {
        DECIMAL_FORMATTER_WITH_THOUSAND_SEPARATOR = new DecimalFormat("###,###");
        REPLACEMENT = new String[] { "'", "'", "\"", "\"", "", "'", "'", "\"", "\"", "...", " ", "#" };
        PATTERNS = new Pattern[] { Pattern.compile(String.valueOf('\u2018')), Pattern.compile(String.valueOf('\u2019')), Pattern.compile("«\\s*"), Pattern.compile("\\s*»"), Pattern.compile("[\n\r]+"), Pattern.compile("[\u2018]+"), Pattern.compile("[\u2019]+"), Pattern.compile("[\u201c]+"), Pattern.compile("[\u201d]+"), Pattern.compile("[\u2026]+"), Pattern.compile("[ ]+"), Pattern.compile("[\uff03]+") };
    }
    
    private static class AlphanumericComparator implements Comparator<CharSequence>
    {
        static final AlphanumericComparator INSTANCE;
        
        static int compareRight(final CharSequence a, final CharSequence b) {
            int bias = 0;
            int ia = 0;
            int ib = 0;
            while (true) {
                final char ca = charAt(a, ia);
                final char cb = charAt(b, ib);
                if (!Character.isDigit(ca) && !Character.isDigit(cb)) {
                    return bias;
                }
                if (!Character.isDigit(ca)) {
                    return -1;
                }
                if (!Character.isDigit(cb)) {
                    return 1;
                }
                if (ca < cb) {
                    if (bias == 0) {
                        bias = -1;
                    }
                }
                else if (ca > cb) {
                    if (bias == 0) {
                        bias = 1;
                    }
                }
                else if (ca == '\0' && cb == '\0') {
                    return bias;
                }
                ++ia;
                ++ib;
            }
        }
        
        @Override
        public int compare(final CharSequence o1, final CharSequence o2) {
            final String a = o1.toString();
            final String b = o2.toString();
            int ia = 0;
            int ib = 0;
            int nza = 0;
            int nzb = 0;
            while (true) {
                nzb = (nza = 0);
                char ca = charAt(a, ia);
                char cb = charAt(b, ib);
                while (Character.isSpaceChar(ca) || ca == '0') {
                    if (ca == '0') {
                        ++nza;
                    }
                    else {
                        nza = 0;
                    }
                    ca = charAt(a, ++ia);
                }
                while (Character.isSpaceChar(cb) || cb == '0') {
                    if (cb == '0') {
                        ++nzb;
                    }
                    else {
                        nzb = 0;
                    }
                    cb = charAt(b, ++ib);
                }
                final int result;
                if (Character.isDigit(ca) && Character.isDigit(cb) && (result = compareRight(a.substring(ia), b.substring(ib))) != 0) {
                    return result;
                }
                if (ca == '\0' && cb == '\0') {
                    return nza - nzb;
                }
                if (ca < cb) {
                    return -1;
                }
                if (ca > cb) {
                    return 1;
                }
                ++ia;
                ++ib;
            }
        }
        
        static char charAt(final CharSequence s, final int i) {
            return (i >= s.length()) ? '\0' : s.charAt(i);
        }
        
        static {
            INSTANCE = new AlphanumericComparator();
        }
    }
}
