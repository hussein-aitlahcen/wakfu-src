package org.apache.tools.ant.filters;

import java.io.*;

public final class StripJavaComments extends BaseFilterReader implements ChainableReader
{
    private int readAheadCh;
    private boolean inString;
    private boolean quoted;
    
    public StripJavaComments() {
        super();
        this.readAheadCh = -1;
        this.inString = false;
        this.quoted = false;
    }
    
    public StripJavaComments(final Reader in) {
        super(in);
        this.readAheadCh = -1;
        this.inString = false;
        this.quoted = false;
    }
    
    public int read() throws IOException {
        int ch = -1;
        if (this.readAheadCh != -1) {
            ch = this.readAheadCh;
            this.readAheadCh = -1;
        }
        else {
            ch = this.in.read();
            if (ch == 34 && !this.quoted) {
                this.inString = !this.inString;
                this.quoted = false;
            }
            else if (ch == 92) {
                this.quoted = !this.quoted;
            }
            else {
                this.quoted = false;
                if (!this.inString && ch == 47) {
                    ch = this.in.read();
                    if (ch == 47) {
                        while (ch != 10 && ch != -1 && ch != 13) {
                            ch = this.in.read();
                        }
                    }
                    else if (ch == 42) {
                        while (ch != -1) {
                            ch = this.in.read();
                            if (ch == 42) {
                                for (ch = this.in.read(); ch == 42; ch = this.in.read()) {}
                                if (ch == 47) {
                                    ch = this.read();
                                    break;
                                }
                                continue;
                            }
                        }
                    }
                    else {
                        this.readAheadCh = ch;
                        ch = 47;
                    }
                }
            }
        }
        return ch;
    }
    
    public Reader chain(final Reader rdr) {
        final StripJavaComments newFilter = new StripJavaComments(rdr);
        return newFilter;
    }
}
