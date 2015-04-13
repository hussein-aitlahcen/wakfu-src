package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.kernel.core.common.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.io.*;
import org.apache.log4j.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.Anm2.Index.*;
import java.util.concurrent.*;

public final class Anm extends MemoryObject
{
    public static final int CUSTOM_COLOR_NONE = 0;
    public static final int CUSTOM_COLOR_SKIN = 1;
    public static final int CUSTOM_COLOR_HAIR = 2;
    public static final int CUSTOM_COLOR_SYMBOL_BG = 3;
    public static final int CUSTOM_COLOR_SYMBOL_FG = 4;
    public static final int CUSTOM_COLOR_SYMBOL_BORDER = 5;
    public static final int CUSTOM_COLOR_1 = 6;
    public static final int CUSTOM_COLOR_2 = 7;
    public static final int CUSTOM_COLOR_PUPIL = 8;
    public static final int CUSTOM_COLOR_CLOTHES = 9;
    public static final int MAX_CUSTOM_COLORS = 10;
    public static final int[] CUSTOM_ALL_COLORS;
    public static final ObjectFactory Factory;
    int m_maxSpriteCount;
    AnmHeader m_header;
    String m_textureName;
    final TShortObjectHashMap<AnmShapeDefinition> m_shapeDefinitionsById;
    SpriteDefinition[] m_spriteDefinitions;
    final TShortObjectHashMap<SpriteDefinition> m_spriteDefinitionsById;
    final TIntObjectHashMap<SpriteDefinition> m_spriteDefinitionsByCRC;
    final TShortObjectHashMap<AnmImport> m_importsById;
    final AnmIndex m_index;
    AnmTransformDataTable m_table;
    private ArrayList<LoadedListener> m_loadedListeners;
    private String m_path;
    String m_fileName;
    private volatile State m_state;
    private AsyncURL m_asyncURL;
    private long m_textureCRC;
    private static final Logger m_logger;
    private static final ExecutorService m_loader;
    private static int m_loaderId;
    private Future m_anmLoadState;
    private boolean m_usePerfectHitTest;
    private short m_life;
    private static final short DEFAULT_LIFE = 500;
    
    private Anm() {
        super();
        this.m_maxSpriteCount = -1;
        this.m_index = new AnmIndex();
        this.m_header = new AnmHeader();
        this.m_shapeDefinitionsById = new TShortObjectHashMap<AnmShapeDefinition>(0, 1.0f);
        this.m_spriteDefinitionsById = new TShortObjectHashMap<SpriteDefinition>(0, 1.0f);
        this.m_spriteDefinitionsByCRC = new TIntObjectHashMap<SpriteDefinition>(0, 1.0f);
        this.m_importsById = new TShortObjectHashMap<AnmImport>(0, 1.0f);
        this.m_state = State.none;
        this.m_textureCRC = 0L;
    }
    
    boolean asyncLoadFailed() {
        return this.m_asyncURL != null && this.m_asyncURL.hasFailed();
    }
    
    public AnmHeader getHeader() {
        return this.m_header;
    }
    
    public void reload(final boolean async) throws IOException {
        final String filePath = this.m_path + '/' + this.m_fileName;
        this.load(filePath, async);
    }
    
    public void load(final String fileName, final boolean async) throws IOException {
        this.m_fileName = FileHelper.getName(fileName);
        this.m_path = FileHelper.getPath(fileName);
        if (async) {
            this.m_asyncURL = ContentFileHelper.loadAsyncURL(fileName);
            this.m_state = State.loadStreaming;
        }
        else {
            this.load(ExtendedDataInputStream.wrap(ContentFileHelper.readFile(fileName)));
        }
    }
    
    public void load(final ExtendedDataInputStream bitStream) throws IOException {
        (this.m_header = new AnmHeader()).load(bitStream);
        if (this.m_header.isUseLocalIndex()) {
            this.m_index.load(bitStream);
        }
        this.m_usePerfectHitTest = this.m_header.usePerfectHitTest();
        final boolean optimized = this.m_header.isOptimized();
        final int numTexture = bitStream.readShort();
        if (numTexture == 1) {
            this.m_textureName = bitStream.readString();
            final int textureCrc = bitStream.readInt();
            this.m_textureCRC = this.computeTextureCRC(textureCrc);
        }
        final int numShapes = bitStream.readShort() & 0xFFFF;
        this.m_shapeDefinitionsById.ensureCapacity(numShapes);
        for (int i = 0; i < numShapes; ++i) {
            final AnmShapeDefinition shapeDefinition = new AnmShapeDefinition(bitStream);
            this.m_shapeDefinitionsById.put(shapeDefinition.m_id, shapeDefinition);
        }
        if (this.m_header.useTransformIndex()) {
            this.m_table = AnmTransformDataTable.createFrom(bitStream);
        }
        assert this.m_maxSpriteCount == -1;
        final int numSprites = bitStream.readShort() & 0xFFFF;
        this.m_spriteDefinitions = new SpriteDefinition[numSprites];
        this.m_spriteDefinitionsById.ensureCapacity(numSprites);
        this.m_spriteDefinitionsByCRC.ensureCapacity(numSprites);
        final boolean useFlip = this.m_index.useFlip();
        for (int j = 0; j < numSprites; ++j) {
            this.m_spriteDefinitions[j] = SpriteDefinition.createFrom(this.m_table, bitStream, optimized);
            if (this.m_spriteDefinitions[j] == null) {
                return;
            }
            this.m_spriteDefinitions[j].load(bitStream);
            if (this.m_spriteDefinitions[j].getFrameCount() != 0) {
                if (this.m_spriteDefinitions[j].m_maxSpriteCount > this.m_maxSpriteCount) {
                    this.m_maxSpriteCount = this.m_spriteDefinitions[j].m_maxSpriteCount;
                }
                if (useFlip && isFlippableAnimation(this.m_spriteDefinitions[j].m_name)) {
                    this.m_spriteDefinitions[j] = null;
                }
                else {
                    this.m_spriteDefinitionsById.put(this.m_spriteDefinitions[j].m_id, this.m_spriteDefinitions[j]);
                    assert !this.m_spriteDefinitionsByCRC.contains(this.m_spriteDefinitions[j].m_nameCRC);
                    this.m_spriteDefinitionsByCRC.put(this.m_spriteDefinitions[j].m_nameCRC, this.m_spriteDefinitions[j]);
                }
            }
        }
        final int numImports = bitStream.readShort() & 0xFFFF;
        this.m_importsById.ensureCapacity(numImports);
        for (int k = 0; k < numImports; ++k) {
            final AnmImport anmImport = new AnmImport();
            anmImport.load(bitStream);
            this.m_importsById.put(anmImport.m_id, anmImport);
        }
        if (!optimized) {
            final int numActions = bitStream.readShort() & 0xFFFF;
            assert numActions == 0;
        }
        this.m_state = State.initCreateTextures;
        bitStream.close();
    }
    
    private static boolean isFlippableAnimation(final String name) {
        if (StringUtils.isEmptyOrNull(name)) {
            return false;
        }
        if (!name.startsWith("_Anim", 1)) {
            return false;
        }
        final char direction = name.charAt(0);
        return direction == '3' || direction == '4' || direction == '7';
    }
    
    private long computeTextureCRC(final int baseCrc) {
        final long name = Engine.getTextureName(this.m_path);
        return 0xBB00BB0000000000L | name << 32 | (baseCrc & 0xFFFFFFFFL);
    }
    
    void update() throws IOException {
        switch (this.m_state) {
            case loadStreaming: {
                if (this.m_asyncURL.isReady()) {
                    final byte[] data = this.m_asyncURL.getData();
                    this.m_asyncURL = null;
                    this.m_state = State.loadAsyncUnserialize;
                    this.m_anmLoadState = Anm.m_loader.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Anm.this.load(ExtendedDataInputStream.wrap(data));
                            }
                            catch (IOException e) {
                                Anm.m_logger.error((Object)("Exception while loading ANM " + this), (Throwable)e);
                            }
                        }
                    });
                    break;
                }
                break;
            }
            case loadAsyncUnserialize: {
                try {
                    if (!this.m_anmLoadState.isDone()) {
                        break;
                    }
                    this.m_state = State.initCreateTextures;
                }
                catch (Exception e) {
                    Anm.m_logger.error((Object)"Exception raised : ", (Throwable)e);
                }
            }
            case initCreateTextures: {
                if (this.m_textureCRC == 0L) {
                    this.m_state = State.ready;
                    break;
                }
                this.createTextures();
                this.m_state = State.initWaitForTextures;
            }
            case initWaitForTextures: {
                final Texture texture = TextureManager.getInstance().getTexture(this.m_textureCRC);
                if (texture == null || !texture.isEmpty()) {
                    this.m_state = State.ready;
                    break;
                }
                break;
            }
        }
        if (this.isReady() && this.m_loadedListeners != null) {
            for (int i = 0; i < this.m_loadedListeners.size(); ++i) {
                this.m_loadedListeners.get(i).run();
            }
            this.m_loadedListeners = null;
        }
    }
    
    public SpriteDefinition[] getSpriteDefinitions() {
        return this.m_spriteDefinitions;
    }
    
    public SpriteDefinition getSpriteDefinition(final short id) {
        return this.m_spriteDefinitionsById.get(id);
    }
    
    public SpriteDefinition getSpriteDefinitionByCRC(final int crc) {
        return this.m_spriteDefinitionsByCRC.get(crc);
    }
    
    public AnmImport getImport(final short id) {
        return this.m_importsById.get(id);
    }
    
    String getPath() {
        return this.m_path;
    }
    
    public String getFileName() {
        return this.m_fileName;
    }
    
    public boolean isReady() {
        return this.m_state == State.ready;
    }
    
    public boolean usePerfectHitTest() {
        return this.m_usePerfectHitTest;
    }
    
    public int getAnimationHeight(final String animation) {
        return this.m_index.getAnimHeight(animation);
    }
    
    public float[] getHighlightColor() {
        return this.m_index.getHighlightColor();
    }
    
    @Override
    protected void checkout() {
        this.m_life = 500;
    }
    
    @Override
    protected void checkin() {
        this.m_state = State.none;
        if (this.m_header != null) {
            final Texture texture = TextureManager.getInstance().getTexture(this.m_textureCRC);
            if (texture != null) {
                texture.removeReference();
            }
        }
        this.m_index.clear();
        this.m_path = null;
        this.m_textureName = null;
        this.m_textureCRC = 0L;
        this.m_fileName = null;
        this.m_header = null;
        this.m_asyncURL = null;
        this.m_shapeDefinitionsById.clear();
        this.m_spriteDefinitions = null;
        this.m_spriteDefinitionsById.clear();
        this.m_spriteDefinitionsByCRC.clear();
        this.m_importsById.clear();
        this.m_loadedListeners = null;
        this.m_maxSpriteCount = -1;
    }
    
    private String getTexturePath() {
        if (this.m_header.isUseAtlas()) {
            return this.m_path + "/Atlas/";
        }
        return this.m_path + "/Textures/";
    }
    
    long getTextureCRC() {
        return this.m_textureCRC;
    }
    
    private String getTextureName(final String textureName) {
        return this.getTexturePath() + textureName + ".tgam";
    }
    
    private void createTextures() {
        if (this.m_textureName == null) {
            return;
        }
        final TextureManager textureManager = TextureManager.getInstance();
        final String textureFileName = this.getTextureName(this.m_textureName);
        final AnmManager anmManager = AnmManager.getInstance();
        final Texture texture = textureManager.createTexture(RendererType.OpenGL.getRenderer(), this.m_textureCRC, textureFileName, anmManager.keepTextureData(), anmManager.getAsyncMode());
        texture.addReference();
    }
    
    @Override
    public void addReference() {
        super.addReference();
        this.m_life = 500;
    }
    
    public short getLife() {
        return this.m_life;
    }
    
    public void reduceLife() {
        if (this.getNumReferences() == 0 && this.m_life > -32768) {
            --this.m_life;
        }
    }
    
    private void addLoadedListener(final LoadedListener listener) {
        if (this.m_loadedListeners == null) {
            this.m_loadedListeners = new ArrayList<LoadedListener>(2);
        }
        this.m_loadedListeners.add(listener);
    }
    
    private void removeLoadedListener(final LoadedListener listener) {
        if (this.m_loadedListeners != null) {
            this.m_loadedListeners.remove(listener);
        }
    }
    
    public static int getColorIndex(final String partName) {
        final int crc = Engine.getPartName(partName);
        switch (crc) {
            case 2483465: {
                return 1;
            }
            case -1887439342: {
                return 2;
            }
            case 1852627997: {
                return 3;
            }
            case 1852628121: {
                return 4;
            }
            case 1994233860: {
                return 5;
            }
            case -1680767435: {
                return 6;
            }
            case -1680767434: {
                return 7;
            }
            case 1442130759: {
                return 8;
            }
            case -1875970338: {
                return 9;
            }
            default: {
                Anm.m_logger.warn((Object)("part inconnue pour la coloration: " + partName));
                return 0;
            }
        }
    }
    
    public AnmAnimationFileRecord[] getAnimationFileRecords() {
        return this.m_index.getAnimationFileRecords();
    }
    
    static {
        CUSTOM_ALL_COLORS = new int[] { 1, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        Factory = new ObjectFactory();
        m_loader = Executors.newFixedThreadPool(3, new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable r) {
                return new Thread(r, "AnmLoader #" + Anm.m_loaderId++);
            }
        });
        m_logger = Logger.getLogger((Class)Anm.class);
        Anm.m_loaderId = 0;
    }
    
    public abstract static class LoadedListener implements Runnable
    {
        private final Anm m_anm;
        
        protected LoadedListener(final Anm anm) {
            super();
            (this.m_anm = anm).addLoadedListener(this);
        }
        
        public final void remove() {
            this.m_anm.removeLoadedListener(this);
        }
    }
    
    enum State
    {
        none, 
        loadStreaming, 
        loadAsyncUnserialize, 
        initCreateTextures, 
        initWaitForTextures, 
        ready;
    }
    
    public enum Color
    {
        Skin("Peau", 1), 
        Hair("Cheveux", 2), 
        SymbolBg("SymbolBg", 3), 
        SymbolFg("SymbolFg", 4), 
        SymbolBorder("SymbolBorder", 5), 
        Color1("Color_1", 6), 
        Color2("Color_2", 7), 
        Pupil("Pupille", 8), 
        Clothes("Vetement", 9);
        
        public final String m_name;
        public final int m_colorIndex;
        
        private Color(final String name, final int colorIndex) {
            this.m_name = name;
            this.m_colorIndex = colorIndex;
        }
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<Anm>
    {
        public ObjectFactory() {
            super(Anm.class);
        }
        
        @Override
        public Anm create() {
            return new Anm(null);
        }
    }
}
