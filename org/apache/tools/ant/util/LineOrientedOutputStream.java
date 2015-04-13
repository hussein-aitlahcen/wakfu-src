package org.apache.tools.ant.util;

import java.io.*;

public abstract class LineOrientedOutputStream extends OutputStream
{
    private static final int INTIAL_SIZE = 132;
    private static final int CR = 13;
    private static final int LF = 10;
    private ByteArrayOutputStream buffer;
    private boolean skip;
    
    public LineOrientedOutputStream() {
        super();
        this.buffer = new ByteArrayOutputStream(132);
        this.skip = false;
    }
    
    public final void write(final int cc) throws IOException {
        final byte c = (byte)cc;
        if (c == 10 || c == 13) {
            if (!this.skip) {
                this.processBuffer();
            }
        }
        else {
            this.buffer.write(cc);
        }
        this.skip = (c == 13);
    }
    
    public void flush() throws IOException {
    }
    
    protected void processBuffer() throws IOException {
        try {
            this.processLine(this.buffer.toByteArray());
        }
        finally {
            this.buffer.reset();
        }
    }
    
    protected abstract void processLine(final String p0) throws IOException;
    
    protected void processLine(final byte[] line) throws IOException {
        this.processLine(new String(line));
    }
    
    public void close() throws IOException {
        if (this.buffer.size() > 0) {
            this.processBuffer();
        }
        super.close();
    }
    
    public final void write(final byte[] b, final int off, final int len) throws IOException {
        int blockStartOffset;
        int offset = blockStartOffset = off;
        int remaining = len;
        while (remaining > 0) {
            while (remaining > 0 && b[offset] != 10 && b[offset] != 13) {
                ++offset;
                --remaining;
            }
            final int blockLength = offset - blockStartOffset;
            if (blockLength > 0) {
                this.buffer.write(b, blockStartOffset, blockLength);
            }
            while (remaining > 0 && (b[offset] == 10 || b[offset] == 13)) {
                this.write(b[offset]);
                ++offset;
                --remaining;
            }
            blockStartOffset = offset;
        }
    }
}
