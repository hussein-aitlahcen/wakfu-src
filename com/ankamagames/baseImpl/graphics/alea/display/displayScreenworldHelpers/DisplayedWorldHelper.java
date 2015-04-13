package com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers;

import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public class DisplayedWorldHelper extends WorldCache<DisplayedScreenMap>
{
    public static byte EXPORT_MASK;
    public static final int NOT_VISIBLE_GFX_ID = 19067;
    private final DisplayedScreenElementFactory m_factory;
    
    public DisplayedWorldHelper(@NotNull final DisplayedScreenElementFactory factory) {
        super(new DistanceComparator<Map<DisplayedScreenMap>>() {
            @Override
            protected ScreenMap getScreenMap(final Map<DisplayedScreenMap> map) {
                return map.m_map.getMap();
            }
        });
        this.m_factory = factory;
    }
    
    @Override
    public final void clear() {
        for (int i = 0; i < this.m_mapsCached.size(); ++i) {
            ((DisplayedScreenMap)this.m_mapsCached.getQuickValue(i)).clear(this.m_factory);
        }
        super.clear();
    }
    
    @Override
    protected void onRemove(final DisplayedScreenMap toClean) {
        toClean.clear(this.m_factory);
    }
    
    static {
        DisplayedWorldHelper.EXPORT_MASK = 1;
    }
}
