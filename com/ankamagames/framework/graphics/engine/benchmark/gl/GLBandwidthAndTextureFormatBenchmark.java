package com.ankamagames.framework.graphics.engine.benchmark.gl;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.benchmark.*;
import javax.media.opengl.*;
import java.nio.*;

public class GLBandwidthAndTextureFormatBenchmark extends AbstractGLBenchmark
{
    protected static Logger m_logger;
    private static final int TEXTURE_WIDTH = 1024;
    private static final int TEXTURE_HEIGHT = 1024;
    private static final int NUM_TEXTURES = 2;
    private static final int NUM_UPLOADS = 200;
    private ByteBuffer[] m_textureBuffer;
    private int[] m_texture;
    
    public GLBandwidthAndTextureFormatBenchmark() {
        super();
        this.m_textureBuffer = new ByteBuffer[2];
        this.m_texture = new int[2];
    }
    
    @Override
    public void initialize() {
        super.initialize();
        for (int i = 0; i < 2; ++i) {
            this.m_textureBuffer[i] = ByteBuffer.allocate(4194304);
            for (int k = 0; k < 1048576; ++k) {
                this.m_textureBuffer[i].putInt(Math.round(MathHelper.randomFloat() * 5.3687091E8f));
            }
            this.m_textureBuffer[i].rewind();
        }
        this.m_gl.glEnable(3553);
        this.m_gl.glGenTextures(2, this.m_texture, 0);
    }
    
    @Override
    public void run(final BenchmarkResult result) {
        long begin = System.nanoTime();
        for (int i = 0; i < 200; ++i) {
            final int textureIndex = i % 2;
            this.m_gl.glBindTexture(3553, this.m_texture[textureIndex]);
            this.m_gl.glPixelStorei(3317, 1);
            this.uploadData(this.m_gl, 32993, this.m_textureBuffer[textureIndex]);
        }
        final long bgraIndex = System.nanoTime() - begin;
        begin = System.nanoTime();
        for (int j = 0; j < 200; ++j) {
            final int textureIndex2 = j % 2;
            this.m_gl.glBindTexture(3553, this.m_texture[textureIndex2]);
            this.m_gl.glPixelStorei(3317, 1);
            this.uploadData(this.m_gl, 6408, this.m_textureBuffer[textureIndex2]);
        }
        final long rgbaIndex = System.nanoTime() - begin;
        final long megaBytesCount = 800L;
        if (bgraIndex < rgbaIndex) {
            result.setBestGLTextureFormat(32993);
        }
        else {
            result.setBestGLTextureFormat(6408);
        }
        result.setBgraBandwidthIndex(800.0 / (bgraIndex / 1.0E9));
        result.setRgbaBandwidthIndex(800.0 / (rgbaIndex / 1.0E9));
    }
    
    @Override
    public void cleanUp() {
        this.m_gl.glDeleteTextures(2, this.m_texture, 0);
        for (int i = 0; i < 2; ++i) {
            this.m_textureBuffer[i] = null;
        }
        super.cleanUp();
    }
    
    private void uploadData(final GL gl, final int format, final ByteBuffer data) {
        gl.glTexImage2D(3553, 0, 32856, 1024, 1024, 0, format, 5121, (Buffer)data);
    }
    
    @Override
    public String getName() {
        return "GL bandwidth and texture format";
    }
    
    static {
        GLBandwidthAndTextureFormatBenchmark.m_logger = Logger.getLogger((Class)GLBandwidthAndTextureFormatBenchmark.class);
    }
}
