package com.ankamagames.framework.fileFormat.document;

public interface DocumentAccessor<D extends DocumentContainer>
{
    void open(String p0) throws Exception;
    
    boolean create(String p0) throws Exception;
    
    void close() throws Exception;
    
    void read(D p0, DocumentEntryParser... p1);
    
    void write(D p0);
    
    D getNewDocumentContainer();
}
