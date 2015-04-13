package org.apache.tools.ant.util;

import org.apache.tools.ant.*;
import java.io.*;

public class StringTokenizer extends ProjectComponent implements Tokenizer
{
    private static final int NOT_A_CHAR = -2;
    private String intraString;
    private int pushed;
    private char[] delims;
    private boolean delimsAreTokens;
    private boolean suppressDelims;
    private boolean includeDelims;
    
    public StringTokenizer() {
        super();
        this.intraString = "";
        this.pushed = -2;
        this.delims = null;
        this.delimsAreTokens = false;
        this.suppressDelims = false;
        this.includeDelims = false;
    }
    
    public void setDelims(final String delims) {
        this.delims = StringUtils.resolveBackSlash(delims).toCharArray();
    }
    
    public void setDelimsAreTokens(final boolean delimsAreTokens) {
        this.delimsAreTokens = delimsAreTokens;
    }
    
    public void setSuppressDelims(final boolean suppressDelims) {
        this.suppressDelims = suppressDelims;
    }
    
    public void setIncludeDelims(final boolean includeDelims) {
        this.includeDelims = includeDelims;
    }
    
    public String getToken(final Reader in) throws IOException {
        int ch = -1;
        if (this.pushed != -2) {
            ch = this.pushed;
            this.pushed = -2;
        }
        else {
            ch = in.read();
        }
        if (ch == -1) {
            return null;
        }
        boolean inToken = true;
        this.intraString = "";
        final StringBuffer word = new StringBuffer();
        final StringBuffer padding = new StringBuffer();
        while (ch != -1) {
            final char c = (char)ch;
            final boolean isDelim = this.isDelim(c);
            if (inToken) {
                if (isDelim) {
                    if (this.delimsAreTokens) {
                        if (word.length() == 0) {
                            word.append(c);
                            break;
                        }
                        this.pushed = ch;
                        break;
                    }
                    else {
                        padding.append(c);
                        inToken = false;
                    }
                }
                else {
                    word.append(c);
                }
            }
            else {
                if (!isDelim) {
                    this.pushed = ch;
                    break;
                }
                padding.append(c);
            }
            ch = in.read();
        }
        this.intraString = padding.toString();
        if (this.includeDelims) {
            word.append(this.intraString);
        }
        return word.toString();
    }
    
    public String getPostToken() {
        return (this.suppressDelims || this.includeDelims) ? "" : this.intraString;
    }
    
    private boolean isDelim(final char ch) {
        if (this.delims == null) {
            return Character.isWhitespace(ch);
        }
        for (int i = 0; i < this.delims.length; ++i) {
            if (this.delims[i] == ch) {
                return true;
            }
        }
        return false;
    }
}
