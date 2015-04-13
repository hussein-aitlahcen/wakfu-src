package com.ankamagames.baseImpl.graphics.alea.environment;

import com.ankamagames.baseImpl.common.clientAndServer.alea.environment.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class ParticleDef extends ElementDef
{
    private static final Logger m_logger;
    CellParticleSystem m_positionedParticles;
    int m_systemId;
    byte m_level;
    byte m_offsetX;
    byte m_offsetY;
    byte m_offsetZ;
    byte m_lod;
    
    public ParticleDef() {
        super();
        this.m_systemId = 0;
        this.m_level = 0;
    }
    
    public ParticleDef(final byte x, final byte y, final short z, final int systemId, final byte level, final byte offsetX, final byte offsetY, final byte offsetZ, final byte lod) {
        super(x, y, z);
        this.m_systemId = 0;
        this.m_level = 0;
        this.m_systemId = systemId;
        this.m_level = level;
        this.m_offsetX = offsetX;
        this.m_offsetY = offsetY;
        this.m_offsetZ = offsetZ;
        this.m_lod = lod;
    }
    
    @Override
    public void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        super.load(istream);
        this.m_systemId = istream.readInt();
        this.m_level = istream.readByte();
        this.m_offsetX = istream.readByte();
        this.m_offsetY = istream.readByte();
        this.m_offsetZ = istream.readByte();
        this.m_lod = istream.readByte();
    }
    
    @Override
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        super.save(ostream);
        ostream.writeInt(this.m_systemId);
        ostream.writeByte(this.m_level);
        ostream.writeByte(this.m_offsetX);
        ostream.writeByte(this.m_offsetY);
        ostream.writeByte(this.m_offsetZ);
        ostream.writeByte(this.m_lod);
    }
    
    @Override
    public String toString() {
        return "ParticleDef{m_systemId=" + this.m_systemId + ", m_level=" + this.m_level + "m_x=" + this.m_x + ", m_y=" + this.m_y + ", m_z=" + this.m_z + ", m_offsetX=" + this.m_offsetX + ", m_offsetY=" + this.m_offsetY + ", m_offsetZ=" + this.m_offsetZ + ", m_lod=" + this.m_lod + '}';
    }
    
    @Override
    public ParticleDef duplicate() {
        return new ParticleDef(this.m_x, this.m_y, this.m_z, this.m_systemId, this.m_level, this.m_offsetY, this.m_offsetY, this.m_offsetZ, this.m_lod);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ParticleDef.class);
    }
}
