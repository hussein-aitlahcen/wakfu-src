package org.apache.tools.ant.filters;

import org.apache.tools.ant.*;
import org.apache.tools.ant.util.*;
import java.io.*;

public abstract class BaseFilterReader extends FilterReader
{
    private static final int BUFFER_SIZE = 8192;
    private boolean initialized;
    private Project project;
    
    public BaseFilterReader() {
        super(new StringReader(""));
        this.initialized = false;
        this.project = null;
        FileUtils.close(this);
    }
    
    public BaseFilterReader(final Reader in) {
        super(in);
        this.initialized = false;
        this.project = null;
    }
    
    public final int read(final char[] cbuf, final int off, final int len) throws IOException {
        int i = 0;
        while (i < len) {
            final int ch = this.read();
            if (ch == -1) {
                if (i == 0) {
                    return -1;
                }
                return i;
            }
            else {
                cbuf[off + i] = (char)ch;
                ++i;
            }
        }
        return len;
    }
    
    public final long skip(final long n) throws IOException, IllegalArgumentException {
        if (n < 0L) {
            throw new IllegalArgumentException("skip value is negative");
        }
        for (long i = 0L; i < n; ++i) {
            if (this.read() == -1) {
                return i;
            }
        }
        return n;
    }
    
    protected final void setInitialized(final boolean initialized) {
        this.initialized = initialized;
    }
    
    protected final boolean getInitialized() {
        return this.initialized;
    }
    
    public final void setProject(final Project project) {
        this.project = project;
    }
    
    protected final Project getProject() {
        return this.project;
    }
    
    protected final String readLine() throws IOException {
        int ch = this.in.read();
        if (ch == -1) {
            return null;
        }
        final StringBuffer line = new StringBuffer();
        while (ch != -1) {
            line.append((char)ch);
            if (ch == 10) {
                break;
            }
            ch = this.in.read();
        }
        return line.toString();
    }
    
    protected final String readFully() throws IOException {
        return FileUtils.readFully(this.in, 8192);
    }
}
