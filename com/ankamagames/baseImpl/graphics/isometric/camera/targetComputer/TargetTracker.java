package com.ankamagames.baseImpl.graphics.isometric.camera.targetComputer;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.isometric.*;

public class TargetTracker implements CameraTargetComputer
{
    private static final Logger m_logger;
    
    @Override
    public Point2 getScreenPosition(final IsoWorldScene scene, final IsoCamera isoCamera) {
        final IsoWorldTarget target = isoCamera.getTrackingTarget();
        return IsoCameraFunc.getScreenPosition(scene, target.getWorldX(), target.getWorldY(), target.getAltitude());
    }
    
    static {
        m_logger = Logger.getLogger((Class)TargetTracker.class);
    }
}
