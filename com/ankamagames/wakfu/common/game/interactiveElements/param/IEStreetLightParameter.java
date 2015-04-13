package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.chaos.*;

public class IEStreetLightParameter extends IEParameter implements ChaosIEParameter
{
    public static final short STATE_OFF = 0;
    public static final short STATE_ON = 1;
    private final int m_color;
    private final float m_range;
    private final int m_apsId;
    private boolean m_nightOnly;
    private final int m_ignitionVisualId;
    private final boolean m_ignitionUseObject;
    private final int m_ignitionDuration;
    private final int m_extinctionVisualId;
    private final boolean m_extinctionUseObject;
    private final int m_extinctionDuration;
    
    public IEStreetLightParameter(final int paramId, final int color, final float range, final int apsId, final boolean nightOnly, final int ignitionVisualId, final boolean ignitionUseObject, final int ignitionDuration, final int extinctionVisualId, final boolean extinctionUseObject, final int extinctionDuration, final ChaosInteractiveCategory chaosCategory, final int chaosCollectorParamId) {
        super(paramId, 0, chaosCategory, chaosCollectorParamId);
        this.m_color = color;
        this.m_range = range;
        this.m_apsId = apsId;
        this.m_nightOnly = nightOnly;
        this.m_ignitionVisualId = ignitionVisualId;
        this.m_ignitionUseObject = ignitionUseObject;
        this.m_ignitionDuration = ignitionDuration;
        this.m_extinctionVisualId = extinctionVisualId;
        this.m_extinctionUseObject = extinctionUseObject;
        this.m_extinctionDuration = extinctionDuration;
    }
    
    public int getColor() {
        return this.m_color;
    }
    
    public float getRange() {
        return this.m_range;
    }
    
    public int getApsId() {
        return this.m_apsId;
    }
    
    public boolean isNightOnly() {
        return this.m_nightOnly;
    }
    
    public int getIgnitionVisualId() {
        return this.m_ignitionVisualId;
    }
    
    public boolean isIgnitionUseObject() {
        return this.m_ignitionUseObject;
    }
    
    public int getIgnitionDuration() {
        return this.m_ignitionDuration;
    }
    
    public int getExtinctionVisualId() {
        return this.m_extinctionVisualId;
    }
    
    public boolean isExtinctionUseObject() {
        return this.m_extinctionUseObject;
    }
    
    public int getExtinctionDuration() {
        return this.m_extinctionDuration;
    }
}
