package com.ankamagames.framework.fileFormat.document;

public interface DocumentContainerEventsHandler
{
    void onLoadBegin(DocumentContainer p0);
    
    void onLoadComplete(DocumentContainer p0);
    
    void onLoadError(DocumentContainer p0, String p1);
    
    void onSaveBegin(DocumentContainer p0);
    
    void onSaveComplete(DocumentContainer p0);
    
    void onSaveError(DocumentContainer p0, String p1);
}
