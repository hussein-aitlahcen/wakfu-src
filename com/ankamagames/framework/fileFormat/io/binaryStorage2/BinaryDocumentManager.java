package com.ankamagames.framework.fileFormat.io.binaryStorage2;

import org.apache.log4j.*;
import gnu.trove.*;
import java.io.*;

public class BinaryDocumentManager
{
    public static final String DEFAULT_INDEX_NAME = "id";
    private static final Logger m_logger;
    private String m_path;
    private static final BinaryDocumentManager m_instance;
    private final THashMap<String, BinaryDocument> m_openedDocuments;
    
    public static BinaryDocumentManager getInstance() {
        return BinaryDocumentManager.m_instance;
    }
    
    private BinaryDocumentManager() {
        super();
        this.m_path = "";
        this.m_openedDocuments = new THashMap<String, BinaryDocument>();
    }
    
    public void setPath(final String path) {
        this.m_path = path;
    }
    
    public static String getFilenameForClass(final int dataType) {
        return dataType + ".bin";
    }
    
    private BinaryDocument openDocument(final BinaryData data) throws IOException {
        return new BinaryDocument(this.getFilename(data), data.getDataTypeId());
    }
    
    private String getFilename(final BinaryData data) {
        final int dataTypeId = data.getDataTypeId();
        return String.format(this.m_path, dataTypeId, dataTypeId);
    }
    
    public BinaryDocument open(final BinaryData data) throws IOException {
        final String filename = this.getFilename(data);
        BinaryDocument document = this.m_openedDocuments.get(filename);
        if (document == null) {
            document = this.openDocument(data);
            this.m_openedDocuments.put(document.getFileName(), document);
            assert document.getFileName().equals(filename);
        }
        return document;
    }
    
    public void close(final BinaryDocument doc) {
        this.m_openedDocuments.remove(doc.getFileName());
    }
    
    public void closeAll() {
        this.m_openedDocuments.clear();
    }
    
    public <T extends BinaryData> void foreach(final T data, final LoadProcedure<T> reader) throws Exception {
        final BinaryDocument doc = this.open(data);
        BinaryDocumentManager.m_logger.trace((Object)(data.getClass().getSimpleName() + ": " + doc.entryCount() + " entr\u00e9es"));
        doc.foreach(data, reader);
        this.close(doc);
    }
    
    public <T extends BinaryData> boolean getId(final int id, final T data) throws Exception {
        final BinaryDocument doc = this.open(data);
        return doc.getData(id, data);
    }
    
    public <T extends BinaryData> boolean forId(final int id, final T data, final LoadProcedure<T> reader) throws Exception {
        if (this.getId(id, data)) {
            reader.load(data);
            return true;
        }
        BinaryDocumentManager.m_logger.error((Object)("Object inconnu d'id=" + id));
        return false;
    }
    
    public <T extends BinaryData> void foreach(final T data, final String indexName, final int id, final LoadProcedure<T> reader) throws Exception {
        final BinaryDocument doc = this.open(data);
        doc.foreach(data, indexName, id, reader);
    }
    
    public <T extends BinaryData> void foreach(final T data, final String indexName, final String id, final LoadProcedure<T> reader) throws Exception {
        this.foreach(data, indexName, id.hashCode(), reader);
    }
    
    static {
        m_logger = Logger.getLogger((Class)BinaryDocumentManager.class);
        m_instance = new BinaryDocumentManager();
    }
}
