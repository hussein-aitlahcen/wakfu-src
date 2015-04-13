package org.apache.tools.ant.filters;

import java.io.*;
import org.apache.tools.ant.types.*;

public final class TabsToSpaces extends BaseParamFilterReader implements ChainableReader
{
    private static final int DEFAULT_TAB_LENGTH = 8;
    private static final String TAB_LENGTH_KEY = "tablength";
    private int tabLength;
    private int spacesRemaining;
    
    public TabsToSpaces() {
        super();
        this.tabLength = 8;
        this.spacesRemaining = 0;
    }
    
    public TabsToSpaces(final Reader in) {
        super(in);
        this.tabLength = 8;
        this.spacesRemaining = 0;
    }
    
    public int read() throws IOException {
        if (!this.getInitialized()) {
            this.initialize();
            this.setInitialized(true);
        }
        int ch = -1;
        if (this.spacesRemaining > 0) {
            --this.spacesRemaining;
            ch = 32;
        }
        else {
            ch = this.in.read();
            if (ch == 9) {
                this.spacesRemaining = this.tabLength - 1;
                ch = 32;
            }
        }
        return ch;
    }
    
    public void setTablength(final int tabLength) {
        this.tabLength = tabLength;
    }
    
    private int getTablength() {
        return this.tabLength;
    }
    
    public Reader chain(final Reader rdr) {
        final TabsToSpaces newFilter = new TabsToSpaces(rdr);
        newFilter.setTablength(this.getTablength());
        newFilter.setInitialized(true);
        return newFilter;
    }
    
    private void initialize() {
        final Parameter[] params = this.getParameters();
        if (params != null) {
            for (int i = 0; i < params.length; ++i) {
                if (params[i] != null && "tablength".equals(params[i].getName())) {
                    this.tabLength = Integer.parseInt(params[i].getValue());
                    break;
                }
            }
        }
    }
}
