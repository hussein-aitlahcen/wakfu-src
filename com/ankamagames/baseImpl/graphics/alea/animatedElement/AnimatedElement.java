package com.ankamagames.baseImpl.graphics.alea.animatedElement;

import com.ankamagames.framework.sound.group.*;
import com.ankamagames.framework.ai.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.longKey.*;
import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.framework.graphics.engine.fx.effets.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import java.net.*;
import com.ankamagames.framework.sound.*;
import com.ankamagames.baseImpl.graphics.alea.display.occlusion.*;
import javax.media.opengl.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.sun.opengl.util.texture.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.*;
import java.nio.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.debugANM.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.sound.openAL.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.actions.*;

public class AnimatedElement implements ScreenTarget, ObservedSource, IsoWorldTarget, Maskable, LitSceneObject, PhysicalRadiusOwner, AnimatedObject, Hiddable, ParticleTarget, LightTarget
{
    private static final Logger m_logger;
    private static boolean m_enableRunningRadius;
    private String m_staticAnimationKey;
    private String m_hitAnimationKey;
    private final Vector4 m_screenPosition;
    protected boolean m_screenPositionNeedsRecompute;
    protected String m_animation;
    protected boolean m_animationChanged;
    protected float m_animationSpeed;
    protected boolean m_animatedChange;
    protected String m_previousAnimation;
    protected boolean m_canPlaySound;
    protected static final float TOTAL_FADE_TIME = 500.0f;
    protected float m_fade;
    protected boolean m_fadeIfOnScreen;
    protected boolean m_fadeWhenReady;
    protected final LongLongLightWeightMap m_soundRefs;
    protected SoundValidator m_soundValidator;
    private String m_animationSuffix;
    protected float m_renderRadius;
    protected EntityGroup m_entity;
    protected String m_gfxId;
    protected String m_path;
    protected int m_lastFrame;
    protected int m_lastDisplayedFrame;
    protected int m_lastTime;
    protected int m_elapsedTime;
    protected boolean m_equipmentChanged;
    private boolean m_animated;
    private AnmInstance m_anmInstance;
    private final Rect m_ANMScreenRect;
    private static final Rect m_maxBoundingRect;
    protected Material m_material;
    private BlendModes m_srcBlend;
    private BlendModes m_destBlend;
    private static final ArrayList<AnmAction> m_actions;
    private byte m_deltaZ;
    private final ArrayList<AnimationEndedListener> m_animationEndedListeners;
    private final ArrayList<AnimationEndedListener> m_animationEndedListenersToRemove;
    private boolean m_needToCleanAnimationEndedListeners;
    private boolean m_alphaMaskEnabled;
    private EntityOcclusionRender m_renderer;
    public static boolean DISPLAY_EXTENDED_OVERHEAD_INFOS;
    private boolean m_occluder;
    protected long m_id;
    protected float m_worldX;
    protected float m_worldY;
    protected float m_altitude;
    protected final Point3 m_coordinates;
    protected int m_maskKey;
    protected short m_layerId;
    protected int m_screenX;
    protected int m_screenY;
    protected int m_screenHeight;
    protected ArrayList<ScreenTargetWatcher> m_watchers;
    protected boolean m_watchersVisible;
    protected boolean m_watchersClipped;
    protected ArrayList<VisibleChangedListener> m_visibiltyListeners;
    private boolean m_visible;
    protected boolean m_visibleChanged;
    private boolean m_lastLayerVisibility;
    protected final float[] m_color;
    private boolean m_colorChanged;
    protected float m_originalAlpha;
    protected float m_desiredAlpha;
    private byte m_hideState;
    private static final float[] m_tempColor;
    protected float m_scale;
    protected short m_visualHeight;
    private boolean m_incrementZOrder;
    private ArrayList<FreeParticleSystem> m_attachedParticles;
    private ArrayList<IsoLightSource> m_attachedLight;
    private boolean m_stopped;
    private AnmInstance m_anmInstanceLoading;
    private final ArrayList<Anm.LoadedListener> m_loadingListeners;
    private static final Rect m_testRect;
    
    protected void reset() {
        for (int i = 0, size = this.m_loadingListeners.size(); i < size; ++i) {
            this.m_loadingListeners.get(i).remove();
        }
        this.m_loadingListeners.clear();
        this.m_renderRadius = 1.0f;
        this.m_fade = 1.0f;
        final float[] color = this.m_color;
        final int n = 0;
        final float[] color2 = this.m_color;
        final int n2 = 1;
        final float[] color3 = this.m_color;
        final int n3 = 2;
        final float[] color4 = this.m_color;
        final int n4 = 3;
        final float n5 = 1.0f;
        color3[n3] = (color4[n4] = n5);
        color[n] = (color2[n2] = n5);
        this.m_originalAlpha = n5;
        this.m_desiredAlpha = n5;
        this.m_incrementZOrder = false;
        this.m_fadeIfOnScreen = false;
        this.m_fadeWhenReady = false;
        this.m_lastFrame = -1;
        this.m_lastDisplayedFrame = -1;
        this.m_lastTime = 0;
        this.m_animationChanged = true;
        this.m_canPlaySound = true;
        this.m_hideState = 3;
        this.clearAnimationEndedListener();
        if (this.m_anmInstance != null) {
            this.m_anmInstance.reset();
            this.m_anmInstance = null;
        }
        if (this.m_anmInstanceLoading != null) {
            this.m_anmInstanceLoading.reset();
            this.m_anmInstanceLoading = null;
        }
        if (this.m_entity != null) {
            this.m_entity.removeReference();
            this.m_entity = null;
            this.m_renderer.reset();
            assert this.m_material != null;
            this.m_material.removeReference();
            this.m_material = null;
        }
        this.onDestroy();
    }
    
    Entity3D createEntity() {
        final Entity3D entity = Entity3D.Factory.newPooledInstance();
        this.initializeEntity(entity);
        return entity;
    }
    
    private void initializeEntity(final Entity entity) {
        final TransformerSRT transformer = new TransformerSRT();
        transformer.setIdentity();
        transformer.setScale(this.getScale(), this.getScale(), 1.0f);
        entity.getTransformer().addTransformer(transformer);
        entity.setEffect(EffectManager.getInstance().getBaseEffect(), FxConstants.TRANSFORM_TECHNIQUE, FxConstants.COLOR_SCALE_FOR_WORLD_PARAMS);
        if (this.m_occluder) {
            entity.m_userFlag1 |= 0x2;
        }
        else {
            entity.m_userFlag1 &= 0xFFFFFFFD;
        }
        entity.m_owner = this;
    }
    
    @Override
    public byte getPhysicalRadius() {
        return 0;
    }
    
    @Override
    public boolean isPositionComputed() {
        return this.m_screenX != Integer.MIN_VALUE && this.m_screenY != Integer.MIN_VALUE;
    }
    
    @Override
    public float getObservedX() {
        return this.m_worldX - this.m_worldY;
    }
    
    @Override
    public float getObservedY() {
        return -(this.m_worldX + this.m_worldY);
    }
    
    @Override
    public float getObservedZ() {
        return this.m_altitude / 4.8f;
    }
    
    @Override
    public boolean isPositionRelative() {
        return false;
    }
    
    @Override
    public int getGroupInstanceId() {
        return this.getMaskKey();
    }
    
    public final Entity getEntity() {
        return this.m_entity;
    }
    
    public final Entity3D getAnmEntity() {
        return this.m_renderer.getAnmEntity();
    }
    
    public boolean displayExtendedInfos() {
        return AnimatedElement.DISPLAY_EXTENDED_OVERHEAD_INFOS;
    }
    
    public final Material getMaterial() {
        return this.m_material;
    }
    
    public final void setMaterial(final Material material) {
        this.m_material.copy(material);
    }
    
    public String getPath() {
        return this.m_path;
    }
    
    public final byte getDeltaZ() {
        return this.m_deltaZ;
    }
    
    public final void setDeltaZ(final int deltaZ) {
        if (deltaZ < 0 || deltaZ >= LayerOrder.LAYER_COUNT) {
            AnimatedElement.m_logger.error((Object)("DeltaZ incorrect " + deltaZ));
            return;
        }
        this.m_deltaZ = (byte)deltaZ;
    }
    
    public void setScale(final float scale) {
        this.m_scale = scale;
    }
    
    public void setGfxId(final String name) {
        final String oldGfx = this.m_gfxId;
        this.m_gfxId = name;
        if (oldGfx != this.m_gfxId) {
            this.forceReloadAnimation();
        }
    }
    
    public String getGfxId() {
        return this.m_gfxId;
    }
    
    protected String getANMKey(final String animName) {
        final String animationSuffix = this.getAnimationSuffix();
        if (animationSuffix != null) {
            return animName + animationSuffix;
        }
        return animName;
    }
    
    public void setSoundValidator(final SoundValidator soundValidator) {
        this.m_soundValidator = soundValidator;
    }
    
    @Override
    public SoundValidator getSoundValidator() {
        return this.m_soundValidator;
    }
    
    public final void resetMaterial() {
        this.m_material.copy(AnmInstance.getDefaultMaterial());
    }
    
    public void copyData(final AnimatedElement source) {
        if (source == null) {
            return;
        }
        this.m_equipmentChanged = true;
        this.m_animatedChange = true;
        this.m_colorChanged = true;
        if (source.getAnmEntity() == null) {
            source.createEntity3D();
        }
        final Entity sourceEntity = source.getEntity();
        final EffectParams params = EffectParams.clone(sourceEntity.getEffectParams());
        this.m_entity.setEffect(sourceEntity.getEffect(), sourceEntity.getTechniqueCRC(), params);
        this.m_material.copy(source.m_material);
        this.m_srcBlend = source.m_srcBlend;
        this.m_destBlend = source.m_destBlend;
        this.m_stopped = source.m_stopped;
        this.m_path = source.m_path;
        this.m_gfxId = source.m_gfxId;
        this.m_renderRadius = source.m_renderRadius;
        this.m_animationSpeed = source.m_animationSpeed;
        if (source.m_anmInstance != null) {
            if (this.m_anmInstance != null) {
                this.m_anmInstance.reset();
            }
            this.m_anmInstance = new AnmInstance(source.m_anmInstance);
        }
        if (source.m_anmInstanceLoading != null) {
            if (this.m_anmInstanceLoading != null) {
                this.m_anmInstanceLoading.reset();
            }
            this.m_anmInstanceLoading = new AnmInstance(source.m_anmInstanceLoading);
            this.initializeAnmInstanceLoading();
        }
    }
    
    public final void setBlendFunc(final BlendModes srcBlend, final BlendModes destBlend) {
        this.m_srcBlend = srcBlend;
        this.m_destBlend = destBlend;
    }
    
    private static Anm loadEquipment(final String path, final boolean async) {
        String directoryPath;
        try {
            final URL url = ContentFileHelper.getURL(path);
            final String urlPath = url.getPath();
            directoryPath = url.getProtocol() + ":" + urlPath;
        }
        catch (MalformedURLException e) {
            if (async) {
                AnimatedElement.m_logger.error((Object)"Exception", (Throwable)e);
                return null;
            }
            directoryPath = path;
        }
        Anm equipment;
        try {
            equipment = AnmManager.getInstance().loadAnmFile(directoryPath, async);
        }
        catch (IOException e2) {
            AnimatedElement.m_logger.error((Object)"Unable to load equipment", (Throwable)e2);
            return null;
        }
        return equipment;
    }
    
    public static Anm loadEquipment(final String path) {
        return loadEquipment(path, true);
    }
    
    public void applyParts(final String path, final String... partsCrc) {
        if (path == null) {
            AnimatedElement.m_logger.warn((Object)"on veut appliquer un equipemnt depuis un path null");
            return;
        }
        final Anm anm = loadEquipment(path);
        if (anm == null) {
            return;
        }
        this.applyParts(anm, Engine.getPartsNames(partsCrc));
    }
    
    public Anm applyAllPartsFrom(final String path) {
        if (path == null) {
            AnimatedElement.m_logger.warn((Object)"on veut appliquer un equipemnt depuis un path null");
            return null;
        }
        final Anm anm = loadEquipment(path);
        if (anm == null) {
            return anm;
        }
        this.applyParts(anm, new int[0]);
        return anm;
    }
    
    public Anm applyAllPartsFrom(final int fileId) {
        final String path = this.getGfxFilePath(fileId);
        if (path == null) {
            AnimatedElement.m_logger.warn((Object)"on veut appliquer un equipemnt depuis un path null");
            return null;
        }
        return this.applyAllPartsFrom(path);
    }
    
    protected String getGfxFilePath(final int fileId) {
        return null;
    }
    
    public void unapplyAllPartsFrom(final Anm anm) {
        if (anm == null) {
            return;
        }
        this.removeParts(anm, new int[0]);
    }
    
    public void applyParts(@NotNull final Anm equipment, final int... partsCrc) {
        if (this.getAnmInstance() == null) {
            return;
        }
        this.getAnmInstance().applyParts(equipment, partsCrc);
        this.forceUpdateEquipment();
    }
    
    public void applyParts(@NotNull final Anm equipment, final String[] partsName) {
        this.applyParts(equipment, Engine.getPartsNames(partsName));
    }
    
    public void forceUpdateEquipment() {
        this.m_equipmentChanged = true;
    }
    
    public void removeParts(@NotNull final Anm equipement, @NotNull final int... partsCrc) {
        if (this.getAnmInstance() != null) {
            this.getAnmInstance().unapplyParts(equipement, partsCrc);
        }
        this.forceUpdateEquipment();
    }
    
    private void loadFromAnm(final String fileName, final boolean async) throws IOException {
        this.m_path = fileName;
        if (this.m_anmInstanceLoading != null) {
            this.m_anmInstanceLoading.reset();
        }
        this.m_anmInstanceLoading = AnmManager.getInstance().createInstanceFromAnm(fileName, async);
        this.initializeAnmInstanceLoading();
    }
    
    private void initializeAnmInstanceLoading() {
        this.onAnmLoaded(this.m_anmInstanceLoading, new Runnable() {
            @Override
            public void run() {
                if (AnimatedElement.this.m_anmInstance != null) {
                    AnimatedElement.this.setRenderRadius(AnimatedElement.this.m_anmInstance.getRenderRadius());
                }
            }
        });
        if (this.m_anmInstance == null) {
            this.m_anmInstance = this.m_anmInstanceLoading;
            this.m_anmInstanceLoading = null;
        }
        else {
            this.onAnmLoaded(this.m_anmInstanceLoading, new Runnable() {
                @Override
                public void run() {
                    if (AnimatedElement.this.m_anmInstanceLoading == null) {
                        return;
                    }
                    if (AnimatedElement.this.m_anmInstance != null) {
                        AnimatedElement.this.m_anmInstance.reset();
                    }
                    AnimatedElement.this.m_anmInstance = AnimatedElement.this.m_anmInstanceLoading;
                    AnimatedElement.this.m_anmInstanceLoading = null;
                    AnimatedElement.this.forceReloadAnimation();
                }
            });
        }
    }
    
    public final void onAnmLoaded(final Runnable loadedListener) {
        final AnmInstance anmInstance = this.getAnmInstance();
        if (anmInstance == null) {
            return;
        }
        this.onAnmLoaded(anmInstance, loadedListener);
    }
    
    private void onAnmLoaded(final AnmInstance anmInstance, final Runnable loadedListener) {
        this.onAnmLoaded(anmInstance.getSource(), loadedListener);
    }
    
    public final void onAnmLoaded(final Anm anm, final Runnable loadedListener) {
        if (anm.isReady()) {
            loadedListener.run();
        }
        else {
            final Anm.LoadedListener listener = new Anm.LoadedListener(anm) {
                @Override
                public void run() {
                    AnimatedElement.this.m_loadingListeners.remove(this);
                    loadedListener.run();
                }
            };
            this.m_loadingListeners.add(listener);
        }
    }
    
    protected void setDefaultRadius() {
        if (this.getAnmInstance() == null) {
            return;
        }
        this.setRenderRadius(this.getAnmInstance().getRenderRadius());
    }
    
    public void load(final String fileName, final boolean async) throws IOException {
        assert fileName.endsWith("anm") || fileName.endsWith("ANM");
        this.loadFromAnm(fileName, async);
    }
    
    public AnimatedElement() {
        this(0L);
    }
    
    public AnimatedElement(final long id) {
        this(id, 0.0f, 0.0f);
    }
    
    public AnimatedElement(final long id, final float worldX, final float worldY) {
        this(id, worldX, worldY, 0.0f);
    }
    
    public AnimatedElement(final long id, final float worldX, final float worldY, final float altitude) {
        super();
        this.m_staticAnimationKey = "AnimStatique";
        this.m_hitAnimationKey = "AnimHit";
        this.m_animation = "AnimStatique";
        this.m_animationChanged = false;
        this.m_animationSpeed = 1.0f;
        this.m_animatedChange = true;
        this.m_previousAnimation = "AnimStatique";
        this.m_canPlaySound = true;
        this.m_fade = 1.0f;
        this.m_fadeIfOnScreen = false;
        this.m_fadeWhenReady = false;
        this.m_soundRefs = new LongLongLightWeightMap(5);
        this.m_soundValidator = SoundValidatorAll.INSTANCE;
        this.m_animationSuffix = null;
        this.m_renderRadius = 1.0f;
        this.m_lastFrame = -1;
        this.m_lastDisplayedFrame = -1;
        this.m_lastTime = 0;
        this.m_elapsedTime = 0;
        this.m_equipmentChanged = false;
        this.m_animated = true;
        this.m_ANMScreenRect = new Rect();
        this.m_srcBlend = BlendModes.One;
        this.m_destBlend = BlendModes.InvSrcAlpha;
        this.m_deltaZ = LayerOrder.MOBILE.getDeltaZ();
        this.m_animationEndedListeners = new ArrayList<AnimationEndedListener>(1);
        this.m_animationEndedListenersToRemove = new ArrayList<AnimationEndedListener>(1);
        this.m_needToCleanAnimationEndedListeners = false;
        this.m_alphaMaskEnabled = false;
        this.m_renderer = OcclusionProcess.getCurrent().createRenderer();
        this.m_occluder = false;
        this.m_altitude = 0.0f;
        this.m_coordinates = new Point3();
        this.m_screenX = Integer.MIN_VALUE;
        this.m_screenY = Integer.MIN_VALUE;
        this.m_watchers = null;
        this.m_watchersVisible = true;
        this.m_watchersClipped = true;
        this.m_visibiltyListeners = null;
        this.m_visible = true;
        this.m_visibleChanged = true;
        this.m_lastLayerVisibility = true;
        this.m_color = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
        this.m_colorChanged = false;
        this.m_originalAlpha = 1.0f;
        this.m_desiredAlpha = 1.0f;
        this.m_hideState = 3;
        this.m_scale = 1.0f;
        this.m_visualHeight = 6;
        this.m_incrementZOrder = false;
        this.m_loadingListeners = new ArrayList<Anm.LoadedListener>();
        this.setId(id);
        this.m_worldX = worldX;
        this.m_worldY = worldY;
        this.m_altitude = altitude;
        this.m_screenPosition = new Vector4();
        this.m_screenPositionNeedsRecompute = true;
        this.onPositionChanged();
        if (Threading.isOpenGLThread()) {
            this.createEntity3D();
        }
        MaskableHelper.setUndefined(this);
        this.fadeIfOnScreen();
    }
    
    private void createEntity3D() {
        assert this.m_entity == null;
        assert this.m_material == null;
        (this.m_material = Material.Factory.newPooledInstance()).copy(AnmInstance.getDefaultMaterial());
        this.initializeEntity(this.m_entity = this.m_renderer.createEntity(this));
    }
    
    public void forceReloadAnimation() {
        this.m_animationChanged = true;
    }
    
    public String getAnimation() {
        return this.m_animation;
    }
    
    @Override
    public boolean setAnimation(final String animation) {
        if (this.m_animation.equals(animation)) {
            return false;
        }
        this.setDefaultRadius();
        this.setIncrementZOrder(false);
        this.m_previousAnimation = this.m_animation;
        this.m_animation = animation;
        this.forceReloadAnimation();
        return true;
    }
    
    public void setCanPlaySound(final boolean canPlaySound) {
        if (!(this.m_canPlaySound = canPlaySound)) {
            this.stopAllSounds();
        }
    }
    
    @Override
    public boolean canPlaySound() {
        return this.m_canPlaySound && this.isVisible();
    }
    
    public String getAnimationSuffix() {
        return this.m_animationSuffix;
    }
    
    @Override
    public String getPreviousAnimation() {
        return this.m_previousAnimation;
    }
    
    public void setAnimationSuffix(final String animationSuffix) {
        final boolean shouldReload = animationSuffix != this.m_animationSuffix || (animationSuffix != null && !animationSuffix.equals(this.m_animationSuffix));
        if (shouldReload) {
            this.m_animationSuffix = animationSuffix;
            this.forceReloadAnimation();
        }
    }
    
    public void setAnimationSpeed(final float animationSpeed) {
        this.m_animationSpeed = animationSpeed;
    }
    
    public String getDisplayObjectLinkage() {
        String linkageName = this.m_animation;
        final String animationSuffix = this.getAnimationSuffix();
        if (animationSuffix != null) {
            linkageName += animationSuffix;
        }
        return linkageName;
    }
    
    public final void fadeIfOnScreen() {
        this.m_fadeIfOnScreen = true;
    }
    
    protected final boolean hitTest(final float x, final float y, final float scaling) {
        boolean hit = this.m_anmInstance != null && this.m_ANMScreenRect.contains((int)x, (int)y);
        if (hit) {
            final float px = x - this.m_ANMScreenRect.getXMin();
            final float py = y - this.m_ANMScreenRect.getYMin();
            hit = this.recursiveGeometryHitTest(this.m_entity, px, py, scaling);
        }
        return hit;
    }
    
    public boolean hitTest(final float x, final float y) {
        return this.hitTest(x, y, 1.0f);
    }
    
    private boolean recursiveGeometryHitTest(final Entity root, final float x, final float y, final float scaling) {
        if (root == null) {
            return false;
        }
        final ArrayList<Entity> children = root.getChildList();
        if (children == null) {
            return false;
        }
        final Vector4 v0 = new Vector4();
        final Vector4 v = new Vector4();
        final Vector4 v2 = new Vector4();
        final Vector2 u = new Vector2();
        final Vector2 v3 = new Vector2();
        final Vector4 p = new Vector4(x, y, 0.0f, 0.0f);
        final boolean usePerfectHit = this.m_anmInstance.usePerfectHitTest() && scaling == 1.0f;
        final float minX = this.m_anmInstance.getMinX();
        final float minY = this.m_anmInstance.getMinY();
        for (final Entity child : children) {
            for (int count = ((Entity3D)child).getNumGeometries(), i = 0; i < count; ++i) {
                final Geometry geometry = ((Entity3D)child).getGeometry(i);
                if (geometry instanceof GLGeometryMesh) {
                    final GLGeometryMesh g = (GLGeometryMesh)geometry;
                    final VertexBufferPCT vbpct = g.getVertexBuffer();
                    final int numVertices = vbpct.getNumVertices();
                    final FloatBuffer vb = vbpct.getPositionBuffer();
                    for (int k = 0; k < numVertices; k += 4) {
                        final int l = k * 2;
                        v0.set(vb.get(l + 0), vb.get(l + 1), 0.0f, 1.0f);
                        v.set(vb.get(l + 2), vb.get(l + 3), 0.0f, 1.0f);
                        v2.set(vb.get(l + 4), vb.get(l + 5), 0.0f, 1.0f);
                        u.set(v2.getX() - v.getX(), v2.getY() - v.getY());
                        v3.set(v0.getX() - v.getX(), v0.getY() - v.getY());
                        final float offsetX = minX - v.getX();
                        final float offsetY = minY - v.getY();
                        p.set(x + offsetX, y + offsetY, 0.0f, 0.0f);
                        if (scaling != 1.0f) {
                            final float s = (scaling - 1.0f) * 0.5f;
                            v.set(v.m_x - s * (u.m_x + v3.m_x), v.m_y - s * (u.m_y + v3.m_y), v.m_z, v.m_w);
                            u.setMul(scaling);
                            v3.setMul(scaling);
                        }
                        final float t = v3.getY() * u.getX() - v3.getX() * u.getY();
                        if (t != 0.0f) {
                            float a;
                            float b;
                            if (u.getX() == 0.0f) {
                                assert v3.getX() != 0.0f;
                                a = v3.getY() * p.getX() - v3.getX() * p.getY();
                                a /= t;
                                if (a < 0.0f) {
                                    continue;
                                }
                                if (a > 1.0f) {
                                    continue;
                                }
                                b = (p.getX() - a * u.getX()) / v3.getX();
                                if (b < 0.0f) {
                                    continue;
                                }
                                if (b > 1.0f) {
                                    continue;
                                }
                            }
                            else {
                                b = p.getY() * u.getX() - p.getX() * u.getY();
                                b /= t;
                                if (b < 0.0f) {
                                    continue;
                                }
                                if (b > 1.0f) {
                                    continue;
                                }
                                a = (p.getX() - b * v3.getX()) / u.getX();
                                if (a < 0.0f) {
                                    continue;
                                }
                                if (a > 1.0f) {
                                    continue;
                                }
                            }
                            assert a >= 0.0f && a <= 1.0f && b >= 0.0f && b <= 1.0f : "et hop, du code tout bugg\u00ef¿½";
                            final FloatBuffer tb = vbpct.getTexCoord0Buffer();
                            final TextureCoords coords = new TextureCoords(tb.get(l), tb.get(l + 1), tb.get(l + 4), tb.get(l + 5));
                            if (!usePerfectHit || this.perfectHitTest(a, b, coords, ((Entity3D)child).getTexture(i))) {
                                return true;
                            }
                        }
                    }
                }
            }
            if (this.recursiveGeometryHitTest(child, x, y, scaling)) {
                return true;
            }
        }
        return false;
    }
    
    private void debug_DrawHitTexture(final Vector4 vector4, final Vector2 u, final Vector2 v, final Vector4 p, final Entity child, final TextureCoords coords) {
        DebugANM.INSTANCE.setVectors(vector4.getX(), vector4.getY(), u, v, p);
        DebugANM.INSTANCE.setTexture(((Entity3D)child).getTexture(0), coords);
        DebugANM.INSTANCE.setTextureHit(-1, -1, false);
        DebugANM.INSTANCE.update();
    }
    
    private void debug_DrawMouseVector(final Vector4 vector4, final Vector2 u, final Vector2 v, final Vector4 p) {
        DebugANM.INSTANCE.setVectors(vector4.getX(), vector4.getY(), u, v, p);
        DebugANM.INSTANCE.update();
    }
    
    private void debug_drawQuads(final int numVertices, final FloatBuffer vb, final float scaling) {
        final Vector4 v0 = new Vector4();
        final Vector4 v = new Vector4();
        final Vector4 v2 = new Vector4();
        final Vector2 u = new Vector2();
        final Vector2 v3 = new Vector2();
        DebugANM.INSTANCE.clear();
        for (int k = 0; k < numVertices; k += 4) {
            final int l = k * 2;
            v0.set(vb.get(l + 0), vb.get(l + 1), 0.0f, 1.0f);
            v.set(vb.get(l + 2), vb.get(l + 3), 0.0f, 1.0f);
            v2.set(vb.get(l + 4), vb.get(l + 5), 0.0f, 1.0f);
            u.set(v2.getX() - v.getX(), v2.getY() - v.getY());
            v3.set(v0.getX() - v.getX(), v0.getY() - v.getY());
            if (scaling != 1.0f) {
                final float s = (scaling - 1.0f) * 0.5f;
                v.set(v.m_x - s * (u.m_x + v3.m_x), v.m_y - s * (u.m_y + v3.m_y), v.m_z, v.m_w);
                u.setMul(scaling);
                v3.setMul(scaling);
            }
            DebugANM.INSTANCE.addQuad(new Vector4[] { new Vector4(v.getX(), v.getY(), 0.0f, 0.0f), new Vector4(v.getX() + u.getX(), v.getY() + u.getY(), 0.0f, 0.0f), new Vector4(v.getX() + u.getX() + v3.getX(), v.getY() + u.getY() + v3.getY(), 0.0f, 0.0f), new Vector4(v.getX() + v3.getX(), v.getY() + v3.getY(), 0.0f, 0.0f) });
        }
    }
    
    private void debug_DrawHitPoint(final AlphaMask alphaMask, final int ix, final int iy) {
        DebugANM.INSTANCE.setTextureHit(ix, iy, alphaMask.getValue(ix, iy));
        DebugANM.INSTANCE.update();
    }
    
    private boolean perfectHitTest(final float x, final float y, final TextureCoords coords, final Texture texture) {
        if (texture == null) {
            return false;
        }
        try {
            final Layer layer = texture.getLayer(0);
            final AlphaMask alphaMask = layer.getAlphaMask();
            final int width = layer.getWidth();
            final int height = layer.getHeight();
            final float dx = MathHelper.lerp(coords.left(), coords.right(), x);
            final float dy = MathHelper.lerp(coords.top(), coords.bottom(), y);
            final int ix = Math.round(dx * width);
            final int iy = height - Math.round(dy * height);
            return alphaMask.getValue(ix, iy);
        }
        catch (Throwable e) {
            AnimatedElement.m_logger.error((Object)"", e);
            return false;
        }
    }
    
    public void dispose() {
        AdviserManager.getInstance().removeTargetedAdvisers(this);
        this.reset();
    }
    
    protected boolean needDrawAgain() {
        return this.m_animationChanged || this.m_animatedChange || this.m_visibleChanged || this.m_colorChanged || this.m_equipmentChanged;
    }
    
    protected void drawnAgain() {
        this.m_animationChanged = false;
        this.m_animatedChange = false;
        this.m_colorChanged = false;
        this.m_equipmentChanged = false;
    }
    
    private void defaultToStaticAnimation() {
        final String animName = this.getStaticAnimationKey();
        this.getAnmInstance().setAnimation(this.getANMKey(animName));
    }
    
    public boolean update(final IsoWorldScene scene, final int deltaTime) {
        return this.update(scene, deltaTime, 0);
    }
    
    private boolean update(final IsoWorldScene scene, final int deltaTime, final int updateDepth) {
        if (updateDepth > 5) {
            AnimatedElement.m_logger.info((Object)("Boucle infinie dans le process de l'anm " + this.m_anmInstance.getAnmFileName() + " animation courante=" + this.m_animation + " (probl\u00ef¿½me dans l'enchainement des goto ?)"));
            return false;
        }
        final boolean onScreen = scene == null || this.isOnScreen(scene);
        final boolean animated = this.m_animated && onScreen;
        if (this.m_needToCleanAnimationEndedListeners) {
            this.m_animationEndedListeners.removeAll(this.m_animationEndedListenersToRemove);
            this.m_animationEndedListenersToRemove.clear();
            this.m_needToCleanAnimationEndedListeners = false;
        }
        if (this.m_anmInstanceLoading != null) {
            return true;
        }
        if (this.m_anmInstance == null) {
            return false;
        }
        final boolean anmInstanceReady = this.m_anmInstance.isReady();
        if (this.m_fadeIfOnScreen) {
            this.m_fadeIfOnScreen = false;
            if (onScreen) {
                if (!anmInstanceReady) {
                    this.m_fadeWhenReady = true;
                }
                else {
                    this.m_fade = 0.0f;
                }
            }
        }
        if (this.m_fadeWhenReady && anmInstanceReady) {
            this.m_fade = 0.0f;
            this.m_fadeWhenReady = false;
        }
        if (this.getAnmEntity() == null) {
            this.createEntity3D();
            this.forceEnableAlphaMask(this.m_alphaMaskEnabled);
            this.m_colorChanged = true;
        }
        assert deltaTime < 1000000 : "DeltaTime is very high (" + deltaTime + "), did you use realTime instead ?";
        if (this.m_gfxId == null) {
            return false;
        }
        this.m_elapsedTime += (int)(deltaTime * this.m_animationSpeed);
        assert this.m_anmInstance != null;
        if (!anmInstanceReady) {
            return false;
        }
        this.m_anmInstance.setUpToDate();
        if (this.m_fade < this.m_desiredAlpha) {
            this.m_fade += deltaTime / 500.0f;
            if (this.m_fade > this.m_desiredAlpha) {
                this.m_fade = this.m_desiredAlpha;
            }
            if (this.m_color[3] != this.m_fade) {
                this.m_color[3] = this.m_fade;
                this.m_colorChanged = true;
            }
        }
        boolean updateAnimation = false;
        this.m_equipmentChanged |= this.m_anmInstance.hasPartsToSet();
        if (this.needDrawAgain() && animated) {
            updateAnimation = true;
            this.m_visibleChanged = false;
            final SpriteDefinition lastAnim = this.m_anmInstance.getAnimation();
            this.m_anmInstance.setAnimation(this.getANMKey(this.m_animation));
            final boolean animRequested = this.m_anmInstance.isAnimationRequested();
            if (this.m_anmInstance.getAnimation() == null) {
                if (animRequested && lastAnim != null) {
                    this.m_anmInstance.setAnimation(lastAnim.getName());
                }
                else {
                    this.m_anmInstance.setAnimation(this.getANMKey("AnimStatique"));
                }
            }
            if (this.m_anmInstance.getAnimation() != null || animRequested) {
                if (this.m_animationChanged && this.m_anmInstance.isJustLoaded()) {
                    this.m_elapsedTime = 0;
                    if (this.m_soundRefs.size() > 0) {
                        this.stopAllSounds();
                    }
                }
                this.m_lastTime = this.m_elapsedTime;
                this.m_animationChanged = false;
                this.m_lastFrame = -1;
                this.m_lastDisplayedFrame = -1;
            }
            else {
                this.defaultToStaticAnimation();
                if (this.m_anmInstance.getAnimation() != lastAnim) {
                    this.m_elapsedTime = 0;
                    this.m_lastFrame = -1;
                    this.m_lastDisplayedFrame = -1;
                    this.m_lastTime = 0;
                }
            }
            this.drawnAgain();
            if (animRequested) {
                return this.m_animationChanged = true;
            }
        }
        final SpriteDefinition currentAnimation = this.m_anmInstance.getAnimation();
        if (currentAnimation != null && currentAnimation.isAnimationNode()) {
            this.updateActions();
            this.update(scene, deltaTime, updateDepth + 1);
            return true;
        }
        if (this.m_anmInstance.updateAnimation()) {
            updateAnimation = true;
        }
        if (this.m_anmInstance.updateEquipment()) {
            updateAnimation = true;
        }
        if (this.m_anmInstance.getAnimation() == null) {
            return false;
        }
        final int frame = this.m_anmInstance.getFrame(this.m_elapsedTime);
        if (frame != this.m_lastDisplayedFrame || frame != this.m_lastFrame) {
            updateAnimation = true;
        }
        boolean forceUpdate = false;
        if (this.m_anmInstance.hasMultiFrameEquipment()) {
            forceUpdate = true;
        }
        if ((!this.m_stopped || this.m_anmInstance.needUpdate()) && (forceUpdate || updateAnimation || this.m_anmInstance.needUpdate())) {
            final boolean needDisplay = this.m_lastDisplayedFrame != frame || forceUpdate;
            final boolean visible = scene == null || (this.isVisible() && onScreen);
            if (animated && needDisplay && visible) {
                this.m_anmInstance.setMaterial(this.m_material);
                this.m_anmInstance.setBlendFunc(this.m_srcBlend, this.m_destBlend);
                this.m_anmInstance.updateFrame(this.m_elapsedTime, this.getAnmEntity(), deltaTime);
                this.m_lastDisplayedFrame = frame;
            }
            if (this.m_lastFrame != frame) {
                this.m_lastFrame = frame;
                this.updateActions();
                if (frame == this.m_anmInstance.getNumFrames() - 1) {
                    this.fireAnimationEnded();
                    if (this.m_anmInstance == null) {
                        return false;
                    }
                }
            }
            this.m_lastTime = this.m_elapsedTime;
        }
        this.m_anmInstance.recomputeAvgBoundingRect(deltaTime);
        return true;
    }
    
    protected boolean needUpdateAnimation() {
        return this.m_animated || this.m_animationChanged;
    }
    
    private void updateActions() {
        this.m_anmInstance.getActions(AnimatedElement.m_actions, this.m_elapsedTime, this.m_lastTime);
        this.onAnimatedObjectActionFlag(AnimatedElement.m_actions);
        AnimatedElement.m_actions.clear();
    }
    
    public Vector4 getPosition(final IsoWorldScene scene) {
        if (this.m_screenPositionNeedsRecompute) {
            final Point2 pt = IsoCameraFunc.getScreenPosition(scene, this);
            this.m_screenPosition.set(pt.m_x, pt.m_y, -1.0f);
            this.m_screenPositionNeedsRecompute = false;
        }
        return this.m_screenPosition;
    }
    
    public void setVisible(final boolean visible) {
        if (visible != this.m_visible) {
            this.setAnimated(visible);
            this.m_visible = visible;
            this.m_visibleChanged = true;
            this.fireVisibilityChanged(visible, VisibleChangedListener.VisibleChangedCause.VISIBLE);
            this.setWatchersVisible(visible);
        }
        if (this.m_visibleChanged && visible) {
            this.m_screenPositionNeedsRecompute = true;
        }
    }
    
    private void setAnimated(final boolean animated) {
        if (this.m_animated == animated) {
            return;
        }
        this.m_animated = animated;
        this.m_animatedChange = true;
    }
    
    public boolean isStopped() {
        return this.m_stopped;
    }
    
    public void setStopped(final boolean stopped) {
        this.m_stopped = stopped;
        this.needUpdateAnimation();
    }
    
    public void onPositionChanged() {
        this.m_screenPositionNeedsRecompute = true;
    }
    
    public boolean isOnScreen(final IsoWorldScene scene) {
        final Vector4 position = this.getPosition(scene);
        final IsoCamera isoCamera = scene.getIsoCamera();
        final int centerX = (int)position.getX();
        final int centerY = (int)position.getY();
        AnimatedElement.m_testRect.set(centerX + AnimatedElement.m_maxBoundingRect.getXMin(), centerX + AnimatedElement.m_maxBoundingRect.getXMax(), centerY + AnimatedElement.m_maxBoundingRect.getYMin(), centerY + AnimatedElement.m_maxBoundingRect.getYMax());
        return isoCamera.isVisibleInScreen(AnimatedElement.m_testRect.m_yMax, AnimatedElement.m_testRect.m_xMin, AnimatedElement.m_testRect.m_yMin, AnimatedElement.m_testRect.m_xMax);
    }
    
    public boolean addToScene(final AleaWorldScene scene) {
        if (!this.isVisible() || !this.isOnScreen(scene) || this.m_anmInstance == null) {
            return false;
        }
        if (this.m_anmInstance.getAnimation() == null) {
            this.m_ANMScreenRect.set(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
            return false;
        }
        final Vector4 position = this.getPosition(scene);
        final IsoCamera isoCamera = scene.getIsoCamera();
        final Rect rect = this.m_anmInstance.getBoundingRect();
        final int posx = MathHelper.fastRound(position.getX());
        final int posy = MathHelper.fastRound(position.getY());
        this.m_entity.m_minX = posx + rect.m_xMin;
        this.m_entity.m_maxX = posx + rect.m_xMax;
        this.m_entity.m_minY = posy + rect.m_yMin;
        this.m_entity.m_maxY = posy + rect.m_yMax;
        final int centerX = posx - isoCamera.getScreenX();
        final int centerY = posy - isoCamera.getScreenY();
        this.m_ANMScreenRect.set(centerX + rect.m_xMin, centerX + rect.m_xMax, centerY + rect.m_yMin, centerY + rect.m_yMax);
        if (!isoCamera.isVisibleInScreen(this.m_entity.m_maxY, this.m_entity.m_minX, this.m_entity.m_minY, this.m_entity.m_maxX)) {
            return false;
        }
        final float x = this.getWorldX();
        final float y = this.getWorldY();
        final float z = this.getAltitude();
        final float xInc = x;
        final float yInc = y;
        final int xi = MathHelper.fastRound(xInc);
        final int yi = MathHelper.fastRound(yInc);
        final int zi = MathHelper.fastRound(z);
        if (MaskableHelper.isUndefined(this)) {
            scene.initialize(this, this.m_entity, xi, yi, zi, this.m_deltaZ, false);
        }
        final float[] layerVisibility = isoCamera.getLayerColor(this);
        final boolean layerVisible = layerVisibility[3] != 0.0f;
        if (this.m_lastLayerVisibility != layerVisible) {
            this.fireVisibilityChanged(layerVisible, VisibleChangedListener.VisibleChangedCause.LAYER);
            this.m_lastLayerVisibility = layerVisible;
        }
        if (!layerVisible) {
            return false;
        }
        System.arraycopy(layerVisibility, 0, AnimatedElement.m_tempColor, 0, layerVisibility.length);
        final byte hideState = HideElementHelper.computeColor(this.m_hideState, AnimatedElement.m_tempColor);
        if (hideState != this.m_hideState) {
            this.m_hideState = hideState;
        }
        this.setGroupLayerColor(AnimatedElement.m_tempColor);
        this.getAnmEntity().updateMaterial(this.m_material);
        this.m_entity.getTransformer().setScale(0, this.getScale(), this.getScale());
        this.m_entity.getTransformer().setTranslation(0, position.getX(), position.getY());
        this.m_entity.m_renderRadius = this.m_renderRadius;
        if (AnimatedElement.m_enableRunningRadius && this.m_renderRadius >= 1.0f && this.m_animated && (x != xi || yi != y)) {
            this.m_entity.m_renderRadius = this.m_renderRadius + 1.0f;
        }
        this.m_entity.m_cellX = xInc;
        this.m_entity.m_cellY = yInc;
        this.m_entity.m_cellZ = z;
        this.m_entity.m_height = this.getVisualHeight();
        if (!scene.accept(this)) {
            return false;
        }
        scene.addEntity(this.m_entity, this.m_renderRadius > 0.0f);
        return true;
    }
    
    protected float getIncrementX() {
        return 0.0f;
    }
    
    protected float getIncrementY() {
        return 0.0f;
    }
    
    public void process(final AleaWorldScene scene, final int deltaTime) {
        this.m_visibleChanged = false;
        this.m_screenPositionNeedsRecompute = true;
    }
    
    public long getZOrder() {
        return this.m_entity.m_zOrder;
    }
    
    public boolean playAction(final AnmAction action) {
        return false;
    }
    
    @Override
    public String getStaticAnimationKey() {
        return this.m_staticAnimationKey;
    }
    
    public void setStaticAnimationKey(final String animationStatic) {
        assert animationStatic != null;
        this.m_staticAnimationKey = animationStatic;
    }
    
    public void onAnimatedObjectActionFlag(final ArrayList<AnmAction> actions) {
        boolean stop = false;
        int previousFrame = -1;
        for (int i = 0, size = actions.size(); i < size; ++i) {
            final int currentFrame = actions.get(i).getFrameIndex();
            if (stop && previousFrame != currentFrame) {
                break;
            }
            stop |= actions.get(i).run(this);
            previousFrame = currentFrame;
        }
    }
    
    public final void colorize(final Material color) {
        if (this.m_material == null) {
            return;
        }
        this.m_material.copy(color);
    }
    
    public void resetColor() {
        if (this.m_material == null) {
            return;
        }
        this.m_material.setSpecularColor(0.0f, 0.0f, 0.0f);
        final ArrayList<Entity> childList = this.m_entity.getChildList();
        for (int numChildren = childList.size(), i = 0; i < numChildren; ++i) {
            childList.get(i).removeEffectForWorld();
        }
    }
    
    public void addSoundRef(final long soundId, final long soundUID) {
        this.m_soundRefs.put(soundId, soundUID);
    }
    
    protected void stopAllSounds() {
        for (int i = this.m_soundRefs.size() - 1; i >= 0; --i) {
            final AudioSource source = AudioSourceManager.getInstance().getAudioSource(this.m_soundRefs.getQuickValue(i));
            if (source != null) {
                source.setStopOnNullGain(true);
                source.fade(0.0f, 0.7f);
            }
        }
        this.m_soundRefs.clear();
    }
    
    public short getHeight() {
        return this.m_visualHeight;
    }
    
    public short getVisualHeight() {
        final Anm anm = this.getAnm();
        if (anm == null) {
            return this.getHeight();
        }
        final String animationSuffix = this.getAnimationSuffix();
        final String animName = (animationSuffix == null) ? this.m_animation : (this.m_animation + animationSuffix);
        final int height = anm.getAnimationHeight(animName);
        if (height == -1) {
            return this.getHeight();
        }
        return (short)height;
    }
    
    public void setVisualHeight(final short visualHeight) {
        this.m_visualHeight = visualHeight;
    }
    
    public void setSelected(final boolean selected) {
    }
    
    public String getHitAnimationKey() {
        return this.m_hitAnimationKey;
    }
    
    public void setHitAnimationKey(final String hitAnimationKey) {
        this.m_hitAnimationKey = hitAnimationKey;
    }
    
    public void setGroupLayerColor(final float[] groupLayerColor) {
        assert groupLayerColor.length == 4;
        final float alpha = this.m_color[3];
        final float r = alpha * this.m_color[0] * groupLayerColor[0];
        final float g = alpha * this.m_color[1] * groupLayerColor[1];
        final float b = alpha * this.m_color[2] * groupLayerColor[2];
        final float a = alpha * groupLayerColor[3];
        this.m_material.setDiffuseColor(r, g, b, a);
    }
    
    @Override
    public void setRenderRadius(final float renderRadius) {
        this.m_renderRadius = renderRadius;
    }
    
    public float getRenderRadius() {
        return this.m_renderRadius;
    }
    
    @Override
    public float getEntityRenderRadius() {
        return this.m_entity.m_renderRadius;
    }
    
    public boolean addAnimationEndedListener(final AnimationEndedListener listener) {
        return !this.m_animationEndedListeners.contains(listener) && this.m_animationEndedListeners.add(listener);
    }
    
    public void clearAnimationEndedListener() {
        this.m_animationEndedListeners.clear();
    }
    
    public void removeAnimationEndedListener(final AnimationEndedListener listener) {
        this.m_animationEndedListenersToRemove.add(listener);
        this.m_needToCleanAnimationEndedListeners = true;
    }
    
    public void fireAndRemoveRemainingAnimationEndedListener() {
        this.fireAnimationEnded();
        this.m_animationEndedListenersToRemove.addAll(this.m_animationEndedListeners);
        this.m_needToCleanAnimationEndedListeners = true;
    }
    
    @Nullable
    public Anm getAnm() {
        final AnmInstance anmInstance = this.getAnmInstance();
        return (anmInstance == null) ? null : anmInstance.getSource();
    }
    
    public AnmInstance getAnmInstance() {
        return (this.m_anmInstanceLoading == null) ? this.m_anmInstance : this.m_anmInstanceLoading;
    }
    
    public int getAnimationDuration(final String animName) {
        if (this.getAnmInstance() == null || animName == null) {
            return 0;
        }
        return this.getAnmInstance().getAnimationDuration(animName);
    }
    
    public final void enableAlphaMask(final boolean alphaMask) {
        if (this.m_alphaMaskEnabled == alphaMask) {
            return;
        }
        this.forceEnableAlphaMask(alphaMask);
    }
    
    public final void forceEnableAlphaMask(final boolean alphaMask) {
        this.m_alphaMaskEnabled = alphaMask;
        if (this.getAnmEntity() == null) {
            return;
        }
        if (this.m_alphaMaskEnabled) {
            final EntityGroup entity = this.m_entity;
            entity.m_userFlag1 |= 0x1;
        }
        else {
            final EntityGroup entity2 = this.m_entity;
            entity2.m_userFlag1 &= 0xFFFFFFFE;
        }
        this.m_renderer.enableOcclusion(this.m_alphaMaskEnabled);
    }
    
    public final boolean isAlphaMaskEnabled() {
        return this.m_alphaMaskEnabled;
    }
    
    public final void canBeOccluder(final boolean canBeOccluder) {
        this.m_occluder = canBeOccluder;
        if (this.m_entity != null) {
            if (this.m_occluder) {
                final EntityGroup entity = this.m_entity;
                entity.m_userFlag1 |= 0x2;
            }
            else {
                final EntityGroup entity2 = this.m_entity;
                entity2.m_userFlag1 &= 0xFFFFFFFD;
            }
        }
    }
    
    private void fireAnimationEnded() {
        for (int i = 0, size = this.m_animationEndedListeners.size(); i < size; ++i) {
            this.m_animationEndedListeners.get(i).animationEnded(this);
        }
    }
    
    public void setColorChanged(final boolean colorChanged) {
        this.m_colorChanged = colorChanged;
    }
    
    @Override
    public void applyLighting(final float[] colors) {
        if (colors[0] == 1.0f && colors[1] == 1.0f && colors[2] == 1.0f) {
            return;
        }
        this.m_material.multDiffuse(colors[0], colors[1], colors[2]);
    }
    
    public void setLinkageVisible(final String[] linkageNames, final boolean visible) {
        for (final String linkageName : linkageNames) {
            this.setLinkageVisible(linkageName, visible);
        }
    }
    
    public void setLinkageVisible(final String linkageName, final boolean visible) {
        final int crc = Engine.getPartName(linkageName);
        if (visible) {
            this.getAnmInstance().unhideSprite(crc);
        }
        else {
            this.getAnmInstance().hideSprite(crc);
        }
    }
    
    protected void setId(final long id) {
        this.m_id = id;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    @Override
    public float getAltitude() {
        return this.m_altitude;
    }
    
    @Override
    public float getWorldX() {
        return this.m_worldX;
    }
    
    @Override
    public float getWorldY() {
        return this.m_worldY;
    }
    
    @Override
    public int getWorldCellX() {
        return MathHelper.fastRound(this.m_worldX);
    }
    
    @Override
    public int getWorldCellY() {
        return MathHelper.fastRound(this.m_worldY);
    }
    
    @Override
    public short getWorldCellAltitude() {
        return (short)MathHelper.fastRound(this.m_altitude);
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY) {
        this.m_worldX = worldX;
        this.m_worldY = worldY;
        this.onPositionChanged();
        MaskableHelper.setUndefined(this);
    }
    
    @Override
    public void setWorldPosition(final float worldX, final float worldY, final float altitude) {
        this.m_worldX = worldX;
        this.m_worldY = worldY;
        if (altitude == -32768.0f) {
            AnimatedInteractiveElement.m_logger.error((Object)"", (Throwable)new Exception("on vient de setter une altitude anormale"));
        }
        this.m_altitude = altitude;
        this.onPositionChanged();
        MaskableHelper.setUndefined(this);
    }
    
    public Point3 getWorldCoordinates() {
        this.m_coordinates.set(this.getWorldCellX(), this.getWorldCellY(), (short)this.m_altitude);
        return this.m_coordinates;
    }
    
    @Override
    public final int getMaskKey() {
        return this.m_maskKey;
    }
    
    @Override
    public final short getLayerId() {
        return this.m_layerId;
    }
    
    @Override
    public void setMaskKey(final int key, final short layerId) {
        this.m_maskKey = key;
        this.m_layerId = layerId;
    }
    
    @Override
    public int getScreenX() {
        return this.m_screenX;
    }
    
    @Override
    public int getScreenY() {
        return this.m_screenY;
    }
    
    @Override
    public void setScreenX(final int x) {
        this.m_screenX = x;
    }
    
    @Override
    public void setScreenY(final int y) {
        this.m_screenY = y;
    }
    
    @Override
    public void setScreenTargetHeight(final int height) {
        this.m_screenHeight = height;
    }
    
    @Override
    public int getScreenTargetHeight() {
        return this.m_screenHeight;
    }
    
    public float getScale() {
        return this.m_scale;
    }
    
    @Override
    public boolean isVisible() {
        return this.m_visible;
    }
    
    public float getAlpha() {
        return this.m_color[3];
    }
    
    public void setAlpha(final float alpha) {
        this.m_color[3] = alpha;
    }
    
    public void setDesiredAlpha(final float desiredAlpha) {
        this.m_color[3] = (this.m_desiredAlpha = desiredAlpha);
    }
    
    public void resetAlpha() {
        this.m_color[3] = this.m_desiredAlpha;
    }
    
    public float getOriginalAlpha() {
        return this.m_originalAlpha;
    }
    
    public boolean hasWatchers() {
        return this.m_watchers != null;
    }
    
    @Override
    public void addWatcher(final ScreenTargetWatcher watcher) {
        if (this.m_watchers == null) {
            this.m_watchers = new ArrayList<ScreenTargetWatcher>();
        }
        this.m_watchers.add(watcher);
        watcher.setTargetIsVisible(this.m_watchersVisible);
    }
    
    @Override
    public void removeWatcher(final ScreenTargetWatcher watcher) {
        if (this.m_watchers == null) {
            return;
        }
        this.m_watchers.remove(watcher);
        if (this.m_watchers.size() == 0) {
            this.m_watchers = null;
            this.m_screenX = Integer.MIN_VALUE;
            this.m_screenY = Integer.MIN_VALUE;
        }
    }
    
    public void fireScreenPositionChanged() {
        if (this.m_watchers != null) {
            for (int i = 0; i < this.m_watchers.size(); ++i) {
                this.m_watchers.get(i).screenTargetMoved(this, this.m_screenX, this.m_screenY, this.m_screenHeight);
            }
        }
    }
    
    public void setWatchersVisible(final boolean visible) {
        if (visible == this.m_watchersVisible) {
            return;
        }
        this.m_watchersVisible = visible;
        this.updateVisibility();
    }
    
    public void setWatchersClipped(final boolean clipped) {
        if (clipped == this.m_watchersClipped) {
            return;
        }
        this.m_watchersClipped = clipped;
        this.updateVisibility();
    }
    
    private void updateVisibility() {
        if (this.m_watchers == null) {
            return;
        }
        for (int i = 0; i < this.m_watchers.size(); ++i) {
            this.m_watchers.get(i).setTargetIsVisible(!this.m_watchersClipped && this.m_watchersVisible);
        }
    }
    
    public void fireVisibilityChanged(final boolean visible, final VisibleChangedListener.VisibleChangedCause cause) {
        if (this.m_visibiltyListeners != null) {
            for (int i = 0; i < this.m_visibiltyListeners.size(); ++i) {
                this.m_visibiltyListeners.get(i).onVisibleChanged(visible, cause);
            }
        }
    }
    
    public void addVisibleChangedListener(final VisibleChangedListener listener) {
        if (this.m_visibiltyListeners == null) {
            this.m_visibiltyListeners = new ArrayList<VisibleChangedListener>();
        }
        this.m_visibiltyListeners.add(listener);
    }
    
    public void removeVisibleChangedListener(final VisibleChangedListener listener) {
        if (this.m_visibiltyListeners != null) {
            this.m_visibiltyListeners.remove(listener);
        }
    }
    
    public DefaultAnmActionFactory getActionFactory() {
        return DefaultAnmActionFactory.INSTANCE;
    }
    
    @Override
    public int getCurrentFightId() {
        return -1;
    }
    
    @Override
    public final void hide(final boolean hide) {
        this.m_hideState = (byte)(hide ? 0 : 2);
    }
    
    public void setIncrementZOrder(final boolean incrementZOrder) {
        this.m_incrementZOrder = incrementZOrder;
        this.m_incrementZOrder = false;
    }
    
    public static void setEnableRunningRadius(final boolean enableRunningRadius) {
        AnimatedElement.m_enableRunningRadius = enableRunningRadius;
    }
    
    @Override
    public void onParticleAttached(final FreeParticleSystem system) {
        if (this.m_attachedParticles == null) {
            this.m_attachedParticles = new ArrayList<FreeParticleSystem>();
        }
        this.m_attachedParticles.add(system);
    }
    
    @Override
    public void onParticleDetached(final FreeParticleSystem system) {
        if (this.m_attachedParticles != null) {
            this.m_attachedParticles.remove(system);
        }
    }
    
    @Override
    public void onLightAttached(final IsoLightSource lightSource) {
        if (this.m_attachedLight == null) {
            this.m_attachedLight = new ArrayList<IsoLightSource>();
        }
        this.m_attachedLight.add(lightSource);
    }
    
    @Override
    public void onLightDetached(final IsoLightSource lightSource) {
        if (this.m_attachedLight != null) {
            this.m_attachedLight.remove(lightSource);
        }
    }
    
    @Override
    public void onDestroy() {
        if (this.m_attachedParticles != null) {
            for (int i = 0; i < this.m_attachedParticles.size(); ++i) {
                this.m_attachedParticles.get(i).kill();
            }
        }
        if (this.m_attachedLight != null) {
            for (int i = 0; i < this.m_attachedLight.size(); ++i) {
                this.m_attachedLight.get(i).shutdown(100);
            }
        }
    }
    
    public float[] getCustomColor(final int index) {
        if (this.getAnmInstance() == null) {
            return null;
        }
        return this.getAnmInstance().getCustomColor(index);
    }
    
    public void setCustomColor(final int index, final float[] color) {
        assert color.length == 4;
        final AnmInstance anmInstance = this.getAnmInstance();
        if (anmInstance == null) {
            return;
        }
        if (color == null) {
            anmInstance.removeCustomColor(index);
        }
        anmInstance.addCustomColor(index, color);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnimatedElement.class);
        AnimatedElement.m_enableRunningRadius = true;
        m_maxBoundingRect = new Rect(-256, 256, -256, 256);
        m_actions = new ArrayList<AnmAction>(5);
        AnimatedElement.DISPLAY_EXTENDED_OVERHEAD_INFOS = false;
        m_tempColor = new float[4];
        m_testRect = new Rect();
    }
}
