package com.ankamagames.framework.graphics.engine.opengl;

import com.ankamagames.framework.graphics.engine.texture.*;
import org.apache.log4j.*;
import javax.media.opengl.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.pools.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.test.*;
import com.ankamagames.framework.kernel.core.common.*;

public final class GLTexture extends Texture
{
    private static final Logger m_logger;
    private static final int DXT1_ID;
    private static final int DXT3_ID;
    private static final int DXT5_ID;
    public static final int InternalFormat = 32856;
    private static final int MIN_ALPHA_HIT = 25;
    private int[] m_id;
    
    public GLTexture(final long name, final String fileName, final boolean keepData) {
        super(name, fileName, keepData);
        this.initialize();
    }
    
    public GLTexture(final long name, final Image image, final boolean keepData) {
        super(name, image, keepData);
        this.initialize();
    }
    
    public GLTexture(final long name, final int width, final int height, final boolean generateMipMaps) {
        super(name, width, height, generateMipMaps);
        this.initialize();
    }
    
    public int getID() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        return this.m_id[0];
    }
    
    public int getRenderTargetID() {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        return this.m_id[3];
    }
    
    @Override
    public boolean prepare(final Renderer renderer) {
        return this.prepare(GLHelper.getGL(renderer));
    }
    
    private boolean prepare(final GL gl) {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        if (!Threading.isOpenGLThread()) {
            return false;
        }
        if (this.m_isRenderTarget) {
            return this.prepareRenderTarget(gl);
        }
        return this.prepareTexture(gl);
    }
    
    @Override
    public boolean updateTextureData(final Renderer renderer) {
        return this.updateTextureData(GLHelper.getGL(renderer));
    }
    
    public boolean updateTextureData(final GL gl) {
        int internalFormat = 0;
        if (this.getFormat().getID() == GLTexture.DXT1_ID) {
            internalFormat = 33777;
        }
        else if (this.getFormat().getID() == GLTexture.DXT3_ID) {
            internalFormat = 33778;
        }
        else if (this.getFormat().getID() == GLTexture.DXT5_ID) {
            internalFormat = 33779;
        }
        gl.glBindTexture(3553, this.m_id[0]);
        gl.glTexParameterf(3553, 10242, 10497.0f);
        gl.glTexParameterf(3553, 10243, 10497.0f);
        gl.glTexParameterf(3553, 10240, 9729.0f);
        gl.glTexParameterf(3553, 10241, 9729.0f);
        final DirectBufferPoolManager directBufferPoolManager = DirectBufferPoolManager.getInstance();
        for (int level = 0; level < this.getNumLayers(); ++level) {
            Layer layer = this.getLayer(level);
            Layer created = null;
            if (layer.getBitDepth() != 32) {
                GLTexture.m_logger.warn((Object)"Setting layer to RGBA32");
                layer = (created = layer.toRGB32());
            }
            final byte[] data = layer.getData();
            if (data == null) {
                if (created != null) {
                    created.removeReference();
                }
                return false;
            }
            final int dataSize = data.length;
            final ByteBufferPool byteBufferPool = directBufferPoolManager.getByteBufferPool(dataSize);
            final ByteBuffer buffer = (ByteBuffer)byteBufferPool.getBuffer();
            buffer.put(data, 0, dataSize);
            buffer.rewind();
            if (this.isCompressed()) {
                gl.glCompressedTexImage2D(3553, level, internalFormat, MathHelper.nearestGreatestPowOfTwo(layer.getWidth()), MathHelper.nearestGreatestPowOfTwo(layer.getHeight()), 0, data.length, (Buffer)buffer);
            }
            else {
                gl.glTexImage2D(3553, level, 32856, MathHelper.nearestGreatestPowOfTwo(layer.getWidth()), MathHelper.nearestGreatestPowOfTwo(layer.getHeight()), 0, getPixelFormat(layer), 5121, (Buffer)buffer);
            }
            byteBufferPool.release();
            if (created != null) {
                created.removeReference();
            }
        }
        this.m_isReady = true;
        this.m_needUpdate = false;
        return true;
    }
    
    private static int getPixelFormat(final Layer layer) {
        switch (layer.getPixelFormat()) {
            case 1: {
                return 6407;
            }
            case 17: {
                return 6408;
            }
            case 18: {
                return 32993;
            }
            case 2: {
                return 32992;
            }
            default: {
                return 6408;
            }
        }
    }
    
    @Override
    public void activate(final Renderer renderer) {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        final GL gl = GLHelper.getGL(renderer);
        if (!this.isReady()) {
            this.prepare(gl);
        }
        this.updateScore();
        final RenderStateManager stateManager = RenderStateManager.getInstance();
        stateManager.enableTextures(true);
        stateManager.applyStates(renderer);
        gl.glBindTexture(3553, this.m_id[0]);
        stateManager.setTextureBlend(this.m_blendMode);
        stateManager.applyStates(renderer);
    }
    
    private boolean prepareRenderTarget(final GL gl) {
        gl.glGenFramebuffersEXT(1, this.m_id, 3);
        gl.glGenTextures(1, this.m_id, 0);
        gl.glGenRenderbuffersEXT(1, this.m_id, 2);
        gl.glBindFramebufferEXT(36160, this.m_id[3]);
        gl.glBindTexture(3553, this.m_id[0]);
        gl.glTexParameteri(3553, 10242, 33071);
        gl.glTexParameteri(3553, 10243, 33071);
        final Layer layer = this.getLayer(0);
        final int width = layer.getWidth();
        final int height = layer.getHeight();
        if (this.m_generateMipMaps) {
            gl.glTexParameterf(3553, 10240, 9729.0f);
            gl.glTexParameterf(3553, 10241, 9987.0f);
            gl.glTexImage2D(3553, 0, 32856, width, height, 0, getPixelFormat(layer), 5121, (Buffer)null);
            gl.glGenerateMipmapEXT(3553);
        }
        else {
            gl.glTexParameteri(3553, 10240, 9729);
            gl.glTexParameteri(3553, 10241, 9729);
            gl.glTexParameteri(3553, 34891, 6409);
            gl.glTexImage2D(3553, 0, 32856, width, height, 0, getPixelFormat(layer), 5121, (Buffer)null);
        }
        gl.glFramebufferTexture2DEXT(36160, 36064, 3553, this.m_id[0], 0);
        gl.glBindRenderbufferEXT(36161, this.m_id[2]);
        gl.glRenderbufferStorageEXT(36161, 35056, width, height);
        gl.glFramebufferRenderbufferEXT(36160, 36096, 36161, this.m_id[2]);
        gl.glFramebufferRenderbufferEXT(36160, 36128, 36161, this.m_id[2]);
        final int checkResult = gl.glCheckFramebufferStatusEXT(36160);
        final boolean result = checkResult == 36053;
        gl.glBindFramebufferEXT(36160, 0);
        this.m_isReady = result;
        this.m_needUpdate = false;
        return result;
    }
    
    private boolean prepareTexture(final GL gl) {
        if (this.m_asyncURL != null) {
            if (this.m_asyncURL.isReady()) {
                this.onLoadComplete();
            }
            else {
                if (!this.m_asyncURL.hasFailed()) {
                    return false;
                }
                this.m_asyncURL = null;
            }
        }
        gl.glPixelStorei(3317, 1);
        gl.glGenTextures(1, this.m_id, 0);
        if (this.m_id[0] == 0) {
            GLTexture.m_logger.error((Object)"Unable to generate a new texture");
            return false;
        }
        final boolean dataUpdated = this.updateTextureData(gl);
        if (dataUpdated && !this.m_keepData) {
            if (this.m_createMask) {
                this.recomputeAlphaMasks(25);
            }
            this.optimize();
        }
        return dataUpdated;
    }
    
    public void activate2(final Renderer renderer) {
        this.activate2(GLHelper.getGL(renderer));
    }
    
    public void activate2(final GL gl) {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        if (!this.isReady()) {
            this.prepare(gl);
        }
        gl.glBindTexture(3553, this.m_id[0]);
        this.updateScore();
    }
    
    public void updateScore() {
        final int numReferences = this.getNumReferences();
        if (numReferences > this.m_maxRefCount) {
            this.m_maxRefCount = numReferences;
        }
    }
    
    @Override
    public void deactivate(final Renderer renderer) {
        assert this.exists() : "Using an item with a reference counter < 0 is forbidden";
        if (!this.isUsed()) {
            return;
        }
        this.m_isUsed = false;
        GLHelper.getGL(renderer).glBindTexture(3553, 0);
    }
    
    @Override
    public void enable(final Renderer renderer) {
    }
    
    @Override
    public void disable(final Renderer renderer) {
    }
    
    @Override
    protected void delete() {
        assert Threading.isOpenGLThread() : "Trying to release a texture in a non-opengl thread";
        assert this.m_managerState == ManagerState.CREATED;
        super.delete();
        final GL gl = GLHelper.getGL(RendererType.OpenGL.getRenderer());
        gl.glDeleteTextures(1, this.m_id, 0);
        if (this.m_isRenderTarget) {
            gl.glDeleteFramebuffersEXT(1, this.m_id, 3);
            gl.glDeleteRenderbuffersEXT(1, this.m_id, 2);
        }
        this.m_isReady = false;
        this.m_maxRefCount = 0;
        this.m_managerState = ManagerState.DELETED;
    }
    
    @Override
    public boolean isCompressed() {
        return this.getFormat().getID() == GLTexture.DXT1_ID || this.getFormat().getID() == GLTexture.DXT3_ID || this.getFormat().getID() == GLTexture.DXT5_ID;
    }
    
    private void initialize() {
        if (this.m_isRenderTarget) {
            (this.m_id = new int[4])[1] = 0;
            this.m_id[2] = 0;
            this.m_id[3] = 0;
        }
        else {
            this.m_id = new int[1];
        }
        this.m_id[0] = 0;
        this.m_isReady = false;
        if (!HardwareFeatureManager.INSTANCE.isFeatureSupported(HardwareFeature.GL_TEXTURE_NON_POWER_OF_TWO)) {
            final int width = this.getSize().getX();
            final int height = this.getSize().getY();
            final int newWidth = MathHelper.nearestGreatestPowOfTwo(width);
            final int newHeight = MathHelper.nearestGreatestPowOfTwo(height);
            if (newWidth != width || newHeight != height) {
                this.toPowerOfTwo();
            }
        }
        this.m_managerState = ManagerState.CREATED;
    }
    
    public static void generateRenderTexture(final GL gl, final int width, final int height, final int[] texture, final boolean linear) {
        gl.glGenTextures(1, texture, 0);
        gl.glBindTexture(3553, texture[0]);
        gl.glTexImage2D(3553, 0, 32856, width, height, 0, 6408, 5121, (Buffer)ByteBuffer.allocateDirect(width * height * 4));
        gl.glTexParameterf(3553, 10242, 33071.0f);
        gl.glTexParameterf(3553, 10243, 33071.0f);
        gl.glTexParameterf(3553, 10240, linear ? 9729.0f : 9728.0f);
        gl.glTexParameterf(3553, 10241, linear ? 9729.0f : 9728.0f);
    }
    
    @Override
    public String toString() {
        return this.m_fileName;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GLTexture.class);
        DXT1_ID = FourCC.stringToID("DXT1");
        DXT3_ID = FourCC.stringToID("DXT3");
        DXT5_ID = FourCC.stringToID("DXT5");
    }
}
