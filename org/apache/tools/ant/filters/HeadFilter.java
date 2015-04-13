package org.apache.tools.ant.filters;

import org.apache.tools.ant.util.*;
import java.io.*;
import org.apache.tools.ant.types.*;

public final class HeadFilter extends BaseParamFilterReader implements ChainableReader
{
    private static final String LINES_KEY = "lines";
    private static final String SKIP_KEY = "skip";
    private long linesRead;
    private static final int DEFAULT_NUM_LINES = 10;
    private long lines;
    private long skip;
    private LineTokenizer lineTokenizer;
    private String line;
    private int linePos;
    private boolean eof;
    
    public HeadFilter() {
        super();
        this.linesRead = 0L;
        this.lines = 10L;
        this.skip = 0L;
        this.lineTokenizer = null;
        this.line = null;
        this.linePos = 0;
    }
    
    public HeadFilter(final Reader in) {
        super(in);
        this.linesRead = 0L;
        this.lines = 10L;
        this.skip = 0L;
        this.lineTokenizer = null;
        this.line = null;
        this.linePos = 0;
        (this.lineTokenizer = new LineTokenizer()).setIncludeDelims(true);
    }
    
    public int read() throws IOException {
        if (!this.getInitialized()) {
            this.initialize();
            this.setInitialized(true);
        }
        while (this.line == null || this.line.length() == 0) {
            this.line = this.lineTokenizer.getToken(this.in);
            if (this.line == null) {
                return -1;
            }
            this.line = this.headFilter(this.line);
            if (this.eof) {
                return -1;
            }
            this.linePos = 0;
        }
        final int ch = this.line.charAt(this.linePos);
        ++this.linePos;
        if (this.linePos == this.line.length()) {
            this.line = null;
        }
        return ch;
    }
    
    public void setLines(final long lines) {
        this.lines = lines;
    }
    
    private long getLines() {
        return this.lines;
    }
    
    public void setSkip(final long skip) {
        this.skip = skip;
    }
    
    private long getSkip() {
        return this.skip;
    }
    
    public Reader chain(final Reader rdr) {
        final HeadFilter newFilter = new HeadFilter(rdr);
        newFilter.setLines(this.getLines());
        newFilter.setSkip(this.getSkip());
        newFilter.setInitialized(true);
        return newFilter;
    }
    
    private void initialize() {
        final Parameter[] params = this.getParameters();
        if (params != null) {
            for (int i = 0; i < params.length; ++i) {
                if ("lines".equals(params[i].getName())) {
                    this.lines = Long.parseLong(params[i].getValue());
                }
                else if ("skip".equals(params[i].getName())) {
                    this.skip = Long.parseLong(params[i].getValue());
                }
            }
        }
    }
    
    private String headFilter(final String line) {
        ++this.linesRead;
        if (this.skip > 0L && this.linesRead - 1L < this.skip) {
            return null;
        }
        if (this.lines > 0L && this.linesRead > this.lines + this.skip) {
            this.eof = true;
            return null;
        }
        return line;
    }
}
