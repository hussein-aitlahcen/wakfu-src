package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.*;

final class DelayedFightPreview implements MessageFrame
{
    private FightInfo m_fightInfo;
    
    public void setFightInfo(final FightInfo fightInfo) {
        this.m_fightInfo = fightInfo;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (message instanceof ClockMessage) {
            this.displayFightPreview();
            return false;
        }
        return true;
    }
    
    private void displayFightPreview() {
        if (this.m_fightInfo == null) {
            return;
        }
        final Collection<CharacterInfo> fighters = this.m_fightInfo.getFighters();
        for (final CharacterInfo fighter : fighters) {
            fighter.getActor().addPassiveTeamParticleSystem(fighter.getTeamId());
        }
    }
    
    void removeFightPreview() {
        MessageScheduler.getInstance().removeAllClocks(this);
        if (this.m_fightInfo == null) {
            return;
        }
        final Fight currentOrObservedFight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOrObservedFight();
        if (currentOrObservedFight != null && this.m_fightInfo.getId() == currentOrObservedFight.getId()) {
            return;
        }
        final Collection<CharacterInfo> fighters = this.m_fightInfo.getFighters();
        for (final CharacterInfo fighter : fighters) {
            fighter.getActor().clearTeamParticleSystem();
        }
    }
    
    @Override
    public long getId() {
        return 176L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
}
