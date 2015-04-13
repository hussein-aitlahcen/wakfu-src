package com.ankamagames.framework.fileFormat.html.parser;

import com.ankamagames.framework.fileFormat.html.parser.datasource.*;

public interface Token extends Cloneable
{
    String toHtml();
    
    int getIndex();
    
    int getStartPosition();
    
    int getEndPosition();
    
    PageSource getPage();
}
