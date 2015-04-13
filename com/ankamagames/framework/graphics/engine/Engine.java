package com.ankamagames.framework.graphics.engine;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.fileFormat.xml.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.graphics.engine.pools.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.fx.*;

public class Engine
{
    public Texture m_maskAlpha;
    public Texture m_perturbMap;
    public static final float MIN_VISIBLE_ALPHA = 0.004f;
    private static final boolean USE_DEBUG_TEXTURE_SIZE = false;
    private static Engine m_instance;
    private boolean m_isInitialized;
    private XMLDocumentContainer m_configFile;
    private String m_effectPath;
    protected static final Logger m_logger;
    
    public static Engine getInstance() {
        return Engine.m_instance;
    }
    
    public static boolean isEqualColor(final float component, final float ref) {
        return MathHelper.isEqual(component, ref, 0.004f);
    }
    
    public void initializePools(final XMLDocumentContainer configFile) {
        this.m_configFile = configFile;
        this.configPools(configFile.getRootNode().getChildByName("engine"));
    }
    
    public void initializePools(final String configFileName) {
        final XMLDocumentAccessor accessor = XMLDocumentAccessor.getInstance();
        final XMLDocumentContainer configFile = accessor.getNewDocumentContainer();
        try {
            accessor.open(configFileName);
            accessor.read(configFile, new DocumentEntryParser[0]);
            accessor.close();
        }
        catch (Exception e) {
            Engine.m_logger.error((Object)"Exception", (Throwable)e);
        }
        this.initializePools(configFile);
    }
    
    public void initializeEffects(final String effectsPath) {
        assert this.m_configFile != null : "You must call initializePools before";
        final DocumentEntry engineNode = this.m_configFile.getRootNode().getChildByName("engine");
        loadEffects(engineNode, effectsPath);
        final TextureManager textureManager = TextureManager.getInstance();
        final Renderer renderer = RendererType.OpenGL.getRenderer();
        this.m_effectPath = effectsPath;
        (this.m_maskAlpha = textureManager.createTexture(renderer, -1296775008915292159L, effectsPath + "textures/maskAlpha.tga", false)).addReference();
        (this.m_perturbMap = textureManager.createTexture(renderer, -1296775008915292158L, effectsPath + "textures/perturb.tga", false)).addReference();
        this.m_isInitialized = true;
    }
    
    public final boolean isInitialiazed() {
        return this.m_isInitialized;
    }
    
    public final String getEffectPath() {
        return this.m_effectPath;
    }
    
    private Engine() {
        super();
        this.m_isInitialized = false;
    }
    
    private void configPools(final DocumentEntry engineNode) {
        assert engineNode != null : "Unable to find engine node";
        final DocumentEntry poolsNode = engineNode.getChildByName("pools");
        assert poolsNode != null : "Unable to find pools node";
        final ArrayList<DirectBufferPoolInfo> poolsInfo = new ArrayList<DirectBufferPoolInfo>(64);
        this.configTexturePools(poolsNode, poolsInfo);
        this.configVertexBufferPools(poolsNode, poolsInfo);
        configIndexBufferPools(poolsNode, poolsInfo);
        DirectBufferPoolManager.getInstance().intialise(poolsInfo);
    }
    
    private void configTexturePools(final DocumentEntry poolsNode, final ArrayList<DirectBufferPoolInfo> poolsInfo) {
        assert poolsNode != null : "Unable to find pools node";
        final DocumentEntry texturePoolsNode = poolsNode.getChildByName("texture_pools");
        assert texturePoolsNode != null : "Unable to find texture_pools node";
        final ArrayList<DocumentEntry> textureChilds = texturePoolsNode.getChildrenByName("texture");
        assert textureChilds != null : "Unable to find texture nodes";
        for (final DocumentEntry texture : textureChilds) {
            final DocumentEntry width = texture.getParameterByName("width");
            assert width != null : "Unable to find width parameter and it's not optionnal";
            final DocumentEntry height = texture.getParameterByName("height");
            assert height != null : "Unable to find height parameter and it's not optionnal";
            final DocumentEntry bpp = texture.getParameterByName("bpp");
            assert bpp != null : "Unable to find bpp parameter and it's not optionnal";
            assert isTextureBitDepthValid(bpp.getIntValue()) : "Texture with a bit per pixel of " + bpp.getIntValue() + " is not supported";
            final DocumentEntry count = texture.getParameterByName("count");
            assert count != null : "Unable to find count parameter and it's not optionnal";
            int compressionFactor = 1;
            final DocumentEntry compression = texture.getParameterByName("compression");
            if (compression != null) {
                final FourCC mode = new FourCC(compression.getStringValue());
                assert isCompressionValid(mode) : "Compression mode " + mode.toString() + " is not supported";
                compressionFactor = this.getCompressionFactor(mode, bpp.getIntValue());
            }
            final int textureSize = (int)(width.getIntValue() * height.getIntValue() * bpp.getIntValue() / (8.0f * compressionFactor));
            final DirectBufferPoolInfo poolInfo = new DirectBufferPoolInfo();
            poolInfo.setType(DirectBufferPool.Type.ByteBuffer);
            poolInfo.setBuffersCount(1);
            poolInfo.setBufferSize(textureSize);
            poolsInfo.add(poolInfo);
        }
    }
    
    private void configVertexBufferPools(final DocumentEntry poolsNode, final ArrayList<DirectBufferPoolInfo> poolsInfo) {
        assert poolsNode != null : "Unable to find pools node";
        final DocumentEntry vertexBufferPoolsNode = poolsNode.getChildByName("vertex_buffer_pools");
        assert vertexBufferPoolsNode != null : "Unable to find vertex_buffer_pools node";
        final ArrayList<DocumentEntry> vertexBufferChildren = vertexBufferPoolsNode.getChildrenByName("vertex_buffer");
        assert vertexBufferChildren != null : "Unable to find vertex_buffer nodes";
        for (final DocumentEntry vertexBuffer : vertexBufferChildren) {
            final DocumentEntry vertexSize = vertexBuffer.getParameterByName("vertex_size");
            assert vertexSize != null : "Unable to find vertex_size parameter and it's not optionnal";
            assert vertexSize.getIntValue() == 32 : "Vertex size should be equal to (color + position + texcoord)*4";
            final DocumentEntry numVertices = vertexBuffer.getParameterByName("num_vertices");
            assert numVertices != null : "Unable to find num_vertices parameter and it's not optionnal";
            final DocumentEntry count = vertexBuffer.getParameterByName("count");
            assert count != null : "Unable to find count parameter and it's not optionnal";
            final int bufferPositionSize = 2 * numVertices.getIntValue() * 4;
            final int bufferColorSize = 4 * numVertices.getIntValue() * 4;
            final int bufferTexCoordSize = 2 * numVertices.getIntValue() * 4;
            DirectBufferPoolInfo poolInfo = new DirectBufferPoolInfo();
            poolInfo.setType(DirectBufferPool.Type.FloatBuffer);
            poolInfo.setBuffersCount(count.getIntValue());
            poolInfo.setBufferSize(bufferTexCoordSize);
            addPoolInfo(poolsInfo, poolInfo);
            poolInfo = new DirectBufferPoolInfo();
            poolInfo.setType(DirectBufferPool.Type.FloatBuffer);
            poolInfo.setBuffersCount(count.getIntValue());
            poolInfo.setBufferSize(bufferPositionSize);
            addPoolInfo(poolsInfo, poolInfo);
            poolInfo = new DirectBufferPoolInfo();
            poolInfo.setType(DirectBufferPool.Type.FloatBuffer);
            poolInfo.setBuffersCount(count.getIntValue());
            poolInfo.setBufferSize(bufferColorSize);
            addPoolInfo(poolsInfo, poolInfo);
        }
    }
    
    private static void addPoolInfo(final ArrayList<DirectBufferPoolInfo> poolsInfo, final DirectBufferPoolInfo poolInfo) {
        for (int poolsInfoSize = poolsInfo.size(), i = 0; i < poolsInfoSize; ++i) {
            final DirectBufferPoolInfo info = poolsInfo.get(i);
            if (info.getType() == poolInfo.getType() && info.getBufferSize() == poolInfo.getBufferSize()) {
                info.setBuffersCount(info.getBuffersCount() + poolInfo.getBuffersCount());
                return;
            }
        }
        poolsInfo.add(poolInfo);
    }
    
    private static void configIndexBufferPools(final DocumentEntry poolsNode, final ArrayList<DirectBufferPoolInfo> poolsInfo) {
        assert poolsNode != null : "Unable to find pools node";
        final DocumentEntry indexBufferPoolsNode = poolsNode.getChildByName("index_buffer_pools");
        assert indexBufferPoolsNode != null : "Unable to find index_buffer_pools node";
        final ArrayList<DocumentEntry> indexBufferChildren = indexBufferPoolsNode.getChildrenByName("index_buffer");
        assert indexBufferChildren != null : "Unable to find index_buffer nodes";
        for (final DocumentEntry indexBuffer : indexBufferChildren) {
            final DocumentEntry bufferSize = indexBuffer.getParameterByName("size");
            assert bufferSize != null : "Unable to find size parameter and it's not optionnal";
            final DocumentEntry count = indexBuffer.getParameterByName("count");
            assert count != null : "Unable to find count parameter and it's not optionnal";
            final DirectBufferPoolInfo poolInfo = new DirectBufferPoolInfo();
            poolInfo.setType(DirectBufferPool.Type.ShortBuffer);
            poolInfo.setBuffersCount(count.getIntValue());
            poolInfo.setBufferSize(bufferSize.getIntValue());
            poolsInfo.add(poolInfo);
        }
    }
    
    private static void loadEffects(final DocumentEntry engineNode, final String effectsPath) {
        assert engineNode != null : "Unable to find engine node";
        final DocumentEntry effectsNode = engineNode.getChildByName("effects");
        assert effectsNode != null : "Unable to find shaders node";
        final ArrayList<DocumentEntry> effectChilds = effectsNode.getChildrenByName("effect");
        assert effectChilds != null : "Unable to find effect nodes";
        for (final DocumentEntry effect : effectChilds) {
            final DocumentEntry name = effect.getParameterByName("name");
            assert name != null : "Unable to find name parameter and it's not optionnal";
            final DocumentEntry file = effect.getParameterByName("file");
            assert file != null : "Unable to find file parameter and it's not optionnal";
            final DocumentEntry classNameEntry = effect.getParameterByName("class");
            final String className = (classNameEntry == null) ? null : classNameEntry.getStringValue();
            final String fileName = effectsPath + file.getStringValue();
            EffectManager.getInstance().addEffect(name.getStringValue(), fileName, className);
        }
    }
    
    private static boolean isTextureBitDepthValid(final int bpp) {
        switch (bpp) {
            case 24:
            case 32: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    private static boolean isCompressionValid(final FourCC mode) {
        final int modeId = mode.getID();
        return modeId == FourCC.stringToID("DXT1") || modeId == FourCC.stringToID("DXT2") || modeId == FourCC.stringToID("DXT3") || modeId == FourCC.stringToID("DXT4") || modeId == FourCC.stringToID("DXT5");
    }
    
    private int getCompressionFactor(final FourCC mode, final int bpp) {
        assert isCompressionValid(mode) : "Compression mode " + mode.toString() + " is not supported";
        assert bpp == 24 : "Compressed texture with a bit per pixel of " + bpp + " is not supported";
        final int modeId = mode.getID();
        if (modeId == FourCC.stringToID("DXT1")) {
            return bpp / 4;
        }
        return bpp / 8;
    }
    
    public static int getTechnic(final String technicName) {
        return technicName.hashCode();
    }
    
    public static int getTextureName(final String name) {
        return name.hashCode();
    }
    
    public static int getPartName(final String name) {
        return name.hashCode();
    }
    
    public static int[] getPartsNames(final String... name) {
        final int count = name.length;
        final int[] crcs = new int[count];
        for (int i = 0; i < count; ++i) {
            crcs[i] = getPartName(name[i]);
        }
        return crcs;
    }
    
    static {
        Engine.m_instance = new Engine();
        m_logger = Logger.getLogger((Class)Engine.class);
    }
}
