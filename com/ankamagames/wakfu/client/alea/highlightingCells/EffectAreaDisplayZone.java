package com.ankamagames.wakfu.client.alea.highlightingCells;

import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class EffectAreaDisplayZone extends RangeAndEffectDisplayer implements CustomTextureHighlightingProvider
{
    private static final String ZONE_EFFECT_NAME = "AreaZoneEffect";
    private static final EffectAreaDisplayZone m_instance;
    
    public static EffectAreaDisplayZone getInstance() {
        return EffectAreaDisplayZone.m_instance;
    }
    
    private EffectAreaDisplayZone() {
        super(null, null, "AreaZoneEffect", WakfuClientConstants.ZONE_EFFECT_COLOR, null, null, null, null, null, null);
    }
    
    public void refreshAreaDisplay(final AbstractEffectArea effectArea, final CharacterInfo fighter) {
        this.selectZoneEffect(effectArea, fighter, new Point3(effectArea.getWorldCellX(), effectArea.getWorldCellY(), effectArea.getWorldCellAltitude()));
    }
    
    public void refreshAreaDisplay(final AbstractEffectArea effectArea, final CharacterInfo fighter, final Point3 target) {
        this.selectZoneEffect(effectArea, fighter, target);
    }
    
    @Override
    protected RangeValidity checkValidity(final Point3 target, final CharacterInfo caster) {
        return RangeValidity.INVALID;
    }
    
    @Override
    public void update() {
    }
    
    static {
        m_instance = new EffectAreaDisplayZone();
    }
}
