package com.ankamagames.framework.fileFormat.html.parser.lexer;

import com.ankamagames.framework.fileFormat.html.parser.*;

public interface TokenProcPlugin
{
    String getEntryString();
    
     <T extends Token> T parse() throws CommonException;
}
