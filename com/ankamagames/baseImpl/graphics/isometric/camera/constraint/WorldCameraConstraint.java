package com.ankamagames.baseImpl.graphics.isometric.camera.constraint;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class WorldCameraConstraint implements CameraConstraint
{
    private static final Logger m_logger;
    private final AleaIsoCamera m_camera;
    private final AnimatedElement m_target;
    
    public WorldCameraConstraint(final AnimatedElement target, final AleaIsoCamera camera) {
        super();
        this.m_target = target;
        this.m_camera = camera;
    }
    
    @Override
    public void clampMouseMove(final float centerScreenX, final float centerScreenY, final Point2 delta) {
        final Rect rect = computeRect(this.m_camera, centerScreenX, centerScreenY, delta);
        if (rect == null) {
            return;
        }
        final int targetX = (int)centerScreenX;
        this.m_target.getScreenX();
        final int targetY = (int)centerScreenY;
        this.m_target.getScreenY();
        if (targetX < rect.m_xMin) {
            delta.m_x -= rect.m_xMin - targetX;
        }
        if (targetX > rect.m_xMax) {
            delta.m_x -= rect.m_xMax - targetX;
        }
        if (targetY < rect.m_yMin) {
            delta.m_y -= rect.m_yMin - targetY;
        }
        if (targetY > rect.m_yMax) {
            delta.m_y -= rect.m_yMax - targetY;
        }
    }
    
    static Rect computeRect(final AleaIsoCamera camera, final float centerScreenX, final float centerScreenY, final Point2 delta) {
        final Rect rect = new Rect();
        final float newCenterX = centerScreenX + delta.m_x;
        final float newCenterY = centerScreenY + delta.m_y;
        camera.computeClipPlanes(newCenterX, newCenterY, rect);
        if (rect.width() < 100 && rect.height() < 100) {
            WorldCameraConstraint.m_logger.error((Object)"la vue est trop petite)");
            delta.set(0.0f, 0.0f);
            return null;
        }
        return rect;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WorldCameraConstraint.class);
    }
}
