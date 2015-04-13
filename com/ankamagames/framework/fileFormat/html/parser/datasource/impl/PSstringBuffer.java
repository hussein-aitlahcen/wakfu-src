package com.ankamagames.framework.fileFormat.html.parser.datasource.impl;

import com.ankamagames.framework.fileFormat.html.parser.datasource.*;

public class PSstringBuffer implements PageSource
{
    private int curPos;
    private CharSequence bufferedSource;
    
    public PSstringBuffer(final CharSequence sbSource) {
        super();
        this.curPos = 0;
        this.bufferedSource = null;
        this.bufferedSource = sbSource;
    }
    
    @Override
    public int getCurrentCursorPosition() {
        return this.curPos;
    }
    
    @Override
    public char getNextChar() {
        if (this.curPos < this.bufferedSource.length()) {
            return this.bufferedSource.charAt(this.curPos++);
        }
        return '\uffff';
    }
    
    @Override
    public char getCurChar() {
        if (this.curPos < this.bufferedSource.length()) {
            return this.bufferedSource.charAt(this.curPos);
        }
        return '\0';
    }
    
    @Override
    public String getSubString(final int start, final int end) {
        if (start >= 0 && start <= this.bufferedSource.length() && end >= 1 && end <= this.bufferedSource.length() && start < end) {
            return this.bufferedSource.subSequence(start, end).toString();
        }
        return null;
    }
    
    @Override
    public boolean hasNextChar() {
        return this.curPos < this.bufferedSource.length();
    }
    
    @Override
    public void setCursorPosition(final int position) {
        this.curPos = position;
    }
    
    @Override
    public RawSourceWrapper getDataSource() {
        return null;
    }
    
    @Override
    public int size() {
        return this.bufferedSource.length();
    }
    
    @Override
    public boolean moveCursorPosition(final int offset) {
        if (this.curPos + offset >= 0 && this.curPos + offset < this.bufferedSource.length()) {
            this.curPos += offset;
            return true;
        }
        return false;
    }
    
    @Override
    public char getChar(final int charIndex) {
        return this.bufferedSource.charAt(charIndex);
    }
}
