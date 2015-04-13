package org.apache.tools.ant.types.selectors;

import java.io.*;

public class TokenizedPattern
{
    public static final TokenizedPattern EMPTY_PATTERN;
    private final String pattern;
    private final String[] tokenizedPattern;
    
    public TokenizedPattern(final String pattern) {
        this(pattern, SelectorUtils.tokenizePathAsArray(pattern));
    }
    
    TokenizedPattern(final String pattern, final String[] tokens) {
        super();
        this.pattern = pattern;
        this.tokenizedPattern = tokens;
    }
    
    public boolean matchPath(final TokenizedPath path, final boolean isCaseSensitive) {
        return SelectorUtils.matchPath(this.tokenizedPattern, path.getTokens(), isCaseSensitive);
    }
    
    public boolean matchStartOf(final TokenizedPath path, final boolean caseSensitive) {
        return SelectorUtils.matchPatternStart(this.tokenizedPattern, path.getTokens(), caseSensitive);
    }
    
    public String toString() {
        return this.pattern;
    }
    
    public String getPattern() {
        return this.pattern;
    }
    
    public boolean equals(final Object o) {
        return o instanceof TokenizedPattern && this.pattern.equals(((TokenizedPattern)o).pattern);
    }
    
    public int hashCode() {
        return this.pattern.hashCode();
    }
    
    public int depth() {
        return this.tokenizedPattern.length;
    }
    
    public boolean containsPattern(final String pat) {
        for (int i = 0; i < this.tokenizedPattern.length; ++i) {
            if (this.tokenizedPattern[i].equals(pat)) {
                return true;
            }
        }
        return false;
    }
    
    public TokenizedPath rtrimWildcardTokens() {
        final StringBuilder sb = new StringBuilder();
        int newLen;
        for (newLen = 0; newLen < this.tokenizedPattern.length && !SelectorUtils.hasWildcards(this.tokenizedPattern[newLen]); ++newLen) {
            if (newLen > 0 && sb.charAt(sb.length() - 1) != File.separatorChar) {
                sb.append(File.separator);
            }
            sb.append(this.tokenizedPattern[newLen]);
        }
        if (newLen == 0) {
            return TokenizedPath.EMPTY_PATH;
        }
        final String[] newPats = new String[newLen];
        System.arraycopy(this.tokenizedPattern, 0, newPats, 0, newLen);
        return new TokenizedPath(sb.toString(), newPats);
    }
    
    public boolean endsWith(final String s) {
        return this.tokenizedPattern.length > 0 && this.tokenizedPattern[this.tokenizedPattern.length - 1].equals(s);
    }
    
    public TokenizedPattern withoutLastToken() {
        if (this.tokenizedPattern.length == 0) {
            throw new IllegalStateException("cant strip a token from nothing");
        }
        if (this.tokenizedPattern.length == 1) {
            return TokenizedPattern.EMPTY_PATTERN;
        }
        final String toStrip = this.tokenizedPattern[this.tokenizedPattern.length - 1];
        final int index = this.pattern.lastIndexOf(toStrip);
        final String[] tokens = new String[this.tokenizedPattern.length - 1];
        System.arraycopy(this.tokenizedPattern, 0, tokens, 0, this.tokenizedPattern.length - 1);
        return new TokenizedPattern(this.pattern.substring(0, index), tokens);
    }
    
    static {
        EMPTY_PATTERN = new TokenizedPattern("", new String[0]);
    }
}
