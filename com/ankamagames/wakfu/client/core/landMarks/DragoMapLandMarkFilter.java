package com.ankamagames.wakfu.client.core.landMarks;

import com.ankamagames.wakfu.client.core.landMarks.agtEnum.*;

public class DragoMapLandMarkFilter extends LandMarkFilter
{
    @Override
    protected LandMarkFilterElement createFilter(final LandMarkEnum e) {
        if (e == LandMarkEnum.ZAAP) {
            return new DragoLandMarkFilterElement(e.getType());
        }
        return super.createFilter(e);
    }
    
    @Override
    protected void setSelected(final byte landMarkType, final boolean selected, final boolean updatePreferences) {
        super.setSelected(landMarkType, selected, false);
    }
}
