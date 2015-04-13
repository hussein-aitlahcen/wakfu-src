package com.ankamagames.framework.fileFormat.io.binaryStorage2.index;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import gnu.trove.*;

public abstract class Index<Hash extends TLongHash>
{
    public final String m_name;
    protected final Hash m_indexes;
    
    protected Index(final RandomByteBufferReader buffer) {
        super();
        this.m_name = buffer.readUTF8().intern();
        final int count = buffer.getInt();
        this.m_indexes = this.createTable(count);
        for (int i = 0; i < count; ++i) {
            final long idx = buffer.getLong();
            this.readEntry(idx, buffer);
        }
    }
    
    public static Index createIndex(final RandomByteBufferReader buffer) {
        final boolean unique = buffer.get() != 0;
        if (unique) {
            return new Unique(buffer);
        }
        return new Multi(buffer);
    }
    
    protected abstract Hash createTable(final int p0);
    
    protected abstract void readEntry(final long p0, final RandomByteBufferReader p1);
    
    public abstract int getEntryCount(final long p0);
    
    public abstract int getEntryCursor(final long p0, final int p1);
    
    private static class Unique extends Index<TLongIntHashMap>
    {
        private Unique(final RandomByteBufferReader buffer) {
            super(buffer);
        }
        
        @Override
        protected TLongIntHashMap createTable(final int count) {
            return new TLongIntHashMap(count, 1.0f);
        }
        
        @Override
        protected void readEntry(final long idx, final RandomByteBufferReader buffer) {
            ((TLongIntHashMap)this.m_indexes).put(idx, buffer.getInt());
        }
        
        @Override
        public int getEntryCount(final long id) {
            return ((TLongIntHashMap)this.m_indexes).contains(id) ? 1 : 0;
        }
        
        @Override
        public int getEntryCursor(final long id, final int i) {
            return ((TLongIntHashMap)this.m_indexes).get(id);
        }
    }
    
    private static class Multi extends Index<TLongObjectHashMap<int[]>>
    {
        Multi(final RandomByteBufferReader buffer) {
            super(buffer);
        }
        
        @Override
        protected TLongObjectHashMap<int[]> createTable(final int count) {
            return new TLongObjectHashMap<int[]>(count, 1.0f);
        }
        
        @Override
        protected void readEntry(final long idx, final RandomByteBufferReader buffer) {
            ((TLongObjectHashMap)this.m_indexes).put(idx, buffer.readIntArray());
        }
        
        @Override
        public int getEntryCount(final long id) {
            final int[] cursors = ((TLongObjectHashMap)this.m_indexes).get(id);
            return (cursors == null) ? 0 : cursors.length;
        }
        
        @Override
        public int getEntryCursor(final long id, final int i) {
            return ((int[])((TLongObjectHashMap)this.m_indexes).get(id))[i];
        }
    }
}
