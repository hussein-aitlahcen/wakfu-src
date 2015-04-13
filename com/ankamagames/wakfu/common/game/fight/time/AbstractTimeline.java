package com.ankamagames.wakfu.common.game.fight.time;

import com.ankamagames.wakfu.common.datas.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.nodes.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;
import com.ankamagames.wakfu.common.game.fight.time.timescore.*;
import gnu.trove.*;
import java.io.*;

public abstract class AbstractTimeline<F extends BasicCharacterInfo> extends TurnBasedTimeline
{
    protected final InitProvider m_initProvider;
    private boolean m_dynamicTimePointGap;
    @NotNull
    private final TimeScoreGauges m_timeScoreGauges;
    @NotNull
    protected final FightersInformationProvider<F> m_fightersInformationProvider;
    TLongObjectHashMap<FighterToken> m_storedOffPlayFighterTokens;
    
    protected AbstractTimeline(@NotNull final TFight owner, @NotNull final TimeScoreGauges timeScoreGauges, @NotNull final InitProvider initProvider, @NotNull final FighterSortingStrategy sortingStrategy, @NotNull final TimelineNodes nodes) {
        super(((BasicFightInfo)owner).getId(), nodes, (TimeEventHandler)owner, sortingStrategy);
        this.m_dynamicTimePointGap = false;
        this.m_storedOffPlayFighterTokens = new TLongObjectHashMap<FighterToken>();
        this.m_timeScoreGauges = timeScoreGauges;
        this.m_fightersInformationProvider = (FightersInformationProvider<F>)owner;
        this.m_initProvider = initProvider;
    }
    
    @Override
    protected TLongArrayList getTurnOrder() {
        return super.getTurnOrder();
    }
    
    @Override
    public void sortInitially() {
        super.sortInitially();
        this.computeTimePointGap();
    }
    
    private void computeTimePointGap() {
        this.getTimeScoreGauges().setTimePointGap(TimescoreRules.calculateTimePointGap());
        this.onTimePointGapChanged();
    }
    
    protected void onTimePointGapChanged() {
    }
    
    public TLongArrayList getDynamicOrder() {
        return this.m_nodes.getOrderNextTurn();
    }
    
    public abstract void startAction();
    
    @Override
    protected void onNewTableTurn() {
    }
    
    @Override
    public void addFighter(final long fighterIdToAdd, final boolean playThisTurn) {
        if (this.m_storedOffPlayFighterTokens.containsKey(fighterIdToAdd)) {
            AbstractTimeline.m_logger.error((Object)this.withFightIdAndState("\u00c9chec de l'ajout \u00e0 la Timeline de " + fighterIdToAdd + " (pr\u00e9sent dans les fighters stock\u00e9s)"));
            this.unShelveFighter(fighterIdToAdd);
            return;
        }
        super.addFighter(fighterIdToAdd, playThisTurn);
    }
    
    @Override
    public void shelveFighter(final long fighterId) {
        if (!this.m_nodes.contains(fighterId)) {
            AbstractTimeline.m_logger.error((Object)this.withFightIdAndState("\u00c9chec du stockage des \u00e9v\u00e9nements pour " + fighterId + " : fighter absent"));
            return;
        }
        short lastTurnPlayed = this.m_nodes.getLastTurnPlayed(fighterId);
        if (this.isFighterActive(fighterId)) {
            ++lastTurnPlayed;
        }
        final FighterToken fighterToken = new FighterToken(lastTurnPlayed);
        this.m_timelineEvents.fighterIsShelved(fighterId);
        this.m_storedOffPlayFighterTokens.put(fighterId, fighterToken);
        super.removeFighter(fighterId);
    }
    
    public void unShelveFighter(final long fighterId) {
        if (!this.m_storedOffPlayFighterTokens.containsKey(fighterId)) {
            AbstractTimeline.m_logger.error((Object)this.withFightIdAndState("\u00c9chec de restauration des \u00e9v\u00e9nements pour " + fighterId + " : absent des fighters stock\u00e9s"));
            return;
        }
        this.m_timelineEvents.fighterUnshelved(fighterId);
        this.m_storedOffPlayFighterTokens.remove(fighterId);
        this.addFighter(fighterId, true);
    }
    
    @Override
    public void removeFighter(final long fighterId) {
        if (!this.m_storedOffPlayFighterTokens.containsKey(fighterId)) {
            AbstractTimeline.m_logger.error((Object)this.withFightIdAndState("\u00c9chec du retrait de la Timeline de " + fighterId + " : absent des fighters stock\u00e9s"));
            return;
        }
        this.m_storedOffPlayFighterTokens.remove(fighterId);
    }
    
    @Override
    public short howLongInTurnsUntil(final RelativeFightTime endTime) {
        if (this.m_nodes.contains(endTime.getFighterId())) {
            return super.howLongInTurnsUntil(endTime);
        }
        int result = endTime.getTableTurn() - this.m_tableTurn;
        if (endTime.isAtEndOfTurn()) {
            ++result;
        }
        if (this.m_storedOffPlayFighterTokens.containsKey(endTime.getFighterId()) && !this.m_storedOffPlayFighterTokens.get(endTime.getFighterId()).willPlayIfUnshelvedAtTurn(this.m_tableTurn)) {
            --result;
        }
        return (short)result;
    }
    
    @Override
    public int serializedSize() {
        int size = super.serializedSize() + 1;
        for (final long fighterId : this.m_storedOffPlayFighterTokens.keys()) {
            size += 8 + this.m_storedOffPlayFighterTokens.get(fighterId).serializedSize();
        }
        final int nbFighters = this.m_fightersInformationProvider.getFighters().size();
        size += 1 + nbFighters * 12;
        return size;
    }
    
    @Override
    protected void serializeTo(final ByteBuffer bb) {
        super.serializeTo(bb);
        bb.put((byte)this.m_storedOffPlayFighterTokens.size());
        for (final long fighterId : this.m_storedOffPlayFighterTokens.keys()) {
            bb.putLong(fighterId);
            this.m_storedOffPlayFighterTokens.get(fighterId).serialize(bb);
        }
        final Collection<F> fighters = this.m_fightersInformationProvider.getFighters();
        bb.put((byte)fighters.size());
        for (final F fighter : fighters) {
            bb.putLong(fighter.getId());
            bb.putInt(this.m_timeScoreGauges.getTimeScore(fighter.getId()));
        }
    }
    
    @Override
    protected void prepareForUnserialize() {
        super.prepareForUnserialize();
        this.m_storedOffPlayFighterTokens.clear();
    }
    
    public void addFighterThisTurnOnly(final long fighterId) {
        this.m_nodes.addThisTurnOnly(fighterId, this.m_tableTurn);
    }
    
    @Override
    protected void read(final TimelineUnmarshallingContext ctx, final ByteBuffer bb) {
        super.read(ctx, bb);
        final byte numStoredFighters = bb.get();
        for (int i = 0; i < numStoredFighters; ++i) {
            final long fighterId = bb.getLong();
            final FighterToken fighterToken = FighterToken.deserialize(ctx, fighterId, bb);
            this.m_storedOffPlayFighterTokens.put(fighterId, fighterToken);
        }
        final byte nbFighters = bb.get();
        for (int j = 0; j < nbFighters; ++j) {
            final long fighterId2 = bb.getLong();
            final int timeScore = bb.getInt();
            ((TimeScoreGaugesImpl)this.m_timeScoreGauges).setTimeScore(fighterId2, timeScore);
        }
    }
    
    @Override
    public StringBuilder stateRepr() {
        final StringBuilder rv = super.stateRepr();
        rv.append(" S:[");
        if (!this.m_storedOffPlayFighterTokens.isEmpty()) {
            for (final long storedFighterId : this.m_storedOffPlayFighterTokens.keys()) {
                rv.append(storedFighterId).append(',');
            }
            rv.setLength(rv.length() - 1);
        }
        rv.append(']');
        rv.append(" E:[");
        this.getTurnOrder().forEach(new TLongProcedure() {
            @Override
            public boolean execute(final long value) {
                rv.append('(').append(value).append(':').append(AbstractTimeline.this.getTimeScoreGauges().getTimeScore(value)).append("),");
                return true;
            }
        });
        rv.setLength(rv.length() - 1);
        rv.append("]/");
        rv.append(this.m_timeScoreGauges.getTimePointGap());
        rv.append(" I:[");
        this.getDynamicOrder().forEach(new TLongProcedure() {
            @Override
            public boolean execute(final long value) {
                rv.append('(').append(value).append(" | ").append(AbstractTimeline.this.m_initProvider.getInit(value)).append("),");
                return true;
            }
        });
        rv.setLength(rv.length() - 1);
        rv.append(']');
        return rv;
    }
    
    @NotNull
    public TimeScoreGauges getTimeScoreGauges() {
        return this.m_timeScoreGauges;
    }
    
    public void forceTimePointGap(final int newGap) {
        this.m_dynamicTimePointGap = false;
        this.m_timeScoreGauges.setTimePointGap(newGap);
        this.onTimePointGapChanged();
    }
}
