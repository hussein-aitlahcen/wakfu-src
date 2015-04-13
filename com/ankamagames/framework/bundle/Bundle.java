package com.ankamagames.framework.bundle;

import gnu.trove.*;
import org.apache.log4j.*;
import java.util.*;
import java.nio.charset.*;
import java.io.*;

public class Bundle
{
    private final TIntObjectHashMap<String> m_bundle;
    protected static final Logger m_logger;
    
    public Bundle(final InputStream stream) {
        super();
        this.m_bundle = new TIntObjectHashMap<String>(20000, 1.0f);
        this.load(stream);
    }
    
    private void fillBundleWithReaderContent(final LineReader reader) {
        if (reader.m_key == null) {
            return;
        }
        String value = reader.m_value;
        if (value != null && value.length() > 0) {
            for (int maxPos = value.length() - 1, i = 0; i < maxPos; ++i) {
                if (value.charAt(i) == '\\') {
                    final char nextChar = value.charAt(i + 1);
                    if (nextChar == 'n') {
                        value = new StringBuilder(value.length()).append(value.substring(0, i)).append('\n').append(value.substring(i + 2)).toString();
                        --maxPos;
                    }
                }
            }
        }
        else {
            value = "";
        }
        this.m_bundle.put(reader.m_key.hashCode(), value.intern());
    }
    
    public void load(final InputStream stream) {
        final LineReader reader = new LineReader(stream);
        this.m_bundle.clear();
        while (reader.advance()) {
            this.fillBundleWithReaderContent(reader);
        }
        this.fillBundleWithReaderContent(reader);
        this.m_bundle.compact();
    }
    
    public String get(final String key) {
        return this.get(key.hashCode());
    }
    
    public String get(final int key) throws MissingResourceException {
        return this.m_bundle.get(key);
    }
    
    public boolean containsKey(final String key) {
        return this.containsKey(key.hashCode());
    }
    
    public boolean containsKey(final int key) {
        return this.m_bundle.containsKey(key);
    }
    
    static {
        m_logger = Logger.getLogger((Class)Bundle.class);
    }
    
    static class LineReader extends InputStreamReader
    {
        private String m_key;
        private String m_value;
        private char[] m_buffer;
        private char[] m_lineBuf;
        private int m_currentOffset;
        private int m_bufferLimit;
        
        public LineReader(final InputStream in) {
            super(in, Charset.forName("UTF-8"));
            this.m_buffer = new char[8192];
            this.m_lineBuf = new char[1024];
            this.m_currentOffset = 0;
            this.m_bufferLimit = 0;
        }
        
        private void fillKeyAndValue(final int length) {
            for (int i = 1; i < length; ++i) {
                if (this.m_lineBuf[i] == '=') {
                    this.m_key = String.valueOf(this.m_lineBuf, 0, i);
                    this.m_value = String.valueOf(this.m_lineBuf, i + 1, length - i - 1);
                    return;
                }
            }
        }
        
        public boolean advance() {
            int len = 0;
            boolean skipWhiteSpace = true;
            boolean isCommentLine = false;
            boolean isNewLine = true;
            boolean appendedLineBegin = false;
            boolean precedingBackslash = false;
            boolean skipLF = false;
            this.m_key = null;
            this.m_value = null;
            while (true) {
                if (this.m_currentOffset >= this.m_bufferLimit) {
                    try {
                        this.m_bufferLimit = this.read(this.m_buffer);
                    }
                    catch (IOException e) {
                        Bundle.m_logger.error((Object)"Exception", (Throwable)e);
                    }
                    this.m_currentOffset = 0;
                    if (this.m_bufferLimit <= 0) {
                        if (len != 0) {
                            this.fillKeyAndValue(len);
                        }
                        return false;
                    }
                }
                final char c = this.m_buffer[this.m_currentOffset++];
                if (skipLF) {
                    skipLF = false;
                    if (c == '\n') {
                        continue;
                    }
                }
                if (skipWhiteSpace) {
                    if (c == ' ' || c == '\t') {
                        continue;
                    }
                    if (c == '\f') {
                        continue;
                    }
                    if (!appendedLineBegin) {
                        if (c == '\r') {
                            continue;
                        }
                        if (c == '\n') {
                            continue;
                        }
                    }
                    skipWhiteSpace = false;
                    appendedLineBegin = false;
                }
                if (isNewLine) {
                    isNewLine = false;
                    if (c == '#' || c == '!') {
                        isCommentLine = true;
                        continue;
                    }
                }
                if (c != '\n' && c != '\r') {
                    this.m_lineBuf[len++] = c;
                    if (len == this.m_lineBuf.length) {
                        int newLength = this.m_lineBuf.length * 2;
                        if (newLength < 0) {
                            newLength = Integer.MAX_VALUE;
                        }
                        final char[] buf = new char[newLength];
                        System.arraycopy(this.m_lineBuf, 0, buf, 0, this.m_lineBuf.length);
                        this.m_lineBuf = buf;
                    }
                    precedingBackslash = (c == '\\' && !precedingBackslash);
                }
                else if (isCommentLine || len == 0) {
                    isCommentLine = false;
                    isNewLine = true;
                    skipWhiteSpace = true;
                    len = 0;
                }
                else {
                    if (this.m_currentOffset >= this.m_bufferLimit) {
                        try {
                            this.m_bufferLimit = this.read(this.m_buffer);
                        }
                        catch (IOException e) {
                            Bundle.m_logger.error((Object)"Exception", (Throwable)e);
                        }
                        this.m_currentOffset = 0;
                        if (this.m_bufferLimit <= 0) {
                            if (len != 0) {
                                this.fillKeyAndValue(len);
                            }
                            return false;
                        }
                    }
                    if (!precedingBackslash) {
                        if (len != 0) {
                            this.fillKeyAndValue(len);
                        }
                        return true;
                    }
                    --len;
                    skipWhiteSpace = true;
                    appendedLineBegin = true;
                    precedingBackslash = false;
                    if (c != '\r') {
                        continue;
                    }
                    skipLF = true;
                }
            }
        }
    }
}
