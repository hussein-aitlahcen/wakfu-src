package com.ankamagames.wakfu.client.alea.graphics.fightView;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.client.core.game.fight.*;

final class ShowExternalFightsViewRules implements FightViewRules
{
    private boolean m_isParticuleVisible;
    private boolean m_isBorderVisible;
    
    ShowExternalFightsViewRules() {
        super();
        this.m_isParticuleVisible = true;
        this.m_isBorderVisible = false;
    }
    
    @Override
    public void onFighterJoinFight(final CharacterInfo character) {
        FightViewUtils.veilCharacter(character);
    }
    
    @Override
    public void onFighterLeaveFight(final CharacterInfo character) {
        FightViewUtils.showCharacter(character);
    }
    
    @Override
    public void onBasicEffectAreaAdded(final FightInfo fightInfo, final BasicEffectArea effectArea) {
    }
    
    @Override
    public void onBasicEffectAreaTeleported(final FightInfo fightInfo, final BasicEffectArea effectArea) {
    }
    
    @Override
    public void onBasicEffectAreaRemoved(final FightInfo fightInfo, final BasicEffectArea effectArea) {
    }
    
    public void setParticuleVisible(final boolean particuleVisible) {
        this.m_isParticuleVisible = particuleVisible;
    }
    
    @Override
    public boolean isParticuleVisibleForFight() {
        return this.m_isParticuleVisible;
    }
    
    public void setBorderVisible(final boolean isBorderVisible) {
        this.m_isBorderVisible = isBorderVisible;
    }
    
    @Override
    public boolean canDisplayFlyingValue() {
        return false;
    }
    
    @Override
    public void onExternalFightCreation(final ExternalFightInfo externalFightInfo) {
        this.updateFightVisibility(externalFightInfo);
    }
    
    @Override
    public void updateFightVisibility(final FightInfo fightInfo) {
        FightViewUtils.changeFightersVisibility(fightInfo, VisibilityType.VEIL);
        FightViewUtils.removeFightersTeamCircle(fightInfo);
        FightViewUtils.updateFloorItemsViews();
        if (this.m_isBorderVisible) {
            FightViewUtils.fadeInLightModifier(fightInfo);
            FightViewUtils.showFightEffectAreas(fightInfo);
        }
        else {
            FightViewUtils.fadeOutLightModifier(fightInfo);
            FightViewUtils.hideFightEffectAreas(fightInfo);
        }
        FightViewUtils.removeFightRepresentation(fightInfo);
    }
}
