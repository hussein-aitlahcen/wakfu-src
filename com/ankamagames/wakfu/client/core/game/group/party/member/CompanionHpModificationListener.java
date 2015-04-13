package com.ankamagames.wakfu.client.core.game.group.party.member;

import com.ankamagames.wakfu.client.core.game.group.party.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public final class CompanionHpModificationListener implements CompanionModelListener
{
    private final PartyDisplayer.PartyMemberDisplayer m_displayer;
    
    public CompanionHpModificationListener(final PartyDisplayer.PartyMemberDisplayer displayer) {
        super();
        this.m_displayer = displayer;
    }
    
    @Override
    public void xpChanged(final CompanionModel model, final long previousXp) {
    }
    
    @Override
    public void nameChanged(final CompanionModel model) {
    }
    
    @Override
    public void idChanged(final CompanionModel model) {
    }
    
    @Override
    public void onCurrentHpChanged(final CompanionModel model) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_displayer, "hpDescription", "hpPercentage");
    }
    
    @Override
    public void onMaxHpChanged(final CompanionModel model) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_displayer, "hpDescription", "hpPercentage");
    }
    
    @Override
    public void onUnlockedChanged(final CompanionModel companionModel) {
    }
}
