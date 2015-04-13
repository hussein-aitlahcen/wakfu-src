package com.ankamagames.baseImpl.graphics.alea.display;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.sun.opengl.util.texture.*;
import com.ankamagames.baseImpl.graphics.alea.worldElements.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import java.util.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.material.*;

public class DisplayedScreenElement extends MemoryObject implements Maskable, HighLightedElement, LitSceneObject, Hiddable
{
    public static final ObjectFactory Factory;
    protected ScreenElement m_element;
    protected byte m_elementVisibilityMask;
    EntitySprite m_sprite;
    boolean m_isVisible;
    boolean m_isClipped;
    boolean m_added;
    private short m_animationTime;
    private final float[] m_color;
    private float[] m_teint;
    private int m_maskKey;
    private int m_groupId;
    private short m_layerIndex;
    private byte m_state;
    private long m_highlightHandle;
    private static final float[] m_tempColor;
    private static final Logger m_logger;
    
    protected DisplayedScreenElement() {
        super();
        this.m_isVisible = true;
        this.m_isClipped = false;
        this.m_added = false;
        this.m_animationTime = 0;
        this.m_color = new float[4];
        this.m_maskKey = 0;
        this.m_groupId = 0;
        this.m_layerIndex = 0;
        this.m_state = 3;
    }
    
    public float getAlpha() {
        return this.m_color[3];
    }
    
    public void setElement(final ScreenElement element) {
        if (this.m_element != null) {
            this.delete();
        }
        this.m_element = element;
        this.m_elementVisibilityMask = this.m_element.m_commonData.getVisibilityMask();
        this.m_element.addReference();
        this.m_highlightHandle = HighLightManager.getHandle(this.m_element.getCellX(), this.m_element.getCellY(), this.m_element.getCellZ());
        this.m_maskKey = this.m_element.getGroupKey();
        this.m_layerIndex = this.m_element.getLayerIndex();
        this.m_groupId = this.m_element.getGroupId();
        this.createSprite();
    }
    
    public void createSprite() {
        final ElementProperties properties = this.m_element.m_commonData;
        final int gfxId = properties.getGfxId();
        float top = this.m_element.m_top;
        float left = this.m_element.m_left;
        Texture texture;
        int spriteSizeX;
        int spriteSizeY;
        if ((DisplayedWorldHelper.EXPORT_MASK & properties.getExportMask()) == 0x0) {
            this.m_isVisible = false;
            texture = AleaTextureManager.getInstance().getTexture(19067);
            if (this.m_isVisible && texture != null) {
                spriteSizeX = texture.getSize().getX();
                spriteSizeY = texture.getSize().getY();
                left = ScreenWorld.isoToScreenX(this.m_element.m_cellX, this.m_element.m_cellY) - spriteSizeX * 0.5f;
                top = ScreenWorld.isoToScreenY(this.m_element.m_cellX, this.m_element.m_cellY, this.m_element.m_cellZ) + spriteSizeY * 0.5f;
            }
            else {
                spriteSizeX = 1;
                spriteSizeY = 1;
            }
        }
        else {
            texture = AleaTextureManager.getInstance().getTexture(gfxId);
            spriteSizeX = properties.getImgWidth();
            spriteSizeY = properties.getImgHeight();
        }
        if ((this.m_element.m_cellX + this.m_element.m_cellY) % 2 != 0) {
            top += ((this.m_element.m_cellY > -this.m_element.m_cellX) ? -0.5f : 0.5f);
        }
        final GLGeometrySprite geom = GLGeometrySprite.Factory.newPooledInstance();
        geom.setBlendFunc(BlendModes.One, BlendModes.InvSrcAlpha);
        geom.setBounds(top, left, spriteSizeX, spriteSizeY);
        this.resetColor();
        if (this.m_sprite != null) {
            this.m_sprite.removeReference();
        }
        this.initializeEntity(this.m_sprite = EntitySprite.Factory.newPooledInstance());
        this.m_sprite.setGeometry(geom);
        this.m_sprite.setTexture(texture);
        this.m_sprite.setColor(this.m_color[0], this.m_color[1], this.m_color[2], this.m_color[3]);
        this.m_sprite.m_minX = this.m_element.m_left;
        this.m_sprite.m_maxX = this.m_element.m_left + spriteSizeX;
        this.m_sprite.m_minY = this.m_element.m_top - spriteSizeY;
        this.m_sprite.m_maxY = this.m_element.m_top;
        this.updateTextureCoord((short)MathHelper.random(Integer.MAX_VALUE));
        geom.removeReference();
    }
    
    protected final void resetColor() {
        this.m_color[0] = 0.5f;
        this.m_color[1] = 0.5f;
        this.m_color[2] = 0.5f;
        this.m_color[3] = 1.0f;
    }
    
    protected final void initializeEntity(final Entity entity) {
        entity.m_owner = this;
        if (this.m_element.isOccluder()) {
            entity.m_userFlag1 |= 0x2;
        }
        entity.removeEffectForWorld();
        entity.m_cellX = this.m_element.m_cellX;
        entity.m_cellY = this.m_element.m_cellY;
        entity.m_cellZ = this.m_element.m_cellZ - this.m_element.m_height;
        entity.m_height = this.m_element.m_height;
        int altitudeOrder = this.m_element.m_altitudeOrder;
        if (this.m_element.m_commonData.isBeforeMobile()) {
            entity.m_cellX += 0.9f;
            entity.m_cellY += 0.9f;
            altitudeOrder += LayerOrder.LAYER_COUNT - 1;
        }
        entity.m_zOrder = LayerOrder.computeZOrder(this.m_element.m_cellX, this.m_element.m_cellY, altitudeOrder, LayerOrder.GROUND.getDeltaZ());
    }
    
    public void changeZOrder(int deltaZ) {
        int x = this.m_element.m_cellX;
        int y = this.m_element.m_cellY;
        if (deltaZ < 0) {
            --x;
            --y;
            deltaZ += LayerOrder.LAYER_COUNT;
        }
        this.m_sprite.m_zOrder = LayerOrder.computeZOrder(x, y, this.m_element.m_altitudeOrder, deltaZ);
    }
    
    public void updateTextureCoord(final short deltaTime) {
        this.m_animationTime += deltaTime;
        final TextureCoords texCoords = this.m_element.m_commonData.getTextureCoordinates(this.m_animationTime);
        this.m_sprite.getGeometry().setTextureCoordinates(texCoords.top(), texCoords.left(), texCoords.bottom(), texCoords.right());
    }
    
    public final void replaceGfx(final int gfxId) {
        final ElementProperties elementProperties = ElementPropertiesLibrary.getElement(gfxId);
        if (elementProperties == null) {
            DisplayedScreenElement.m_logger.error((Object)("Unable to replace element gfxId : " + gfxId + " unknown"));
            return;
        }
        final ScreenElement element = this.m_element;
        element.m_top -= this.m_element.m_commonData.getOriginY();
        final ScreenElement element2 = this.m_element;
        element2.m_left += this.m_element.m_commonData.getOriginX();
        this.m_element.m_commonData = elementProperties;
        final ScreenElement element3 = this.m_element;
        element3.m_top += elementProperties.getOriginY();
        final ScreenElement element4 = this.m_element;
        element4.m_left -= elementProperties.getOriginX();
        this.createSprite();
    }
    
    public final void clear(final DisplayedScreenElementFactory factory) {
        factory.deleteDisplayedScreenElement(this);
    }
    
    private boolean isVisible(final AbstractCamera camera) {
        final Entity entity = this.getEntitySprite();
        return entity != null && camera.isVisibleInScreen(entity.m_maxY, entity.m_minX, entity.m_minY, entity.m_maxX);
    }
    
    public void addToScene(final IsoWorldScene scene, final ArrayList<DisplayedScreenElement> entities, final AbstractCamera camera) {
        if (!this.m_isVisible) {
            return;
        }
        if (!this.isVisible(camera)) {
            this.m_isClipped = true;
            return;
        }
        if (this.m_added) {
            return;
        }
        float[] color;
        if (this.m_teint == null) {
            color = scene.getLayerColor(this);
        }
        else {
            this.m_teint[3] = scene.getLayerColor(this)[3];
            final float[] teint = this.m_teint;
            final int n = 3;
            teint[n] *= 0.5f;
            final float[] teint2 = this.m_teint;
            final int n2 = 0;
            teint2[n2] *= this.m_teint[3];
            final float[] teint3 = this.m_teint;
            final int n3 = 1;
            teint3[n3] *= this.m_teint[3];
            final float[] teint4 = this.m_teint;
            final int n4 = 2;
            teint4[n4] *= this.m_teint[3];
            color = this.m_teint;
        }
        assert color != null;
        System.arraycopy(color, 0, DisplayedScreenElement.m_tempColor, 0, 4);
        HiddenElementManager.Fade fade = HiddenElementManager.getInstance().getFade(this.m_highlightHandle);
        if (fade != null) {
            final float a = fade.m_alpha;
            final float[] tempColor = DisplayedScreenElement.m_tempColor;
            final int n5 = 0;
            tempColor[n5] *= fade.m_red * a;
            final float[] tempColor2 = DisplayedScreenElement.m_tempColor;
            final int n6 = 1;
            tempColor2[n6] *= fade.m_green * a;
            final float[] tempColor3 = DisplayedScreenElement.m_tempColor;
            final int n7 = 2;
            tempColor3[n7] *= fade.m_blue * a;
            final float[] tempColor4 = DisplayedScreenElement.m_tempColor;
            final int n8 = 3;
            tempColor4[n8] *= a;
        }
        fade = HiddenElementManager.getInstance().getFadeByLayer(this.m_layerIndex);
        if (fade != null) {
            final float a = fade.m_alpha;
            final float[] tempColor5 = DisplayedScreenElement.m_tempColor;
            final int n9 = 0;
            tempColor5[n9] *= fade.m_red * a;
            final float[] tempColor6 = DisplayedScreenElement.m_tempColor;
            final int n10 = 1;
            tempColor6[n10] *= fade.m_green * a;
            final float[] tempColor7 = DisplayedScreenElement.m_tempColor;
            final int n11 = 2;
            tempColor7[n11] *= fade.m_blue * a;
            final float[] tempColor8 = DisplayedScreenElement.m_tempColor;
            final int n12 = 3;
            tempColor8[n12] *= a;
        }
        if (color[3] < 0.004f) {
            this.m_isClipped = true;
            return;
        }
        this.m_state = HideElementHelper.computeColor(this.m_state, DisplayedScreenElement.m_tempColor);
        this.m_isClipped = false;
        this.updateColor(DisplayedScreenElement.m_tempColor);
        HighLightManager.getInstance().prepareElementBeforeRendering(scene, this, this.m_color[3]);
        this.m_added = true;
        entities.add(this);
    }
    
    public boolean fineHitTest(final int x, final int y) {
        assert this.isVisible();
        final Texture texture = this.m_sprite.getTexture();
        if (texture == null) {
            return false;
        }
        final Layer layer = texture.getLayer(0);
        final int imgY = y - this.m_sprite.m_minY;
        if (imgY >= layer.getHeight() || imgY < 0) {
            return false;
        }
        int imgX = x - this.m_sprite.m_minX;
        if (imgX >= layer.getWidth() || imgX < 0) {
            return false;
        }
        if (this.m_element.m_commonData.isFlip()) {
            imgX = layer.getWidth() - imgX - 1;
        }
        return layer.getAlphaMask().getValue(imgX, imgY);
    }
    
    public final ScreenElement getElement() {
        return this.m_element;
    }
    
    public Entity getEntitySprite() {
        return this.m_sprite;
    }
    
    public final long getHashCode() {
        return this.m_element.m_hashCode;
    }
    
    public final boolean isVisible() {
        return this.m_isVisible && this.m_state == 3 && !this.m_isClipped;
    }
    
    final boolean isVisible_withoutClipping() {
        return this.m_isVisible && this.m_state == 3;
    }
    
    public final void setVisible(final boolean visible) {
        this.m_isVisible = visible;
    }
    
    @Override
    public final void hide(final boolean hide) {
        this.m_state = (byte)(hide ? 0 : 2);
    }
    
    public int getGroupId() {
        return this.m_groupId;
    }
    
    @Override
    public final int getMaskKey() {
        return this.m_maskKey;
    }
    
    @Override
    public final void setMaskKey(final int key, final short layerId) {
        throw new UnsupportedOperationException("");
    }
    
    @Override
    public final short getLayerId() {
        return this.m_layerIndex;
    }
    
    @Override
    public long getLayerReference() {
        return this.m_highlightHandle;
    }
    
    @Override
    public boolean isHollow() {
        return this.m_element.m_commonData.isMoveTop();
    }
    
    @Override
    public void transformHighLightEntity(final IsoWorldScene scene, final HighLightEntity entity, final float size, final Point2i initialTextureSize, final int elevationUnit, final HighLightTextureApplication textureApplication, final int highlightLevel) {
        if (entity == null) {
            return;
        }
        final byte slope = this.m_element.m_commonData.getSlope();
        final float height = this.isHollow() ? 0.0f : this.m_element.m_commonData.getVisualHeight();
        final float slopHeight = (slope == 0) ? height : (height * 0.5f);
        final int altitude = this.m_element.m_cellZ - this.m_element.m_height;
        final int worldX = this.m_element.m_cellX;
        final int worldY = this.m_element.m_cellY;
        final Point2 pt = IsoCameraFunc.getScreenPosition(scene, worldX, worldY, slopHeight + altitude);
        entity.getTransformer().setIdentity(0);
        entity.getTransformer().setTranslation(0, pt.m_x, pt.m_y);
        entity.m_cellX = worldX;
        entity.m_cellY = worldY;
        entity.m_cellZ = altitude;
        entity.m_height = 0.0f;
        this.setZOrder(entity);
        textureApplication.apply(entity, slope, height * elevationUnit, size, initialTextureSize.getX() * 0.5f, initialTextureSize.getY() * 0.5f);
    }
    
    @Override
    public void setZOrder(final HighLightEntity entity) {
        final Entity entitySprite = this.getEntitySprite();
        long zOrder = (entitySprite != null) ? entitySprite.m_zOrder : 0L;
        zOrder += LayerOrder.HIGHTLIGHT.getDeltaZ();
        if (zOrder >= entity.m_zOrder) {
            entity.m_zOrder = zOrder;
        }
    }
    
    public void setTeint(final float[] color) {
        this.m_teint = color;
    }
    
    @Override
    protected void checkout() {
        this.m_state = 3;
        this.m_added = false;
        this.m_isClipped = false;
        this.m_animationTime = 0;
        this.m_maskKey = 0;
        this.m_groupId = 0;
        this.m_layerIndex = 0;
        this.m_highlightHandle = 0L;
    }
    
    @Override
    protected void checkin() {
        this.m_element.removeReference();
        this.m_element = null;
        this.m_elementVisibilityMask = 0;
        this.m_isVisible = true;
        this.m_teint = null;
        if (this.m_sprite != null) {
            this.m_sprite.removeReference();
            this.m_sprite = null;
        }
    }
    
    private void updateColor(final float[] color) {
        if (!this.m_element.isGradient()) {
            this.processColor(color);
        }
    }
    
    protected void processColor(final float[] color) {
        this.m_element.computeColor(color);
        if (color[0] < 0.0f) {
            color[0] = 0.0f;
        }
        else if (color[0] > 1.0f) {
            color[0] = 1.0f;
        }
        if (color[1] < 0.0f) {
            color[1] = 0.0f;
        }
        else if (color[1] > 1.0f) {
            color[1] = 1.0f;
        }
        if (color[2] < 0.0f) {
            color[2] = 0.0f;
        }
        else if (color[2] > 1.0f) {
            color[2] = 1.0f;
        }
        if (color[3] < 0.0f) {
            color[3] = 0.0f;
        }
        else if (color[3] > 1.0f) {
            color[3] = 1.0f;
        }
        if (this.m_color[0] == color[0] && this.m_color[1] == color[1] && this.m_color[2] == color[2] && this.m_color[3] == color[3]) {
            return;
        }
        this.m_color[0] = color[0];
        this.m_color[1] = color[1];
        this.m_color[2] = color[2];
        this.m_color[3] = color[3];
        this.updateEntityColor(color);
    }
    
    protected void updateEntityColor(final float[] color) {
        this.m_sprite.setColor(color[0], color[1], color[2], color[3]);
        this.m_sprite.updateMaterial();
    }
    
    @Override
    public int getWorldCellX() {
        return this.m_element.m_cellX;
    }
    
    @Override
    public int getWorldCellY() {
        return this.m_element.m_cellY;
    }
    
    @Override
    public short getWorldCellAltitude() {
        return this.m_element.m_cellZ;
    }
    
    @Override
    public void applyLighting(final float[] colors) {
        if (this.m_sprite != null && this.m_element != null) {
            applyLightingTo(this.m_sprite, colors);
        }
    }
    
    protected static void applyLightingTo(final EntitySprite sprite, final float[] colors) {
        final Material material = sprite.getMaterial();
        final float[] mDiffuse = material.getDiffuseColor();
        if (colors[0] == mDiffuse[0] && colors[1] == mDiffuse[1] && colors[2] == mDiffuse[2]) {
            return;
        }
        material.setDiffuseColorNoAlpha(colors);
        sprite.updateMaterial();
    }
    
    @Override
    public String toString() {
        return "DisplayScreenElement (" + this.m_element.m_cellX + ", " + this.m_element.m_cellY + ", " + this.m_element.m_cellZ + ")";
    }
    
    public final boolean checkVisibilityMask(final byte visibilityMask) {
        return (this.m_elementVisibilityMask & visibilityMask) == this.m_elementVisibilityMask;
    }
    
    static {
        Factory = new ObjectFactory();
        m_tempColor = new float[4];
        m_logger = Logger.getLogger((Class)DisplayedScreenElement.class);
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<DisplayedScreenElement>
    {
        public ObjectFactory() {
            super(DisplayedScreenElement.class);
        }
        
        @Override
        public DisplayedScreenElement create() {
            return new DisplayedScreenElement();
        }
    }
}
