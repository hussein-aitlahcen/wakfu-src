package com.ankamagames.wakfu.client.ui.protocol.message.effectArea;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.datas.specific.*;

public class UIEffectAreaMessage extends UIMessage
{
    private final AbstractEffectArea m_area;
    private Carrier m_target;
    
    public UIEffectAreaMessage(final AbstractEffectArea area) {
        super();
        this.m_area = area;
    }
    
    public Carrier getTarget() {
        return this.m_target;
    }
    
    public void setTarget(final Carrier target) {
        this.m_target = target;
    }
    
    public AbstractEffectArea getArea() {
        return this.m_area;
    }
}
