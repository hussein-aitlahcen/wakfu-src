package com.ankamagames.framework.sound.stream;

import java.util.concurrent.atomic.*;
import java.nio.*;

public class JOrbisVirtualStream implements AudioStream
{
    private final AtomicInteger m_refCount;
    private int m_offset;
    private JOrbisSound m_sound;
    
    public JOrbisVirtualStream(final JOrbisSound sound) {
        super();
        this.m_refCount = new AtomicInteger(0);
        this.m_offset = 0;
        this.m_sound = sound;
    }
    
    @Override
    public boolean reinitialize() {
        this.m_offset = 0;
        return true;
    }
    
    @Override
    public boolean initialize(final AudioStreamProvider url) {
        this.m_offset = 0;
        return true;
    }
    
    @Override
    public String getDescription() {
        return this.m_sound.getStream().getDescription();
    }
    
    @Override
    public String getFileId() {
        return this.m_sound.getStream().getFileId();
    }
    
    @Override
    public int read(final ByteBuffer buffer, final int pos) {
        if (!this.m_sound.isInitialized()) {
            return 0;
        }
        buffer.position(pos);
        final byte[] pcmSoundBuffer = this.m_sound.getPcmSoundBuffer();
        int bytesCount = Math.min(buffer.limit() - pos, pcmSoundBuffer.length - this.m_offset);
        bytesCount = Math.max(0, bytesCount - bytesCount % this.m_sound.getStream().getCurrentBytesPerSample());
        buffer.put(pcmSoundBuffer, this.m_offset, bytesCount);
        this.m_offset += bytesCount;
        if (this.m_offset >= pcmSoundBuffer.length) {
            return -bytesCount;
        }
        return bytesCount;
    }
    
    @Override
    public int getNumChannels() {
        return this.m_sound.getStream().getNumChannels();
    }
    
    @Override
    public int getSampleRate() {
        return this.m_sound.getStream().getSampleRate();
    }
    
    @Override
    public void setSwap(final boolean swap) {
    }
    
    @Override
    public void reset() {
        this.m_offset = 0;
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public int getDurationInMs() {
        return this.m_sound.getStream().getDurationInMs();
    }
    
    @Override
    public long rawTell() {
        throw new UnsupportedOperationException("JOrbisVirtualStream.rawTell");
    }
    
    @Override
    public long pcmTell() {
        return this.m_offset;
    }
    
    @Override
    public float timeTell() {
        throw new UnsupportedOperationException("JOrbisVirtualStream.timeTell");
    }
    
    @Override
    public int timeSeek(final float seconds) {
        throw new UnsupportedOperationException("Impossible de seek sur un son non stream\u00e9 \u00e0 la seconds=" + seconds);
    }
    
    @Override
    public int pcmSeek(final long offset) {
        if (offset > 2147483647L) {
            throw new UnsupportedOperationException("Impossible de pcmSeek sur un son non stream\u00e9 \u00e0 la position offset=" + offset);
        }
        this.m_offset = (int)offset;
        return 0;
    }
    
    @Override
    public int rawSeek(final long offset) {
        throw new UnsupportedOperationException("Impossible de rawSeek sur un son non stream\u00e9 \u00e0 la position offset=" + offset);
    }
    
    @Override
    public int getCurrentBytesPerSample() {
        return this.m_sound.getStream().getCurrentBytesPerSample();
    }
    
    @Override
    public int getWeight() {
        return this.m_sound.getStream().getWeight();
    }
    
    @Override
    public void setWeight(final int weight) {
    }
    
    @Override
    public int getRefCount() {
        return this.m_refCount.get();
    }
    
    @Override
    public void addRefCount() {
        this.m_refCount.incrementAndGet();
    }
    
    @Override
    public void subRefCount() {
        this.m_refCount.decrementAndGet();
    }
}
