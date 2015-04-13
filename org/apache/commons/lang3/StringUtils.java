package org.apache.commons.lang3;

import java.util.regex.*;
import java.lang.reflect.*;
import java.util.*;

public class StringUtils
{
    public static final String EMPTY = "";
    public static final int INDEX_NOT_FOUND = -1;
    private static final int PAD_LIMIT = 8192;
    private static final Pattern WHITESPACE_BLOCK;
    private static boolean sunAvailable;
    private static Method sunDecomposeMethod;
    private static final Pattern sunPattern;
    private static boolean java6Available;
    private static Method java6NormalizeMethod;
    private static Object java6NormalizerFormNFD;
    private static final Pattern java6Pattern;
    
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
    
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }
    
    public static boolean isBlank(final CharSequence cs) {
        final int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }
    
    public static String trim(final String str) {
        return (str == null) ? null : str.trim();
    }
    
    public static String trimToNull(final String str) {
        final String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }
    
    public static String trimToEmpty(final String str) {
        return (str == null) ? "" : str.trim();
    }
    
    public static String strip(final String str) {
        return strip(str, null);
    }
    
    public static String stripToNull(String str) {
        if (str == null) {
            return null;
        }
        str = strip(str, null);
        return (str.length() == 0) ? null : str;
    }
    
    public static String stripToEmpty(final String str) {
        return (str == null) ? "" : strip(str, null);
    }
    
    public static String strip(String str, final String stripChars) {
        if (isEmpty(str)) {
            return str;
        }
        str = stripStart(str, stripChars);
        return stripEnd(str, stripChars);
    }
    
    public static String stripStart(final String str, final String stripChars) {
        final int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        int start = 0;
        if (stripChars == null) {
            while (start != strLen && Character.isWhitespace(str.charAt(start))) {
                ++start;
            }
        }
        else {
            if (stripChars.length() == 0) {
                return str;
            }
            while (start != strLen && stripChars.indexOf(str.charAt(start)) != -1) {
                ++start;
            }
        }
        return str.substring(start);
    }
    
    public static String stripEnd(final String str, final String stripChars) {
        int end;
        if (str == null || (end = str.length()) == 0) {
            return str;
        }
        if (stripChars == null) {
            while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
                --end;
            }
        }
        else {
            if (stripChars.length() == 0) {
                return str;
            }
            while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
                --end;
            }
        }
        return str.substring(0, end);
    }
    
    public static String[] stripAll(final String... strs) {
        return stripAll(strs, null);
    }
    
    public static String[] stripAll(final String[] strs, final String stripChars) {
        final int strsLen;
        if (strs == null || (strsLen = strs.length) == 0) {
            return strs;
        }
        final String[] newArr = new String[strsLen];
        for (int i = 0; i < strsLen; ++i) {
            newArr[i] = strip(strs[i], stripChars);
        }
        return newArr;
    }
    
    public static String stripAccents(final String input) {
        if (input == null) {
            return null;
        }
        try {
            String result = null;
            if (StringUtils.java6Available) {
                result = removeAccentsJava6(input);
            }
            else {
                if (!StringUtils.sunAvailable) {
                    throw new UnsupportedOperationException("The stripAccents(CharSequence) method requires at least Java 1.6 or a Sun JVM");
                }
                result = removeAccentsSUN(input);
            }
            return result;
        }
        catch (IllegalArgumentException iae) {
            throw new RuntimeException("IllegalArgumentException occurred", iae);
        }
        catch (IllegalAccessException iae2) {
            throw new RuntimeException("IllegalAccessException occurred", iae2);
        }
        catch (InvocationTargetException ite) {
            throw new RuntimeException("InvocationTargetException occurred", ite);
        }
        catch (SecurityException se) {
            throw new RuntimeException("SecurityException occurred", se);
        }
    }
    
    private static String removeAccentsJava6(final CharSequence text) throws IllegalAccessException, InvocationTargetException {
        if (!StringUtils.java6Available || StringUtils.java6NormalizerFormNFD == null) {
            throw new IllegalStateException("java.text.Normalizer is not available");
        }
        String result = (String)StringUtils.java6NormalizeMethod.invoke(null, text, StringUtils.java6NormalizerFormNFD);
        result = StringUtils.java6Pattern.matcher(result).replaceAll("");
        return result;
    }
    
    private static String removeAccentsSUN(final CharSequence text) throws IllegalAccessException, InvocationTargetException {
        if (!StringUtils.sunAvailable) {
            throw new IllegalStateException("sun.text.Normalizer is not available");
        }
        String result = (String)StringUtils.sunDecomposeMethod.invoke(null, text, Boolean.FALSE, 0);
        result = StringUtils.sunPattern.matcher(result).replaceAll("");
        return result;
    }
    
    public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        return (cs1 == null) ? (cs2 == null) : cs1.equals(cs2);
    }
    
    public static boolean equalsIgnoreCase(final CharSequence str1, final CharSequence str2) {
        if (str1 == null || str2 == null) {
            return str1 == str2;
        }
        return CharSequenceUtils.regionMatches(str1, true, 0, str2, 0, Math.max(str1.length(), str2.length()));
    }
    
    public static int indexOf(final CharSequence seq, final int searchChar) {
        if (isEmpty(seq)) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchChar, 0);
    }
    
    public static int indexOf(final CharSequence seq, final int searchChar, final int startPos) {
        if (isEmpty(seq)) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchChar, startPos);
    }
    
    public static int indexOf(final CharSequence seq, final CharSequence searchSeq) {
        if (seq == null || searchSeq == null) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchSeq, 0);
    }
    
    public static int indexOf(final CharSequence seq, final CharSequence searchSeq, final int startPos) {
        if (seq == null || searchSeq == null) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchSeq, startPos);
    }
    
    public static int ordinalIndexOf(final CharSequence str, final CharSequence searchStr, final int ordinal) {
        return ordinalIndexOf(str, searchStr, ordinal, false);
    }
    
    private static int ordinalIndexOf(final CharSequence str, final CharSequence searchStr, final int ordinal, final boolean lastIndex) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return lastIndex ? str.length() : 0;
        }
        int found = 0;
        int index = lastIndex ? str.length() : -1;
        do {
            if (lastIndex) {
                index = CharSequenceUtils.lastIndexOf(str, searchStr, index - 1);
            }
            else {
                index = CharSequenceUtils.indexOf(str, searchStr, index + 1);
            }
            if (index < 0) {
                return index;
            }
        } while (++found < ordinal);
        return index;
    }
    
    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
        return indexOfIgnoreCase(str, searchStr, 0);
    }
    
    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        if (startPos < 0) {
            startPos = 0;
        }
        final int endLimit = str.length() - searchStr.length() + 1;
        if (startPos > endLimit) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return startPos;
        }
        for (int i = startPos; i < endLimit; ++i) {
            if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
                return i;
            }
        }
        return -1;
    }
    
    public static int lastIndexOf(final CharSequence seq, final int searchChar) {
        if (isEmpty(seq)) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(seq, searchChar, seq.length());
    }
    
    public static int lastIndexOf(final CharSequence seq, final int searchChar, final int startPos) {
        if (isEmpty(seq)) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(seq, searchChar, startPos);
    }
    
    public static int lastIndexOf(final CharSequence seq, final CharSequence searchSeq) {
        if (seq == null || searchSeq == null) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(seq, searchSeq, seq.length());
    }
    
    public static int lastOrdinalIndexOf(final CharSequence str, final CharSequence searchStr, final int ordinal) {
        return ordinalIndexOf(str, searchStr, ordinal, true);
    }
    
    public static int lastIndexOf(final CharSequence seq, final CharSequence searchSeq, final int startPos) {
        if (seq == null || searchSeq == null) {
            return -1;
        }
        return CharSequenceUtils.lastIndexOf(seq, searchSeq, startPos);
    }
    
    public static int lastIndexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return lastIndexOfIgnoreCase(str, searchStr, str.length());
    }
    
    public static int lastIndexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return -1;
        }
        if (startPos > str.length() - searchStr.length()) {
            startPos = str.length() - searchStr.length();
        }
        if (startPos < 0) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return startPos;
        }
        for (int i = startPos; i >= 0; --i) {
            if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
                return i;
            }
        }
        return -1;
    }
    
    public static boolean contains(final CharSequence seq, final int searchChar) {
        return !isEmpty(seq) && CharSequenceUtils.indexOf(seq, searchChar, 0) >= 0;
    }
    
    public static boolean contains(final CharSequence seq, final CharSequence searchSeq) {
        return seq != null && searchSeq != null && CharSequenceUtils.indexOf(seq, searchSeq, 0) >= 0;
    }
    
    public static boolean containsIgnoreCase(final CharSequence str, final CharSequence searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        final int len = searchStr.length();
        for (int max = str.length() - len, i = 0; i <= max; ++i) {
            if (CharSequenceUtils.regionMatches(str, true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean containsWhitespace(final CharSequence seq) {
        if (isEmpty(seq)) {
            return false;
        }
        for (int strLen = seq.length(), i = 0; i < strLen; ++i) {
            if (Character.isWhitespace(seq.charAt(i))) {
                return true;
            }
        }
        return false;
    }
    
    public static int indexOfAny(final CharSequence cs, final char... searchChars) {
        if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
            return -1;
        }
        final int csLen = cs.length();
        final int csLast = csLen - 1;
        final int searchLen = searchChars.length;
        final int searchLast = searchLen - 1;
        for (int i = 0; i < csLen; ++i) {
            final char ch = cs.charAt(i);
            for (int j = 0; j < searchLen; ++j) {
                if (searchChars[j] == ch) {
                    if (i >= csLast || j >= searchLast || !Character.isHighSurrogate(ch)) {
                        return i;
                    }
                    if (searchChars[j + 1] == cs.charAt(i + 1)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    
    public static int indexOfAny(final CharSequence cs, final String searchChars) {
        if (isEmpty(cs) || isEmpty(searchChars)) {
            return -1;
        }
        return indexOfAny(cs, searchChars.toCharArray());
    }
    
    public static boolean containsAny(final CharSequence cs, final char... searchChars) {
        if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
            return false;
        }
        final int csLength = cs.length();
        final int searchLength = searchChars.length;
        final int csLast = csLength - 1;
        final int searchLast = searchLength - 1;
        for (int i = 0; i < csLength; ++i) {
            final char ch = cs.charAt(i);
            for (int j = 0; j < searchLength; ++j) {
                if (searchChars[j] == ch) {
                    if (!Character.isHighSurrogate(ch)) {
                        return true;
                    }
                    if (j == searchLast) {
                        return true;
                    }
                    if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean containsAny(final CharSequence cs, final CharSequence searchChars) {
        return searchChars != null && containsAny(cs, CharSequenceUtils.toCharArray(searchChars));
    }
    
    public static int indexOfAnyBut(final CharSequence cs, final char... searchChars) {
        if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
            return -1;
        }
        final int csLen = cs.length();
        final int csLast = csLen - 1;
        final int searchLen = searchChars.length;
        final int searchLast = searchLen - 1;
        int i = 0;
    Label_0040:
        while (i < csLen) {
            final char ch = cs.charAt(i);
            for (int j = 0; j < searchLen; ++j) {
                if (searchChars[j] == ch && (i >= csLast || j >= searchLast || !Character.isHighSurrogate(ch) || searchChars[j + 1] == cs.charAt(i + 1))) {
                    ++i;
                    continue Label_0040;
                }
            }
            return i;
        }
        return -1;
    }
    
    public static int indexOfAnyBut(final CharSequence seq, final CharSequence searchChars) {
        if (isEmpty(seq) || isEmpty(searchChars)) {
            return -1;
        }
        for (int strLen = seq.length(), i = 0; i < strLen; ++i) {
            final char ch = seq.charAt(i);
            final boolean chFound = CharSequenceUtils.indexOf(searchChars, ch, 0) >= 0;
            if (i + 1 < strLen && Character.isHighSurrogate(ch)) {
                final char ch2 = seq.charAt(i + 1);
                if (chFound && CharSequenceUtils.indexOf(searchChars, ch2, 0) < 0) {
                    return i;
                }
            }
            else if (!chFound) {
                return i;
            }
        }
        return -1;
    }
    
    public static boolean containsOnly(final CharSequence cs, final char... valid) {
        return valid != null && cs != null && (cs.length() == 0 || (valid.length != 0 && indexOfAnyBut(cs, valid) == -1));
    }
    
    public static boolean containsOnly(final CharSequence cs, final String validChars) {
        return cs != null && validChars != null && containsOnly(cs, validChars.toCharArray());
    }
    
    public static boolean containsNone(final CharSequence cs, final char... searchChars) {
        if (cs == null || searchChars == null) {
            return true;
        }
        final int csLen = cs.length();
        final int csLast = csLen - 1;
        final int searchLen = searchChars.length;
        final int searchLast = searchLen - 1;
        for (int i = 0; i < csLen; ++i) {
            final char ch = cs.charAt(i);
            for (int j = 0; j < searchLen; ++j) {
                if (searchChars[j] == ch) {
                    if (!Character.isHighSurrogate(ch)) {
                        return false;
                    }
                    if (j == searchLast) {
                        return false;
                    }
                    if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public static boolean containsNone(final CharSequence cs, final String invalidChars) {
        return cs == null || invalidChars == null || containsNone(cs, invalidChars.toCharArray());
    }
    
    public static int indexOfAny(final CharSequence str, final CharSequence... searchStrs) {
        if (str == null || searchStrs == null) {
            return -1;
        }
        final int sz = searchStrs.length;
        int ret = Integer.MAX_VALUE;
        int tmp = 0;
        for (final CharSequence search : searchStrs) {
            if (search != null) {
                tmp = CharSequenceUtils.indexOf(str, search, 0);
                if (tmp != -1) {
                    if (tmp < ret) {
                        ret = tmp;
                    }
                }
            }
        }
        return (ret == Integer.MAX_VALUE) ? -1 : ret;
    }
    
    public static int lastIndexOfAny(final CharSequence str, final CharSequence... searchStrs) {
        if (str == null || searchStrs == null) {
            return -1;
        }
        final int sz = searchStrs.length;
        int ret = -1;
        int tmp = 0;
        for (final CharSequence search : searchStrs) {
            if (search != null) {
                tmp = CharSequenceUtils.lastIndexOf(str, search, str.length());
                if (tmp > ret) {
                    ret = tmp;
                }
            }
        }
        return ret;
    }
    
    public static String substring(final String str, int start) {
        if (str == null) {
            return null;
        }
        if (start < 0) {
            start += str.length();
        }
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return "";
        }
        return str.substring(start);
    }
    
    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return null;
        }
        if (end < 0) {
            end += str.length();
        }
        if (start < 0) {
            start += str.length();
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return "";
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        return str.substring(start, end);
    }
    
    public static String left(final String str, final int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }
    
    public static String right(final String str, final int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }
    
    public static String mid(final String str, int pos, final int len) {
        if (str == null) {
            return null;
        }
        if (len < 0 || pos > str.length()) {
            return "";
        }
        if (pos < 0) {
            pos = 0;
        }
        if (str.length() <= pos + len) {
            return str.substring(pos);
        }
        return str.substring(pos, pos + len);
    }
    
    public static String substringBefore(final String str, final String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.length() == 0) {
            return "";
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }
    
    public static String substringAfter(final String str, final String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return "";
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return "";
        }
        return str.substring(pos + separator.length());
    }
    
    public static String substringBeforeLast(final String str, final String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        final int pos = str.lastIndexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }
    
    public static String substringAfterLast(final String str, final String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (isEmpty(separator)) {
            return "";
        }
        final int pos = str.lastIndexOf(separator);
        if (pos == -1 || pos == str.length() - separator.length()) {
            return "";
        }
        return str.substring(pos + separator.length());
    }
    
    public static String substringBetween(final String str, final String tag) {
        return substringBetween(str, tag, tag);
    }
    
    public static String substringBetween(final String str, final String open, final String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        final int start = str.indexOf(open);
        if (start != -1) {
            final int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }
    
    public static String[] substringsBetween(final String str, final String open, final String close) {
        if (str == null || isEmpty(open) || isEmpty(close)) {
            return null;
        }
        final int strLen = str.length();
        if (strLen == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        final int closeLen = close.length();
        final int openLen = open.length();
        final List<String> list = new ArrayList<String>();
        int end;
        for (int pos = 0; pos < strLen - closeLen; pos = end + closeLen) {
            int start = str.indexOf(open, pos);
            if (start < 0) {
                break;
            }
            start += openLen;
            end = str.indexOf(close, start);
            if (end < 0) {
                break;
            }
            list.add(str.substring(start, end));
        }
        if (list.isEmpty()) {
            return null;
        }
        return list.toArray(new String[list.size()]);
    }
    
    public static String[] split(final String str) {
        return split(str, null, -1);
    }
    
    public static String[] split(final String str, final char separatorChar) {
        return splitWorker(str, separatorChar, false);
    }
    
    public static String[] split(final String str, final String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }
    
    public static String[] split(final String str, final String separatorChars, final int max) {
        return splitWorker(str, separatorChars, max, false);
    }
    
    public static String[] splitByWholeSeparator(final String str, final String separator) {
        return splitByWholeSeparatorWorker(str, separator, -1, false);
    }
    
    public static String[] splitByWholeSeparator(final String str, final String separator, final int max) {
        return splitByWholeSeparatorWorker(str, separator, max, false);
    }
    
    public static String[] splitByWholeSeparatorPreserveAllTokens(final String str, final String separator) {
        return splitByWholeSeparatorWorker(str, separator, -1, true);
    }
    
    public static String[] splitByWholeSeparatorPreserveAllTokens(final String str, final String separator, final int max) {
        return splitByWholeSeparatorWorker(str, separator, max, true);
    }
    
    private static String[] splitByWholeSeparatorWorker(final String str, final String separator, final int max, final boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        if (separator == null || "".equals(separator)) {
            return splitWorker(str, null, max, preserveAllTokens);
        }
        final int separatorLength = separator.length();
        final ArrayList<String> substrings = new ArrayList<String>();
        int numberOfSubstrings = 0;
        int beg = 0;
        int end = 0;
        while (end < len) {
            end = str.indexOf(separator, beg);
            if (end > -1) {
                if (end > beg) {
                    if (++numberOfSubstrings == max) {
                        end = len;
                        substrings.add(str.substring(beg));
                    }
                    else {
                        substrings.add(str.substring(beg, end));
                        beg = end + separatorLength;
                    }
                }
                else {
                    if (preserveAllTokens) {
                        if (++numberOfSubstrings == max) {
                            end = len;
                            substrings.add(str.substring(beg));
                        }
                        else {
                            substrings.add("");
                        }
                    }
                    beg = end + separatorLength;
                }
            }
            else {
                substrings.add(str.substring(beg));
                end = len;
            }
        }
        return substrings.toArray(new String[substrings.size()]);
    }
    
    public static String[] splitPreserveAllTokens(final String str) {
        return splitWorker(str, null, -1, true);
    }
    
    public static String[] splitPreserveAllTokens(final String str, final char separatorChar) {
        return splitWorker(str, separatorChar, true);
    }
    
    private static String[] splitWorker(final String str, final char separatorChar, final boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        final List<String> list = new ArrayList<String>();
        int i = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
            }
            else {
                lastMatch = false;
                match = true;
                ++i;
            }
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return list.toArray(new String[list.size()]);
    }
    
    public static String[] splitPreserveAllTokens(final String str, final String separatorChars) {
        return splitWorker(str, separatorChars, -1, true);
    }
    
    public static String[] splitPreserveAllTokens(final String str, final String separatorChars, final int max) {
        return splitWorker(str, separatorChars, max, true);
    }
    
    private static String[] splitWorker(final String str, final String separatorChars, final int max, final boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        final List<String> list = new ArrayList<String>();
        int sizePlus1 = 1;
        int i = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                }
                else {
                    lastMatch = false;
                    match = true;
                    ++i;
                }
            }
        }
        else if (separatorChars.length() == 1) {
            final char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                }
                else {
                    lastMatch = false;
                    match = true;
                    ++i;
                }
            }
        }
        else {
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                }
                else {
                    lastMatch = false;
                    match = true;
                    ++i;
                }
            }
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return list.toArray(new String[list.size()]);
    }
    
    public static String[] splitByCharacterType(final String str) {
        return splitByCharacterType(str, false);
    }
    
    public static String[] splitByCharacterTypeCamelCase(final String str) {
        return splitByCharacterType(str, true);
    }
    
    private static String[] splitByCharacterType(final String str, final boolean camelCase) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        final char[] c = str.toCharArray();
        final List<String> list = new ArrayList<String>();
        int tokenStart = 0;
        int currentType = Character.getType(c[tokenStart]);
        for (int pos = tokenStart + 1; pos < c.length; ++pos) {
            final int type = Character.getType(c[pos]);
            if (type != currentType) {
                if (camelCase && type == 2 && currentType == 1) {
                    final int newTokenStart = pos - 1;
                    if (newTokenStart != tokenStart) {
                        list.add(new String(c, tokenStart, newTokenStart - tokenStart));
                        tokenStart = newTokenStart;
                    }
                }
                else {
                    list.add(new String(c, tokenStart, pos - tokenStart));
                    tokenStart = pos;
                }
                currentType = type;
            }
        }
        list.add(new String(c, tokenStart, c.length - tokenStart));
        return list.toArray(new String[list.size()]);
    }
    
    public static <T> String join(final T... elements) {
        return join((Object[])elements, null);
    }
    
    public static String join(final Object[] array, final char separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }
    
    public static String join(final Object[] array, final char separator, final int startIndex, final int endIndex) {
        if (array == null) {
            return null;
        }
        final int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return "";
        }
        final StringBuilder buf = new StringBuilder(noOfItems * 16);
        for (int i = startIndex; i < endIndex; ++i) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
    
    public static String join(final Object[] array, final String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }
    
    public static String join(final Object[] array, String separator, final int startIndex, final int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }
        final int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return "";
        }
        final StringBuilder buf = new StringBuilder(noOfItems * 16);
        for (int i = startIndex; i < endIndex; ++i) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
    
    public static String join(final Iterator<?> iterator, final char separator) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        final Object first = iterator.next();
        if (!iterator.hasNext()) {
            return ObjectUtils.toString(first);
        }
        final StringBuilder buf = new StringBuilder(256);
        if (first != null) {
            buf.append(first);
        }
        while (iterator.hasNext()) {
            buf.append(separator);
            final Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }
    
    public static String join(final Iterator<?> iterator, final String separator) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        final Object first = iterator.next();
        if (!iterator.hasNext()) {
            return ObjectUtils.toString(first);
        }
        final StringBuilder buf = new StringBuilder(256);
        if (first != null) {
            buf.append(first);
        }
        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            final Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }
    
    public static String join(final Iterable<?> iterable, final char separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }
    
    public static String join(final Iterable<?> iterable, final String separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }
    
    public static String deleteWhitespace(final String str) {
        if (isEmpty(str)) {
            return str;
        }
        final int sz = str.length();
        final char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }
        return new String(chs, 0, count);
    }
    
    public static String removeStart(final String str, final String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (str.startsWith(remove)) {
            return str.substring(remove.length());
        }
        return str;
    }
    
    public static String removeStartIgnoreCase(final String str, final String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (startsWithIgnoreCase(str, remove)) {
            return str.substring(remove.length());
        }
        return str;
    }
    
    public static String removeEnd(final String str, final String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }
    
    public static String removeEndIgnoreCase(final String str, final String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (endsWithIgnoreCase(str, remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }
    
    public static String remove(final String str, final String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        return replace(str, remove, "", -1);
    }
    
    public static String remove(final String str, final char remove) {
        if (isEmpty(str) || str.indexOf(remove) == -1) {
            return str;
        }
        final char[] chars = str.toCharArray();
        int pos = 0;
        for (int i = 0; i < chars.length; ++i) {
            if (chars[i] != remove) {
                chars[pos++] = chars[i];
            }
        }
        return new String(chars, 0, pos);
    }
    
    public static String replaceOnce(final String text, final String searchString, final String replacement) {
        return replace(text, searchString, replacement, 1);
    }
    
    public static String replace(final String text, final String searchString, final String replacement) {
        return replace(text, searchString, replacement, -1);
    }
    
    public static String replace(final String text, final String searchString, final String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = ((increase < 0) ? 0 : increase);
        increase *= ((max < 0) ? 16 : ((max > 64) ? 64 : max));
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }
    
    public static String replaceEach(final String text, final String[] searchList, final String[] replacementList) {
        return replaceEach(text, searchList, replacementList, false, 0);
    }
    
    public static String replaceEachRepeatedly(final String text, final String[] searchList, final String[] replacementList) {
        final int timeToLive = (searchList == null) ? 0 : searchList.length;
        return replaceEach(text, searchList, replacementList, true, timeToLive);
    }
    
    private static String replaceEach(final String text, final String[] searchList, final String[] replacementList, final boolean repeat, final int timeToLive) {
        if (text == null || text.length() == 0 || searchList == null || searchList.length == 0 || replacementList == null || replacementList.length == 0) {
            return text;
        }
        if (timeToLive < 0) {
            throw new IllegalStateException("Aborting to protect against StackOverflowError - output of one loop is the input of another");
        }
        final int searchLength = searchList.length;
        final int replacementLength = replacementList.length;
        if (searchLength != replacementLength) {
            throw new IllegalArgumentException("Search and Replace array lengths don't match: " + searchLength + " vs " + replacementLength);
        }
        final boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
        int textIndex = -1;
        int replaceIndex = -1;
        int tempIndex = -1;
        for (int i = 0; i < searchLength; ++i) {
            if (!noMoreMatchesForReplIndex[i] && searchList[i] != null && searchList[i].length() != 0) {
                if (replacementList[i] != null) {
                    tempIndex = text.indexOf(searchList[i]);
                    if (tempIndex == -1) {
                        noMoreMatchesForReplIndex[i] = true;
                    }
                    else if (textIndex == -1 || tempIndex < textIndex) {
                        textIndex = tempIndex;
                        replaceIndex = i;
                    }
                }
            }
        }
        if (textIndex == -1) {
            return text;
        }
        int start = 0;
        int increase = 0;
        for (int j = 0; j < searchList.length; ++j) {
            if (searchList[j] != null) {
                if (replacementList[j] != null) {
                    final int greater = replacementList[j].length() - searchList[j].length();
                    if (greater > 0) {
                        increase += 3 * greater;
                    }
                }
            }
        }
        increase = Math.min(increase, text.length() / 5);
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (textIndex != -1) {
            for (int k = start; k < textIndex; ++k) {
                buf.append(text.charAt(k));
            }
            buf.append(replacementList[replaceIndex]);
            start = textIndex + searchList[replaceIndex].length();
            textIndex = -1;
            replaceIndex = -1;
            tempIndex = -1;
            for (int k = 0; k < searchLength; ++k) {
                if (!noMoreMatchesForReplIndex[k] && searchList[k] != null && searchList[k].length() != 0) {
                    if (replacementList[k] != null) {
                        tempIndex = text.indexOf(searchList[k], start);
                        if (tempIndex == -1) {
                            noMoreMatchesForReplIndex[k] = true;
                        }
                        else if (textIndex == -1 || tempIndex < textIndex) {
                            textIndex = tempIndex;
                            replaceIndex = k;
                        }
                    }
                }
            }
        }
        for (int textLength = text.length(), l = start; l < textLength; ++l) {
            buf.append(text.charAt(l));
        }
        final String result = buf.toString();
        if (!repeat) {
            return result;
        }
        return replaceEach(result, searchList, replacementList, repeat, timeToLive - 1);
    }
    
    public static String replaceChars(final String str, final char searchChar, final char replaceChar) {
        if (str == null) {
            return null;
        }
        return str.replace(searchChar, replaceChar);
    }
    
    public static String replaceChars(final String str, final String searchChars, String replaceChars) {
        if (isEmpty(str) || isEmpty(searchChars)) {
            return str;
        }
        if (replaceChars == null) {
            replaceChars = "";
        }
        boolean modified = false;
        final int replaceCharsLength = replaceChars.length();
        final int strLength = str.length();
        final StringBuilder buf = new StringBuilder(strLength);
        for (int i = 0; i < strLength; ++i) {
            final char ch = str.charAt(i);
            final int index = searchChars.indexOf(ch);
            if (index >= 0) {
                modified = true;
                if (index < replaceCharsLength) {
                    buf.append(replaceChars.charAt(index));
                }
            }
            else {
                buf.append(ch);
            }
        }
        if (modified) {
            return buf.toString();
        }
        return str;
    }
    
    public static String overlay(final String str, String overlay, int start, int end) {
        if (str == null) {
            return null;
        }
        if (overlay == null) {
            overlay = "";
        }
        final int len = str.length();
        if (start < 0) {
            start = 0;
        }
        if (start > len) {
            start = len;
        }
        if (end < 0) {
            end = 0;
        }
        if (end > len) {
            end = len;
        }
        if (start > end) {
            final int temp = start;
            start = end;
            end = temp;
        }
        return new StringBuilder(len + start - end + overlay.length() + 1).append(str.substring(0, start)).append(overlay).append(str.substring(end)).toString();
    }
    
    public static String chomp(final String str) {
        if (isEmpty(str)) {
            return str;
        }
        if (str.length() != 1) {
            int lastIdx = str.length() - 1;
            final char last = str.charAt(lastIdx);
            if (last == '\n') {
                if (str.charAt(lastIdx - 1) == '\r') {
                    --lastIdx;
                }
            }
            else if (last != '\r') {
                ++lastIdx;
            }
            return str.substring(0, lastIdx);
        }
        final char ch = str.charAt(0);
        if (ch == '\r' || ch == '\n') {
            return "";
        }
        return str;
    }
    
    public static String chomp(final String str, final String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (str.endsWith(separator)) {
            return str.substring(0, str.length() - separator.length());
        }
        return str;
    }
    
    public static String chop(final String str) {
        if (str == null) {
            return null;
        }
        final int strLen = str.length();
        if (strLen < 2) {
            return "";
        }
        final int lastIdx = strLen - 1;
        final String ret = str.substring(0, lastIdx);
        final char last = str.charAt(lastIdx);
        if (last == '\n' && ret.charAt(lastIdx - 1) == '\r') {
            return ret.substring(0, lastIdx - 1);
        }
        return ret;
    }
    
    public static String repeat(final String str, final int repeat) {
        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return "";
        }
        final int inputLength = str.length();
        if (repeat == 1 || inputLength == 0) {
            return str;
        }
        if (inputLength == 1 && repeat <= 8192) {
            return repeat(str.charAt(0), repeat);
        }
        final int outputLength = inputLength * repeat;
        switch (inputLength) {
            case 1: {
                return repeat(str.charAt(0), repeat);
            }
            case 2: {
                final char ch0 = str.charAt(0);
                final char ch = str.charAt(1);
                final char[] output2 = new char[outputLength];
                for (int i = repeat * 2 - 2; i >= 0; --i, --i) {
                    output2[i] = ch0;
                    output2[i + 1] = ch;
                }
                return new String(output2);
            }
            default: {
                final StringBuilder buf = new StringBuilder(outputLength);
                for (int j = 0; j < repeat; ++j) {
                    buf.append(str);
                }
                return buf.toString();
            }
        }
    }
    
    public static String repeat(final String str, final String separator, final int repeat) {
        if (str == null || separator == null) {
            return repeat(str, repeat);
        }
        final String result = repeat(str + separator, repeat);
        return removeEnd(result, separator);
    }
    
    public static String repeat(final char ch, final int repeat) {
        final char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; --i) {
            buf[i] = ch;
        }
        return new String(buf);
    }
    
    public static String rightPad(final String str, final int size) {
        return rightPad(str, size, ' ');
    }
    
    public static String rightPad(final String str, final int size, final char padChar) {
        if (str == null) {
            return null;
        }
        final int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (pads > 8192) {
            return rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(repeat(padChar, pads));
    }
    
    public static String rightPad(final String str, final int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        final int padLen = padStr.length();
        final int strLen = str.length();
        final int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        if (padLen == 1 && pads <= 8192) {
            return rightPad(str, size, padStr.charAt(0));
        }
        if (pads == padLen) {
            return str.concat(padStr);
        }
        if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        }
        final char[] padding = new char[pads];
        final char[] padChars = padStr.toCharArray();
        for (int i = 0; i < pads; ++i) {
            padding[i] = padChars[i % padLen];
        }
        return str.concat(new String(padding));
    }
    
    public static String leftPad(final String str, final int size) {
        return leftPad(str, size, ' ');
    }
    
    public static String leftPad(final String str, final int size, final char padChar) {
        if (str == null) {
            return null;
        }
        final int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (pads > 8192) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return repeat(padChar, pads).concat(str);
    }
    
    public static String leftPad(final String str, final int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        final int padLen = padStr.length();
        final int strLen = str.length();
        final int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        if (padLen == 1 && pads <= 8192) {
            return leftPad(str, size, padStr.charAt(0));
        }
        if (pads == padLen) {
            return padStr.concat(str);
        }
        if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        }
        final char[] padding = new char[pads];
        final char[] padChars = padStr.toCharArray();
        for (int i = 0; i < pads; ++i) {
            padding[i] = padChars[i % padLen];
        }
        return new String(padding).concat(str);
    }
    
    public static int length(final CharSequence cs) {
        return (cs == null) ? 0 : cs.length();
    }
    
    public static String center(final String str, final int size) {
        return center(str, size, ' ');
    }
    
    public static String center(String str, final int size, final char padChar) {
        if (str == null || size <= 0) {
            return str;
        }
        final int strLen = str.length();
        final int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        str = leftPad(str, strLen + pads / 2, padChar);
        str = rightPad(str, size, padChar);
        return str;
    }
    
    public static String center(String str, final int size, String padStr) {
        if (str == null || size <= 0) {
            return str;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        final int strLen = str.length();
        final int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        str = leftPad(str, strLen + pads / 2, padStr);
        str = rightPad(str, size, padStr);
        return str;
    }
    
    public static String upperCase(final String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }
    
    public static String upperCase(final String str, final Locale locale) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase(locale);
    }
    
    public static String lowerCase(final String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }
    
    public static String lowerCase(final String str, final Locale locale) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase(locale);
    }
    
    public static String capitalize(final String str) {
        final int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString();
    }
    
    public static String uncapitalize(final String str) {
        final int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
    }
    
    public static String swapCase(final String str) {
        if (isEmpty(str)) {
            return str;
        }
        final char[] buffer = str.toCharArray();
        for (int i = 0; i < buffer.length; ++i) {
            final char ch = buffer[i];
            if (Character.isUpperCase(ch)) {
                buffer[i] = Character.toLowerCase(ch);
            }
            else if (Character.isTitleCase(ch)) {
                buffer[i] = Character.toLowerCase(ch);
            }
            else if (Character.isLowerCase(ch)) {
                buffer[i] = Character.toUpperCase(ch);
            }
        }
        return new String(buffer);
    }
    
    public static int countMatches(final CharSequence str, final CharSequence sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        for (int idx = 0; (idx = CharSequenceUtils.indexOf(str, sub, idx)) != -1; idx += sub.length()) {
            ++count;
        }
        return count;
    }
    
    public static boolean isAlpha(final CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        for (int sz = cs.length(), i = 0; i < sz; ++i) {
            if (!Character.isLetter(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isAlphaSpace(final CharSequence cs) {
        if (cs == null) {
            return false;
        }
        for (int sz = cs.length(), i = 0; i < sz; ++i) {
            if (!Character.isLetter(cs.charAt(i)) && cs.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isAlphanumeric(final CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        for (int sz = cs.length(), i = 0; i < sz; ++i) {
            if (!Character.isLetterOrDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isAlphanumericSpace(final CharSequence cs) {
        if (cs == null) {
            return false;
        }
        for (int sz = cs.length(), i = 0; i < sz; ++i) {
            if (!Character.isLetterOrDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isAsciiPrintable(final CharSequence cs) {
        if (cs == null) {
            return false;
        }
        for (int sz = cs.length(), i = 0; i < sz; ++i) {
            if (!CharUtils.isAsciiPrintable(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNumeric(final CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        for (int sz = cs.length(), i = 0; i < sz; ++i) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNumericSpace(final CharSequence cs) {
        if (cs == null) {
            return false;
        }
        for (int sz = cs.length(), i = 0; i < sz; ++i) {
            if (!Character.isDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isWhitespace(final CharSequence cs) {
        if (cs == null) {
            return false;
        }
        for (int sz = cs.length(), i = 0; i < sz; ++i) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isAllLowerCase(final CharSequence cs) {
        if (cs == null || isEmpty(cs)) {
            return false;
        }
        for (int sz = cs.length(), i = 0; i < sz; ++i) {
            if (!Character.isLowerCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isAllUpperCase(final CharSequence cs) {
        if (cs == null || isEmpty(cs)) {
            return false;
        }
        for (int sz = cs.length(), i = 0; i < sz; ++i) {
            if (!Character.isUpperCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static String defaultString(final String str) {
        return (str == null) ? "" : str;
    }
    
    public static String defaultString(final String str, final String defaultStr) {
        return (str == null) ? defaultStr : str;
    }
    
    public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }
    
    public static <T extends CharSequence> T defaultIfEmpty(final T str, final T defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }
    
    public static String reverse(final String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }
    
    public static String reverseDelimited(final String str, final char separatorChar) {
        if (str == null) {
            return null;
        }
        final String[] strs = split(str, separatorChar);
        ArrayUtils.reverse(strs);
        return join((Object[])strs, separatorChar);
    }
    
    public static String abbreviate(final String str, final int maxWidth) {
        return abbreviate(str, 0, maxWidth);
    }
    
    public static String abbreviate(final String str, int offset, final int maxWidth) {
        if (str == null) {
            return null;
        }
        if (maxWidth < 4) {
            throw new IllegalArgumentException("Minimum abbreviation width is 4");
        }
        if (str.length() <= maxWidth) {
            return str;
        }
        if (offset > str.length()) {
            offset = str.length();
        }
        if (str.length() - offset < maxWidth - 3) {
            offset = str.length() - (maxWidth - 3);
        }
        final String abrevMarker = "...";
        if (offset <= 4) {
            return str.substring(0, maxWidth - 3) + "...";
        }
        if (maxWidth < 7) {
            throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
        }
        if (offset + (maxWidth - 3) < str.length()) {
            return "..." + abbreviate(str.substring(offset), maxWidth - 3);
        }
        return "..." + str.substring(str.length() - (maxWidth - 3));
    }
    
    public static String abbreviateMiddle(final String str, final String middle, final int length) {
        if (isEmpty(str) || isEmpty(middle)) {
            return str;
        }
        if (length >= str.length() || length < middle.length() + 2) {
            return str;
        }
        final int targetSting = length - middle.length();
        final int startOffset = targetSting / 2 + targetSting % 2;
        final int endOffset = str.length() - targetSting / 2;
        final StringBuilder builder = new StringBuilder(length);
        builder.append(str.substring(0, startOffset));
        builder.append(middle);
        builder.append(str.substring(endOffset));
        return builder.toString();
    }
    
    public static String difference(final String str1, final String str2) {
        if (str1 == null) {
            return str2;
        }
        if (str2 == null) {
            return str1;
        }
        final int at = indexOfDifference(str1, str2);
        if (at == -1) {
            return "";
        }
        return str2.substring(at);
    }
    
    public static int indexOfDifference(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return -1;
        }
        if (cs1 == null || cs2 == null) {
            return 0;
        }
        int i;
        for (i = 0; i < cs1.length() && i < cs2.length() && cs1.charAt(i) == cs2.charAt(i); ++i) {}
        if (i < cs2.length() || i < cs1.length()) {
            return i;
        }
        return -1;
    }
    
    public static int indexOfDifference(final CharSequence... css) {
        if (css == null || css.length <= 1) {
            return -1;
        }
        boolean anyStringNull = false;
        boolean allStringsNull = true;
        final int arrayLen = css.length;
        int shortestStrLen = Integer.MAX_VALUE;
        int longestStrLen = 0;
        for (int i = 0; i < arrayLen; ++i) {
            if (css[i] == null) {
                anyStringNull = true;
                shortestStrLen = 0;
            }
            else {
                allStringsNull = false;
                shortestStrLen = Math.min(css[i].length(), shortestStrLen);
                longestStrLen = Math.max(css[i].length(), longestStrLen);
            }
        }
        if (allStringsNull || (longestStrLen == 0 && !anyStringNull)) {
            return -1;
        }
        if (shortestStrLen == 0) {
            return 0;
        }
        int firstDiff = -1;
        for (int stringPos = 0; stringPos < shortestStrLen; ++stringPos) {
            final char comparisonChar = css[0].charAt(stringPos);
            for (int arrayPos = 1; arrayPos < arrayLen; ++arrayPos) {
                if (css[arrayPos].charAt(stringPos) != comparisonChar) {
                    firstDiff = stringPos;
                    break;
                }
            }
            if (firstDiff != -1) {
                break;
            }
        }
        if (firstDiff == -1 && shortestStrLen != longestStrLen) {
            return shortestStrLen;
        }
        return firstDiff;
    }
    
    public static String getCommonPrefix(final String... strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        final int smallestIndexOfDiff = indexOfDifference((CharSequence[])strs);
        if (smallestIndexOfDiff == -1) {
            if (strs[0] == null) {
                return "";
            }
            return strs[0];
        }
        else {
            if (smallestIndexOfDiff == 0) {
                return "";
            }
            return strs[0].substring(0, smallestIndexOfDiff);
        }
    }
    
    public static int getLevenshteinDistance(CharSequence s, CharSequence t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        int n = s.length();
        int m = t.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        if (n > m) {
            final CharSequence tmp = s;
            s = t;
            t = tmp;
            n = m;
            m = t.length();
        }
        int[] p = new int[n + 1];
        int[] d = new int[n + 1];
        for (int i = 0; i <= n; ++i) {
            p[i] = i;
        }
        for (int j = 1; j <= m; ++j) {
            final char t_j = t.charAt(j - 1);
            d[0] = j;
            for (int i = 1; i <= n; ++i) {
                final int cost = (s.charAt(i - 1) != t_j) ? 1 : 0;
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
            }
            final int[] _d = p;
            p = d;
            d = _d;
        }
        return p[n];
    }
    
    public static int getLevenshteinDistance(CharSequence s, CharSequence t, final int threshold) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
        }
        int n = s.length();
        int m = t.length();
        if (n == 0) {
            return (m <= threshold) ? m : -1;
        }
        if (m == 0) {
            return (n <= threshold) ? n : -1;
        }
        if (n > m) {
            final CharSequence tmp = s;
            s = t;
            t = tmp;
            n = m;
            m = t.length();
        }
        int[] p = new int[n + 1];
        int[] d = new int[n + 1];
        final int boundary = Math.min(n, threshold) + 1;
        for (int i = 0; i < boundary; ++i) {
            p[i] = i;
        }
        Arrays.fill(p, boundary, p.length, Integer.MAX_VALUE);
        Arrays.fill(d, Integer.MAX_VALUE);
        for (int j = 1; j <= m; ++j) {
            final char t_j = t.charAt(j - 1);
            d[0] = j;
            final int min = Math.max(1, j - threshold);
            final int max = Math.min(n, j + threshold);
            if (min > max) {
                return -1;
            }
            if (min > 1) {
                d[min - 1] = Integer.MAX_VALUE;
            }
            for (int k = min; k <= max; ++k) {
                if (s.charAt(k - 1) == t_j) {
                    d[k] = p[k - 1];
                }
                else {
                    d[k] = 1 + Math.min(Math.min(d[k - 1], p[k]), p[k - 1]);
                }
            }
            final int[] _d = p;
            p = d;
            d = _d;
        }
        if (p[n] <= threshold) {
            return p[n];
        }
        return -1;
    }
    
    public static boolean startsWith(final CharSequence str, final CharSequence prefix) {
        return startsWith(str, prefix, false);
    }
    
    public static boolean startsWithIgnoreCase(final CharSequence str, final CharSequence prefix) {
        return startsWith(str, prefix, true);
    }
    
    private static boolean startsWith(final CharSequence str, final CharSequence prefix, final boolean ignoreCase) {
        if (str == null || prefix == null) {
            return str == null && prefix == null;
        }
        return prefix.length() <= str.length() && CharSequenceUtils.regionMatches(str, ignoreCase, 0, prefix, 0, prefix.length());
    }
    
    public static boolean startsWithAny(final CharSequence string, final CharSequence... searchStrings) {
        if (isEmpty(string) || ArrayUtils.isEmpty(searchStrings)) {
            return false;
        }
        for (final CharSequence searchString : searchStrings) {
            if (startsWith(string, searchString)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean endsWith(final CharSequence str, final CharSequence suffix) {
        return endsWith(str, suffix, false);
    }
    
    public static boolean endsWithIgnoreCase(final CharSequence str, final CharSequence suffix) {
        return endsWith(str, suffix, true);
    }
    
    private static boolean endsWith(final CharSequence str, final CharSequence suffix, final boolean ignoreCase) {
        if (str == null || suffix == null) {
            return str == null && suffix == null;
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        final int strOffset = str.length() - suffix.length();
        return CharSequenceUtils.regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
    }
    
    public static String normalizeSpace(final String str) {
        if (str == null) {
            return null;
        }
        return StringUtils.WHITESPACE_BLOCK.matcher(trim(str)).replaceAll(" ");
    }
    
    public static boolean endsWithAny(final CharSequence string, final CharSequence... searchStrings) {
        if (isEmpty(string) || ArrayUtils.isEmpty(searchStrings)) {
            return false;
        }
        for (final CharSequence searchString : searchStrings) {
            if (endsWith(string, searchString)) {
                return true;
            }
        }
        return false;
    }
    
    static {
        WHITESPACE_BLOCK = Pattern.compile("\\s+");
        StringUtils.sunAvailable = false;
        StringUtils.sunDecomposeMethod = null;
        sunPattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        StringUtils.java6Available = false;
        StringUtils.java6NormalizeMethod = null;
        StringUtils.java6NormalizerFormNFD = null;
        java6Pattern = StringUtils.sunPattern;
        try {
            final Class<?> normalizerFormClass = Thread.currentThread().getContextClassLoader().loadClass("java.text.Normalizer$Form");
            StringUtils.java6NormalizerFormNFD = normalizerFormClass.getField("NFD").get(null);
            final Class<?> normalizerClass = Thread.currentThread().getContextClassLoader().loadClass("java.text.Normalizer");
            StringUtils.java6NormalizeMethod = normalizerClass.getMethod("normalize", CharSequence.class, normalizerFormClass);
            StringUtils.java6Available = true;
        }
        catch (ClassNotFoundException e) {
            StringUtils.java6Available = false;
        }
        catch (NoSuchFieldException e2) {
            StringUtils.java6Available = false;
        }
        catch (IllegalAccessException e3) {
            StringUtils.java6Available = false;
        }
        catch (NoSuchMethodException e4) {
            StringUtils.java6Available = false;
        }
        try {
            final Class<?> normalizerClass2 = Thread.currentThread().getContextClassLoader().loadClass("sun.text.Normalizer");
            StringUtils.sunDecomposeMethod = normalizerClass2.getMethod("decompose", String.class, Boolean.TYPE, Integer.TYPE);
            StringUtils.sunAvailable = true;
        }
        catch (ClassNotFoundException e) {
            StringUtils.sunAvailable = false;
        }
        catch (NoSuchMethodException e4) {
            StringUtils.sunAvailable = false;
        }
    }
}
