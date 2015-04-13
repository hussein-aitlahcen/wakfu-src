package com.ankamagames.baseImpl.graphics.sound.binary;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import gnu.trove.*;

public class PlayLocalRandomSoundData implements AnimatedElementRunScriptData
{
    private static final Logger m_logger;
    private int m_rollOffPreset;
    private boolean m_stopOnAnimationChange;
    private long[] m_soundIds;
    private byte[] m_gains;
    
    public PlayLocalRandomSoundData(final ExtendedDataInputStream is) {
        super();
        this.load(is);
    }
    
    public PlayLocalRandomSoundData(final int rollOffPreset, final boolean stopOnAnimationChange, final long[] soundIds, final byte[] gains) {
        super();
        this.m_rollOffPreset = rollOffPreset;
        this.m_stopOnAnimationChange = stopOnAnimationChange;
        this.m_soundIds = soundIds;
        this.m_gains = gains;
    }
    
    @Override
    public void play(final AnimatedObject ae) {
        final int index = MathHelper.random(this.m_soundIds.length);
        final long soundId = this.m_soundIds[index];
        if (!SoundDataHelper.canPlaySound(ae, soundId)) {
            return;
        }
        if (!SoundDataHelper.tryRegisterSound(soundId)) {
            return;
        }
        SoundDataHelper.playLocalSound(ae, soundId, this.m_gains[index], (short)1, this.m_rollOffPreset, this.m_stopOnAnimationChange);
    }
    
    @Override
    public int getType() {
        return 6;
    }
    
    @Override
    public void load(final ExtendedDataInputStream is) {
        final int numSounds = is.readInt();
        this.m_soundIds = new long[numSounds];
        this.m_gains = new byte[numSounds];
        for (int i = 0; i < numSounds; ++i) {
            this.m_soundIds[i] = is.readLong();
            this.m_gains[i] = is.readByte();
        }
        this.m_rollOffPreset = is.readInt();
        this.m_stopOnAnimationChange = is.readBooleanBit();
    }
    
    @Override
    public void save(final OutputBitStream os) throws IOException {
        final int numSounds = (this.m_soundIds != null) ? this.m_soundIds.length : 0;
        os.writeInt(numSounds);
        for (int i = 0; i < numSounds; ++i) {
            os.writeLong(this.m_soundIds[i]);
            os.writeByte(this.m_gains[i]);
        }
        os.writeInt(this.m_rollOffPreset);
        os.writeBooleanBit(this.m_stopOnAnimationChange);
    }
    
    public void setRollOffPreset(final int rollOffPreset) {
        this.m_rollOffPreset = rollOffPreset;
    }
    
    public void setStopOnAnimationChange(final boolean stopOnAnimationChange) {
        this.m_stopOnAnimationChange = stopOnAnimationChange;
    }
    
    public void setSoundIds(final long[] soundIds) {
        this.m_soundIds = soundIds;
    }
    
    public void setGains(final byte[] gains) {
        this.m_gains = gains;
    }
    
    @Override
    public void getSoundIds(final TLongArrayList sounds) {
        sounds.add(this.m_soundIds);
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlayLocalRandomSoundData.class);
    }
}
