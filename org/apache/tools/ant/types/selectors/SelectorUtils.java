package org.apache.tools.ant.types.selectors;

import org.apache.tools.ant.util.*;
import java.io.*;
import java.util.*;
import org.apache.tools.ant.types.*;

public final class SelectorUtils
{
    public static final String DEEP_TREE_MATCH = "**";
    private static final SelectorUtils instance;
    private static final FileUtils FILE_UTILS;
    
    public static SelectorUtils getInstance() {
        return SelectorUtils.instance;
    }
    
    public static boolean matchPatternStart(final String pattern, final String str) {
        return matchPatternStart(pattern, str, true);
    }
    
    public static boolean matchPatternStart(final String pattern, final String str, final boolean isCaseSensitive) {
        if (str.startsWith(File.separator) != pattern.startsWith(File.separator)) {
            return false;
        }
        final String[] patDirs = tokenizePathAsArray(pattern);
        final String[] strDirs = tokenizePathAsArray(str);
        return matchPatternStart(patDirs, strDirs, isCaseSensitive);
    }
    
    static boolean matchPatternStart(final String[] patDirs, final String[] strDirs, final boolean isCaseSensitive) {
        int patIdxStart;
        int patIdxEnd;
        int strIdxStart;
        int strIdxEnd;
        for (patIdxStart = 0, patIdxEnd = patDirs.length - 1, strIdxStart = 0, strIdxEnd = strDirs.length - 1; patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd; ++patIdxStart, ++strIdxStart) {
            final String patDir = patDirs[patIdxStart];
            if (patDir.equals("**")) {
                break;
            }
            if (!match(patDir, strDirs[strIdxStart], isCaseSensitive)) {
                return false;
            }
        }
        return strIdxStart > strIdxEnd || patIdxStart <= patIdxEnd;
    }
    
    public static boolean matchPath(final String pattern, final String str) {
        final String[] patDirs = tokenizePathAsArray(pattern);
        return matchPath(patDirs, tokenizePathAsArray(str), true);
    }
    
    public static boolean matchPath(final String pattern, final String str, final boolean isCaseSensitive) {
        final String[] patDirs = tokenizePathAsArray(pattern);
        return matchPath(patDirs, tokenizePathAsArray(str), isCaseSensitive);
    }
    
    static boolean matchPath(final String[] tokenizedPattern, final String[] strDirs, final boolean isCaseSensitive) {
        int patIdxStart;
        int patIdxEnd;
        int strIdxStart;
        int strIdxEnd;
        for (patIdxStart = 0, patIdxEnd = tokenizedPattern.length - 1, strIdxStart = 0, strIdxEnd = strDirs.length - 1; patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd; ++patIdxStart, ++strIdxStart) {
            final String patDir = tokenizedPattern[patIdxStart];
            if (patDir.equals("**")) {
                break;
            }
            if (!match(patDir, strDirs[strIdxStart], isCaseSensitive)) {
                return false;
            }
        }
        if (strIdxStart > strIdxEnd) {
            for (int i = patIdxStart; i <= patIdxEnd; ++i) {
                if (!tokenizedPattern[i].equals("**")) {
                    return false;
                }
            }
            return true;
        }
        if (patIdxStart > patIdxEnd) {
            return false;
        }
        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            final String patDir = tokenizedPattern[patIdxEnd];
            if (patDir.equals("**")) {
                break;
            }
            if (!match(patDir, strDirs[strIdxEnd], isCaseSensitive)) {
                return false;
            }
            --patIdxEnd;
            --strIdxEnd;
        }
        if (strIdxStart > strIdxEnd) {
            for (int i = patIdxStart; i <= patIdxEnd; ++i) {
                if (!tokenizedPattern[i].equals("**")) {
                    return false;
                }
            }
            return true;
        }
        while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
            int patIdxTmp = -1;
            for (int j = patIdxStart + 1; j <= patIdxEnd; ++j) {
                if (tokenizedPattern[j].equals("**")) {
                    patIdxTmp = j;
                    break;
                }
            }
            if (patIdxTmp == patIdxStart + 1) {
                ++patIdxStart;
            }
            else {
                final int patLength = patIdxTmp - patIdxStart - 1;
                final int strLength = strIdxEnd - strIdxStart + 1;
                int foundIdx = -1;
                int k = 0;
            Label_0304:
                while (k <= strLength - patLength) {
                    for (int l = 0; l < patLength; ++l) {
                        final String subPat = tokenizedPattern[patIdxStart + l + 1];
                        final String subStr = strDirs[strIdxStart + k + l];
                        if (!match(subPat, subStr, isCaseSensitive)) {
                            ++k;
                            continue Label_0304;
                        }
                    }
                    foundIdx = strIdxStart + k;
                    break;
                }
                if (foundIdx == -1) {
                    return false;
                }
                patIdxStart = patIdxTmp;
                strIdxStart = foundIdx + patLength;
            }
        }
        for (int i = patIdxStart; i <= patIdxEnd; ++i) {
            if (!tokenizedPattern[i].equals("**")) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean match(final String pattern, final String str) {
        return match(pattern, str, true);
    }
    
    public static boolean match(final String pattern, final String str, final boolean caseSensitive) {
        final char[] patArr = pattern.toCharArray();
        final char[] strArr = str.toCharArray();
        int patIdxStart = 0;
        int patIdxEnd = patArr.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strArr.length - 1;
        boolean containsStar = false;
        for (int i = 0; i < patArr.length; ++i) {
            if (patArr[i] == '*') {
                containsStar = true;
                break;
            }
        }
        if (!containsStar) {
            if (patIdxEnd != strIdxEnd) {
                return false;
            }
            for (int i = 0; i <= patIdxEnd; ++i) {
                final char ch = patArr[i];
                if (ch != '?' && different(caseSensitive, ch, strArr[i])) {
                    return false;
                }
            }
            return true;
        }
        else {
            if (patIdxEnd == 0) {
                return true;
            }
            while (true) {
                char ch = patArr[patIdxStart];
                if (ch != '*' && strIdxStart <= strIdxEnd) {
                    if (ch != '?' && different(caseSensitive, ch, strArr[strIdxStart])) {
                        return false;
                    }
                    ++patIdxStart;
                    ++strIdxStart;
                }
                else {
                    if (strIdxStart > strIdxEnd) {
                        return allStars(patArr, patIdxStart, patIdxEnd);
                    }
                    while (true) {
                        ch = patArr[patIdxEnd];
                        if (ch != '*' && strIdxStart <= strIdxEnd) {
                            if (ch != '?' && different(caseSensitive, ch, strArr[strIdxEnd])) {
                                return false;
                            }
                            --patIdxEnd;
                            --strIdxEnd;
                        }
                        else {
                            if (strIdxStart > strIdxEnd) {
                                return allStars(patArr, patIdxStart, patIdxEnd);
                            }
                            while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
                                int patIdxTmp = -1;
                                for (int j = patIdxStart + 1; j <= patIdxEnd; ++j) {
                                    if (patArr[j] == '*') {
                                        patIdxTmp = j;
                                        break;
                                    }
                                }
                                if (patIdxTmp == patIdxStart + 1) {
                                    ++patIdxStart;
                                }
                                else {
                                    final int patLength = patIdxTmp - patIdxStart - 1;
                                    final int strLength = strIdxEnd - strIdxStart + 1;
                                    int foundIdx = -1;
                                    int k = 0;
                                Label_0365:
                                    while (k <= strLength - patLength) {
                                        for (int l = 0; l < patLength; ++l) {
                                            ch = patArr[patIdxStart + l + 1];
                                            if (ch != '?' && different(caseSensitive, ch, strArr[strIdxStart + k + l])) {
                                                ++k;
                                                continue Label_0365;
                                            }
                                        }
                                        foundIdx = strIdxStart + k;
                                        break;
                                    }
                                    if (foundIdx == -1) {
                                        return false;
                                    }
                                    patIdxStart = patIdxTmp;
                                    strIdxStart = foundIdx + patLength;
                                }
                            }
                            return allStars(patArr, patIdxStart, patIdxEnd);
                        }
                    }
                }
            }
        }
    }
    
    private static boolean allStars(final char[] chars, final int start, final int end) {
        for (int i = start; i <= end; ++i) {
            if (chars[i] != '*') {
                return false;
            }
        }
        return true;
    }
    
    private static boolean different(final boolean caseSensitive, final char ch, final char other) {
        return caseSensitive ? (ch != other) : (Character.toUpperCase(ch) != Character.toUpperCase(other));
    }
    
    public static Vector<String> tokenizePath(final String path) {
        return tokenizePath(path, File.separator);
    }
    
    public static Vector<String> tokenizePath(String path, final String separator) {
        final Vector<String> ret = new Vector<String>();
        if (FileUtils.isAbsolutePath(path)) {
            final String[] s = SelectorUtils.FILE_UTILS.dissect(path);
            ret.add(s[0]);
            path = s[1];
        }
        final StringTokenizer st = new StringTokenizer(path, separator);
        while (st.hasMoreTokens()) {
            ret.addElement(st.nextToken());
        }
        return ret;
    }
    
    static String[] tokenizePathAsArray(String path) {
        String root = null;
        if (FileUtils.isAbsolutePath(path)) {
            final String[] s = SelectorUtils.FILE_UTILS.dissect(path);
            root = s[0];
            path = s[1];
        }
        final char sep = File.separatorChar;
        int start = 0;
        final int len = path.length();
        int count = 0;
        for (int pos = 0; pos < len; ++pos) {
            if (path.charAt(pos) == sep) {
                if (pos != start) {
                    ++count;
                }
                start = pos + 1;
            }
        }
        if (len != start) {
            ++count;
        }
        final String[] l = new String[count + ((root != null) ? 1 : 0)];
        if (root != null) {
            l[0] = root;
            count = 1;
        }
        else {
            count = 0;
        }
        start = 0;
        for (int pos2 = 0; pos2 < len; ++pos2) {
            if (path.charAt(pos2) == sep) {
                if (pos2 != start) {
                    final String tok = path.substring(start, pos2);
                    l[count++] = tok;
                }
                start = pos2 + 1;
            }
        }
        if (len != start) {
            final String tok2 = path.substring(start);
            l[count] = tok2;
        }
        return l;
    }
    
    public static boolean isOutOfDate(final File src, final File target, final int granularity) {
        return src.exists() && (!target.exists() || src.lastModified() - granularity > target.lastModified());
    }
    
    public static boolean isOutOfDate(final Resource src, final Resource target, final int granularity) {
        return isOutOfDate(src, target, (long)granularity);
    }
    
    public static boolean isOutOfDate(final Resource src, final Resource target, final long granularity) {
        final long sourceLastModified = src.getLastModified();
        final long targetLastModified = target.getLastModified();
        return src.isExists() && (sourceLastModified == 0L || targetLastModified == 0L || sourceLastModified - granularity > targetLastModified);
    }
    
    public static String removeWhitespace(final String input) {
        final StringBuffer result = new StringBuffer();
        if (input != null) {
            final StringTokenizer st = new StringTokenizer(input);
            while (st.hasMoreTokens()) {
                result.append(st.nextToken());
            }
        }
        return result.toString();
    }
    
    public static boolean hasWildcards(final String input) {
        return input.indexOf(42) != -1 || input.indexOf(63) != -1;
    }
    
    public static String rtrimWildcardTokens(final String input) {
        return new TokenizedPattern(input).rtrimWildcardTokens().toString();
    }
    
    static {
        instance = new SelectorUtils();
        FILE_UTILS = FileUtils.getFileUtils();
    }
}
