package com.ankamagames.baseImpl.graphics.alea.sound;

import com.ankamagames.framework.sound.*;
import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class SoundData implements IAmbienceSound
{
    private int m_id;
    private long m_fileId;
    private float m_maxGain;
    private boolean m_stereo;
    private boolean m_streaming;
    private short m_refDistance;
    private short m_maxDistance;
    private float m_rollOffFactor;
    private int m_minSilentTimeBeforeLoop;
    private int m_maxSilentTimeBeforeLoop;
    
    public SoundData() {
        super();
    }
    
    public SoundData(final int id, final long fileId, final float maxGain, final boolean stereo, final boolean streaming, final short refDistance, final short maxDistance, final float rollOffFactor, final int minSilentTimeBeforeLoop, final int maxSilentTimeBeforeLoop) {
        super();
        this.m_id = id;
        this.m_fileId = fileId;
        this.m_maxGain = maxGain;
        this.m_stereo = stereo;
        this.m_streaming = streaming;
        this.m_refDistance = refDistance;
        this.m_maxDistance = maxDistance;
        this.m_rollOffFactor = rollOffFactor;
        this.m_minSilentTimeBeforeLoop = minSilentTimeBeforeLoop;
        this.m_maxSilentTimeBeforeLoop = maxSilentTimeBeforeLoop;
    }
    
    final void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        this.m_id = istream.readInt();
        this.m_fileId = istream.readLong();
        this.m_maxGain = istream.readFloat();
        this.m_stereo = istream.readBooleanBit();
        this.m_streaming = istream.readBooleanBit();
        this.m_refDistance = istream.readShort();
        this.m_maxDistance = istream.readShort();
        this.m_rollOffFactor = istream.readFloat();
        this.m_minSilentTimeBeforeLoop = istream.readInt();
        this.m_maxSilentTimeBeforeLoop = istream.readInt();
    }
    
    final void save(@NotNull final OutputBitStream ostream) throws IOException {
        ostream.writeInt(this.m_id);
        ostream.writeLong(this.m_fileId);
        ostream.writeFloat(this.m_maxGain);
        ostream.writeBooleanBit(this.m_stereo);
        ostream.writeBooleanBit(this.m_streaming);
        ostream.writeShort(this.m_refDistance);
        ostream.writeShort(this.m_maxDistance);
        ostream.writeFloat(this.m_rollOffFactor);
        ostream.writeInt(this.m_minSilentTimeBeforeLoop);
        ostream.writeInt(this.m_maxSilentTimeBeforeLoop);
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public long getFileId() {
        return this.m_fileId;
    }
    
    @Override
    public boolean getStereo() {
        return this.m_stereo;
    }
    
    @Override
    public float getMaxGain() {
        return this.m_maxGain;
    }
    
    @Override
    public float getMaxDistance() {
        return this.m_maxDistance;
    }
    
    @Override
    public int getMaxTimeBeforeLoop() {
        return this.m_maxSilentTimeBeforeLoop;
    }
    
    @Override
    public float getRollOffFactor() {
        return this.m_rollOffFactor;
    }
    
    @Override
    public float getRefDistance() {
        return this.m_refDistance;
    }
    
    @Override
    public int getMinTimeBeforeLoop() {
        return this.m_minSilentTimeBeforeLoop;
    }
}
