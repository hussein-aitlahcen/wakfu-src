package org.json;

import java.io.*;

public class JSONTokener
{
    private long character;
    private boolean eof;
    private long index;
    private long line;
    private char previous;
    private Reader reader;
    private boolean usePrevious;
    
    public JSONTokener(final Reader in) {
        super();
        this.reader = (in.markSupported() ? in : new BufferedReader(in));
        this.eof = false;
        this.usePrevious = false;
        this.previous = '\0';
        this.index = 0L;
        this.character = 1L;
        this.line = 1L;
    }
    
    public JSONTokener(final InputStream in) throws JSONException {
        this(new InputStreamReader(in));
    }
    
    public JSONTokener(final String s) {
        this(new StringReader(s));
    }
    
    public void back() throws JSONException {
        if (this.usePrevious || this.index <= 0L) {
            throw new JSONException("Stepping back two steps is not supported");
        }
        --this.index;
        --this.character;
        this.usePrevious = true;
        this.eof = false;
    }
    
    public static int dehexchar(final char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - '7';
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'W';
        }
        return -1;
    }
    
    public boolean end() {
        return this.eof && !this.usePrevious;
    }
    
    public boolean more() throws JSONException {
        this.next();
        if (this.end()) {
            return false;
        }
        this.back();
        return true;
    }
    
    public char next() throws JSONException {
        int n;
        if (this.usePrevious) {
            this.usePrevious = false;
            n = this.previous;
        }
        else {
            try {
                n = this.reader.read();
            }
            catch (IOException ex) {
                throw new JSONException(ex);
            }
            if (n <= 0) {
                this.eof = true;
                n = 0;
            }
        }
        ++this.index;
        if (this.previous == '\r') {
            ++this.line;
            this.character = ((n != 10) ? 1 : 0);
        }
        else if (n == 10) {
            ++this.line;
            this.character = 0L;
        }
        else {
            ++this.character;
        }
        return this.previous = (char)n;
    }
    
    public char next(final char c) throws JSONException {
        final char next = this.next();
        if (next != c) {
            throw this.syntaxError("Expected '" + c + "' and instead saw '" + next + "'");
        }
        return next;
    }
    
    public String next(final int n) throws JSONException {
        if (n == 0) {
            return "";
        }
        final char[] value = new char[n];
        for (int i = 0; i < n; ++i) {
            value[i] = this.next();
            if (this.end()) {
                throw this.syntaxError("Substring bounds error");
            }
        }
        return new String(value);
    }
    
    public char nextClean() throws JSONException {
        char next;
        do {
            next = this.next();
        } while (next != '\0' && next <= ' ');
        return next;
    }
    
    public String nextString(final char c) throws JSONException {
        final StringBuffer sb = new StringBuffer();
        while (true) {
            final char next = this.next();
            switch (next) {
                case 0:
                case 10:
                case 13: {
                    throw this.syntaxError("Unterminated string");
                }
                case 92: {
                    final char next2 = this.next();
                    switch (next2) {
                        case 98: {
                            sb.append('\b');
                            continue;
                        }
                        case 116: {
                            sb.append('\t');
                            continue;
                        }
                        case 110: {
                            sb.append('\n');
                            continue;
                        }
                        case 102: {
                            sb.append('\f');
                            continue;
                        }
                        case 114: {
                            sb.append('\r');
                            continue;
                        }
                        case 117: {
                            sb.append((char)Integer.parseInt(this.next(4), 16));
                            continue;
                        }
                        case 34:
                        case 39:
                        case 47:
                        case 92: {
                            sb.append(next2);
                            continue;
                        }
                        default: {
                            throw this.syntaxError("Illegal escape.");
                        }
                    }
                    break;
                }
                default: {
                    if (next == c) {
                        return sb.toString();
                    }
                    sb.append(next);
                    continue;
                }
            }
        }
    }
    
    public String nextTo(final char c) throws JSONException {
        final StringBuffer sb = new StringBuffer();
        char next;
        while (true) {
            next = this.next();
            if (next == c || next == '\0' || next == '\n' || next == '\r') {
                break;
            }
            sb.append(next);
        }
        if (next != '\0') {
            this.back();
        }
        return sb.toString().trim();
    }
    
    public String nextTo(final String s) throws JSONException {
        final StringBuffer sb = new StringBuffer();
        char next;
        while (true) {
            next = this.next();
            if (s.indexOf(next) >= 0 || next == '\0' || next == '\n' || next == '\r') {
                break;
            }
            sb.append(next);
        }
        if (next != '\0') {
            this.back();
        }
        return sb.toString().trim();
    }
    
    public Object nextValue() throws JSONException {
        char c = this.nextClean();
        switch (c) {
            case 34:
            case 39: {
                return this.nextString(c);
            }
            case 123: {
                this.back();
                return new JSONObject(this);
            }
            case 91: {
                this.back();
                return new JSONArray(this);
            }
            default: {
                final StringBuffer sb = new StringBuffer();
                while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
                    sb.append(c);
                    c = this.next();
                }
                this.back();
                final String trim = sb.toString().trim();
                if ("".equals(trim)) {
                    throw this.syntaxError("Missing value");
                }
                return JSONObject.stringToValue(trim);
            }
        }
    }
    
    public char skipTo(final char c) throws JSONException {
        char next;
        try {
            final long index = this.index;
            final long character = this.character;
            final long line = this.line;
            this.reader.mark(1000000);
            do {
                next = this.next();
                if (next == '\0') {
                    this.reader.reset();
                    this.index = index;
                    this.character = character;
                    this.line = line;
                    return next;
                }
            } while (next != c);
        }
        catch (IOException ex) {
            throw new JSONException(ex);
        }
        this.back();
        return next;
    }
    
    public JSONException syntaxError(final String str) {
        return new JSONException(str + this.toString());
    }
    
    public String toString() {
        return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
    }
}
