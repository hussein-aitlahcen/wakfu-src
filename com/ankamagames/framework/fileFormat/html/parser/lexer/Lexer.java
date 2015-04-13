package com.ankamagames.framework.fileFormat.html.parser.lexer;

import com.ankamagames.framework.fileFormat.html.parser.datasource.*;
import com.ankamagames.framework.fileFormat.html.parser.*;

public interface Lexer
{
    void addTokepProcPluging(TokenProcPlugin p0);
    
    PageSource getPage();
    
    Token getNextToken();
    
    boolean hasNextToken();
}
