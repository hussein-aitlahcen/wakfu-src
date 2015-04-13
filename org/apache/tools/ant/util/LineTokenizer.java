package org.apache.tools.ant.util;

import org.apache.tools.ant.*;
import java.io.*;

public class LineTokenizer extends ProjectComponent implements Tokenizer
{
    private static final int NOT_A_CHAR = -2;
    private String lineEnd;
    private int pushed;
    private boolean includeDelims;
    
    public LineTokenizer() {
        super();
        this.lineEnd = "";
        this.pushed = -2;
        this.includeDelims = false;
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
        this.lineEnd = "";
        final StringBuffer line = new StringBuffer();
        int state = 0;
        while (ch != -1) {
            if (state == 0) {
                if (ch == 13) {
                    state = 1;
                }
                else {
                    if (ch == 10) {
                        this.lineEnd = "\n";
                        break;
                    }
                    line.append((char)ch);
                }
                ch = in.read();
            }
            else {
                state = 0;
                if (ch == 10) {
                    this.lineEnd = "\r\n";
                    break;
                }
                this.pushed = ch;
                this.lineEnd = "\r";
                break;
            }
        }
        if (ch == -1 && state == 1) {
            this.lineEnd = "\r";
        }
        if (this.includeDelims) {
            line.append(this.lineEnd);
        }
        return line.toString();
    }
    
    public String getPostToken() {
        if (this.includeDelims) {
            return "";
        }
        return this.lineEnd;
    }
}
