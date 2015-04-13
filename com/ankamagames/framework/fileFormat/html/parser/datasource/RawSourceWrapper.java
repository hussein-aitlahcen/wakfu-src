package com.ankamagames.framework.fileFormat.html.parser.datasource;

public interface RawSourceWrapper
{
    char getNextChar();
    
    Object getRawSource();
}
