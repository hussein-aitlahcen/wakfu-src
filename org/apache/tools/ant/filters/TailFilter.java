package org.apache.tools.ant.filters;

import org.apache.tools.ant.util.*;
import java.util.*;
import java.io.*;
import org.apache.tools.ant.types.*;

public final class TailFilter extends BaseParamFilterReader implements ChainableReader
{
    private static final String LINES_KEY = "lines";
    private static final String SKIP_KEY = "skip";
    private static final int DEFAULT_NUM_LINES = 10;
    private long lines;
    private long skip;
    private boolean completedReadAhead;
    private LineTokenizer lineTokenizer;
    private String line;
    private int linePos;
    private LinkedList<String> lineList;
    
    public TailFilter() {
        super();
        this.lines = 10L;
        this.skip = 0L;
        this.completedReadAhead = false;
        this.lineTokenizer = null;
        this.line = null;
        this.linePos = 0;
        this.lineList = new LinkedList<String>();
    }
    
    public TailFilter(final Reader in) {
        super(in);
        this.lines = 10L;
        this.skip = 0L;
        this.completedReadAhead = false;
        this.lineTokenizer = null;
        this.line = null;
        this.linePos = 0;
        this.lineList = new LinkedList<String>();
        (this.lineTokenizer = new LineTokenizer()).setIncludeDelims(true);
    }
    
    public int read() throws IOException {
        if (!this.getInitialized()) {
            this.initialize();
            this.setInitialized(true);
        }
        while (this.line == null || this.line.length() == 0) {
            this.line = this.lineTokenizer.getToken(this.in);
            this.line = this.tailFilter(this.line);
            if (this.line == null) {
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
        final TailFilter newFilter = new TailFilter(rdr);
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
                    this.setLines(Long.parseLong(params[i].getValue()));
                }
                else if ("skip".equals(params[i].getName())) {
                    this.skip = Long.parseLong(params[i].getValue());
                }
            }
        }
    }
    
    private String tailFilter(final String line) {
        if (!this.completedReadAhead) {
            if (line != null) {
                this.lineList.add(line);
                if (this.lines == -1L) {
                    if (this.lineList.size() > this.skip) {
                        return this.lineList.removeFirst();
                    }
                }
                else {
                    final long linesToKeep = this.lines + ((this.skip > 0L) ? this.skip : 0L);
                    if (linesToKeep < this.lineList.size()) {
                        this.lineList.removeFirst();
                    }
                }
                return "";
            }
            this.completedReadAhead = true;
            if (this.skip > 0L) {
                for (int i = 0; i < this.skip; ++i) {
                    this.lineList.removeLast();
                }
            }
            if (this.lines > -1L) {
                while (this.lineList.size() > this.lines) {
                    this.lineList.removeFirst();
                }
            }
        }
        if (this.lineList.size() > 0) {
            return this.lineList.removeFirst();
        }
        return null;
    }
}
