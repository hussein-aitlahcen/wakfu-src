package com.ankamagames.framework.fileFormat.document;

import java.util.*;

public interface DocumentContainer
{
    DocumentEntry getEntryByName(String p0);
    
    ArrayList<DocumentEntry> getEntriesByName(String p0);
    
    void addEventsHandler(DocumentContainerEventsHandler p0);
    
    void notifyOnLoadBegin();
    
    void notifyOnLoadComplete();
    
    void notifyOnLoadError(String p0);
    
    void notifyOnSaveBegin();
    
    void notifyOnSaveComplete();
    
    void notifyOnSaveError(String p0);
}
