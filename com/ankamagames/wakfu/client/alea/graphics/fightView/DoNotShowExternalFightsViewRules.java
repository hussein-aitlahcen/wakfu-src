package com.ankamagames.wakfu.client.alea.graphics.fightView;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.client.core.game.fight.*;

final class DoNotShowExternalFightsViewRules implements FightViewRules
{
    @Override
    public void onFighterJoinFight(final CharacterInfo character) {
        FightViewUtils.hideCharacter(character);
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
    
    @Override
    public boolean isParticuleVisibleForFight() {
        return false;
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
        FightViewUtils.hideFight(fightInfo);
        FightViewUtils.addFightRepresentation((ExternalFightInfo)fightInfo);
    }
}
