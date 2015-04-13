package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.chaos.*;

public abstract class IEParameter implements ChaosIEParameter
{
    private final int m_id;
    private final int m_visualId;
    private final ChaosInteractiveCategory m_chaosCategory;
    private final int m_chaosCollectorParamId;
    
    protected IEParameter(final int paramId, final int visualId, final ChaosInteractiveCategory chaosCategory, final int chaosCollectorId) {
        super();
        this.m_id = paramId;
        this.m_visualId = visualId;
        this.m_chaosCategory = chaosCategory;
        this.m_chaosCollectorParamId = chaosCollectorId;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public int getVisualId() {
        return this.m_visualId;
    }
    
    @Override
    public ChaosInteractiveCategory getChaosCategory() {
        return this.m_chaosCategory;
    }
    
    @Override
    public int getChaosCollectorParamId() {
        return this.m_chaosCollectorParamId;
    }
}
