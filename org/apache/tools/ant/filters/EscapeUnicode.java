package org.apache.tools.ant.filters;

import org.apache.tools.ant.util.*;
import java.io.*;

public class EscapeUnicode extends BaseParamFilterReader implements ChainableReader
{
    private StringBuffer unicodeBuf;
    
    public EscapeUnicode() {
        super();
        this.unicodeBuf = new StringBuffer();
    }
    
    public EscapeUnicode(final Reader in) {
        super(in);
        this.unicodeBuf = new StringBuffer();
    }
    
    public final int read() throws IOException {
        if (!this.getInitialized()) {
            this.initialize();
            this.setInitialized(true);
        }
        int ch = -1;
        if (this.unicodeBuf.length() == 0) {
            ch = this.in.read();
            if (ch != -1) {
                final char achar = (char)ch;
                if (achar >= '\u0080') {
                    this.unicodeBuf = UnicodeUtil.EscapeUnicode(achar);
                    ch = 92;
                }
            }
        }
        else {
            ch = this.unicodeBuf.charAt(0);
            this.unicodeBuf.deleteCharAt(0);
        }
        return ch;
    }
    
    public final Reader chain(final Reader rdr) {
        final EscapeUnicode newFilter = new EscapeUnicode(rdr);
        newFilter.setInitialized(true);
        return newFilter;
    }
    
    private void initialize() {
    }
}
