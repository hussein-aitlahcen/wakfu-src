package com.ankamagames.xulor2.component;

import com.ankamagames.framework.graphics.engine.video.*;
import java.nio.*;
import uk.co.caprica.vlcj.player.direct.*;
import com.sun.jna.*;

public final class VideoTextureProducerFromNative extends VideoTextureProducer implements RenderCallback
{
    private ByteBuffer m_dataBuffer;
    private long m_currentImageUid;
    private BufferFormat m_bufferFormat;
    
    public VideoTextureProducerFromNative() {
        super();
        this.m_currentImageUid = this.NO_IMAGE_UID;
        this.initialize();
    }
    
    @Override
    public byte getPixelFormat() {
        return 18;
    }
    
    @Override
    protected long getImageUid() {
        return this.m_currentImageUid;
    }
    
    @Override
    protected ByteBuffer getImageData() {
        return this.m_dataBuffer;
    }
    
    @Override
    protected int getImageWidth() {
        return (this.m_bufferFormat != null) ? this.m_bufferFormat.getWidth() : 0;
    }
    
    @Override
    protected int getImageHeight() {
        return (this.m_bufferFormat != null) ? this.m_bufferFormat.getHeight() : 0;
    }
    
    public void display(final DirectMediaPlayer mediaPlayer, final Memory[] nativeBuffers, final BufferFormat bufferFormat) {
        if (this.m_currentImageUid != this.NO_IMAGE_UID) {
            ++this.m_currentImageUid;
        }
        else {
            this.m_currentImageUid = 1L;
        }
        this.m_bufferFormat = bufferFormat;
        this.m_dataBuffer = nativeBuffers[0].getByteBuffer(0L, nativeBuffers[0].size());
    }
}
