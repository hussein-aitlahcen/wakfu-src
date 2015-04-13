package com.ankamagames.baseImpl.graphics.alea.environment;

import com.ankamagames.baseImpl.common.clientAndServer.alea.environment.*;
import org.apache.log4j.*;
import com.ankamagames.framework.sound.group.field.*;
import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class SoundDef extends ElementDef
{
    private static final Logger m_logger;
    PositionedSound m_positionedSound;
    int m_soundId;
    
    public SoundDef() {
        super();
    }
    
    public SoundDef(final byte x, final byte y, final short z, final int soundId) {
        super(x, y, z);
        this.m_soundId = soundId;
    }
    
    public SoundDef(final SoundDef def) {
        super(def);
        this.m_soundId = def.m_soundId;
    }
    
    @Override
    public void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        super.load(istream);
        this.m_soundId = istream.readInt();
    }
    
    @Override
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        super.save(ostream);
        ostream.writeInt(this.m_soundId);
    }
    
    @Override
    public String toString() {
        return "SoundDef{m_x=" + this.m_x + ", m_y=" + this.m_y + ", m_z=" + this.m_z + "m_soundId=" + this.m_soundId + '}';
    }
    
    @Override
    public SoundDef duplicate() {
        return new SoundDef(this.m_x, this.m_y, this.m_z, this.m_soundId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)SoundDef.class);
    }
}
