package com.ankamagames.wakfu.client.core.landMarks;

import com.ankamagames.xulor2.util.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;

public class DragoLandMarkFilterElement extends LandMarkFilterElement
{
    public DragoLandMarkFilterElement(final byte id) {
        super(id);
    }
    
    @Override
    public boolean isSelected(final DisplayableMapPoint point) {
        if (!(point.getValue() instanceof InteractiveElementDef)) {
            return super.isSelected(point);
        }
        final InteractiveElementDef def = (InteractiveElementDef)point.getValue();
        return def.m_type == 47 || super.isSelected(point);
    }
}
