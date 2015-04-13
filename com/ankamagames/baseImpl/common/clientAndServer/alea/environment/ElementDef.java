package com.ankamagames.baseImpl.common.clientAndServer.alea.environment;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class ElementDef implements IElementDef
{
    private static final Logger m_logger;
    public byte m_x;
    public byte m_y;
    public short m_z;
    
    public ElementDef() {
        super();
    }
    
    public ElementDef(final ElementDef def) {
        super();
        this.m_x = def.m_x;
        this.m_y = def.m_y;
        this.m_z = def.m_z;
    }
    
    public ElementDef(final byte x, final byte y, final short z) {
        super();
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
        assert this.m_x >= 0 && this.m_x < 18;
        assert this.m_y >= 0 && this.m_y < 18;
    }
    
    @Override
    public void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        this.m_x = istream.readByte();
        this.m_y = istream.readByte();
        this.m_z = istream.readShort();
        assert this.m_x >= 0 && this.m_x < 18;
        assert this.m_y >= 0 && this.m_y < 18;
    }
    
    @Override
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        assert this.m_x >= 0 && this.m_x < 18;
        assert this.m_y >= 0 && this.m_y < 18;
        ostream.writeByte(this.m_x);
        ostream.writeByte(this.m_y);
        ostream.writeShort(this.m_z);
    }
    
    @Override
    public String toString() {
        return "ElementDef{m_x=" + this.m_x + ", m_y=" + this.m_y + ", m_z=" + this.m_z + '}';
    }
    
    public ElementDef duplicate() {
        throw new UnsupportedOperationException("TODO");
    }
    
    static {
        m_logger = Logger.getLogger((Class)ElementDef.class);
    }
}
