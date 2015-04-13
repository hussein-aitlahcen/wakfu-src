package com.ankamagames.wakfu.client.alea.graphics.havenWorldMini;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public class MiniSpriteFactory
{
    private static final Logger m_logger;
    public static final float NUM_BY_WIDTH = 4.0f;
    public static final float NUM_BY_HEIGHT = 4.0f;
    private static final TextureManager m_forPatch;
    private static final TextureManager m_forBuilding;
    
    public static void setGfxPath(final String gfxPathPatch, final String gfxPathBuilding) {
        MiniSpriteFactory.m_forPatch.setGfxPath(gfxPathPatch);
        MiniSpriteFactory.m_forBuilding.setGfxPath(gfxPathBuilding);
    }
    
    static void addPatch(final EntityGroup group, final int patchId, final int patchCoordX, final int patchCoordY, final float offsetZ, final Point2i texOffset) {
        final Texture texture = MiniSpriteFactory.m_forPatch.getTexture(patchId);
        final float top = getScreenY(patchCoordX, patchCoordY);
        float left = getScreenX(patchCoordX, patchCoordY);
        left -= WakfuClientInstance.getInstance().getWorldScene().getCellHeight() / 4.0f;
        try {
            addSprite(group, texture, top, left, offsetZ, texOffset);
        }
        catch (NullPointerException e) {
            MiniSpriteFactory.m_logger.error((Object)("probl\u00e8me avec le patch " + patchId), (Throwable)e);
        }
    }
    
    static void addBuilding(final EntityGroup group, final int buildingId, final int cellX, final int cellY, final float offsetZ, final Point2i texOffset) {
        final Texture texture = MiniSpriteFactory.m_forBuilding.getTexture(buildingId);
        final float cx = cellX / 9.0f;
        final float cy = cellY / 9.0f;
        final float top = getScreenY(cx, cy);
        final float left = getScreenX(cx, cy);
        try {
            addSprite(group, texture, top, left, offsetZ, texOffset);
        }
        catch (NullPointerException e) {
            MiniSpriteFactory.m_logger.error((Object)("probl\u00e8me avec le building " + buildingId), (Throwable)e);
        }
    }
    
    private static float getScreenX(final float patchCoordX, final float patchCoordY) {
        return (patchCoordX - patchCoordY) * getPatchScreenHalfWidth();
    }
    
    private static float getScreenY(final float patchCoordX, final float patchCoordY) {
        return -(patchCoordX + patchCoordY) * getPatchScreenHalfHeight() + WakfuClientInstance.getInstance().getWorldScene().getCellHeight() * 0.5f;
    }
    
    private static void addSprite(final EntityGroup entity, final Texture texture, float top, float left, final float offsetZ, final Point2i texOffset) throws NullPointerException {
        if (texture == null || texOffset == null) {
            throw new NullPointerException();
        }
        final AleaWorldSceneWithParallax scene = WakfuClientInstance.getInstance().getWorldScene();
        left += scene.isoToScreenX(entity.m_cellX, entity.m_cellY);
        top += scene.isoToScreenY(entity.m_cellX, entity.m_cellY) + offsetZ;
        left += texOffset.getX() / 4.0f / 9.0f;
        top += texOffset.getY() / 4.0f / 9.0f;
        final Point2i size = texture.getSize();
        final EntitySprite sprite = createSprite(texture);
        sprite.setBounds(top, left, size.getX() + 1, size.getY() + 1);
        sprite.m_minX = MathHelper.fastFloor(left);
        sprite.m_minY = MathHelper.fastFloor(top);
        sprite.m_maxX = MathHelper.fastCeil(left + size.getX());
        sprite.m_maxY = MathHelper.fastCeil(top - size.getY());
        entity.addChild(sprite);
        sprite.removeReference();
    }
    
    private static EntitySprite createSprite(final Texture texture) {
        final Point2i textureSize = texture.getSize();
        final Point2i size = texture.getPowerOfTwoSize();
        final GLGeometrySprite geom = GLGeometrySprite.Factory.newPooledInstance();
        geom.setBlendFunc(BlendModes.One, BlendModes.InvSrcAlpha);
        geom.setBounds(0.0f, 0.0f, textureSize.getX(), textureSize.getY());
        geom.setTextureCoordinates(0.0f, 0.0f, textureSize.getY() / size.getY(), textureSize.getX() / size.getX());
        final EntitySprite sprite = EntitySprite.Factory.newPooledInstance();
        sprite.setGeometry(geom);
        sprite.setTexture(texture);
        sprite.removeEffectForWorld();
        sprite.setColor(0.5f, 0.5f, 0.5f, 1.0f);
        geom.removeReference();
        return sprite;
    }
    
    private static float getPatchScreenHalfWidth() {
        final AleaWorldSceneWithParallax scene = WakfuClientInstance.getInstance().getWorldScene();
        return 0.5f * scene.getCellWidth() / 4.0f;
    }
    
    private static float getPatchScreenHalfHeight() {
        final AleaWorldSceneWithParallax scene = WakfuClientInstance.getInstance().getWorldScene();
        return 0.5f * scene.getCellHeight() / 4.0f;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MiniSpriteFactory.class);
        m_forPatch = new TextureManager();
        m_forBuilding = new TextureManager();
    }
    
    private static class TextureManager extends AleaTextureManager
    {
        protected TextureManager() {
            super(".tga", true);
        }
    }
}
