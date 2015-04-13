package com.ankamagames.xulor2.core.converter;

import com.ankamagames.xulor2.util.xmlToJava.*;
import com.ankamagames.xulor2.core.*;

public interface Converter<T>
{
    T convert(Class<? extends T> p0, String p1, ElementMap p2);
    
    T convert(Class<? extends T> p0, String p1);
    
    T convert(String p0);
    
    Class<T> convertsTo();
    
    boolean canConvertFromScratch();
    
    boolean canConvertWithoutVariables();
    
    String toJavaCommandLine(AbstractClassDocument p0, DocumentParser p1, Class<? extends T> p2, String p3, Environment p4);
}
