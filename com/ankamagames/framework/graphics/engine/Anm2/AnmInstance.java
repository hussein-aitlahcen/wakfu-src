package com.ankamagames.framework.graphics.engine.Anm2;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.material.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.Anm2.Index.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;
import java.io.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.geometry.*;

public final class AnmInstance
{
    private static final Logger m_logger;
    private static final Logger m_anmLogger;
    private static final BlendModes DEFAULT_SRC_BLEND;
    private static final Material m_defaultMaterial;
    private static final float[] m_positionBuffer;
    private static final float[] m_colorBuffer;
    private static final float[] m_texCoordsBuffer;
    private static int m_numQuads;
    private static int m_positionIndex;
    private static int m_colorIndex;
    private static int m_texCoordsIndex;
    private static long m_textureCRC;
    private static Texture m_texture;
    private static final IndexBuffer m_indexBuffer;
    private static final AnmTransform[] m_transforms;
    private static final int LINK_CLIP = 1003439990;
    private static final int CARRIER_CLIP = 1272524161;
    static final int MAX_VERTICES = 2048;
    String m_requestedAnimName;
    private final Anm m_definitions;
    private final String m_baseFileName;
    private final String m_path;
    @NotNull
    private PartsHelper m_partsHelper;
    private PartsHelper m_subPartsHelper;
    @NotNull
    private HidePartsHelper m_hiddenSprites;
    private float[][] m_customColors;
    private String m_animName;
    private SpriteDefinition m_animation;
    private Anm m_animationSource;
    private int m_maxSpriteCount;
    private boolean m_flipAnimation;
    private Material m_material;
    private BlendModes m_srcBlend;
    private BlendModes m_destBlend;
    private final Rect m_boundingRect;
    private byte m_recomputeAvgBoundingBox;
    private final Rect m_avgBoundingRect;
    float m_minX;
    float m_maxX;
    float m_minY;
    float m_maxY;
    private final Matrix44 m_carrierMatrix;
    private final Matrix44 m_linkMatrix;
    private int m_frame;
    private boolean m_needUpdate;
    private boolean m_justLoaded;
    private final AnmTransform m_root;
    private byte m_quadsCount;
    private byte m_batchCount;
    private GLGeometryMesh m_geometry;
    
    public AnmInstance(final Anm definitions, final String baseName, final String path) {
        super();
        this.m_partsHelper = PartsHelper.NULL;
        this.m_subPartsHelper = null;
        this.m_hiddenSprites = HidePartsHelper.NULL;
        this.m_maxSpriteCount = -1;
        this.m_boundingRect = new Rect();
        this.m_recomputeAvgBoundingBox = -1;
        this.m_avgBoundingRect = new Rect();
        this.m_needUpdate = false;
        this.m_justLoaded = false;
        this.m_root = new AnmTransform();
        assert definitions != null;
        (this.m_definitions = definitions).addReference();
        this.setScale(definitions.m_index.getScale());
        this.m_baseFileName = baseName;
        this.m_path = path;
        this.m_animationSource = null;
        this.m_animation = null;
        this.m_partsHelper = PartsHelper.NULL;
        this.m_hiddenSprites = HidePartsHelper.NULL;
        this.m_srcBlend = AnmInstance.DEFAULT_SRC_BLEND;
        this.m_destBlend = BlendModes.InvSrcAlpha;
        this.m_material = null;
        this.setScale(1.0f);
        this.m_boundingRect.set(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
        this.m_avgBoundingRect.set(0, 0, 0, 0);
        (this.m_carrierMatrix = Matrix44.Factory.newPooledInstance()).setIdentity();
        (this.m_linkMatrix = Matrix44.Factory.newPooledInstance()).setIdentity();
        this.m_flipAnimation = false;
        this.m_needUpdate = false;
    }
    
    public AnmInstance(final AnmInstance source) {
        this(source.m_definitions, source.m_baseFileName, source.m_path);
        assert this.m_baseFileName.equals(source.m_baseFileName);
        assert this.m_path.equals(source.m_path);
        if (source.m_animName != null) {
            this.setAnimation(source.m_animName);
        }
        this.m_partsHelper = source.m_partsHelper.duplicate();
        this.m_hiddenSprites = source.m_hiddenSprites.duplicate();
        this.copyCustomColor(source);
        this.m_srcBlend = source.m_srcBlend;
        this.m_destBlend = source.m_destBlend;
        this.m_material = source.m_material;
        if (this.m_material != null) {
            this.m_material.addReference();
        }
        this.m_boundingRect.set(source.m_boundingRect.getXMin(), source.m_boundingRect.getXMax(), source.m_boundingRect.getYMin(), source.m_boundingRect.getYMax());
        this.m_carrierMatrix.set(source.m_carrierMatrix);
        this.m_linkMatrix.set(source.m_linkMatrix);
        this.m_needUpdate = source.m_needUpdate;
        this.m_frame = source.m_frame;
        this.setScale(source.getScale());
    }
    
    public Anm getSource() {
        return this.m_definitions;
    }
    
    public BlendModes getSrcBlend() {
        return this.m_srcBlend;
    }
    
    public BlendModes getDstBlend() {
        return this.m_destBlend;
    }
    
    public static Texture getTexture() {
        return AnmInstance.m_texture;
    }
    
    public void reset() {
        this.m_definitions.removeReference();
        this.m_partsHelper.clear();
        this.m_partsHelper = PartsHelper.NULL;
        this.m_hiddenSprites.clear();
        if (this.m_subPartsHelper != null) {
            this.m_subPartsHelper.clear();
            this.m_subPartsHelper = null;
        }
        this.m_animName = null;
        if (this.m_animationSource != null && this.m_animationSource != this.m_definitions) {
            this.m_animationSource.removeReference();
            this.m_animationSource = null;
            this.m_maxSpriteCount = -1;
        }
        this.m_animation = null;
        this.m_srcBlend = AnmInstance.DEFAULT_SRC_BLEND;
        this.m_destBlend = BlendModes.InvSrcAlpha;
        if (this.m_material != null) {
            this.m_material.removeReference();
            this.m_material = null;
        }
        this.m_customColors = null;
        this.m_flipAnimation = false;
        this.m_frame = 0;
        this.m_needUpdate = false;
        this.m_carrierMatrix.removeReference();
        this.m_linkMatrix.removeReference();
        this.setScale(1.0f);
        if (this.m_geometry != null) {
            this.m_geometry.removeReference();
            this.m_geometry = null;
        }
    }
    
    public String getPath() {
        return this.m_path;
    }
    
    public void applyParts(@NotNull final Anm equipment, final int... partsCrc) {
        if (this.m_partsHelper == PartsHelper.NULL) {
            this.m_partsHelper = new PartsHelper();
        }
        this.m_partsHelper.setPartsFrom(equipment, convert(partsCrc));
        if (equipment.m_maxSpriteCount > 1) {
            this.m_maxSpriteCount = -1;
        }
        this.updateHiddenParts();
    }
    
    public void unapplyParts(@NotNull final Anm equipement, final int... partsCrc) {
        if (this.m_partsHelper == PartsHelper.NULL) {
            return;
        }
        final TIntHashSet crcs = convert(partsCrc);
        this.m_needUpdate |= this.m_partsHelper.removePartsFrom(equipement, crcs);
        this.updateHiddenParts();
    }
    
    private static TIntHashSet convert(final int[] partsCrc) {
        if (partsCrc == null || partsCrc.length == 0) {
            return null;
        }
        return new TIntHashSet(partsCrc);
    }
    
    public void updateHiddenParts() {
        this.m_hiddenSprites.clearEquipement();
        final HiddingPart[] partsHiddenByItem = this.m_definitions.m_index.getPartsHiddenByItem();
        if (partsHiddenByItem == null) {
            return;
        }
        final ArrayList<CanHidePart> hideParts = this.m_partsHelper.getCanHideParts();
        final int size = hideParts.size();
        if (size == 0) {
            return;
        }
        final int numHiddenParts = partsHiddenByItem.length;
        for (int i = 0; i < size; ++i) {
            final CanHidePart hidePart = hideParts.get(i);
            for (final HiddingPart hiddingPart : partsHiddenByItem) {
                if (hidePart.crcKey == hiddingPart.crcKey) {
                    if (this.m_hiddenSprites == HidePartsHelper.NULL) {
                        this.m_hiddenSprites = new HidePartsHelper();
                    }
                    this.m_hiddenSprites.hideEquipment(hiddingPart.crcToHide);
                }
            }
        }
    }
    
    public void hideSprite(final int baseNameCRC) {
        if (this.m_hiddenSprites == HidePartsHelper.NULL) {
            this.m_hiddenSprites = new HidePartsHelper();
        }
        this.m_hiddenSprites.hide(baseNameCRC);
    }
    
    public void unhideSprite(final int baseNameCRC) {
        assert this.m_hiddenSprites != HidePartsHelper.NULL;
        this.m_hiddenSprites.unhide(baseNameCRC);
    }
    
    public boolean updateAnimation() {
        boolean updateFrame = false;
        if (this.m_requestedAnimName != null) {
            this.setAnimation(this.m_requestedAnimName);
            if (this.m_requestedAnimName == null) {
                updateFrame = true;
                this.m_needUpdate = true;
            }
        }
        return updateFrame;
    }
    
    public void setAnimation(final String animationName) {
        final AnmIndex anmIndex = this.m_definitions.m_index;
        final String animName = this.getFlippedAnimName(animationName);
        if (animName != animationName) {
            this.m_flipAnimation = true;
            this.m_root.setRotationSkew(-1.0f, 0.0f, 0.0f, 1.0f);
        }
        else {
            this.m_flipAnimation = false;
            this.m_root.setRotationToId();
        }
        this.m_animName = animName;
        this.m_animation = null;
        this.m_requestedAnimName = null;
        this.m_justLoaded = true;
        this.m_needUpdate = true;
        final AnmAnimationFileRecord fileRecord = anmIndex.getAnimationFileRecord(animName);
        if (fileRecord == null) {
            AnmInstance.m_anmLogger.error((Object)("Animation " + animName + " not found (" + this.m_path + this.m_baseFileName + ")!"));
            return;
        }
        assert fileRecord.m_name.equals(animName);
        if (fileRecord.m_fileIndex == -1) {
            assert this.m_definitions.isReady();
            this.setAnimation(this.m_definitions, this.m_definitions.getSpriteDefinitionByCRC(fileRecord.m_crc));
        }
        else {
            final String anmFile = anmIndex.getFileName(fileRecord.m_fileIndex);
            AnmManager.getInstance().setAnimation(anmFile, fileRecord, this);
        }
        this.m_recomputeAvgBoundingBox = -1;
    }
    
    public SpriteDefinition getAnimation() {
        return this.m_animation;
    }
    
    public boolean isAnimationRequested() {
        return this.m_requestedAnimName != null;
    }
    
    public boolean isJustLoaded() {
        return this.m_justLoaded;
    }
    
    public void getAnimationList(final ArrayList<String> animations) {
        final AnmAnimationFileRecord[] animFilerecords = this.m_definitions.m_index.getAnimationFileRecords();
        for (int i = 0; i < animFilerecords.length; ++i) {
            animations.add(animFilerecords[i].m_name);
        }
    }
    
    public void setBlendFunc(final BlendModes srcBlend, final BlendModes destBlend) {
        this.m_srcBlend = srcBlend;
        this.m_destBlend = destBlend;
    }
    
    public boolean updateEquipment() {
        boolean updateFrame = this.m_partsHelper.update();
        if (this.m_subPartsHelper != null) {
            updateFrame |= this.m_subPartsHelper.update();
        }
        if (updateFrame) {
            this.updateHiddenParts();
        }
        return updateFrame;
    }
    
    public boolean hasMultiFrameEquipment() {
        return this.m_partsHelper.hasMultiFramePart();
    }
    
    public void updateFrame(final int time, final Entity3D entity, final int deltaTime) {
        entity.clear();
        if (this.m_geometry != null && this.m_maxSpriteCount == -1) {
            this.m_geometry.removeReference();
            this.m_geometry = null;
        }
        if (this.m_animation == null) {
            AnmInstance.m_logger.error((Object)"On ne peut pas mettre a jour une animation nulle");
            return;
        }
        this.setScale(this.m_definitions.m_index.getScale());
        final int frameIndex = this.getFrameIndex(time);
        this.m_frame = frameIndex;
        this.m_needUpdate = true;
        this.m_justLoaded = false;
        this.m_quadsCount = 0;
        this.m_batchCount = 0;
        AnmInstance.m_numQuads = 0;
        AnmInstance.m_textureCRC = 0L;
        AnmInstance.m_positionIndex = 0;
        AnmInstance.m_colorIndex = 0;
        AnmInstance.m_texCoordsIndex = 0;
        AnmInstance.m_texture = null;
        this.m_minX = Float.MAX_VALUE;
        this.m_maxX = -3.4028235E38f;
        this.m_minY = Float.MAX_VALUE;
        this.m_maxY = -3.4028235E38f;
        this.m_root.m_customColorIndex = 0;
        this.processFrame(frameIndex, this.m_animation, entity, this.m_root, this.m_animationSource, 0);
        if (AnmInstance.m_numQuads != 0) {
            this.addQuads(entity);
        }
        this.m_boundingRect.set((int)this.m_minX, (int)this.m_maxX, (int)this.m_minY, (int)this.m_maxY);
    }
    
    public void recomputeAvgBoundingRect(final float deltaTime) {
        final Rect rect = this.m_boundingRect;
        if (this.m_recomputeAvgBoundingBox == 0) {
            return;
        }
        if (rect.m_xMin == Integer.MAX_VALUE || rect.m_yMin == Integer.MAX_VALUE || rect.m_xMax == Integer.MIN_VALUE || rect.m_yMax == Integer.MIN_VALUE) {
            this.m_avgBoundingRect.set(rect.m_xMin, rect.m_xMax, rect.m_yMin, rect.m_yMax);
            this.m_recomputeAvgBoundingBox = -1;
            return;
        }
        if (this.m_recomputeAvgBoundingBox == -1) {
            this.m_avgBoundingRect.set(rect.m_xMin, rect.m_xMax, rect.m_yMin, rect.m_yMax);
            this.m_recomputeAvgBoundingBox = 0;
            return;
        }
        final float frameCountRatio = deltaTime / 300.0f;
        this.m_recomputeAvgBoundingBox = 0;
        final int minX = this.interpolate(this.m_avgBoundingRect.m_xMin, rect.m_xMin, frameCountRatio);
        final int maxX = this.interpolate(this.m_avgBoundingRect.m_xMax, rect.m_xMax, frameCountRatio);
        final int minY = this.interpolate(this.m_avgBoundingRect.m_yMin, rect.m_yMin, frameCountRatio);
        final int maxY = this.interpolate(this.m_avgBoundingRect.m_yMax, rect.m_yMax, frameCountRatio);
        this.m_avgBoundingRect.set(minX, maxX, minY, maxY);
    }
    
    private int interpolate(final int avgValue, final int destValue, final float ratio) {
        final float delta = destValue - avgValue;
        if (Math.abs(delta) <= 1.0f) {
            return avgValue;
        }
        this.m_recomputeAvgBoundingBox = 1;
        return MathHelper.fastCeil(avgValue + delta * ratio);
    }
    
    public static Material getDefaultMaterial() {
        return AnmInstance.m_defaultMaterial;
    }
    
    public void setMaterial(final Material material) {
        if (material != null) {
            material.addReference();
        }
        if (this.m_material != null) {
            this.m_material.removeReference();
        }
        this.m_material = material;
    }
    
    public Rect getBoundingRect() {
        return this.m_boundingRect;
    }
    
    public int getFrame(final int time) {
        SpriteDefinition animation;
        if (this.m_animation.hasOnlyOneSprite()) {
            animation = this.m_animationSource.getSpriteDefinition(this.m_animation.firstSpriteId());
        }
        else {
            animation = this.m_animation;
        }
        if (animation == null) {
            return 0;
        }
        final int frameIndex = this.getFrameIndex(time);
        return getRealFrameIndex(frameIndex, animation);
    }
    
    private static int getRealFrameIndex(final int frameIndex, final SpriteDefinition animation) {
        final int frameCount = animation.getFrameCount();
        if (frameIndex < frameCount) {
            return frameIndex;
        }
        if (animation.isLoop()) {
            return frameIndex % frameCount;
        }
        return frameCount - 1;
    }
    
    public float getScale() {
        return this.m_definitions.m_index.getScale();
    }
    
    public void setScale(final float scale) {
        if (this.m_flipAnimation) {
            this.m_root.setRotationSkew(-scale, 0.0f, 0.0f, scale);
        }
        else {
            this.m_root.setRotationSkew(scale, 0.0f, 0.0f, scale);
        }
    }
    
    public boolean usePerfectHitTest() {
        return this.m_animationSource != null && this.m_animationSource.usePerfectHitTest();
    }
    
    public int getNumFrames() {
        assert this.m_animation != null;
        SpriteDefinition animation;
        if (this.m_animation.hasOnlyOneSprite()) {
            animation = this.m_animationSource.getSpriteDefinition(this.m_animation.firstSpriteId());
            if (animation == null) {
                return 1;
            }
        }
        else {
            animation = this.m_animation;
        }
        return animation.getFrameCount();
    }
    
    public float[] getCustomColor(final int customColorIndex) {
        if (this.m_customColors == null) {
            return null;
        }
        return this.m_customColors[customColorIndex];
    }
    
    public void addCustomColor(final int customColorIndex, final float[] color) {
        if (this.m_customColors == null) {
            this.m_customColors = new float[10][];
        }
        this.m_customColors[customColorIndex] = color;
    }
    
    public void copyCustomColor(final AnmInstance from) {
        final float[][] colors = from.m_customColors;
        if (colors == null) {
            this.m_customColors = null;
        }
        else {
            this.m_customColors = new float[colors.length][];
            for (int i = 0; i < colors.length; ++i) {
                if (colors[i] == null) {
                    this.m_customColors[i] = null;
                }
                else {
                    this.m_customColors[i] = colors[i].clone();
                }
            }
        }
    }
    
    public void removeCustomColor(final int customColorIndex) {
        if (this.m_customColors == null) {
            return;
        }
        this.m_customColors[customColorIndex] = null;
    }
    
    public void removeCustomColors() {
        this.m_customColors = null;
    }
    
    public void getActions(final ArrayList<AnmAction> actions, final int time, final int previousTime) {
        assert this.m_animation != null;
        if (time == previousTime && time != 0) {
            return;
        }
        final int frameCount = this.getNumFrames();
        final int lastFrame = (time == 0) ? -1 : this.getFrame(previousTime);
        final int currentFrame = (time == 0) ? 0 : this.getFrame(time);
        if (lastFrame + 1 > currentFrame) {
            for (int i = lastFrame + 1; i <= frameCount - 1; ++i) {
                this.m_animation.pushActions(i, actions);
            }
            for (int i = 0; i <= currentFrame; ++i) {
                this.m_animation.pushActions(i, actions);
            }
        }
        else {
            for (int i = lastFrame + 1; i <= currentFrame; ++i) {
                this.m_animation.pushActions(i, actions);
            }
        }
    }
    
    private int getFrameIndex(final int previousTime) {
        return (int)(this.m_animationSource.m_header.getFrameRate() * (previousTime / 1000.0f));
    }
    
    public Matrix44 getCarrierMatrix() {
        return this.m_carrierMatrix;
    }
    
    public Matrix44 getLinkMatrix() {
        return this.m_linkMatrix;
    }
    
    public boolean isReady() {
        return this.m_definitions.isReady();
    }
    
    public int getAnimationDuration(String animName) {
        if (!this.m_definitions.isReady()) {
            AnmInstance.m_logger.error((Object)("Impossible de r\u00e9cup\u00e9rer la duree de l'animation " + animName + " dans " + this.m_path + this.m_baseFileName + " : la definition n'est pas chargee"));
            return 0;
        }
        final AnmIndex anmIndex = this.m_definitions.m_index;
        animName = this.getFlippedAnimName(animName);
        final AnmAnimationFileRecord fileRecord = anmIndex.getAnimationFileRecord(animName);
        if (fileRecord == null) {
            AnmInstance.m_anmLogger.error((Object)("Animation " + animName + " not found (" + this.m_path + this.m_baseFileName + ")!"));
            return 0;
        }
        assert fileRecord.m_name.equals(animName);
        SpriteDefinition spriteDef;
        Anm source;
        if (fileRecord.m_fileIndex == -1) {
            spriteDef = this.m_definitions.getSpriteDefinitionByCRC(fileRecord.m_crc);
            source = this.m_animationSource;
        }
        else {
            final String anmFile = this.m_path + anmIndex.getFileName(fileRecord.m_fileIndex) + ".anm";
            try {
                source = AnmManager.getInstance().loadAnmFile(anmFile, true);
                spriteDef = ((source != null) ? source.getSpriteDefinitionByCRC(fileRecord.m_crc) : null);
            }
            catch (IOException e) {
                AnmInstance.m_logger.error((Object)"", (Throwable)e);
                return 0;
            }
        }
        return getAnimationDuration(spriteDef, source);
    }
    
    private String getFlippedAnimName(String animName) {
        if (this.m_definitions.m_index.useFlip()) {
            final char direction = animName.charAt(0);
            switch (direction) {
                case '4': {
                    animName = '0' + animName.substring(1);
                    break;
                }
                case '3': {
                    animName = '1' + animName.substring(1);
                    break;
                }
                case '7': {
                    animName = '5' + animName.substring(1);
                    break;
                }
            }
        }
        return animName;
    }
    
    private static int getAnimationDuration(SpriteDefinition animation, final Anm source) {
        if (animation == null || source == null) {
            return 0;
        }
        if (animation.hasOnlyOneSprite()) {
            animation = source.getSpriteDefinition(animation.firstSpriteId());
            if (animation == null) {
                return 0;
            }
        }
        if (animation.isLoop()) {
            return Integer.MAX_VALUE;
        }
        final int frameCount = animation.getFrameCount();
        return (int)(1000 * frameCount / source.m_header.getFrameRate());
    }
    
    public int getAnimationDuration() {
        return getAnimationDuration(this.m_animation, this.m_animationSource);
    }
    
    public boolean needUpdate() {
        return this.m_needUpdate;
    }
    
    public void setUpToDate() {
        this.m_needUpdate = false;
    }
    
    public void forceUpdate() {
        this.m_needUpdate = true;
    }
    
    void setAnimation(final Anm anm, final SpriteDefinition animation) {
        if (anm != null && anm != this.m_definitions) {
            anm.addReference();
        }
        if (this.m_animationSource != null && this.m_animationSource != this.m_definitions) {
            this.m_animationSource.removeReference();
        }
        this.m_maxSpriteCount = (((this.m_animationSource = anm) != null) ? anm.m_maxSpriteCount : 0);
        this.m_animation = animation;
        this.m_recomputeAvgBoundingBox = 1;
    }
    
    private void processFrame(final int frameIndex, final SpriteDefinition spriteDef, final Entity3D entity, final AnmTransform parentTransform, final Anm source, final int level) {
        final int index = getRealFrameIndex(frameIndex, spriteDef);
        this.m_hiddenSprites.update();
        final int spriteCount = spriteDef.beginProcessFrame(index);
        final boolean hasImport = !source.m_importsById.isEmpty();
        final boolean usePerfectHit = source.usePerfectHitTest();
        for (int i = 0; i < spriteCount; ++i) {
            spriteDef.nextSprite();
            final AnmTransform transform = AnmInstance.m_transforms[level];
            transform.m_customColorIndex = parentTransform.m_customColorIndex;
            final short spriteId = spriteDef.process(parentTransform, transform);
            if (transform.m_alpha != 0.0f || !usePerfectHit) {
                if (hasImport) {
                    final AnmImport anmImport = source.getImport(spriteId);
                    if (anmImport != null) {
                        this.attachImported(frameIndex, entity, source, level, transform, anmImport);
                        continue;
                    }
                }
                final SpriteDefinition spriteDefinition = source.m_spriteDefinitionsById.get(spriteId);
                if (this.spriteDefinitionIsVisible(spriteDefinition)) {
                    this.processSprite(this.m_partsHelper, spriteDefinition, frameIndex, entity, transform, source, level);
                }
                else {
                    final AnmShapeDefinition shapeDefinition = source.m_shapeDefinitionsById.get(spriteId);
                    if (shapeDefinition != null) {
                        if (transform.m_alpha > 0.004f || !usePerfectHit) {
                            final float tx = shapeDefinition.m_offsetX * transform.m_rotationSkewX0 + shapeDefinition.m_offsetY * transform.m_rotationSkewX1 + transform.m_translationX;
                            final float ty = shapeDefinition.m_offsetX * -transform.m_rotationSkewY0 + shapeDefinition.m_offsetY * -transform.m_rotationSkewY1 - transform.m_translationY;
                            final float hx = transform.m_rotationSkewX1 * shapeDefinition.m_height;
                            final float hy = -transform.m_rotationSkewY1 * shapeDefinition.m_height;
                            final float wx = transform.m_rotationSkewX0 * shapeDefinition.m_width;
                            final float wy = -transform.m_rotationSkewY0 * shapeDefinition.m_width;
                            final long textureCRC = source.getTextureCRC();
                            if (AnmInstance.m_textureCRC != textureCRC) {
                                if (AnmInstance.m_numQuads != 0) {
                                    this.addQuads(entity);
                                }
                                AnmInstance.m_textureCRC = textureCRC;
                                AnmInstance.m_texture = TextureManager.getInstance().getTexture(textureCRC);
                            }
                            float x2 = hx + tx;
                            float y2 = hy + ty;
                            x2 = wx + hx + tx;
                            y2 = wy + hy + ty;
                            final float x3 = wx + tx;
                            final float y3 = wy + ty;
                            AnmInstance.m_positionBuffer[AnmInstance.m_positionIndex++] = tx;
                            AnmInstance.m_positionBuffer[AnmInstance.m_positionIndex++] = ty;
                            AnmInstance.m_positionBuffer[AnmInstance.m_positionIndex++] = x2;
                            AnmInstance.m_positionBuffer[AnmInstance.m_positionIndex++] = y2;
                            AnmInstance.m_positionBuffer[AnmInstance.m_positionIndex++] = x2;
                            AnmInstance.m_positionBuffer[AnmInstance.m_positionIndex++] = y2;
                            AnmInstance.m_positionBuffer[AnmInstance.m_positionIndex++] = x3;
                            AnmInstance.m_positionBuffer[AnmInstance.m_positionIndex++] = y3;
                            if (tx > this.m_maxX) {
                                this.m_maxX = tx;
                            }
                            else if (tx < this.m_minX) {
                                this.m_minX = tx;
                            }
                            if (x2 > this.m_maxX) {
                                this.m_maxX = x2;
                            }
                            else if (x2 < this.m_minX) {
                                this.m_minX = x2;
                            }
                            if (x2 > this.m_maxX) {
                                this.m_maxX = x2;
                            }
                            else if (x2 < this.m_minX) {
                                this.m_minX = x2;
                            }
                            if (x3 > this.m_maxX) {
                                this.m_maxX = x3;
                            }
                            else if (x3 < this.m_minX) {
                                this.m_minX = x3;
                            }
                            if (ty > this.m_maxY) {
                                this.m_maxY = ty;
                            }
                            else if (ty < this.m_minY) {
                                this.m_minY = ty;
                            }
                            if (y2 > this.m_maxY) {
                                this.m_maxY = y2;
                            }
                            else if (y2 < this.m_minY) {
                                this.m_minY = y2;
                            }
                            if (y2 > this.m_maxY) {
                                this.m_maxY = y2;
                            }
                            else if (y2 < this.m_minY) {
                                this.m_minY = y2;
                            }
                            if (y3 > this.m_maxY) {
                                this.m_maxY = y3;
                            }
                            else if (y3 < this.m_minY) {
                                this.m_minY = y3;
                            }
                            float r = transform.m_red;
                            float g = transform.m_green;
                            float b = transform.m_blue;
                            final float a = transform.m_alpha;
                            r *= 0.5f;
                            g *= 0.5f;
                            b *= 0.5f;
                            AnmInstance.m_colorBuffer[AnmInstance.m_colorIndex++] = r;
                            AnmInstance.m_colorBuffer[AnmInstance.m_colorIndex++] = g;
                            AnmInstance.m_colorBuffer[AnmInstance.m_colorIndex++] = b;
                            AnmInstance.m_colorBuffer[AnmInstance.m_colorIndex++] = a;
                            AnmInstance.m_colorBuffer[AnmInstance.m_colorIndex++] = r;
                            AnmInstance.m_colorBuffer[AnmInstance.m_colorIndex++] = g;
                            AnmInstance.m_colorBuffer[AnmInstance.m_colorIndex++] = b;
                            AnmInstance.m_colorBuffer[AnmInstance.m_colorIndex++] = a;
                            AnmInstance.m_colorBuffer[AnmInstance.m_colorIndex++] = r;
                            AnmInstance.m_colorBuffer[AnmInstance.m_colorIndex++] = g;
                            AnmInstance.m_colorBuffer[AnmInstance.m_colorIndex++] = b;
                            AnmInstance.m_colorBuffer[AnmInstance.m_colorIndex++] = a;
                            AnmInstance.m_colorBuffer[AnmInstance.m_colorIndex++] = r;
                            AnmInstance.m_colorBuffer[AnmInstance.m_colorIndex++] = g;
                            AnmInstance.m_colorBuffer[AnmInstance.m_colorIndex++] = b;
                            AnmInstance.m_colorBuffer[AnmInstance.m_colorIndex++] = a;
                            AnmInstance.m_texCoordsBuffer[AnmInstance.m_texCoordsIndex++] = shapeDefinition.m_left;
                            AnmInstance.m_texCoordsBuffer[AnmInstance.m_texCoordsIndex++] = shapeDefinition.m_bottom;
                            AnmInstance.m_texCoordsBuffer[AnmInstance.m_texCoordsIndex++] = shapeDefinition.m_left;
                            AnmInstance.m_texCoordsBuffer[AnmInstance.m_texCoordsIndex++] = shapeDefinition.m_top;
                            AnmInstance.m_texCoordsBuffer[AnmInstance.m_texCoordsIndex++] = shapeDefinition.m_right;
                            AnmInstance.m_texCoordsBuffer[AnmInstance.m_texCoordsIndex++] = shapeDefinition.m_top;
                            AnmInstance.m_texCoordsBuffer[AnmInstance.m_texCoordsIndex++] = shapeDefinition.m_right;
                            AnmInstance.m_texCoordsBuffer[AnmInstance.m_texCoordsIndex++] = shapeDefinition.m_bottom;
                            ++AnmInstance.m_numQuads;
                        }
                    }
                }
            }
        }
    }
    
    private void attachImported(final int frameIndex, final Entity3D entity, final Anm source, final int level, final AnmTransform transform, final AnmImport anmImport) {
        if (this.m_subPartsHelper != null) {
            final EquipementDef def = this.m_subPartsHelper.getDefinition(anmImport.m_crc);
            if (def != null) {
                final SpriteDefinition spriteDefinition = def.m_spriteDef;
                if (this.spriteDefinitionIsVisible(spriteDefinition)) {
                    this.processSprite(this.m_subPartsHelper, spriteDefinition, frameIndex, entity, transform, source, level);
                }
                return;
            }
        }
        final SpriteDefinition spriteDefinition2 = this.m_definitions.m_spriteDefinitionsByCRC.get(anmImport.m_crc);
        if (this.spriteDefinitionIsVisible(spriteDefinition2)) {
            this.processSprite(this.m_partsHelper, spriteDefinition2, frameIndex, entity, transform, this.m_definitions, level);
        }
    }
    
    private boolean spriteDefinitionIsVisible(final SpriteDefinition spriteDefinition) {
        return spriteDefinition != null && !this.m_hiddenSprites.contains(spriteDefinition.m_baseNameCRC);
    }
    
    private void createQuads(final Entity3D entity) {
        final int numVertices = AnmInstance.m_numQuads * 4;
        final VertexBufferPCT vertexBuffer = VertexBufferPCT.Factory.newPooledInstance(numVertices);
        this.fillVertexBuffer(vertexBuffer, numVertices);
        final GLGeometryMesh geom = GLGeometryMesh.Factory.newPooledInstance();
        geom.setBlendFunc(this.m_srcBlend, this.m_destBlend);
        geom.create(GeometryMesh.MeshType.Quad, vertexBuffer, AnmInstance.m_indexBuffer, AnmInstance.m_colorBuffer);
        entity.addTexturedGeometry(geom, AnmInstance.m_texture, this.m_material);
        geom.removeReference();
        AnmInstance.m_numQuads = 0;
        AnmInstance.m_positionIndex = 0;
        AnmInstance.m_colorIndex = 0;
        AnmInstance.m_texCoordsIndex = 0;
        vertexBuffer.removeReference();
    }
    
    private void fillVertexBuffer(final VertexBufferPCT vertexBuffer, final int numVertices) {
        vertexBuffer.setPositionBuffer(AnmInstance.m_positionBuffer, AnmInstance.m_positionIndex);
        vertexBuffer.setColorBuffer(AnmInstance.m_colorBuffer, AnmInstance.m_colorIndex);
        vertexBuffer.setTexCoord0Buffer(AnmInstance.m_texCoordsBuffer, AnmInstance.m_texCoordsIndex);
        vertexBuffer.setNumVertices(numVertices);
    }
    
    private void addQuads(final Entity3D entity) {
        ++this.m_batchCount;
        this.m_quadsCount += (byte)AnmInstance.m_numQuads;
        if (this.m_maxSpriteCount == -1 || entity.getNumGeometries() != 0) {
            this.createQuads(entity);
            return;
        }
        final int numVertices = AnmInstance.m_numQuads * 4;
        if (this.m_geometry != null && this.m_geometry.getVertexBuffer().getMaxVertices() < numVertices) {
            this.m_geometry.removeReference();
            this.m_geometry = null;
        }
        if (this.m_geometry == null) {
            final VertexBufferPCT vertexBuffer = VertexBufferPCT.Factory.newPooledInstance(Math.max(this.m_maxSpriteCount * 4, numVertices));
            (this.m_geometry = GLGeometryMesh.Factory.newPooledInstance()).create(GeometryMesh.MeshType.Quad, vertexBuffer, AnmInstance.m_indexBuffer, AnmInstance.m_colorBuffer);
            vertexBuffer.removeReference();
        }
        this.m_geometry.allocateColorFromBuffer(AnmInstance.m_colorBuffer, numVertices);
        this.m_geometry.setBlendFunc(this.m_srcBlend, this.m_destBlend);
        this.fillVertexBuffer(this.m_geometry.getVertexBuffer(), numVertices);
        entity.addTexturedGeometry(this.m_geometry, AnmInstance.m_texture, this.m_material);
        AnmInstance.m_numQuads = 0;
        AnmInstance.m_positionIndex = 0;
        AnmInstance.m_colorIndex = 0;
        AnmInstance.m_texCoordsIndex = 0;
    }
    
    private void processSprite(final PartsHelper partsHelper, final SpriteDefinition spriteDefinition, final int frameIndex, final Entity3D entity, final AnmTransform transform, final Anm source, final int level) {
        final int customColorIndex = spriteDefinition.getColorIndex();
        if (partsHelper.hasPartSetted() && !this.processParts(partsHelper, spriteDefinition, frameIndex, entity, transform, level, customColorIndex)) {
            return;
        }
        if (this.m_customColors != null && customColorIndex != 0) {
            this.applyColor(transform, customColorIndex);
        }
        if (spriteDefinition.m_name != null) {
            switch (spriteDefinition.m_baseNameCRC) {
                case 1272524161: {
                    this.setTransformToBuffer(transform, this.m_carrierMatrix.getBuffer());
                    break;
                }
                case 1003439990: {
                    this.setTransformToBuffer(transform, this.m_linkMatrix.getBuffer());
                    break;
                }
            }
        }
        this.processFrame(frameIndex, spriteDefinition, entity, transform, source, level + 1);
    }
    
    private boolean processParts(final PartsHelper partsHelper, final SpriteDefinition spriteDefinition, final int frameIndex, final Entity3D entity, final AnmTransform transform, final int level, final int customColorIndex) {
        final EquipementDef equipementDef = partsHelper.getDefinition(spriteDefinition.m_nameCRC);
        if (equipementDef == null) {
            return true;
        }
        if (spriteDefinition.m_name != null && customColorIndex != 0 && customColorIndex != 9 && customColorIndex != 6 && customColorIndex != 7 && this.m_customColors != null) {
            this.applyColor(transform, customColorIndex);
        }
        final SpriteDefinition spriteDef = equipementDef.m_spriteDef;
        assert spriteDef != null;
        this.processFrame(frameIndex, spriteDef, entity, transform, equipementDef.m_anm, level + 1);
        return false;
    }
    
    private void applyColor(final AnmTransform transform, final int customColorIndex) {
        if (customColorIndex != transform.m_customColorIndex) {
            float[] customColor = this.m_customColors[transform.m_customColorIndex];
            if (customColor != null) {
                transform.m_red /= customColor[0];
                transform.m_green /= customColor[1];
                transform.m_blue /= customColor[2];
                transform.m_alpha /= customColor[3];
            }
            customColor = this.m_customColors[customColorIndex];
            if (customColor != null) {
                transform.m_red *= customColor[0];
                transform.m_green *= customColor[1];
                transform.m_blue *= customColor[2];
                transform.m_alpha *= customColor[3];
                transform.m_customColorIndex = (byte)customColorIndex;
            }
        }
    }
    
    private void setTransformToBuffer(final AnmTransform transform, final float[] buffer) {
        buffer[0] = (this.m_flipAnimation ? (-transform.m_rotationSkewX0) : transform.m_rotationSkewX0);
        buffer[1] = transform.m_rotationSkewY0;
        buffer[4] = transform.m_rotationSkewX1;
        buffer[5] = transform.m_rotationSkewY1;
        buffer[12] = transform.m_translationX;
        buffer[13] = -transform.m_translationY;
    }
    
    public boolean containsAnimation(String animName) {
        animName = this.getFlippedAnimName(animName);
        return this.m_definitions.m_index.getAnimationFileRecord(animName) != null;
    }
    
    public float getRenderRadius() {
        if (this.m_definitions.m_index.hasRenderRadius()) {
            return this.m_definitions.m_index.getRenderRadius();
        }
        return 1.0f;
    }
    
    public Rect getAvgBoundingRect() {
        return this.m_avgBoundingRect;
    }
    
    public float getMinX() {
        return this.m_minX;
    }
    
    public float getMinY() {
        return this.m_minY;
    }
    
    public String getAnmFileName() {
        return this.m_path + this.m_baseFileName + ".anm";
    }
    
    public boolean hasPartsToSet() {
        return this.m_partsHelper.hasPartToSet();
    }
    
    public PartsHelper copyPartHelper() {
        return this.m_partsHelper.duplicate();
    }
    
    public PartsHelper copySubPartHelper() {
        return (this.m_subPartsHelper == null) ? null : this.m_subPartsHelper.duplicate();
    }
    
    public void setPartsFrom(final PartsHelper partsHelper, final PartsHelper subpartsHelper) {
        assert this.m_partsHelper != null;
        this.m_partsHelper.clear();
        this.m_partsHelper = partsHelper.duplicate();
        this.m_subPartsHelper = ((subpartsHelper == null) ? null : subpartsHelper.duplicate());
        this.updateHiddenParts();
    }
    
    public HidePartsHelper copyHidePartHelper() {
        return this.m_hiddenSprites.duplicate();
    }
    
    public void setHidePartsFrom(final HidePartsHelper hiddenSprites) {
        this.m_hiddenSprites.clear();
        this.m_hiddenSprites = hiddenSprites.duplicate();
    }
    
    public void unapplyAllParts() {
        this.m_partsHelper.clear();
        this.updateHiddenParts();
    }
    
    @Override
    public String toString() {
        return "AnmInstance - " + this.m_baseFileName + " [" + this.m_animName + "]";
    }
    
    public byte getQuadsCount() {
        return this.m_quadsCount;
    }
    
    public byte getBatchCount() {
        return this.m_batchCount;
    }
    
    public void attachAnmTo(final Anm anm) {
        if (this.m_subPartsHelper == null) {
            this.m_subPartsHelper = new PartsHelper();
        }
        this.m_subPartsHelper.setPartsFrom(anm, null);
    }
    
    public void dettachAnmTo(final Anm anm) {
        if (this.m_subPartsHelper != null) {
            this.m_subPartsHelper.removePartsFrom(anm, null);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnmInstance.class);
        m_anmLogger = Logger.getLogger("animation");
        DEFAULT_SRC_BLEND = BlendModes.One;
        m_indexBuffer = IndexBuffer.INDICES;
        m_defaultMaterial = Material.WHITE_NO_SPECULAR;
        m_positionBuffer = new float[4096];
        m_colorBuffer = new float[8192];
        m_texCoordsBuffer = new float[4096];
        final int maxDepth = 32;
        m_transforms = new AnmTransform[32];
        for (int i = 0; i < AnmInstance.m_transforms.length; ++i) {
            AnmInstance.m_transforms[i] = new AnmTransform();
        }
    }
}
