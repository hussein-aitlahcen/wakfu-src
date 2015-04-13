package com.ankamagames.wakfu.client.core.game.protector.event;

import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.game.protector.snapshot.*;

public class ProtectorDefended extends ProtectorEvent
{
    private Nation m_attackingNation;
    
    public void setAttackingNation(final Nation attackingNation) {
        this.m_attackingNation = attackingNation;
    }
    
    public Nation getAttackingNation() {
        return this.m_attackingNation;
    }
    
    @Override
    public ProtectorMood getProtectorMood() {
        return ProtectorMood.HAPPY;
    }
}
