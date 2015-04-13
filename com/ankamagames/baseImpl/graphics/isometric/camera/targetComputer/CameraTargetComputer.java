package com.ankamagames.baseImpl.graphics.isometric.camera.targetComputer;

import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.kernel.core.maths.*;

public interface CameraTargetComputer
{
    Point2 getScreenPosition(IsoWorldScene p0, IsoCamera p1);
}
