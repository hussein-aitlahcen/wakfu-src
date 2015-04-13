package com.ankamagames.baseImpl.graphics.sound.binary;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import gnu.trove.*;

public class PlaySoundData implements AnimatedElementRunScriptData
{
    private static final Logger m_logger;
    private boolean m_stopOnAnimationChange;
    private long m_soundId;
    private byte m_gain;
    private short m_playCount;
    
    public PlaySoundData() {
        super();
        this.m_gain = 100;
        this.m_playCount = 1;
    }
    
    public PlaySoundData(final ExtendedDataInputStream is) {
        super();
        this.m_gain = 100;
        this.m_playCount = 1;
        this.load(is);
    }
    
    public PlaySoundData(final boolean stopOnAnimationChange, final long soundId, final byte gain, final short playCount) {
        super();
        this.m_gain = 100;
        this.m_playCount = 1;
        this.m_stopOnAnimationChange = stopOnAnimationChange;
        this.m_soundId = soundId;
        this.m_gain = gain;
        this.m_playCount = playCount;
    }
    
    @Override
    public void play(final AnimatedObject ae) {
        if (!SoundDataHelper.canPlaySound(ae, this.m_soundId)) {
            return;
        }
        if (!SoundDataHelper.tryRegisterSound(this.m_soundId)) {
            return;
        }
        SoundDataHelper.playSound(ae, this.m_soundId, this.m_gain, this.m_playCount, this.m_stopOnAnimationChange);
    }
    
    @Override
    public int getType() {
        return 3;
    }
    
    @Override
    public void load(final ExtendedDataInputStream is) {
        this.m_soundId = is.readLong();
        this.m_gain = is.readByte();
        this.m_stopOnAnimationChange = is.readBooleanBit();
        this.m_playCount = is.readShort();
    }
    
    @Override
    public void save(final OutputBitStream os) throws IOException {
        os.writeLong(this.m_soundId);
        os.writeByte(this.m_gain);
        os.writeBooleanBit(this.m_stopOnAnimationChange);
        os.writeShort(this.m_playCount);
    }
    
    public void setStopOnAnimationChange(final boolean stopOnAnimationChange) {
        this.m_stopOnAnimationChange = stopOnAnimationChange;
    }
    
    public void setSoundId(final long soundId) {
        this.m_soundId = soundId;
    }
    
    public void setGain(final byte gain) {
        this.m_gain = gain;
    }
    
    public void setPlayCount(final short playCount) {
        this.m_playCount = playCount;
    }
    
    @Override
    public void getSoundIds(final TLongArrayList sounds) {
        sounds.add(this.m_soundId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlaySoundData.class);
    }
}
