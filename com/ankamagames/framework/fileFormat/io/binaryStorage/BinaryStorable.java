package com.ankamagames.framework.fileFormat.io.binaryStorage;

import org.apache.log4j.*;
import java.util.zip.*;
import java.nio.*;

public abstract class BinaryStorable
{
    protected static final Logger m_logger;
    private static final CRC32 m_crc32;
    private int m_globalStorageId;
    private short m_version;
    private long m_lastSaveDate;
    private long m_saveDelay;
    private boolean m_needToBeSaved;
    private boolean m_unused;
    
    private BinaryStorable() {
        super();
        this.m_needToBeSaved = false;
        this.m_lastSaveDate = System.currentTimeMillis();
        this.m_saveDelay = Long.MAX_VALUE;
        this.m_unused = false;
        this.m_needToBeSaved = false;
    }
    
    protected BinaryStorable(final short version) {
        this();
        this.m_version = version;
    }
    
    public int getGlobalId() {
        return this.m_globalStorageId;
    }
    
    public void setGlobalId(final int id) {
        this.m_globalStorageId = id;
    }
    
    public short getVersion() {
        return this.m_version;
    }
    
    public void setVersion(final short version) {
        this.m_version = version;
    }
    
    public long computeCheckSum(final byte[] serial) {
        long checkSum;
        if (serial != null && serial.length > 0) {
            BinaryStorable.m_crc32.reset();
            BinaryStorable.m_crc32.update(serial);
            checkSum = BinaryStorable.m_crc32.getValue();
        }
        else {
            checkSum = 0L;
        }
        return checkSum;
    }
    
    public final void toggleModified() {
        if (!this.m_unused) {
            if (this.m_saveDelay < Long.MAX_VALUE) {
                this.m_needToBeSaved = true;
            }
        }
        else {
            BinaryStorable.m_logger.error((Object)("toggleModified sur un BinaryStorable flagg\u00e9 UNUSED " + this.getGlobalId()));
        }
    }
    
    public abstract int getBinaryType();
    
    public abstract byte[] getBinaryData();
    
    public abstract void build(final ByteBuffer p0, final int p1, final short p2);
    
    public abstract BinaryStorable createInstance();
    
    public long getSaveDelay() {
        return this.m_saveDelay;
    }
    
    public void setSaveDelay(final long saveDelay) {
        this.m_saveDelay = saveDelay;
    }
    
    public boolean needToBeSaved() {
        return this.m_needToBeSaved;
    }
    
    public long getLastSaveDate() {
        return this.m_lastSaveDate;
    }
    
    public void save(final AbstractBinaryStorage storage) {
        storage.pushOperation(AbstractBinaryStorage.OperationType.SAVE, this);
        this.m_needToBeSaved = false;
        this.m_lastSaveDate = System.currentTimeMillis();
    }
    
    public void destroy(final AbstractBinaryStorage storage) {
        storage.pushOperation(AbstractBinaryStorage.OperationType.DESTROY, this);
        this.m_needToBeSaved = false;
        this.m_lastSaveDate = System.currentTimeMillis();
    }
    
    public boolean isUnused() {
        return this.m_unused;
    }
    
    @Override
    public String toString() {
        return "Bstorable type:" + this.getBinaryType() + ", id:" + this.getGlobalId() + ", version:" + this.getVersion();
    }
    
    static {
        m_logger = Logger.getLogger((Class)BinaryStorable.class);
        m_crc32 = new CRC32();
    }
}
