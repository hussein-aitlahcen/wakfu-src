package com.ankamagames.baseImpl.graphics.alea.animatedElement.actions;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;

public class AnmActionAddParticle extends AnmAction
{
    private static final Logger m_logger;
    private int m_particleId;
    private short m_offsetX;
    private short m_offsetY;
    private short m_offsetZ;
    
    public int getParticleId() {
        return this.m_particleId;
    }
    
    @Override
    public boolean run(final AnimatedObject animatedElement) {
        try {
            final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(this.m_particleId);
            system.setFightId(animatedElement.getCurrentFightId());
            IsoParticleSystemManager.getInstance().addParticleSystem(system);
            if (animatedElement instanceof AnimatedElement) {
                final AnimatedElement e = (AnimatedElement)animatedElement;
                system.setPosition(e.getWorldX() + this.m_offsetX, e.getWorldY() + this.m_offsetY, e.getWorldCellAltitude() + this.m_offsetZ);
                MaskableHelper.setUndefined(system);
            }
        }
        catch (Exception e2) {
            AnmActionAddParticle.m_logger.error((Object)("AddParticle (" + this.m_particleId + ") depuis un animatedElement " + animatedElement), (Throwable)e2);
        }
        return false;
    }
    
    @Override
    public void load(final byte parametersCount, final ExtendedDataInputStream bitStream) throws Exception {
        this.m_particleId = bitStream.readInt();
        if (parametersCount == 3) {
            this.m_offsetX = bitStream.readShort();
            this.m_offsetY = bitStream.readShort();
        }
        else if (parametersCount == 4) {
            this.m_offsetX = bitStream.readShort();
            this.m_offsetY = bitStream.readShort();
            this.m_offsetZ = bitStream.readShort();
        }
    }
    
    @Override
    public void write(final OutputBitStream ostream) throws IOException {
        super.write(ostream);
        int paramCount = 1;
        if (this.m_offsetX != 0 || this.m_offsetY != 0 || this.m_offsetZ != 0) {
            paramCount = 4;
        }
        ostream.writeByte((byte)paramCount);
        ostream.writeInt(this.m_particleId);
        if (paramCount == 4) {
            ostream.writeShort(this.m_offsetX);
            ostream.writeShort(this.m_offsetY);
            ostream.writeShort(this.m_offsetZ);
        }
    }
    
    @Override
    public AnmActionTypes getType() {
        return AnmActionTypes.ADD_PARTICLE;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnmActionAddParticle)) {
            return false;
        }
        final AnmActionAddParticle that = (AnmActionAddParticle)o;
        return this.m_particleId == that.m_particleId && this.m_offsetX == that.m_offsetX && this.m_offsetY == that.m_offsetY && this.m_offsetZ == that.m_offsetZ;
    }
    
    @Override
    public final int hashCode() {
        int result = this.getType().getId();
        result = 31 * result + this.m_particleId;
        result = 31 * result + this.m_offsetX;
        result = 31 * result + this.m_offsetY;
        result = 31 * result + this.m_offsetZ;
        return result;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnmActionAddParticle.class);
    }
}
