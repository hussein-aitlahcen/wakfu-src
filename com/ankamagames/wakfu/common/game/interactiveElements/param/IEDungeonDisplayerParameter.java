package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.chaos.*;

public class IEDungeonDisplayerParameter extends IEParameter
{
    private final int m_dungeonId;
    
    public IEDungeonDisplayerParameter(final int paramId, final int visualId, final int dungeonId, final ChaosInteractiveCategory chaosInteractiveCategory, final int chaosCollectorParamId) {
        super(paramId, visualId, chaosInteractiveCategory, chaosCollectorParamId);
        this.m_dungeonId = dungeonId;
    }
    
    public int getDungeonId() {
        return this.m_dungeonId;
    }
}
