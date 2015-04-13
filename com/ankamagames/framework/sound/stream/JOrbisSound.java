package com.ankamagames.framework.sound.stream;

import java.nio.*;

public class JOrbisSound
{
    private boolean m_initialized;
    private byte[] m_pcmSoundBuffer;
    private JOrbisStream m_stream;
    
    public JOrbisSound(final JOrbisStream stream) {
        super();
        this.m_stream = stream;
        this.m_initialized = false;
    }
    
    public boolean initialize() {
        final long pcmSize = this.m_stream.uncompressedBytesTotal();
        if (pcmSize > 2147483647L) {
            throw new UnsupportedOperationException("Impossible de charger un son de plus de 2147483647 octets");
        }
        this.m_pcmSoundBuffer = new byte[(int)pcmSize];
        final ByteBuffer pcmSoundBuffer = ByteBuffer.wrap(this.m_pcmSoundBuffer);
        int position = 0;
        int byteCount;
        do {
            byteCount = this.m_stream.read(pcmSoundBuffer, position);
            position += byteCount;
        } while (byteCount > 0);
        this.m_stream.close();
        return this.m_initialized = true;
    }
    
    public void close() {
        this.m_stream.close();
        this.m_pcmSoundBuffer = null;
        this.m_stream = null;
        this.m_initialized = false;
    }
    
    public JOrbisStream getStream() {
        return this.m_stream;
    }
    
    public byte[] getPcmSoundBuffer() {
        return this.m_pcmSoundBuffer;
    }
    
    public int getMemorySize() {
        return this.m_pcmSoundBuffer.length;
    }
    
    public boolean isInitialized() {
        return this.m_initialized;
    }
    
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(this.m_stream);
        buffer.append("\ninitialized=").append(this.m_initialized);
        return buffer.toString();
    }
}
