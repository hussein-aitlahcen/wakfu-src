package com.ankamagames.framework.fileFormat.io.binaryStorage.descriptor;

import java.io.*;

public final class ReadOnlyIndexEntry implements EntryDescriptor
{
    private int m_type;
    private String m_indexName;
    private String m_indexValue;
    private long m_position;
    
    public ReadOnlyIndexEntry() {
        super();
    }
    
    public ReadOnlyIndexEntry(final int type, final String indexName, final String indexValue, final long position) {
        super();
        this.m_type = type;
        this.m_indexName = ((indexName != null) ? indexName.intern() : null);
        this.m_indexValue = ((indexValue != null) ? indexValue.intern() : null);
        this.m_position = position;
    }
    
    public int getType() {
        return this.m_type;
    }
    
    public void setType(final int type) {
        this.m_type = type;
    }
    
    public String getIndexName() {
        return this.m_indexName;
    }
    
    public void setIndexName(final String indexName) {
        this.m_indexName = indexName;
    }
    
    public String getIndexValue() {
        return this.m_indexValue;
    }
    
    public void setIndexValue(final String indexValue) {
        this.m_indexValue = indexValue;
    }
    
    public long getPosition() {
        return this.m_position;
    }
    
    public void setPosition(final long position) {
        this.m_position = position;
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeInt(this.m_type);
        out.writeUTF(this.m_indexName);
        out.writeUTF(this.m_indexValue);
        out.writeLong(this.m_position);
    }
    
    @Override
    public void read(final DataInputStream in) throws IOException {
        this.m_type = in.readInt();
        this.m_indexName = in.readUTF().intern();
        this.m_indexValue = in.readUTF().intern();
        this.m_position = in.readLong();
    }
}
