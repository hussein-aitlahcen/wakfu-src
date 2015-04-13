package com.ankamagames.framework.fileFormat.html.parser.datasource;

public interface PageSource
{
    public static final char EOS = '\uffff';
    
    int getCurrentCursorPosition();
    
    void setCursorPosition(int p0);
    
    boolean moveCursorPosition(int p0);
    
    int size();
    
    boolean hasNextChar();
    
    char getChar(int p0);
    
    char getNextChar();
    
    char getCurChar();
    
    String getSubString(int p0, int p1);
    
    RawSourceWrapper getDataSource();
}
