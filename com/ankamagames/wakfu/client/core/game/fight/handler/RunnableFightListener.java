package com.ankamagames.wakfu.client.core.game.fight.handler;

import com.ankamagames.wakfu.common.game.fight.handler.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public class RunnableFightListener implements FightListener
{
    public static final RunnableFightListener INSTANCE;
    private final TIntObjectHashMap<ArrayList<FightEventRunnable>> m_runnable;
    
    private RunnableFightListener() {
        super();
        this.m_runnable = new TIntObjectHashMap<ArrayList<FightEventRunnable>>();
    }
    
    public void addEventListener(final FightEventType eventType, final FightEventRunnable runnable) {
        ArrayList<FightEventRunnable> runs = this.m_runnable.get(eventType.ordinal());
        if (runs == null) {
            runs = new ArrayList<FightEventRunnable>();
            this.m_runnable.put(eventType.ordinal(), runs);
        }
        runs.add(runnable);
    }
    
    private void run(final FightEventType eventType) {
        final ArrayList<FightEventRunnable> runs = this.m_runnable.get(eventType.ordinal());
        if (runs != null) {
            for (int i = 0; i < runs.size(); ++i) {
                runs.get(i).runFightEvent();
            }
        }
    }
    
    private void run(final FightEventType eventType, final BasicCharacterInfo fighter) {
        final ArrayList<FightEventRunnable> runs = this.m_runnable.get(eventType.ordinal());
        if (runs != null) {
            for (int i = 0; i < runs.size(); ++i) {
                runs.get(i).runFighterEvent(fighter);
            }
        }
    }
    
    private void runSpellCast(final FightEventType eventType, final BasicCharacterInfo caster, final long spellElement) {
        final ArrayList<FightEventRunnable> runs = this.m_runnable.get(eventType.ordinal());
        if (runs != null) {
            for (int i = 0; i < runs.size(); ++i) {
                runs.get(i).runSpellCastEvent(caster, spellElement);
            }
        }
    }
    
    @Override
    public void onPlacementStart() {
        this.run(FightEventType.PLACEMENT_START);
    }
    
    @Override
    public void onPlacementEnd() {
        this.run(FightEventType.PLACEMENT_END);
    }
    
    @Override
    public void onFightStart() {
        this.run(FightEventType.FIGHT_START);
    }
    
    @Override
    public void onFightEnd() {
    }
    
    @Override
    public void onTableTurnStart() {
        this.run(FightEventType.TABLE_TURN_START);
    }
    
    @Override
    public void onTableTurnEnd() {
        this.run(FightEventType.TABLE_TURN_END);
    }
    
    @Override
    public void onFighterStartTurn(final BasicCharacterInfo fighter) {
        this.run(FightEventType.FIGHTER_TURN_START, fighter);
    }
    
    @Override
    public void onFighterEndTurn(final BasicCharacterInfo fighter) {
        this.run(FightEventType.FIGHTER_TURN_END, fighter);
    }
    
    @Override
    public void onFighterJoinFight(final BasicCharacterInfo fighter) {
        this.run(FightEventType.FIGHTER_JOIN_FIGHT, fighter);
    }
    
    @Override
    public void onFighterOutOfPlay(final BasicCharacterInfo fighter) {
        this.run(FightEventType.FIGHTER_LEAVE_FIGHT, fighter);
    }
    
    @Override
    public void onFighterWinFight(final BasicCharacterInfo fighter) {
        this.run(FightEventType.FIGHTER_WIN_FIGHT, fighter);
    }
    
    @Override
    public void onFighterLoseFight(final BasicCharacterInfo fighter) {
        this.run(FightEventType.FIGHTER_LOSE_FIGHT, fighter);
    }
    
    @Override
    public void onFighterCastSpell(final BasicCharacterInfo caster, final AbstractSpell spell) {
        this.runSpellCast(FightEventType.FIGHTER_CAST_SPELL, caster, spell.getElementId());
    }
    
    @Override
    public void onEffectAreaGoesOffPlay(final AbstractEffectArea area) {
    }
    
    @Override
    public void onFighterRemovedFromFight(final BasicCharacterInfo fighter) {
    }
    
    @Override
    public void onFightEnded() {
        this.run(FightEventType.FIGHT_ENDED);
        this.m_runnable.clear();
    }
    
    static {
        INSTANCE = new RunnableFightListener();
    }
}
