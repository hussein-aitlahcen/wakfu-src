package com.ankamagames.wakfu.client.core.game.fight.time;

import com.ankamagames.wakfu.common.game.fight.handler.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.datas.*;

public final class TimelineFightListener implements FightListener<CharacterInfo>
{
    private final Timeline m_timeline;
    
    public TimelineFightListener(final Timeline timeline) {
        super();
        this.m_timeline = timeline;
    }
    
    @Override
    public void onPlacementStart() {
    }
    
    @Override
    public void onPlacementEnd() {
    }
    
    @Override
    public void onFightStart() {
    }
    
    @Override
    public void onFightEnd() {
    }
    
    @Override
    public void onTableTurnStart() {
    }
    
    @Override
    public void onTableTurnEnd() {
    }
    
    @Override
    public void onFighterStartTurn(final CharacterInfo fighter) {
    }
    
    @Override
    public void onFighterEndTurn(final CharacterInfo fighter) {
    }
    
    @Override
    public void onFighterJoinFight(final CharacterInfo fighter) {
        this.m_timeline.updateUIForAddedFighter(fighter);
    }
    
    @Override
    public void onFighterOutOfPlay(final CharacterInfo fighter) {
        this.m_timeline.updateUIForRemovedFighter(fighter);
    }
    
    @Override
    public void onFighterWinFight(final CharacterInfo fighter) {
    }
    
    @Override
    public void onFighterLoseFight(final CharacterInfo fighter) {
    }
    
    @Override
    public void onFighterCastSpell(final CharacterInfo caster, final AbstractSpell spell) {
    }
    
    @Override
    public void onEffectAreaGoesOffPlay(final AbstractEffectArea area) {
    }
    
    @Override
    public void onFighterRemovedFromFight(final CharacterInfo fighter) {
    }
    
    @Override
    public void onFightEnded() {
    }
}
