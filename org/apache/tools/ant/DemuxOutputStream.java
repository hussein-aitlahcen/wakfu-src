package org.apache.tools.ant;

import java.util.*;
import java.io.*;

public class DemuxOutputStream extends OutputStream
{
    private static final int MAX_SIZE = 1024;
    private static final int INTIAL_SIZE = 132;
    private static final int CR = 13;
    private static final int LF = 10;
    private WeakHashMap<Thread, BufferInfo> buffers;
    private Project project;
    private boolean isErrorStream;
    
    public DemuxOutputStream(final Project project, final boolean isErrorStream) {
        super();
        this.buffers = new WeakHashMap<Thread, BufferInfo>();
        this.project = project;
        this.isErrorStream = isErrorStream;
    }
    
    private BufferInfo getBufferInfo() {
        final Thread current = Thread.currentThread();
        BufferInfo bufferInfo = this.buffers.get(current);
        if (bufferInfo == null) {
            bufferInfo = new BufferInfo();
            bufferInfo.buffer = new ByteArrayOutputStream(132);
            bufferInfo.crSeen = false;
            this.buffers.put(current, bufferInfo);
        }
        return bufferInfo;
    }
    
    private void resetBufferInfo() {
        final Thread current = Thread.currentThread();
        final BufferInfo bufferInfo = this.buffers.get(current);
        try {
            bufferInfo.buffer.close();
        }
        catch (IOException ex) {}
        bufferInfo.buffer = new ByteArrayOutputStream();
        bufferInfo.crSeen = false;
    }
    
    private void removeBuffer() {
        final Thread current = Thread.currentThread();
        this.buffers.remove(current);
    }
    
    public void write(final int cc) throws IOException {
        final byte c = (byte)cc;
        final BufferInfo bufferInfo = this.getBufferInfo();
        if (c == 10) {
            bufferInfo.buffer.write(cc);
            this.processBuffer(bufferInfo.buffer);
        }
        else {
            if (bufferInfo.crSeen) {
                this.processBuffer(bufferInfo.buffer);
            }
            bufferInfo.buffer.write(cc);
        }
        bufferInfo.crSeen = (c == 13);
        if (!bufferInfo.crSeen && bufferInfo.buffer.size() > 1024) {
            this.processBuffer(bufferInfo.buffer);
        }
    }
    
    protected void processBuffer(final ByteArrayOutputStream buffer) {
        final String output = buffer.toString();
        this.project.demuxOutput(output, this.isErrorStream);
        this.resetBufferInfo();
    }
    
    protected void processFlush(final ByteArrayOutputStream buffer) {
        final String output = buffer.toString();
        this.project.demuxFlush(output, this.isErrorStream);
        this.resetBufferInfo();
    }
    
    public void close() throws IOException {
        this.flush();
        this.removeBuffer();
    }
    
    public void flush() throws IOException {
        final BufferInfo bufferInfo = this.getBufferInfo();
        if (bufferInfo.buffer.size() > 0) {
            this.processFlush(bufferInfo.buffer);
        }
    }
    
    public void write(final byte[] b, final int off, final int len) throws IOException {
        int blockStartOffset;
        int offset = blockStartOffset = off;
        int remaining = len;
        final BufferInfo bufferInfo = this.getBufferInfo();
        while (remaining > 0) {
            while (remaining > 0 && b[offset] != 10 && b[offset] != 13) {
                ++offset;
                --remaining;
            }
            final int blockLength = offset - blockStartOffset;
            if (blockLength > 0) {
                bufferInfo.buffer.write(b, blockStartOffset, blockLength);
            }
            while (remaining > 0 && (b[offset] == 10 || b[offset] == 13)) {
                this.write(b[offset]);
                ++offset;
                --remaining;
            }
            blockStartOffset = offset;
        }
    }
    
    private static class BufferInfo
    {
        private ByteArrayOutputStream buffer;
        private boolean crSeen;
        
        private BufferInfo() {
            super();
            this.crSeen = false;
        }
    }
}
