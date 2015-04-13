package org.jdom;

import java.io.*;
import org.jdom.filter.*;
import java.util.*;

public interface Parent extends Cloneable, Serializable
{
    Object clone();
    
    List cloneContent();
    
    List getContent();
    
    Content getContent(int p0);
    
    List getContent(Filter p0);
    
    int getContentSize();
    
    Iterator getDescendants();
    
    Iterator getDescendants(Filter p0);
    
    Document getDocument();
    
    Parent getParent();
    
    int indexOf(Content p0);
    
    List removeContent();
    
    Content removeContent(int p0);
    
    boolean removeContent(Content p0);
    
    List removeContent(Filter p0);
}
