package com.ankamagames.wakfu.common.game.fight;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.time.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.protagonists.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.turnBased.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;

public abstract class WakfuTurnBasedFight<F extends BasicCharacterInfo> extends BasicFight<F>
{
    protected WakfuTurnBasedFight(final int id, final FightModel model, final FightMap fightMap) {
        super(id, model, fightMap);
    }
    
    @Override
    public AbstractTimeline getTimeline() {
        return this.m_timeline;
    }
    
    public void setTurnDurationInMilliSecond(final int turnDurationInMilliSecond) {
        if (this.getTimeline() != null) {
            this.getTimeline().setCurrentTurnDurationInMillisecond(this.getDurationForFighterCurrentTurn(0L));
        }
    }
    
    public void forceTimePointGap(final int newGap) {
        if (this.getTimeline() != null) {
            this.getTimeline().forceTimePointGap(newGap);
        }
    }
    
    public int getDurationForFighterCurrentTurn(final long fighterId) {
        if (this.getTimeline() != null) {
            final boolean fighterAllowedToSelectBonus = this.getTimeline().getTimeScoreGauges().isFighterAllowedToSelectBonus(fighterId);
            return this.getTimeline().getCurrentTurnDurationInMillisecond() + (fighterAllowedToSelectBonus ? 30000 : 0);
        }
        return -1;
    }
    
    public void askForTableTurnBegin() {
        if (this.getTimeline() != null && this.getTimeline().isRunning()) {
            this.getTimeline().askForStartTurn();
        }
    }
    
    public boolean askForFighterTurnBegin(final F fighter) {
        if (!this.checkTimelineIsRunning()) {
            return false;
        }
        final boolean success = this.getTimeline().askForFighterStartTurn(fighter.getId());
        if (!success) {
            WakfuTurnBasedFight.m_logger.error((Object)this.withFightId("Impossible de d\u00e9buter le tour de " + fighter.getId()));
        }
        return success;
    }
    
    protected boolean checkTimelineIsRunning() {
        if (this.getTimeline() == null || !this.getTimeline().isRunning()) {
            WakfuTurnBasedFight.m_logger.error((Object)this.withFightId("timeline null ou arret\u00e9e"));
            return false;
        }
        return true;
    }
    
    public boolean askForFighterTurnEnd(final F fighter) {
        return fighter != null && this.askForFighterTurnEnd(fighter.getId());
    }
    
    public boolean askForFighterTurnEnd(final long fighterId) {
        if (!this.checkTimelineIsRunning()) {
            return false;
        }
        final boolean success = this.getTimeline().askForFighterEndTurn(fighterId);
        if (!success) {
            WakfuTurnBasedFight.m_logger.error((Object)this.withFightId("Impossible de terminer le tour de " + fighterId));
        }
        return success;
    }
    
    protected final void onFighterStartTurn(final long fighterId) {
        final F fighter = this.getFighterFromId(fighterId);
        if (fighter != null) {
            this.onFighterStartTurn(fighter);
        }
    }
    
    protected void onFighterStartTurn(@NotNull final F fighter) {
        fighter.onSpecialFighterEvent(new StartTurnEvent());
    }
    
    protected void onFighterEndTurn(final long fighterId) {
        final F fighter = this.getFighterFromId(fighterId);
        if (fighter != null) {
            this.onFighterEndTurn(fighter);
        }
    }
    
    protected void onFighterEndTurn(@NotNull final F fighter) {
        try {
            fighter.onSpecialFighterEvent(new EndTurnEvent());
        }
        catch (Exception e) {
            WakfuTurnBasedFight.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    public void onTableTurnBegin() {
        this.checkFightEnd();
        for (final F f : this.m_protagonists.getFighters(ProtagonistFilter.offPlay())) {
            if (f != null) {
                f.onSpecialFighterEvent(new TableTurnEvent());
                final FighterCharacteristic ko = f.getCharacteristic((CharacteristicType)FighterCharacteristicType.KO_TIME_BEFORE_DEATH);
                ko.substract(1);
            }
        }
    }
    
    public void onTableTurnEnd() {
        this.checkFightEnd();
    }
}
