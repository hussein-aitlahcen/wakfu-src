package com.ankamagames.framework.fileFormat.io.binaryStorage.descriptor;

import org.apache.log4j.*;
import java.io.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class IndexEntry implements EntryDescriptor, Poolable
{
    private static final Logger m_logger;
    public int idFile;
    public long position;
    public long checksum;
    public String value;
    private static final ObjectPool m_pool;
    
    private IndexEntry() {
        super();
    }
    
    public IndexEntry(final String value, final int id, final long pos, final long checksum) {
        super();
        this.value = value;
        this.idFile = id;
        this.position = pos;
        this.checksum = checksum;
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeUTF(this.value);
        out.writeInt(this.idFile);
        out.writeLong(this.position);
        out.writeLong(this.checksum);
    }
    
    @Override
    public void read(final DataInputStream in) throws IOException {
        this.value = in.readUTF();
        this.idFile = in.readInt();
        this.position = in.readLong();
        this.checksum = in.readLong();
    }
    
    public static IndexEntry checkOut(final String value, final int id, final long pos, final long checksum) {
        IndexEntry entry;
        try {
            entry = (IndexEntry)IndexEntry.m_pool.borrowObject();
        }
        catch (Exception e) {
            entry = new IndexEntry();
            IndexEntry.m_logger.error((Object)"Erreur lors d'un checkout d'un IndexEntry", (Throwable)e);
        }
        entry.value = value;
        entry.idFile = id;
        entry.position = pos;
        entry.checksum = checksum;
        return entry;
    }
    
    public static IndexEntry checkOut() {
        IndexEntry entry;
        try {
            entry = (IndexEntry)IndexEntry.m_pool.borrowObject();
        }
        catch (Exception e) {
            entry = new IndexEntry();
            IndexEntry.m_logger.error((Object)"Erreur lors d'un checkout d'un IndexEntry", (Throwable)e);
        }
        return entry;
    }
    
    public void release() {
        try {
            IndexEntry.m_pool.returnObject(this);
        }
        catch (Exception e) {
            IndexEntry.m_logger.error((Object)"Erreur lors d'un release d'un IndexEntry", (Throwable)e);
        }
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        this.value = null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)IndexEntry.class);
        m_pool = new MonitoredPool(new ObjectFactory<IndexEntry>() {
            @Override
            public IndexEntry makeObject() {
                return new IndexEntry(null);
            }
        });
    }
}
