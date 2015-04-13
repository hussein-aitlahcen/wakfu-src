package com.ankamagames.framework.fileFormat.html.parser;

import java.util.*;

public interface TokenTag extends Token
{
    String getTagName();
    
    boolean isEndClosed();
    
    boolean isClosedTag();
    
    List<TagAttribute> getAttrs();
}
