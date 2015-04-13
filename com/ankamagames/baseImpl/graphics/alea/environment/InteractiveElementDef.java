package com.ankamagames.baseImpl.graphics.alea.environment;

import com.ankamagames.baseImpl.common.clientAndServer.alea.environment.*;
import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class InteractiveElementDef implements IElementDef
{
    private static final Logger m_logger;
    public long m_id;
    public short m_type;
    public int[] m_views;
    public byte[] m_data;
    public boolean m_clientOnly;
    public short m_landMarkType;
    
    public InteractiveElementDef() {
        super();
        this.m_landMarkType = -1;
    }
    
    @Override
    public void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        this.m_id = istream.readLong();
        this.m_type = istream.readShort();
        final int numView = istream.readByte() & 0xFF;
        this.m_views = istream.readInts(numView);
        final int dataSize = istream.readShort() & 0xFFFF;
        this.m_data = istream.readBytes(dataSize);
        this.m_clientOnly = istream.readBooleanBit();
        this.m_landMarkType = istream.readShort();
    }
    
    @Override
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        ostream.writeLong(this.m_id);
        ostream.writeShort(this.m_type);
        if (this.m_views == null) {
            ostream.writeByte((byte)0);
        }
        else {
            ostream.writeByte((byte)(this.m_views.length & 0xFF));
            for (int i = 0; i < this.m_views.length; ++i) {
                ostream.writeInt(this.m_views[i]);
            }
        }
        ostream.writeShort((short)(this.m_data.length & 0xFFFF));
        ostream.writeBytes(this.m_data);
        ostream.writeBooleanBit(this.m_clientOnly);
        ostream.writeShort(this.m_landMarkType);
        ostream.align();
    }
    
    static {
        m_logger = Logger.getLogger((Class)InteractiveElementDef.class);
    }
}
