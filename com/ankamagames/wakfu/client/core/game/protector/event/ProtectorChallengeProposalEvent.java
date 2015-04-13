package com.ankamagames.wakfu.client.core.game.protector.event;

import com.ankamagames.wakfu.client.core.game.protector.snapshot.*;

public class ProtectorChallengeProposalEvent extends ProtectorChallengeEvent
{
    private String m_title;
    private String m_itemLink;
    
    @Override
    public ProtectorMood getProtectorMood() {
        return ProtectorMood.NEUTRAL;
    }
    
    public void setReward(final String itemLink) {
        this.m_itemLink = itemLink;
    }
    
    public void setTitle(final String title) {
        this.m_title = title;
    }
    
    @Override
    public String[] getParams() {
        final String[] params = { this.m_title, this.m_itemLink };
        return params;
    }
}
