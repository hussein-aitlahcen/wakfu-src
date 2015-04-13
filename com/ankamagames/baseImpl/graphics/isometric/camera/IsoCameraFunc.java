package com.ankamagames.baseImpl.graphics.isometric.camera;

import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class IsoCameraFunc
{
    public static Point2 getScreenPosition(final IsoWorldScene scene, final WorldPositionable element) {
        return getScreenPosition(scene, element.getWorldX(), element.getWorldY(), element.getAltitude());
    }
    
    public static Point2 getScreenPosition(final IsoWorldScene scene, final float worldX, final float worldY, final float worldZ) {
        final float screenX = scene.isoToScreenX(worldX, worldY);
        final float screenY = scene.isoToScreenY(worldX, worldY, worldZ);
        return new Point2(screenX, screenY);
    }
    
    public static Point2 getScreenPositionFromCenter(final IsoWorldScene scene, final WorldPositionable element) {
        return getScreenPositionFromCenter(scene, element.getWorldX(), element.getWorldY(), element.getAltitude());
    }
    
    public static Point2 getScreenPositionFromCenter(final IsoWorldScene scene, final float worldX, final float worldY, final float worldZ) {
        final IsoCamera camera = scene.getIsoCamera();
        final float zoomFactor = camera.getZoomResolutionFactor();
        float screenX = scene.isoToScreenX(worldX, worldY) - camera.getScreenFloatX();
        float screenY = scene.isoToScreenY(worldX, worldY, worldZ) - camera.getScreenFloatY();
        screenX *= zoomFactor;
        screenY *= zoomFactor;
        return new Point2(screenX, screenY);
    }
    
    public static Point2 getScreenPositionFromBottomLeft(final IsoWorldScene scene, final WorldPositionable element) {
        return getScreenPositionFromBottomLeft(scene, element.getWorldX(), element.getWorldY(), element.getAltitude());
    }
    
    public static Point2 getScreenPositionFromBottomLeft(final IsoWorldScene scene, final float worldX, final float worldY, final float worldZ) {
        final Point2 screenPositionFromCenter;
        final Point2 p = screenPositionFromCenter = getScreenPositionFromCenter(scene, worldX, worldY, worldZ);
        screenPositionFromCenter.m_x += scene.getFrustumWidth() * 0.5f;
        final Point2 point2 = p;
        point2.m_y += scene.getFrustumHeight() * 0.5f;
        return p;
    }
    
    public static int getScreenHeight(final IsoWorldScene scene, final short visualHeight) {
        return (int)(visualHeight * scene.getElevationUnit() * scene.getIsoCamera().getZoomResolutionFactor());
    }
}
