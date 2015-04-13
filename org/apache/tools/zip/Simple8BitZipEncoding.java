package org.apache.tools.zip;

import java.util.*;
import java.nio.*;
import java.io.*;

class Simple8BitZipEncoding implements ZipEncoding
{
    private final char[] highChars;
    private final List<Simple8BitChar> reverseMapping;
    
    public Simple8BitZipEncoding(final char[] highChars) {
        super();
        this.highChars = highChars.clone();
        final List<Simple8BitChar> temp = new ArrayList<Simple8BitChar>(this.highChars.length);
        byte code = 127;
        for (int i = 0; i < this.highChars.length; ++i) {
            final List<Simple8BitChar> list = temp;
            ++code;
            list.add(new Simple8BitChar(code, this.highChars[i]));
        }
        Collections.sort(temp);
        this.reverseMapping = Collections.unmodifiableList((List<? extends Simple8BitChar>)temp);
    }
    
    public char decodeByte(final byte b) {
        if (b >= 0) {
            return (char)b;
        }
        return this.highChars[128 + b];
    }
    
    public boolean canEncodeChar(final char c) {
        if (c >= '\0' && c < '\u0080') {
            return true;
        }
        final Simple8BitChar r = this.encodeHighChar(c);
        return r != null;
    }
    
    public boolean pushEncodedChar(final ByteBuffer bb, final char c) {
        if (c >= '\0' && c < '\u0080') {
            bb.put((byte)c);
            return true;
        }
        final Simple8BitChar r = this.encodeHighChar(c);
        if (r == null) {
            return false;
        }
        bb.put(r.code);
        return true;
    }
    
    private Simple8BitChar encodeHighChar(final char c) {
        int i0 = 0;
        int i = this.reverseMapping.size();
        while (i > i0) {
            final int j = i0 + (i - i0) / 2;
            final Simple8BitChar m = this.reverseMapping.get(j);
            if (m.unicode == c) {
                return m;
            }
            if (m.unicode < c) {
                i0 = j + 1;
            }
            else {
                i = j;
            }
        }
        if (i0 >= this.reverseMapping.size()) {
            return null;
        }
        final Simple8BitChar r = this.reverseMapping.get(i0);
        if (r.unicode != c) {
            return null;
        }
        return r;
    }
    
    public boolean canEncode(final String name) {
        for (int i = 0; i < name.length(); ++i) {
            final char c = name.charAt(i);
            if (!this.canEncodeChar(c)) {
                return false;
            }
        }
        return true;
    }
    
    public ByteBuffer encode(final String name) {
        ByteBuffer out = ByteBuffer.allocate(name.length() + 6 + (name.length() + 1) / 2);
        for (int i = 0; i < name.length(); ++i) {
            final char c = name.charAt(i);
            if (out.remaining() < 6) {
                out = ZipEncodingHelper.growBuffer(out, out.position() + 6);
            }
            if (!this.pushEncodedChar(out, c)) {
                ZipEncodingHelper.appendSurrogate(out, c);
            }
        }
        out.limit(out.position());
        out.rewind();
        return out;
    }
    
    public String decode(final byte[] data) throws IOException {
        final char[] ret = new char[data.length];
        for (int i = 0; i < data.length; ++i) {
            ret[i] = this.decodeByte(data[i]);
        }
        return new String(ret);
    }
    
    private static final class Simple8BitChar implements Comparable<Simple8BitChar>
    {
        public final char unicode;
        public final byte code;
        
        Simple8BitChar(final byte code, final char unicode) {
            super();
            this.code = code;
            this.unicode = unicode;
        }
        
        public int compareTo(final Simple8BitChar a) {
            return this.unicode - a.unicode;
        }
        
        public String toString() {
            return "0x" + Integer.toHexString('\uffff' & this.unicode) + "->0x" + Integer.toHexString(0xFF & this.code);
        }
        
        public boolean equals(final Object o) {
            if (o instanceof Simple8BitChar) {
                final Simple8BitChar other = (Simple8BitChar)o;
                return this.unicode == other.unicode && this.code == other.code;
            }
            return false;
        }
        
        public int hashCode() {
            return this.unicode;
        }
    }
}
