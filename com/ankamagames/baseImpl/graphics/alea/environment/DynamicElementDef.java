package com.ankamagames.baseImpl.graphics.alea.environment;

import com.ankamagames.baseImpl.common.clientAndServer.alea.environment.*;
import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class DynamicElementDef extends ElementDef
{
    private static final Logger m_logger;
    public int m_id;
    public int m_gfxId;
    public int m_type;
    public byte m_direction;
    public boolean m_occluder;
    public byte m_height;
    public String m_baseAnimation;
    public String m_params;
    
    public DynamicElementDef() {
        super();
    }
    
    public DynamicElementDef(final int id, final int gfxId, final int type, final byte direction, final byte x, final byte y, final short z, final boolean occluder, final byte height, final String baseAnimation, final String params) {
        super(x, y, z);
        this.m_id = id;
        this.m_gfxId = gfxId;
        this.m_type = type;
        this.m_direction = direction;
        this.m_occluder = occluder;
        this.m_height = height;
        this.m_baseAnimation = baseAnimation;
        this.m_params = params;
    }
    
    @Override
    public void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        super.load(istream);
        this.m_id = istream.readInt();
        this.m_gfxId = istream.readInt();
        this.m_type = istream.readShort();
        this.m_direction = istream.readByte();
        this.m_occluder = istream.readBooleanBit();
        this.m_height = istream.readByte();
        this.m_baseAnimation = istream.readString();
        this.m_params = istream.readString();
    }
    
    @Override
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        super.save(ostream);
        ostream.writeInt(this.m_id);
        ostream.writeInt(this.m_gfxId);
        ostream.writeShort((short)this.m_type);
        ostream.writeByte(this.m_direction);
        ostream.writeBooleanBit(this.m_occluder);
        ostream.writeByte(this.m_height);
        ostream.writeString(this.m_baseAnimation);
        ostream.writeString(this.m_params);
    }
    
    @Override
    public String toString() {
        return "SoundDef{m_id=" + this.m_id + ", m_gfxId=" + this.m_gfxId + ", m_type=" + this.m_type + ", m_direction=" + this.m_direction + ", m_x=" + this.m_x + ", m_y=" + this.m_y + ", m_z=" + this.m_z + ", m_occluder=" + this.m_occluder + ", m_height=" + this.m_height + ", m_baseAnimation=" + this.m_baseAnimation + ", m_params=" + this.m_params + '}';
    }
    
    @Override
    public DynamicElementDef duplicate() {
        return new DynamicElementDef(this.m_id, this.m_gfxId, this.m_type, this.m_direction, this.m_x, this.m_y, this.m_z, this.m_occluder, this.m_height, this.m_baseAnimation, this.m_params);
    }
    
    static {
        m_logger = Logger.getLogger((Class)DynamicElementDef.class);
    }
}
