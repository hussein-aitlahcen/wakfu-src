package com.ankamagames.xulor2.component;

import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.states.*;

public class AnimatedElementViewer extends Widget implements ModulationColorClient
{
    public static final String TAG = "AnimatedElementViewer";
    private static final RenderStates PRE_RENDER_STATES;
    private static final RenderStates POST_RENDER_STATES;
    protected AnimatedElementWithDirection m_animatedElement;
    private String m_filePath;
    private String m_animName;
    protected float m_offsetX;
    protected float m_offsetY;
    protected float m_scale;
    private boolean m_animationChanged;
    private int m_direction;
    private Material m_material;
    private boolean m_useDefaultMaterial;
    private PartsHelper m_partsHelper;
    private PartsHelper m_subPartsHelper;
    private HidePartsHelper m_hiddenSprites;
    private BlendModes m_sourceBlend;
    private BlendModes m_destinationBlend;
    private boolean m_materialChanged;
    protected boolean m_entityNeedUpdate;
    private boolean m_directionChanged;
    private boolean m_equipmentChanged;
    private boolean m_blendModeChanged;
    private boolean m_forceReloadOnAnimNameChange;
    private boolean m_flipHorizontal;
    private boolean m_flipVertical;
    private boolean m_stopped;
    public static final int ANIMATED_ELEMENT_HASH;
    public static final int DIRECTION_HASH;
    public static final int FILE_PATH_HASH;
    public static final int EQUIPMENT_HASH;
    public static final int ANIM_NAME_HASH;
    public static final int OFFSET_X_HASH;
    public static final int OFFSET_Y_HASH;
    public static final int SCALE_HASH;
    public static final int USE_DEFAULT_MATERIAL_HASH;
    public static final int USE_BLEND_PREMULT;
    public static final int FORCE_RELOAD_ON_ANIM_NAME_CHANGE;
    public static final int FLIP_HORIZONTAL_HASH;
    public static final int FLIP_VERTICAL_HASH;
    public static final int STOPPED_HASH;
    
    public AnimatedElementViewer() {
        super();
        this.m_partsHelper = PartsHelper.NULL;
        this.m_subPartsHelper = null;
        this.m_hiddenSprites = HidePartsHelper.NULL;
        this.m_sourceBlend = BlendModes.One;
        this.m_destinationBlend = BlendModes.InvSrcAlpha;
        this.m_forceReloadOnAnimNameChange = false;
        this.m_flipHorizontal = false;
        this.m_flipVertical = false;
        this.m_equipmentChanged = false;
        this.m_materialChanged = false;
        this.m_useDefaultMaterial = true;
    }
    
    @Override
    public String getTag() {
        return "AnimatedElementViewer";
    }
    
    public final String getFilePath() {
        return this.m_filePath;
    }
    
    public final void setFilePath(final String filePath) {
        if (filePath == null) {
            return;
        }
        this.m_filePath = filePath;
        final String animationPath = Xulor.getInstance().m_animationPath;
        if (animationPath == null) {
            return;
        }
        this.m_animatedElement.setGfxId(FileHelper.getNameWithoutExt(filePath));
        String fileName;
        if (filePath.startsWith("jar:") || filePath.startsWith("file:")) {
            fileName = filePath;
        }
        else {
            fileName = animationPath + filePath;
        }
        this.loadAnimatedElement(fileName);
    }
    
    private void loadAnimatedElement(final String fileName) {
        try {
            this.m_animatedElement.load(fileName, false);
        }
        catch (IOException e) {
            AnimatedElementViewer.m_logger.error((Object)"Unable to load anm file", (Throwable)e);
        }
        this.m_animatedElement.setBlendFunc(this.m_sourceBlend, this.m_destinationBlend);
    }
    
    public final String getAnimName() {
        return this.m_animName;
    }
    
    public final void setAnimName(final String animName) {
        if (animName != null) {
            this.m_animName = animName;
            this.m_animationChanged = true;
        }
    }
    
    public AnimatedElement getAnimatedElement() {
        return this.m_animatedElement;
    }
    
    public void setAnimatedElement(final AnimatedElement animatedElement) {
        if (this.m_animatedElement == null) {
            return;
        }
        this.m_animatedElement.copyData(animatedElement);
        if (this.m_useDefaultMaterial) {
            this.m_animatedElement.resetMaterial();
        }
        this.m_entityNeedUpdate = true;
        this.m_materialChanged = true;
        this.m_directionChanged = true;
    }
    
    public boolean isForceReloadOnAnimNameChange() {
        return this.m_forceReloadOnAnimNameChange;
    }
    
    public void setForceReloadOnAnimNameChange(final boolean forceReloadOnAnimNameChange) {
        this.m_forceReloadOnAnimNameChange = forceReloadOnAnimNameChange;
    }
    
    public void setFlipVertical(final boolean flipVertical) {
        this.m_flipVertical = flipVertical;
    }
    
    public void setFlipHorizontal(final boolean flipHorizontal) {
        this.m_flipHorizontal = flipHorizontal;
    }
    
    public float getOffsetX() {
        return this.m_offsetX;
    }
    
    public final void setOffsetX(final float offsetX) {
        this.m_offsetX = offsetX;
        this.m_entityNeedUpdate = true;
    }
    
    public final float getOffsetY() {
        return this.m_offsetY;
    }
    
    public final void setOffsetY(final float offsetY) {
        this.m_offsetY = offsetY;
        this.m_entityNeedUpdate = true;
    }
    
    public float getScale() {
        return this.m_scale;
    }
    
    public void setScale(final float scale) {
        this.m_scale = scale;
        this.m_entityNeedUpdate = true;
    }
    
    public int getDirection() {
        return this.m_direction;
    }
    
    public void setDirection(final int direction) {
        this.m_direction = direction;
        this.m_directionChanged = true;
    }
    
    public final void setMaterial(final Material material) {
        if (material == null) {
            return;
        }
        this.m_material.copy(material);
        this.m_materialChanged = true;
    }
    
    public void setEquipment(final AnmInstance anmInstance) {
        if (anmInstance == null) {
            return;
        }
        this.m_partsHelper = anmInstance.copyPartHelper();
        this.m_subPartsHelper = anmInstance.copySubPartHelper();
        this.m_hiddenSprites = anmInstance.copyHidePartHelper();
        this.m_equipmentChanged = true;
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return true;
    }
    
    public boolean isUseDefaultMaterial() {
        return this.m_useDefaultMaterial;
    }
    
    public void setUseDefaultMaterial(final boolean useDefaultMaterial) {
        this.m_useDefaultMaterial = useDefaultMaterial;
    }
    
    public void setUseBlendPremult(final boolean useBlendPremult) {
        if (useBlendPremult) {
            this.m_sourceBlend = BlendModes.One;
        }
        else {
            this.m_sourceBlend = BlendModes.SrcAlpha;
        }
        this.m_blendModeChanged = true;
    }
    
    private void setStopped(final boolean stopped) {
        this.m_stopped = stopped;
        this.setNeedsToPostProcess();
    }
    
    @Override
    public final void onCheckIn() {
        super.onCheckIn();
        if (this.m_animatedElement != null) {
            this.m_animatedElement.dispose();
            this.m_animatedElement = null;
        }
        this.m_partsHelper = PartsHelper.NULL;
        this.m_hiddenSprites = HidePartsHelper.NULL;
        if (this.m_material != null) {
            this.m_material.removeReference();
            this.m_material = null;
        }
    }
    
    @Override
    public final void onCheckOut() {
        super.onCheckOut();
        assert this.m_animatedElement == null;
        this.m_animatedElement = new AnimatedElementWithDirection(0L);
        this.m_entityNeedUpdate = true;
        final AnimatedElementViewerAppearance app = new AnimatedElementViewerAppearance();
        app.onCheckOut();
        app.setWidget(this);
        this.add(app);
        this.setNeedsToPostProcess();
        this.m_sourceBlend = BlendModes.One;
        this.m_destinationBlend = BlendModes.InvSrcAlpha;
        (this.m_material = Material.Factory.newPooledInstance()).copy(Material.WHITE_NO_SPECULAR);
        this.m_materialChanged = true;
        this.m_useDefaultMaterial = true;
        this.m_forceReloadOnAnimNameChange = false;
        this.m_flipHorizontal = false;
        this.m_flipVertical = false;
        this.m_stopped = false;
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        super.postProcess(deltaTime);
        if (!this.m_visible) {
            return true;
        }
        this.updateEntity();
        if (this.m_animationChanged) {
            this.m_animatedElement.setAnimation(this.m_animName);
            if (this.m_forceReloadOnAnimNameChange) {
                this.m_animatedElement.forceReloadAnimation();
            }
            this.m_animationChanged = false;
        }
        if (this.m_directionChanged) {
            this.m_animatedElement.setDirection(Direction8.getDirectionFromIndex(this.m_direction));
            this.m_directionChanged = false;
        }
        if (this.m_equipmentChanged) {
            final AnmInstance anmInstance = this.m_animatedElement.getAnmInstance();
            anmInstance.setPartsFrom(this.m_partsHelper, this.m_subPartsHelper);
            anmInstance.setHidePartsFrom(this.m_hiddenSprites);
            this.m_animatedElement.forceUpdateEquipment();
            this.m_equipmentChanged = false;
        }
        if (this.m_materialChanged) {
            this.m_animatedElement.setMaterial(this.m_material);
            this.m_materialChanged = false;
        }
        if (this.m_blendModeChanged) {
            this.m_animatedElement.setBlendFunc(this.m_sourceBlend, this.m_destinationBlend);
            this.m_blendModeChanged = false;
        }
        if (this.m_animatedElement.isStopped() != this.m_stopped) {
            this.m_animatedElement.setStopped(this.m_stopped);
        }
        this.m_animatedElement.update(null, deltaTime);
        return true;
    }
    
    @Override
    public final void copyElement(final BasicElement d) {
        final AnimatedElementViewer e = (AnimatedElementViewer)d;
        super.copyElement(e);
        e.setFilePath(this.m_filePath);
        e.setOffsetX(this.m_offsetX);
        e.setOffsetY(this.m_offsetY);
        e.setScale(this.m_scale);
        e.setDirection(this.m_direction);
        e.setMaterial(this.m_material);
        e.setAnimName(this.m_animName);
        e.setAnimatedElement(this.m_animatedElement);
        e.setForceReloadOnAnimNameChange(this.m_forceReloadOnAnimNameChange);
        e.setFlipHorizontal(this.m_flipHorizontal);
        e.setFlipVertical(this.m_flipVertical);
        e.setStopped(this.m_stopped);
        e.m_useDefaultMaterial = this.m_useDefaultMaterial;
        e.setNeedsToPostProcess();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == AnimatedElementViewer.FILE_PATH_HASH) {
            this.setFilePath(value);
        }
        else if (hash == AnimatedElementViewer.ANIM_NAME_HASH) {
            this.setAnimName(value);
        }
        else if (hash == AnimatedElementViewer.DIRECTION_HASH) {
            this.setDirection(PrimitiveConverter.getInteger(value));
        }
        else if (hash == AnimatedElementViewer.OFFSET_X_HASH) {
            this.setOffsetX(PrimitiveConverter.getFloat(value));
        }
        else if (hash == AnimatedElementViewer.OFFSET_Y_HASH) {
            this.setOffsetY(PrimitiveConverter.getFloat(value));
        }
        else if (hash == AnimatedElementViewer.SCALE_HASH) {
            this.setScale(PrimitiveConverter.getFloat(value));
        }
        else if (hash == AnimatedElementViewer.USE_DEFAULT_MATERIAL_HASH) {
            this.setUseDefaultMaterial(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == AnimatedElementViewer.USE_BLEND_PREMULT) {
            this.setUseDefaultMaterial(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == AnimatedElementViewer.FORCE_RELOAD_ON_ANIM_NAME_CHANGE) {
            this.setForceReloadOnAnimNameChange(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == AnimatedElementViewer.FLIP_HORIZONTAL_HASH) {
            this.setFlipHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == AnimatedElementViewer.FLIP_VERTICAL_HASH) {
            this.setFlipVertical(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != AnimatedElementViewer.STOPPED_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setStopped(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == AnimatedElementViewer.ANIMATED_ELEMENT_HASH) {
            this.setAnimatedElement((AnimatedElement)value);
        }
        else if (hash == AnimatedElementViewer.FILE_PATH_HASH) {
            this.setFilePath((String)value);
        }
        else if (hash == AnimatedElementViewer.ANIM_NAME_HASH) {
            this.setAnimName((String)value);
        }
        else if (hash == AnimatedElementViewer.DIRECTION_HASH) {
            this.setDirection(PrimitiveConverter.getInteger(value));
        }
        else if (hash == AnimatedElementViewer.OFFSET_X_HASH) {
            this.setOffsetX(PrimitiveConverter.getFloat(value));
        }
        else if (hash == AnimatedElementViewer.OFFSET_Y_HASH) {
            this.setOffsetY(PrimitiveConverter.getFloat(value));
        }
        else if (hash == AnimatedElementViewer.SCALE_HASH) {
            this.setScale(PrimitiveConverter.getFloat(value));
        }
        else if (hash == AnimatedElementViewer.EQUIPMENT_HASH) {
            this.setEquipment((AnmInstance)value);
        }
        else if (hash == AnimatedElementViewer.USE_DEFAULT_MATERIAL_HASH) {
            this.setUseDefaultMaterial(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == AnimatedElementViewer.FORCE_RELOAD_ON_ANIM_NAME_CHANGE) {
            this.setForceReloadOnAnimNameChange(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == AnimatedElementViewer.FLIP_HORIZONTAL_HASH) {
            this.setFlipHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == AnimatedElementViewer.FLIP_VERTICAL_HASH) {
            this.setFlipVertical(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != AnimatedElementViewer.STOPPED_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setStopped(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    protected void addInnerMeshes() {
        super.addInnerMeshes();
        final Entity entity = this.m_animatedElement.getEntity();
        this.m_entity.addChild(entity);
        entity.setPreRenderStates(AnimatedElementViewer.PRE_RENDER_STATES);
        entity.setPostRenderStates(AnimatedElementViewer.POST_RENDER_STATES);
    }
    
    protected void updateEntity() {
        final Entity entity = this.m_animatedElement.getEntity();
        final TransformerSRT transformer = (TransformerSRT)entity.getTransformer().getTransformer(0);
        transformer.setTranslation((float)this.m_size.getWidth() / 2.0f + this.m_offsetX, (float)this.m_size.getHeight() / 2.0f + this.m_offsetY, 0.0f);
        float scale = 1.0f;
        if (this.m_animatedElement.getAnmInstance() != null) {
            scale = this.m_animatedElement.getAnmInstance().getScale();
        }
        transformer.setScale(this.m_scale / scale * (this.m_flipHorizontal ? -1.0f : 1.0f), this.m_scale / scale * (this.m_flipVertical ? -1.0f : 1.0f), 0.0f);
        entity.getTransformer().setToUpdate();
        this.m_entityNeedUpdate = false;
        if (this.m_material != null && this.m_materialChanged) {
            this.m_animatedElement.setMaterial(this.m_material);
            this.m_animatedElement.getAnmEntity().updateMaterial(this.m_animatedElement.getMaterial());
            this.m_materialChanged = false;
        }
    }
    
    @Override
    public void setModulationColor(final Color c) {
        if (this.m_animatedElement == null || c == null) {
            return;
        }
        final Entity3D entity = this.m_animatedElement.getAnmEntity();
        if (entity == null) {
            return;
        }
        final float[] specular = { 0.0f, 0.0f, 0.0f, 0.0f };
        final float[] diffuse = { c.getRed() * c.getAlpha(), c.getGreen() * c.getAlpha(), c.getBlue() * c.getAlpha(), c.getAlpha() };
        this.m_material.setDiffuseColor(diffuse);
        this.m_material.setSpecularColor(specular);
        this.m_animatedElement.setMaterial(this.m_material);
        entity.updateMaterial(this.m_animatedElement.getMaterial());
    }
    
    @Override
    public Color getModulationColor() {
        final float[] floats = this.m_animatedElement.getMaterial().getDiffuseColor();
        return new Color(floats[0], floats[1], floats[2], floats[3]);
    }
    
    static {
        PRE_RENDER_STATES = new RenderStates() {
            @Override
            public void apply(final Renderer renderer) {
                super.apply(renderer);
                RenderStateManager.getInstance().setColorScale(2.0f);
            }
        };
        POST_RENDER_STATES = new RenderStates() {
            @Override
            public void apply(final Renderer renderer) {
                super.apply(renderer);
                RenderStateManager.getInstance().setColorScale(1.0f);
            }
        };
        ANIMATED_ELEMENT_HASH = "animatedElement".hashCode();
        DIRECTION_HASH = "direction".hashCode();
        FILE_PATH_HASH = "filePath".hashCode();
        EQUIPMENT_HASH = "equipment".hashCode();
        ANIM_NAME_HASH = "animName".hashCode();
        OFFSET_X_HASH = "offsetX".hashCode();
        OFFSET_Y_HASH = "offsetY".hashCode();
        SCALE_HASH = "scale".hashCode();
        USE_DEFAULT_MATERIAL_HASH = "useDefaultMaterial".hashCode();
        USE_BLEND_PREMULT = "blendPremult".hashCode();
        FORCE_RELOAD_ON_ANIM_NAME_CHANGE = "forceReloadOnAnimNameChange".hashCode();
        FLIP_HORIZONTAL_HASH = "flipHorizontal".hashCode();
        FLIP_VERTICAL_HASH = "flipVertical".hashCode();
        STOPPED_HASH = "stopped".hashCode();
    }
}
