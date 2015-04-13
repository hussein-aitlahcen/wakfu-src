package com.ankamagames.xulor2.util.xmlToJava.init;

import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.xmlToJava.*;

public interface InitLoader
{
    void init(DocumentParser p0);
    
    void addToDocument(ThemeInitClassDocument p0);
    
    boolean isInitialized();
}
