package com.ankamagames.baseImpl.graphics.isometric.highlight;

import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.kernel.core.maths.*;

interface HighLightMeshTransformer
{
    void transformHighLightEntity(IsoWorldScene p0, HighLightEntity p1, float p2, Point2i p3, int p4, HighLightTextureApplication p5, int p6);
    
    void setZOrder(HighLightEntity p0);
}
