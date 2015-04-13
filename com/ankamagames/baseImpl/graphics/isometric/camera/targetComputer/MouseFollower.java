package com.ankamagames.baseImpl.graphics.isometric.camera.targetComputer;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.constraint.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class MouseFollower implements CameraTargetComputer
{
    private static final Logger m_logger;
    private CameraConstraint m_cameraConstraint;
    private int m_mouseX;
    private int m_mouseY;
    private final int m_startMouseX;
    private final int m_startMouseY;
    private final float m_startCameraScreenX;
    private final float m_startCameraScreenY;
    
    public MouseFollower(final AleaIsoCamera isoCamera, final int mouseX, final int mouseY) {
        super();
        this.m_startCameraScreenX = isoCamera.getScreenFloatX();
        this.m_startCameraScreenY = isoCamera.getScreenFloatY();
        this.setLocation(this.m_startMouseX = mouseX, this.m_startMouseY = mouseY);
    }
    
    public void setCameraConstraint(final CameraConstraint cameraConstraint) {
        this.m_cameraConstraint = cameraConstraint;
    }
    
    public void setLocation(final int mouseX, final int mouseY) {
        this.m_mouseX = mouseX;
        this.m_mouseY = mouseY;
    }
    
    @Override
    public Point2 getScreenPosition(final IsoWorldScene scene, final IsoCamera isoCamera) {
        final float sensibility = 1.0f / isoCamera.getZoomFactor();
        final Point2 delta = this.computeDeltaMouse(sensibility);
        final float maxDistance = 500.0f;
        delta.m_x = MathHelper.clamp(delta.m_x, -500.0f, 500.0f);
        delta.m_y = MathHelper.clamp(delta.m_y, -500.0f, 500.0f);
        Point2 point2 = delta;
        point2.m_x += this.m_startCameraScreenX;
        point2 = delta;
        point2.m_y += this.m_startCameraScreenY;
        return delta;
    }
    
    private Point2 computeDeltaMouse(final float sensibility) {
        float dx = this.m_mouseX - this.m_startMouseX;
        float dy = -(this.m_mouseY - this.m_startMouseY);
        dx = -dx;
        dy = -dy;
        dx *= sensibility;
        dy *= sensibility;
        return new Point2(dx, dy);
    }
    
    static {
        m_logger = Logger.getLogger((Class)MouseFollower.class);
    }
}
