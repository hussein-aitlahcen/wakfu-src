package com.ankamagames.framework.graphics.engine.particleSystem;

import com.ankamagames.framework.graphics.engine.entity.*;
import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.material.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.particleSystem.definitions.*;
import com.ankamagames.framework.graphics.engine.particleSystem.lightColorHelper.*;
import com.ankamagames.framework.graphics.engine.particleSystem.utils.*;
import com.ankamagames.framework.sound.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.nio.*;
import gnu.trove.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.image.*;

public class ParticleSystem extends Entity3D
{
    private static final Logger m_logger;
    private EditableData m_editableData;
    public static final int m_maxIndices;
    public static final short[] m_indexBuffer;
    public static final Material m_defaultMaterial;
    public long m_textureId;
    public int m_fileId;
    public boolean m_behindMobile;
    public byte m_lod;
    public BlendModes m_srcBlend;
    public BlendModes m_dstBlend;
    protected int m_numMaxParticles;
    protected Particle m_root;
    protected ArrayList<EmitterDefinition> m_definitions;
    protected float m_duration;
    protected boolean m_geocentric;
    protected long m_systemGUID;
    protected float m_timeBeforeStop;
    protected float m_x;
    protected float m_y;
    protected float m_z;
    private static int CURRENT_ID;
    private int m_id;
    private boolean m_kill;
    private boolean m_waitForStop;
    private float m_timeBeforeKill;
    private boolean m_justInit;
    String m_fileName;
    protected final float[] m_globalColor;
    private boolean m_soundIsRunning;
    private boolean m_canStopSound;
    private boolean m_enableSound;
    private static final float[] m_particleGeomPosition;
    protected static final float[] m_particleGeomColor;
    private static final float[] m_particleGeomTexCoord;
    private static int m_positionIndex;
    private static int m_colorIndex;
    private static int m_texCoordsIndex;
    protected GeometryMesh m_previousGeometry;
    protected ColorHelper m_colorHelper;
    private final RotationMatrix m_rotationMatrix;
    
    public ParticleSystem(final boolean isEditable) {
        super();
        this.m_srcBlend = BlendModes.One;
        this.m_dstBlend = BlendModes.InvSrcAlpha;
        this.m_justInit = false;
        this.m_globalColor = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        this.m_soundIsRunning = false;
        this.m_canStopSound = false;
        this.m_enableSound = true;
        this.m_previousGeometry = null;
        this.m_rotationMatrix = new RotationMatrix();
        this.checkout();
        if (isEditable) {
            this.m_editableData = new EditableData();
        }
        else {
            this.m_numMaxParticles = 0;
        }
        this.m_systemGUID = -2521772799257739264L;
    }
    
    protected boolean playSound() {
        return this.playSound(-1);
    }
    
    protected final boolean playSound(final int fightId) {
        return this.isVisible() && ParticleSoundManager.getInstance().playApsSound(this.m_fileId, this.m_id, fightId, -1);
    }
    
    public void setSoundEnable(final boolean enable) {
        this.m_enableSound = enable;
    }
    
    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
        final boolean isVisible = this.isVisible();
        if (isVisible) {
            if (!this.m_soundIsRunning) {
                this.startSound();
            }
        }
        else if (this.m_soundIsRunning && this.m_canStopSound) {
            this.stopSound();
        }
    }
    
    @Override
    public void update(final float timeIncrement) {
        if (this.m_root == null) {
            return;
        }
        if (this.m_justInit && this.isVisible()) {
            this.startSound();
            this.m_justInit = false;
        }
        this.m_root.update(this, timeIncrement);
        if (this.m_duration != 0.0f) {
            this.m_timeBeforeStop -= timeIncrement;
            if (this.m_timeBeforeStop <= 0.0f && !this.m_waitForStop) {
                this.stopAndKill();
            }
        }
        if (!this.m_root.isAlive() && !this.m_waitForStop) {
            this.stopAndKill();
        }
        if (!this.m_kill) {
            return;
        }
        if (this.m_waitForStop) {
            if (this.m_root.isEmitter()) {
                final Emitter[] emitters = this.m_root.m_emitters;
                for (int emitterIndex = 0, size = emitters.length; emitterIndex < size; ++emitterIndex) {
                    if (emitters[emitterIndex].isAlive()) {
                        return;
                    }
                }
            }
            this.immediateKill();
        }
        else {
            this.m_timeBeforeKill -= timeIncrement;
            if (this.m_timeBeforeKill <= 0.0f) {
                this.immediateKill();
            }
        }
    }
    
    private void startSound() {
        if (this.m_enableSound && !this.m_soundIsRunning) {
            this.m_canStopSound = this.playSound();
            this.m_soundIsRunning = true;
        }
    }
    
    private void stopSound() {
        if (this.m_soundIsRunning) {
            ParticleSoundManager.getInstance().cleanAps(this.m_id, this.m_canStopSound);
        }
        this.m_canStopSound = false;
        this.m_soundIsRunning = false;
    }
    
    @Override
    public void renderWithoutEffect(final Renderer renderer) {
        if (this.m_root == null || this.m_root.m_emitters == null) {
            return;
        }
        RenderStateManager.getInstance().setColorScale(1.0f);
        super.renderWithoutEffect(renderer);
    }
    
    public void start() {
        if (this.m_root == null || this.m_root.m_emitters == null) {
            return;
        }
        final Emitter[] emitters = this.m_root.m_emitters;
        for (int emitterIndex = 0, size = emitters.length; emitterIndex < size; ++emitterIndex) {
            final Emitter emitter = emitters[emitterIndex];
            emitter.reset();
            emitter.stopEmitting(false);
        }
    }
    
    public void reset() {
        this.m_previousGeometry = null;
        this.m_kill = false;
        this.m_waitForStop = false;
        this.m_justInit = true;
        this.m_enableSound = true;
        this.m_canStopSound = false;
        this.m_soundIsRunning = false;
        this.m_colorHelper = null;
    }
    
    public void stop() {
        if (this.m_root == null) {
            return;
        }
        if (this.m_root.m_emitters == null) {
            return;
        }
        final Emitter[] emitters = this.m_root.m_emitters;
        for (int i = 0, size = emitters.length; i < size; ++i) {
            emitters[i].stopEmitting(true);
        }
    }
    
    public void stopAndKill() {
        this.stop();
        this.m_kill = true;
        this.m_waitForStop = true;
    }
    
    public void stopAndKill(final int timeBeforeKill) {
        this.stop();
        this.m_timeBeforeKill = timeBeforeKill / 1000.0f;
        this.m_kill = true;
        this.m_waitForStop = false;
    }
    
    public void kill() {
        this.m_kill = true;
        this.m_waitForStop = false;
    }
    
    protected void immediateKill() {
        if (this.m_root == null) {
            return;
        }
        for (int numDefinitions = this.m_definitions.size(), definitionIndex = 0; definitionIndex < numDefinitions; ++definitionIndex) {
            this.m_definitions.get(definitionIndex).reset();
        }
        this.stopSound();
        this.m_enableSound = true;
        this.m_root.kill(this);
        this.m_root.removeReference();
        this.m_root = null;
        this.removeReference();
    }
    
    public boolean isEditable() {
        return this.m_editableData != null;
    }
    
    public void addEmitterDefinition(final EmitterDefinition definition) {
        this.m_definitions.add(definition);
        if (!this.isEditable()) {
            this.m_numMaxParticles += definition.m_maxParticlesCount;
        }
    }
    
    public final int getId() {
        return this.m_id;
    }
    
    public int getFileId() {
        return this.m_fileId;
    }
    
    public int getDuration() {
        return (int)(this.m_duration * 1000.0f);
    }
    
    public void setDuration(final int duration) {
        this.m_duration = duration / 1000.0f;
        if (this.m_duration != 0.0f) {
            this.m_timeBeforeStop = this.m_duration;
        }
    }
    
    public boolean isGeocentric() {
        return this.m_geocentric;
    }
    
    public void setGeocentric(final boolean geocentric) {
        this.m_geocentric = geocentric;
        this.m_root.m_geocentric = geocentric;
    }
    
    public ArrayList<EmitterDefinition> getEmitterDefinitions() {
        return this.m_definitions;
    }
    
    public final void setGlobalColor(final float[] color) {
        System.arraycopy(color, 0, this.m_globalColor, 0, color.length);
    }
    
    public final void setGlobalColor(final float r, final float g, final float b, final float a) {
        this.m_globalColor[0] = r;
        this.m_globalColor[1] = g;
        this.m_globalColor[2] = b;
        this.m_globalColor[3] = a;
    }
    
    public void setPosition(final float x, final float y) {
        this.m_x = x;
        this.m_y = y;
    }
    
    public void setPosition(final float x, final float y, final float z) {
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
    }
    
    public float getX() {
        return this.m_x;
    }
    
    public float getY() {
        return this.m_y;
    }
    
    public float getZ() {
        return this.m_z;
    }
    
    public void setSystemGUID(final long systemGUID) {
        this.m_systemGUID = systemGUID;
    }
    
    public final long getTextureName(final int bitmapId) {
        assert this.m_systemGUID != -2521772799257739264L;
        return this.m_systemGUID + bitmapId;
    }
    
    public void registerAllBaseEmitters() {
        this.m_root.addEmitters(this, this.m_definitions);
    }
    
    public static int countNumMaxParticles(final EmitterDefinition definition) {
        int numMaxParticles = 0;
        numMaxParticles += definition.m_maxParticlesCount;
        if (definition.m_emitterDefinitions == null) {
            return numMaxParticles;
        }
        for (int numDefinitions = definition.m_emitterDefinitions.size(), definitionIndex = 0; definitionIndex < numDefinitions; ++definitionIndex) {
            final EmitterDefinition emitterDefinition = definition.m_emitterDefinitions.get(definitionIndex);
            numMaxParticles += definition.m_maxParticlesCount * countNumMaxParticles(emitterDefinition);
        }
        return numMaxParticles;
    }
    
    public final void initialize(final Texture texture) {
        assert !this.isEditable() : "Initialize should not be called on editable particle systems";
        this.updateNumMaxParticles();
        if (this.m_numMaxParticles == 0) {
            return;
        }
        final GLGeometryMesh geometryMesh = GLGeometryMesh.Factory.newPooledInstance();
        this.updateGeometry(geometryMesh);
        geometryMesh.setBlendFunc(this.m_srcBlend, this.m_dstBlend);
        this.addTexturedGeometry(geometryMesh, texture, ParticleSystem.m_defaultMaterial);
        geometryMesh.removeReference();
        this.m_justInit = true;
    }
    
    private void updateNumMaxParticles() {
        this.m_numMaxParticles = 0;
        for (int numDefinitions = this.m_definitions.size(), definitionIndex = 0; definitionIndex < numDefinitions; ++definitionIndex) {
            final EmitterDefinition definition = this.m_definitions.get(definitionIndex);
            this.m_numMaxParticles += countNumMaxParticles(definition);
        }
    }
    
    private void updateGeometry(final GeometryMesh geometry) {
        geometry.create(GeometryMesh.MeshType.Quad, this.m_numMaxParticles * 4, IndexBuffer.INDICES);
    }
    
    public void reloadGeometry() {
        this.updateNumMaxParticles();
        this.updateGeometry((GeometryMesh)this.getGeometry(0));
    }
    
    protected static boolean particleBufferIsFull() {
        return ParticleSystem.m_positionIndex >= ParticleSystem.m_particleGeomPosition.length;
    }
    
    protected void addParticleGeometry(final Particle particle, final float x, final float y) {
        final float width = 2.0f * particle.m_halfWidth * particle.m_scaleX;
        final float height = 2.0f * particle.m_halfHeight * particle.m_scaleY;
        final float angleCos = MathHelper.cosf(particle.m_angle);
        final float angleSin = MathHelper.sinf(particle.m_angle);
        final float hotX = -particle.m_hotX * particle.m_scaleX;
        final float hotY = (particle.m_hotY - particle.m_halfHeight * 2.0f) * particle.m_scaleY;
        final float topLeftX = x + (angleCos * hotX - angleSin * hotY);
        final float topLeftY = y + (angleSin * hotX + angleCos * hotY);
        final float bottomLeftX = topLeftX - angleSin * height;
        final float bottomLeftY = topLeftY + angleCos * height;
        final float txAxisX = angleCos * width;
        final float txAxisY = angleSin * width;
        final float bottomRightX = bottomLeftX + txAxisX;
        final float bottomRightY = bottomLeftY + txAxisY;
        final float topRightX = topLeftX + txAxisX;
        final float topRightY = topLeftY + txAxisY;
        GeometryMesh geometryMesh;
        if (this.isEditable()) {
            geometryMesh = particle.m_sourceEmitter.m_geometries.get(particle.m_batchId);
        }
        else {
            geometryMesh = (GeometryMesh)this.getGeometry(0);
        }
        if (this.m_previousGeometry != null && this.m_previousGeometry != geometryMesh) {
            this.updateGeometry();
        }
        this.m_previousGeometry = geometryMesh;
        if (topLeftX == Float.NaN || topLeftY == Float.NaN || bottomLeftX == Float.NaN || bottomLeftY == Float.NaN || bottomRightX == Float.NaN || bottomRightY == Float.NaN || topRightX == Float.NaN || topRightY == Float.NaN) {
            return;
        }
        this.m_rotationMatrix.changeAngle(particle.m_angleX, particle.m_angleY, particle.m_angleZ);
        final float cX = particle.m_x + x;
        final float cY = particle.m_y + y;
        final float cZ = particle.m_z;
        this.m_rotationMatrix.transform(topLeftX, topLeftY, 0.0f, cX, cY, 0.0f);
        ParticleSystem.m_particleGeomPosition[ParticleSystem.m_positionIndex++] = this.m_rotationMatrix.m_x;
        ParticleSystem.m_particleGeomPosition[ParticleSystem.m_positionIndex++] = this.m_rotationMatrix.m_y;
        this.m_rotationMatrix.transform(bottomLeftX, bottomLeftY, 0.0f, cX, cY, 0.0f);
        ParticleSystem.m_particleGeomPosition[ParticleSystem.m_positionIndex++] = this.m_rotationMatrix.m_x;
        ParticleSystem.m_particleGeomPosition[ParticleSystem.m_positionIndex++] = this.m_rotationMatrix.m_y;
        this.m_rotationMatrix.transform(bottomRightX, bottomRightY, 0.0f, cX, cY, 0.0f);
        ParticleSystem.m_particleGeomPosition[ParticleSystem.m_positionIndex++] = this.m_rotationMatrix.m_x;
        ParticleSystem.m_particleGeomPosition[ParticleSystem.m_positionIndex++] = this.m_rotationMatrix.m_y;
        this.m_rotationMatrix.transform(topRightX, topRightY, 0.0f, cX, cY, 0.0f);
        ParticleSystem.m_particleGeomPosition[ParticleSystem.m_positionIndex++] = this.m_rotationMatrix.m_x;
        ParticleSystem.m_particleGeomPosition[ParticleSystem.m_positionIndex++] = this.m_rotationMatrix.m_y;
        final float red = particle.m_red * this.m_globalColor[0];
        final float green = particle.m_green * this.m_globalColor[1];
        final float blue = particle.m_blue * this.m_globalColor[2];
        final float alpha = particle.m_alpha * this.m_globalColor[3];
        ParticleSystem.m_particleGeomColor[ParticleSystem.m_colorIndex++] = red;
        ParticleSystem.m_particleGeomColor[ParticleSystem.m_colorIndex++] = green;
        ParticleSystem.m_particleGeomColor[ParticleSystem.m_colorIndex++] = blue;
        ParticleSystem.m_particleGeomColor[ParticleSystem.m_colorIndex++] = alpha;
        ParticleSystem.m_particleGeomColor[ParticleSystem.m_colorIndex++] = red;
        ParticleSystem.m_particleGeomColor[ParticleSystem.m_colorIndex++] = green;
        ParticleSystem.m_particleGeomColor[ParticleSystem.m_colorIndex++] = blue;
        ParticleSystem.m_particleGeomColor[ParticleSystem.m_colorIndex++] = alpha;
        ParticleSystem.m_particleGeomColor[ParticleSystem.m_colorIndex++] = red;
        ParticleSystem.m_particleGeomColor[ParticleSystem.m_colorIndex++] = green;
        ParticleSystem.m_particleGeomColor[ParticleSystem.m_colorIndex++] = blue;
        ParticleSystem.m_particleGeomColor[ParticleSystem.m_colorIndex++] = alpha;
        ParticleSystem.m_particleGeomColor[ParticleSystem.m_colorIndex++] = red;
        ParticleSystem.m_particleGeomColor[ParticleSystem.m_colorIndex++] = green;
        ParticleSystem.m_particleGeomColor[ParticleSystem.m_colorIndex++] = blue;
        ParticleSystem.m_particleGeomColor[ParticleSystem.m_colorIndex++] = alpha;
        ParticleSystem.m_particleGeomTexCoord[ParticleSystem.m_texCoordsIndex++] = particle.m_textureLeft;
        ParticleSystem.m_particleGeomTexCoord[ParticleSystem.m_texCoordsIndex++] = particle.m_textureBottom;
        ParticleSystem.m_particleGeomTexCoord[ParticleSystem.m_texCoordsIndex++] = particle.m_textureLeft;
        ParticleSystem.m_particleGeomTexCoord[ParticleSystem.m_texCoordsIndex++] = particle.m_textureTop;
        ParticleSystem.m_particleGeomTexCoord[ParticleSystem.m_texCoordsIndex++] = particle.m_textureRight;
        ParticleSystem.m_particleGeomTexCoord[ParticleSystem.m_texCoordsIndex++] = particle.m_textureTop;
        ParticleSystem.m_particleGeomTexCoord[ParticleSystem.m_texCoordsIndex++] = particle.m_textureRight;
        ParticleSystem.m_particleGeomTexCoord[ParticleSystem.m_texCoordsIndex++] = particle.m_textureBottom;
        if (topLeftX < this.m_minX) {
            this.m_minX = (int)topLeftX;
        }
        if (topLeftY < this.m_minY) {
            this.m_minY = (int)topLeftY;
        }
        if (bottomRightX > this.m_maxX) {
            this.m_maxX = (int)bottomRightX;
        }
        if (bottomRightY > this.m_maxY) {
            this.m_maxY = (int)bottomRightY;
        }
    }
    
    protected void updateGeometry() {
        if (this.m_previousGeometry == null) {
            return;
        }
        final VertexBufferPCT vertexBuffer = this.m_previousGeometry.getVertexBuffer();
        try {
            final int numVertices = ParticleSystem.m_positionIndex / 2;
            vertexBuffer.addPositions(ParticleSystem.m_particleGeomPosition, ParticleSystem.m_positionIndex);
            this.m_colorHelper.applyImmediate(vertexBuffer, ParticleSystem.m_particleGeomColor, ParticleSystem.m_colorIndex);
            vertexBuffer.addTexCoords(ParticleSystem.m_particleGeomTexCoord, ParticleSystem.m_texCoordsIndex);
            vertexBuffer.setNumVertices(vertexBuffer.getNumVertices() + numVertices);
        }
        catch (BufferOverflowException e) {
            ParticleSystem.m_logger.error((Object)("systemFile=" + this.getFileId() + " positionIndex=" + ParticleSystem.m_positionIndex + "  maxVertex=" + vertexBuffer.getMaxVertices() + "  numVertex=" + vertexBuffer.getNumVertices()));
            ParticleSystem.m_logger.error((Object)"", (Throwable)e);
            System.exit(0);
        }
        resetIndexes();
    }
    
    protected static void resetIndexes() {
        ParticleSystem.m_positionIndex = 0;
        ParticleSystem.m_colorIndex = 0;
        ParticleSystem.m_texCoordsIndex = 0;
    }
    
    private static int getNextFreeId() {
        return ParticleSystem.CURRENT_ID++;
    }
    
    public boolean isAlive() {
        return !this.m_kill;
    }
    
    public Emitter[] getEmitters() {
        if (this.m_root != null) {
            return this.m_root.m_emitters;
        }
        return null;
    }
    
    public boolean isWaitForStop() {
        return this.m_waitForStop;
    }
    
    public float getTimeBeforeStop() {
        return this.m_timeBeforeStop;
    }
    
    public EditableData getEditableData() {
        return this.m_editableData;
    }
    
    public void setBlendMode(final BlendModes src, final BlendModes dst) {
        this.m_srcBlend = src;
        this.m_dstBlend = dst;
    }
    
    @Override
    protected void checkin() {
        super.checkin();
        if (this.m_root != null) {
            this.m_root.reset();
            this.m_root.removeReference();
            this.m_root = null;
        }
        this.m_definitions = null;
        this.m_previousGeometry = null;
    }
    
    @Override
    protected void checkout() {
        super.checkout();
        if (this.m_editableData != null) {
            this.m_editableData = new EditableData();
        }
        else {
            this.m_numMaxParticles = 0;
        }
        this.m_systemGUID = -2521772799257739264L;
        this.m_root = new Particle() {
            @Override
            public float getX() {
                return this.m_geocentric ? 0.0f : ParticleSystem.this.getX();
            }
            
            @Override
            public float getY() {
                return this.m_geocentric ? 0.0f : ParticleSystem.this.getY();
            }
            
            @Override
            public float getZ() {
                return this.m_geocentric ? 0.0f : ParticleSystem.this.getZ();
            }
        };
        this.m_root.m_life = 0.0f;
        this.m_root.m_lifeTime = Float.MAX_VALUE;
        this.m_root.m_x = 0.0f;
        this.m_root.m_y = 0.0f;
        this.m_root.m_z = 0.0f;
        this.m_root.m_velocityX = 0.0f;
        this.m_root.m_velocityY = 0.0f;
        this.m_root.m_velocityZ = 0.0f;
        this.m_definitions = new ArrayList<EmitterDefinition>(1);
        this.m_id = getNextFreeId();
        this.m_textureId = 0L;
        this.m_fileId = 0;
        this.m_behindMobile = false;
        this.m_lod = 0;
        this.m_srcBlend = BlendModes.One;
        this.m_dstBlend = BlendModes.InvSrcAlpha;
        this.reset();
    }
    
    public void setColorHelperProvider(final ColorHelper helper) {
        this.m_colorHelper = helper;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ParticleSystem.class);
        m_maxIndices = 16384;
        m_indexBuffer = new short[ParticleSystem.m_maxIndices];
        for (int i = 0; i < ParticleSystem.m_indexBuffer.length; ++i) {
            ParticleSystem.m_indexBuffer[i] = (short)i;
        }
        (m_defaultMaterial = Material.Factory.newInstance()).copy(Material.WHITE_NO_SPECULAR);
        m_particleGeomPosition = new float[2 * ParticleSystem.m_maxIndices];
        m_particleGeomColor = new float[4 * ParticleSystem.m_maxIndices];
        m_particleGeomTexCoord = new float[2 * ParticleSystem.m_maxIndices];
        ParticleSystem.CURRENT_ID = 1;
        ParticleSystem.m_positionIndex = 0;
        ParticleSystem.m_colorIndex = 0;
        ParticleSystem.m_texCoordsIndex = 0;
    }
    
    public class EditableData
    {
        protected final TIntObjectHashMap<AlphaBitmapData> m_bitmapLibrary;
        protected final TIntObjectHashMap<Texture> m_textureUsed;
        
        public EditableData() {
            super();
            this.m_bitmapLibrary = new TIntObjectHashMap<AlphaBitmapData>();
            this.m_textureUsed = new TIntObjectHashMap<Texture>();
        }
        
        public final AlphaBitmapData getBitmap(final int id) {
            return this.m_bitmapLibrary.get(id);
        }
        
        public final void addBitmap(final int id, final AlphaBitmapData alphaBitmapData, final boolean keepData) {
            this.m_bitmapLibrary.put(id, alphaBitmapData);
            this.addTexture(id, alphaBitmapData, keepData);
        }
        
        public final void removeBitmap(final int id) {
            this.m_bitmapLibrary.remove(id);
        }
        
        private void addTexture(final int id, final AlphaBitmapData alphaBitmapData, final boolean keepData) {
            final long textureName = ParticleSystem.this.getTextureName(id);
            Texture texture = TextureManager.getInstance().getTexture(textureName);
            if (texture != null) {
                texture.addReference();
                this.m_textureUsed.put(id, texture);
                return;
            }
            final Layer layer = Image.toPowerOfTwo(alphaBitmapData.getData(), alphaBitmapData.getWidth(), alphaBitmapData.getHeight(), 32);
            final Image image = new Image(FourCC.RAW, layer);
            texture = TextureManager.getInstance().createTexture(RendererType.OpenGL.getRenderer(), textureName, image, keepData);
            texture.addReference();
            this.m_textureUsed.put(id, texture);
            layer.removeReference();
            image.removeReference();
        }
        
        void addEmitter(final Emitter emitter) {
            assert ParticleSystem.this.isEditable();
            final int numVertices = emitter.m_definition.m_maxParticlesCount * 4;
            final ArrayList<ParticleModel> models = emitter.m_definition.m_models;
            final int numModels = models.size();
            if (emitter.m_geometries == null) {
                emitter.m_geometries = new ArrayList<GeometryMesh>(numModels);
            }
            for (int modelIndex = 0; modelIndex < numModels; ++modelIndex) {
                final ParticleModel particleModel = models.get(modelIndex);
                final GLGeometryMesh geometryMesh = GLGeometryMesh.Factory.newPooledInstance();
                geometryMesh.create(GeometryMesh.MeshType.Quad, numVertices, IndexBuffer.INDICES);
                geometryMesh.setBlendFunc(BlendModes.One, BlendModes.InvSrcAlpha);
                final Texture texture = ParticleSystem.this.m_editableData.m_textureUsed.get(particleModel.getBitmapId());
                ParticleSystem.this.addTexturedGeometry(geometryMesh, texture, ParticleSystem.m_defaultMaterial);
                geometryMesh.removeReference();
                geometryMesh.setBlendFunc(ParticleSystem.this.m_srcBlend, ParticleSystem.this.m_dstBlend);
                emitter.m_geometries.add(geometryMesh);
            }
            for (int i = 0, count = ParticleSystem.this.getNumGeometries(); i < count; ++i) {
                final Geometry geometry = ParticleSystem.this.getGeometry(i);
                geometry.addReference();
                final Texture texture = ParticleSystem.this.getTexture(i);
                texture.addReference();
            }
            ParticleSystem.this.clear();
            if (ParticleSystem.this.m_root.m_emitters != null) {
                final Emitter[] emitters = ParticleSystem.this.m_root.m_emitters;
                for (int emitterIndex = 0, size = emitters.length; emitterIndex < size; ++emitterIndex) {
                    this.addEmitterRecurse(emitters[emitterIndex]);
                }
            }
            for (int i = 0, count = ParticleSystem.this.getNumGeometries(); i < count; ++i) {
                ParticleSystem.this.getTexture(i).removeReference();
            }
        }
        
        private void addEmitterRecurse(final Emitter emitter) {
            if (emitter == null) {
                return;
            }
            final ArrayList<ParticleModel> models = emitter.m_definition.m_models;
            for (int numModels = models.size(), modelIndex = 0; modelIndex < numModels; ++modelIndex) {
                final ParticleModel particleModel = models.get(modelIndex);
                final Texture texture = ParticleSystem.this.m_editableData.m_textureUsed.get(particleModel.getBitmapId());
                final GeometryMesh geometryMesh = emitter.m_geometries.get(modelIndex);
                ParticleSystem.this.addTexturedGeometry(geometryMesh, texture, ParticleSystem.m_defaultMaterial);
                geometryMesh.removeReference();
            }
            if (emitter.m_children != null) {
                for (int numChildren = emitter.m_children.size(), childIndex = 0; childIndex < numChildren; ++childIndex) {
                    final Particle particle = emitter.m_children.get(childIndex);
                    if (particle.isEmitter()) {
                        final Emitter[] emitters = particle.m_emitters;
                        for (int emitterIndex = 0, size = emitters.length; emitterIndex < size; ++emitterIndex) {
                            this.addEmitterRecurse(emitters[emitterIndex]);
                        }
                    }
                }
            }
        }
    }
}
