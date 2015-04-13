package org.apache.tools.ant.filters;

import java.io.*;
import org.apache.tools.ant.types.*;

public final class PrefixLines extends BaseParamFilterReader implements ChainableReader
{
    private static final String PREFIX_KEY = "prefix";
    private String prefix;
    private String queuedData;
    
    public PrefixLines() {
        super();
        this.prefix = null;
        this.queuedData = null;
    }
    
    public PrefixLines(final Reader in) {
        super(in);
        this.prefix = null;
        this.queuedData = null;
    }
    
    public int read() throws IOException {
        if (!this.getInitialized()) {
            this.initialize();
            this.setInitialized(true);
        }
        int ch = -1;
        if (this.queuedData != null && this.queuedData.length() == 0) {
            this.queuedData = null;
        }
        if (this.queuedData != null) {
            ch = this.queuedData.charAt(0);
            this.queuedData = this.queuedData.substring(1);
            if (this.queuedData.length() == 0) {
                this.queuedData = null;
            }
        }
        else {
            this.queuedData = this.readLine();
            if (this.queuedData != null) {
                if (this.prefix != null) {
                    this.queuedData = this.prefix + this.queuedData;
                }
                return this.read();
            }
            ch = -1;
        }
        return ch;
    }
    
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }
    
    private String getPrefix() {
        return this.prefix;
    }
    
    public Reader chain(final Reader rdr) {
        final PrefixLines newFilter = new PrefixLines(rdr);
        newFilter.setPrefix(this.getPrefix());
        newFilter.setInitialized(true);
        return newFilter;
    }
    
    private void initialize() {
        final Parameter[] params = this.getParameters();
        if (params != null) {
            for (int i = 0; i < params.length; ++i) {
                if ("prefix".equals(params[i].getName())) {
                    this.prefix = params[i].getValue();
                    break;
                }
            }
        }
    }
}
