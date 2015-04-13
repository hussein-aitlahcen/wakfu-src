package com.ankamagames.framework.sound.group.music;

import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class MusicData
{
    private long m_musicId;
    private long m_alternativeMusicId;
    private byte m_volume;
    private short m_silenceDuration;
    private byte m_order;
    private int m_numLoops;
    
    public MusicData() {
        super();
    }
    
    public MusicData(final long musicId, final byte volume, final short silenceDuration, final byte order) {
        this(musicId, volume, silenceDuration, order, 1);
    }
    
    public MusicData(final long musicId, final byte volume, final short silenceDuration, final byte order, final int numLoops) {
        this(musicId, musicId, volume, silenceDuration, order, numLoops);
    }
    
    public MusicData(final long musicId, final long alternativeMusicId, final byte volume, final short silenceDuration, final byte order, final int numLoops) {
        super();
        this.m_musicId = musicId;
        this.m_alternativeMusicId = alternativeMusicId;
        this.m_volume = volume;
        this.m_silenceDuration = silenceDuration;
        this.m_order = order;
        this.m_numLoops = numLoops;
    }
    
    public long getMusicId() {
        return this.m_musicId;
    }
    
    public long getAlternativeMusicId() {
        return (this.m_alternativeMusicId == -1L) ? this.m_musicId : this.m_alternativeMusicId;
    }
    
    public byte getVolume() {
        return this.m_volume;
    }
    
    public short getSilenceDuration() {
        return this.m_silenceDuration;
    }
    
    public byte getOrder() {
        return this.m_order;
    }
    
    public int getNumLoops() {
        return this.m_numLoops;
    }
    
    public void load(final ExtendedDataInputStream istream) throws IOException {
        this.m_musicId = istream.readLong();
        this.m_alternativeMusicId = istream.readLong();
        this.m_volume = istream.readByte();
        this.m_silenceDuration = istream.readShort();
        this.m_order = istream.readByte();
        this.m_numLoops = istream.readInt();
    }
    
    public void save(final OutputBitStream ostream) throws IOException {
        ostream.writeLong(this.m_musicId);
        ostream.writeLong(this.m_alternativeMusicId);
        ostream.writeByte(this.m_volume);
        ostream.writeShort(this.m_silenceDuration);
        ostream.writeByte(this.m_order);
        ostream.writeInt(this.m_numLoops);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MusicData)) {
            return false;
        }
        final MusicData data = (MusicData)obj;
        return this.m_musicId == data.m_musicId && this.m_alternativeMusicId == data.m_alternativeMusicId && this.m_volume == data.m_volume && this.m_silenceDuration == data.m_silenceDuration && this.m_order == data.m_order && this.m_numLoops == data.m_numLoops;
    }
}
