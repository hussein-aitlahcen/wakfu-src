package com.ankamagames.wakfu.common.datas.group;

import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;

public class GuildBlazon
{
    private long m_id;
    private byte m_shapeId;
    private byte m_symbolId;
    private short m_shapeColor;
    private short m_symbolColor;
    
    public GuildBlazon(final byte shapeId, final short shapeColor, final byte symbolId, final short symbolColor) {
        super();
        this.m_shapeId = shapeId;
        this.m_shapeColor = shapeColor;
        this.m_symbolId = symbolId;
        this.m_symbolColor = symbolColor;
        this.setId();
    }
    
    public GuildBlazon(final long id) {
        super();
        this.m_id = id;
        final int ids = MathHelper.getFirstIntFromLong(id);
        final int colors = MathHelper.getSecondIntFromLong(id);
        this.m_shapeId = (byte)MathHelper.getFirstShortFromInt(ids);
        this.m_symbolId = (byte)MathHelper.getSecondShortFromInt(ids);
        this.m_shapeColor = MathHelper.getFirstShortFromInt(colors);
        this.m_symbolColor = MathHelper.getSecondShortFromInt(colors);
    }
    
    public byte getShapeId() {
        return this.m_shapeId;
    }
    
    public short getShapeColor() {
        return this.m_shapeColor;
    }
    
    public byte getSymbolId() {
        return this.m_symbolId;
    }
    
    public short getSymbolColor() {
        return this.m_symbolColor;
    }
    
    public long getId() {
        if (this.m_id == 0L) {
            this.setId();
        }
        return this.m_id;
    }
    
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putShort(MathHelper.getShortFromTwoBytes(this.m_shapeId, this.m_symbolId));
        buffer.putInt(MathHelper.getIntFromTwoShort(this.m_shapeColor, this.m_symbolColor));
        return true;
    }
    
    public boolean unserialize(final ByteBuffer buffer) {
        final short ids = buffer.getShort();
        final int colors = buffer.getInt();
        this.m_shapeId = MathHelper.getFirstByteFromShort(ids);
        this.m_symbolId = MathHelper.getSecondByteFromShort(ids);
        this.m_shapeColor = MathHelper.getFirstShortFromInt(colors);
        this.m_symbolColor = MathHelper.getSecondShortFromInt(colors);
        this.setId();
        return true;
    }
    
    private void setId() {
        final int ids = MathHelper.getIntFromTwoShort(this.m_shapeId, this.m_symbolId);
        final int colors = MathHelper.getIntFromTwoShort(this.m_shapeColor, this.m_symbolColor);
        this.m_id = MathHelper.getLongFromTwoInt(ids, colors);
    }
    
    public int serializedSize() {
        return 6;
    }
    
    @Override
    public String toString() {
        return "GuildBlazon{m_shapeId=" + this.m_shapeId + ", m_symbolId=" + this.m_symbolId + ", m_shapeColor=" + this.m_shapeColor + ", m_symbolColor=" + this.m_symbolColor + '}';
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuildBlazon)) {
            return false;
        }
        final GuildBlazon that = (GuildBlazon)o;
        return this.m_id == that.m_id;
    }
    
    @Override
    public int hashCode() {
        return (int)(this.m_id ^ this.m_id >>> 32);
    }
}
