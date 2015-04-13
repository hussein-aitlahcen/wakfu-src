package com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers;

import com.ankamagames.baseImpl.graphics.alea.display.*;
import java.util.*;

public final class ScreenWorldHelper extends WorldCache<ScreenMap>
{
    private boolean m_clearMapOnRemove;
    
    public ScreenWorldHelper() {
        super(new DistanceComparator<Map<ScreenMap>>() {
            @Override
            protected ScreenMap getScreenMap(final Map<ScreenMap> map) {
                return map.m_map;
            }
        });
        this.m_clearMapOnRemove = true;
    }
    
    @Override
    protected void onRemove(final ScreenMap toClean) {
        if (this.m_clearMapOnRemove) {
            toClean.clear();
        }
    }
    
    @Override
    public void clear() {
        for (final ScreenMap mapsCached : this.m_mapsCached) {
            mapsCached.clear();
        }
        super.clear();
    }
    
    public void clearMapOnRemove(final boolean clearOnRemove) {
        this.m_clearMapOnRemove = clearOnRemove;
    }
}
