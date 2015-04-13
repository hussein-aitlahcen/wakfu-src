package com.ankamagames.framework.fileFormat.html.parser;

public interface TokenIgnoreTagValue extends Token
{
    TokenTag getParentTag();
    
    String getValue();
}
