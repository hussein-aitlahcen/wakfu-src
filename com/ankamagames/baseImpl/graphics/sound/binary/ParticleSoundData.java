package com.ankamagames.baseImpl.graphics.sound.binary;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class ParticleSoundData
{
    private long[] m_soundIds;
    private byte[] m_gains;
    private boolean m_loop;
    private int m_rollOffId;
    private int m_fadeOutTime;
    private boolean m_stopOnRemoveAps;
    private int m_delay;
    
    public ParticleSoundData() {
        super();
    }
    
    public ParticleSoundData(final ExtendedDataInputStream is) {
        super();
        this.load(is);
    }
    
    public void load(final ExtendedDataInputStream is) {
        final int numSounds = is.readInt();
        this.m_soundIds = new long[numSounds];
        this.m_gains = new byte[numSounds];
        for (int i = 0; i < numSounds; ++i) {
            this.m_soundIds[i] = is.readLong();
            this.m_gains[i] = is.readByte();
        }
        this.m_loop = is.readBooleanBit();
        this.m_rollOffId = is.readInt();
        this.m_fadeOutTime = is.readInt();
        this.m_stopOnRemoveAps = is.readBooleanBit();
        this.m_delay = is.readInt();
    }
    
    public void save(final OutputBitStream os) throws IOException {
        final int numSounds = (this.m_soundIds != null) ? this.m_soundIds.length : 0;
        os.writeInt(numSounds);
        for (int i = 0; i < numSounds; ++i) {
            os.writeLong(this.m_soundIds[i]);
            os.writeByte(this.m_gains[i]);
        }
        os.writeBooleanBit(this.m_loop);
        os.writeInt(this.m_rollOffId);
        os.writeInt(this.m_fadeOutTime);
        os.writeBooleanBit(this.m_stopOnRemoveAps);
        os.writeInt(this.m_delay);
    }
    
    public long[] getSoundIds() {
        return this.m_soundIds;
    }
    
    public byte[] getGains() {
        return this.m_gains;
    }
    
    public boolean isLoop() {
        return this.m_loop;
    }
    
    public int getRollOffId() {
        return this.m_rollOffId;
    }
    
    public int getFadeOutTime() {
        return this.m_fadeOutTime;
    }
    
    public boolean isStopOnRemoveAps() {
        return this.m_stopOnRemoveAps;
    }
    
    public int getDelay() {
        return this.m_delay;
    }
    
    public void setDelay(final int delay) {
        this.m_delay = delay;
    }
    
    public void setSoundIds(final long[] soundIds) {
        this.m_soundIds = soundIds;
    }
    
    public void setGains(final byte[] gains) {
        this.m_gains = gains;
    }
    
    public void setLoop(final boolean loop) {
        this.m_loop = loop;
    }
    
    public void setRollOffId(final int rollOffId) {
        this.m_rollOffId = rollOffId;
    }
    
    public void setFadeOutTime(final int fadeOutTime) {
        this.m_fadeOutTime = fadeOutTime;
    }
    
    public void setStopOnRemoveAps(final boolean stopOnRemoveAps) {
        this.m_stopOnRemoveAps = stopOnRemoveAps;
    }
    
    public boolean checkSoundGains() {
        if (this.m_soundIds == null) {
            return this.m_gains == null;
        }
        return this.getGains().length == this.getSoundIds().length;
    }
}
