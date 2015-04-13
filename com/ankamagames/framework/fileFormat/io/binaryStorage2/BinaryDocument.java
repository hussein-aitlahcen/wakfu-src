package com.ankamagames.framework.fileFormat.io.binaryStorage2;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.index.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.nio.*;
import java.io.*;

class BinaryDocument
{
    private static final Logger m_logger;
    private final Entry[] m_entries;
    private final LightWeightMap<String, Index> m_indexes;
    private final String m_filename;
    private final RandomByteBufferReader m_buffer;
    
    BinaryDocument(final String filename, final int dataTypeId) throws IOException {
        super();
        this.m_filename = filename;
        final byte[] bytes = ContentFileHelper.readFile(this.m_filename);
        final ByteBuffer buff = ByteBuffer.wrap(bytes);
        buff.order(ByteOrder.LITTLE_ENDIAN);
        final int version = buff.getInt() + 756423;
        final RandomByteBufferReader buffer = new RandomByteBufferReader(buff, dataTypeId, version);
        final int entryCount = buffer.getInt();
        this.m_entries = new Entry[entryCount];
        for (int i = 0; i < entryCount; ++i) {
            final long id = buffer.getLong();
            final int pos = buffer.getInt();
            final int size = buffer.getInt();
            final byte seed = buffer.get();
            this.m_entries[i] = new Entry(id, pos, size, seed);
        }
        final int indexCount = buffer.get();
        this.m_indexes = new LightWeightMap<String, Index>(indexCount);
        for (int j = 0; j < indexCount; ++j) {
            final Index index = Index.createIndex(buffer);
            this.m_indexes.put(index.m_name, index);
        }
        final ByteBuffer slice = buff.slice();
        slice.order(buff.order());
        this.m_buffer = new RandomByteBufferReader(slice, dataTypeId, version);
    }
    
    public final String getFileName() {
        return this.m_filename;
    }
    
    private Index getIndex(final String indexName) {
        return this.m_indexes.get(indexName);
    }
    
    final boolean getData(final long id, final BinaryData data) {
        try {
            data.reset();
            final Index index = this.m_indexes.get("id");
            if (index.getEntryCount(id) == 0) {
                BinaryDocument.m_logger.error((Object)("Pas de " + data.getClass().getSimpleName() + " existant. id=" + id), (Throwable)new Exception());
                return false;
            }
            final int cur = index.getEntryCursor(id, 0);
            this.getData(this.m_entries[cur], data);
            return true;
        }
        catch (Exception e) {
            BinaryDocument.m_logger.error((Object)("Probl\u00e8me  de lecture de " + data.getClass().getSimpleName() + "id=" + id), (Throwable)e);
            return false;
        }
    }
    
    private void getData(final Entry entry, final BinaryData data) throws Exception {
        final int position = entry.m_position;
        this.m_buffer.position(position, entry.m_seed);
        data.read(this.m_buffer);
        if (entry.m_size + position != this.m_buffer.position()) {
            throw new Exception("Taille de donn\u00e9e incorrecte ");
        }
    }
    
    final <T extends BinaryData> void foreach(final T data, final LoadProcedure<T> procedure) throws Exception {
        for (int i = 0, size = this.m_entries.length; i < size; ++i) {
            final Entry entry = this.m_entries[i];
            data.reset();
            this.getData(entry, data);
            procedure.load(data);
        }
    }
    
    final <T extends BinaryData> void foreach(final T data, final String indexName, final int objectId, final LoadProcedure<T> procedure) throws Exception {
        final Index index = this.getIndex(indexName);
        for (int count = index.getEntryCount(objectId), i = 0; i < count; ++i) {
            try {
                final int cursor = index.getEntryCursor(objectId, i);
                final Entry entry = this.m_entries[cursor];
                data.reset();
                this.getData(entry, data);
                procedure.load(data);
            }
            catch (Exception e) {
                BinaryDocument.m_logger.error((Object)("Probl\u00e8me  de lecture de " + data.getClass().getSimpleName() + "id=" + objectId + "item num:" + i), (Throwable)e);
            }
        }
    }
    
    public int entryCount() {
        return this.m_entries.length;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BinaryDocument.class);
    }
}
