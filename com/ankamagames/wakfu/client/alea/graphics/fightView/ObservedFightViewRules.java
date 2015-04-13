package com.ankamagames.wakfu.client.alea.graphics.fightView;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import java.util.*;

final class ObservedFightViewRules implements FightViewRules
{
    private static Logger m_logger;
    
    @Override
    public void onFighterJoinFight(final CharacterInfo character) {
        character.getActor().enableAlphaMask(true);
        if (character.isInvisibleForLocalPlayer()) {
            return;
        }
        character.getActor().setVisible(true);
    }
    
    @Override
    public void onFighterLeaveFight(final CharacterInfo character) {
        FightViewUtils.showCharacter(character);
    }
    
    @Override
    public void onBasicEffectAreaAdded(final FightInfo fightInfo, final BasicEffectArea effectArea) {
        FightViewUtils.showEffectAreaIfNecessary(fightInfo, effectArea);
    }
    
    @Override
    public void onBasicEffectAreaTeleported(final FightInfo fightInfo, final BasicEffectArea effectArea) {
        StaticEffectAreaDisplayer.getInstance().removeStaticEffectArea(effectArea);
        StaticEffectAreaDisplayer.getInstance().addStaticEffectArea(effectArea);
    }
    
    @Override
    public void onBasicEffectAreaRemoved(final FightInfo fightInfo, final BasicEffectArea effectArea) {
        FightViewUtils.hideEffectArea(effectArea);
    }
    
    @Override
    public boolean isParticuleVisibleForFight() {
        return true;
    }
    
    @Override
    public boolean canDisplayFlyingValue() {
        return true;
    }
    
    @Override
    public void onExternalFightCreation(final ExternalFightInfo externalFightInfo) {
        ObservedFightViewRules.m_logger.error((Object)"[Fight][View] On ne doit pas demander de regles de visualisation pour un combat externe en train d'etre cr\u00e9er");
    }
    
    @Override
    public void updateFightVisibility(final FightInfo fightInfo) {
        FightViewUtils.fadeInLightModifier(fightInfo);
        FightViewUtils.showFightEffectAreas(fightInfo);
        FightViewUtils.updateFloorItemsViews();
        final Collection<CharacterInfo> fighters = fightInfo.getFighters();
        for (final CharacterInfo fighter : fighters) {
            if (fighter.isInvisibleForLocalPlayer()) {
                fighter.getActor().setVisible(false);
            }
            else {
                FightViewUtils.showCharacter(fighter);
                fighter.getActor().addPassiveTeamParticleSystem(fighter.getTeamId());
                fighter.getActor().addDirectionParticleSystem(fighter.getDirection());
                fighter.getActor().enableAlphaMask(true);
            }
        }
        final Iterator<ClientInteractiveAnimatedElementSceneView> it = AnimatedElementSceneViewManager.getInstance().getDisplayedElementIterator();
        while (it.hasNext()) {
            final ClientInteractiveAnimatedElementSceneView ie = it.next();
            ie.setVisible(ie.isVisible() && ie.getInteractiveElement().isVisible());
        }
        FightViewUtils.activateFightObservationView(fightInfo);
        if (fightInfo instanceof Fight) {
            ((Fight)fightInfo).setHideExternalCharacter(true);
        }
    }
    
    static {
        ObservedFightViewRules.m_logger = Logger.getLogger((Class)ObservedFightViewRules.class);
    }
}
