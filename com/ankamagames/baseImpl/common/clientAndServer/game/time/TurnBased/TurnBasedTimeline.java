package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.nodes.*;
import gnu.trove.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;
import com.ankamagames.framework.kernel.utils.*;

public abstract class TurnBasedTimeline implements BasicTimeline
{
    protected static final Logger m_logger;
    protected int m_fightId;
    protected TimelineNodes m_nodes;
    protected TimeEventHandler m_timeEventHandler;
    protected TimelineEvents m_timelineEvents;
    private FighterSortingStrategy m_fighterSortingStrategy;
    protected short m_tableTurn;
    private int m_turnCounter;
    protected int m_currentTurnDurationInMilliseconds;
    private boolean m_running;
    protected TimelineState m_state;
    protected boolean m_wasCurrentFighterRemoved;
    private long m_removedFighterId;
    
    protected TurnBasedTimeline(final int fightId, final TimelineNodes nodes, final TimeEventHandler handler, final FighterSortingStrategy sortingStrategy) {
        super();
        this.m_fightId = -1;
        this.m_timelineEvents = new TimelineEvents();
        this.m_turnCounter = 0;
        this.m_state = TimelineState.INITIAL;
        this.m_fightId = fightId;
        this.m_nodes = nodes;
        this.m_timeEventHandler = handler;
        this.m_fighterSortingStrategy = sortingStrategy;
    }
    
    protected TurnBasedTimeline(final TimeEventHandler eventHandler) {
        this(0, new TimelineNodesImpl(), eventHandler, null);
    }
    
    @Deprecated
    protected void setFighterSortingStrategy(final FighterSortingStrategy fighterSortingStrategy) {
        this.m_fighterSortingStrategy = fighterSortingStrategy;
        this.m_nodes.setDynamicLists(new DynamicLists(fighterSortingStrategy));
    }
    
    public void setCurrentTurnDurationInMillisecond(final int currentTurnDurationInMillisecond) {
        this.m_currentTurnDurationInMilliseconds = currentTurnDurationInMillisecond;
    }
    
    public int getCurrentTurnDurationInMillisecond() {
        return this.m_currentTurnDurationInMilliseconds;
    }
    
    @Override
    public void start() {
        this.m_running = true;
    }
    
    @Override
    public void stop() {
        this.m_running = false;
    }
    
    @Override
    public boolean isRunning() {
        return this.m_running;
    }
    
    public void addFighter(final long fighterId, final boolean playThisTurn) {
        if (playThisTurn) {
            this.m_nodes.addFromThisTurn(fighterId, this.m_tableTurn);
        }
        else {
            this.m_nodes.addFromNextTurn(fighterId, this.m_tableTurn);
        }
        this.onFighterAdded(fighterId);
    }
    
    public void removeFighter(final long fighterId) {
        if (this.isRunning()) {
            if (this.m_nodes.isCurrentFighter(fighterId) && (this.m_state == TimelineState.BEGINNING_FIGHTER_TURN || this.m_state == TimelineState.ON_FIGHTER_TURN || this.m_state == TimelineState.ENDING_FIGHTER_TURN)) {
                this.setCurrentFighterRemoved(fighterId);
            }
            this.m_nodes.remove(fighterId);
        }
        this.onFighterRemoved(fighterId);
    }
    
    private void setCurrentFighterRemoved(final long fighterId) {
        this.m_wasCurrentFighterRemoved = true;
        this.m_removedFighterId = fighterId;
    }
    
    public boolean wasCurrentFighterRemoved() {
        return this.m_wasCurrentFighterRemoved;
    }
    
    public void clearCurrentFighterRemoved() {
        this.m_wasCurrentFighterRemoved = false;
        this.m_removedFighterId = 0L;
    }
    
    protected TLongArrayList getTurnOrder() {
        return this.m_nodes.getOrderThisTurn();
    }
    
    public boolean hasCurrentFighter() {
        return this.m_nodes.hasCurrentFighter() || this.m_wasCurrentFighterRemoved;
    }
    
    public boolean isCurrentFighter(final long fighterId) {
        if (this.m_wasCurrentFighterRemoved) {
            return this.m_removedFighterId == fighterId;
        }
        return this.m_nodes.isCurrentFighter(fighterId);
    }
    
    public boolean isFighterTurn(final long fighterId) {
        if (this.m_state != TimelineState.ON_FIGHTER_TURN) {
            return false;
        }
        if (this.m_wasCurrentFighterRemoved) {
            return this.m_removedFighterId == fighterId;
        }
        return this.m_nodes.isCurrentFighter(fighterId);
    }
    
    public long getCurrentFighterId() {
        return this.m_wasCurrentFighterRemoved ? this.m_removedFighterId : this.m_nodes.currentFighter();
    }
    
    public int getCurrentFighterPosition() {
        return this.m_nodes.currentPosition() + (this.m_wasCurrentFighterRemoved ? 1 : 0);
    }
    
    public int getFighterPosition(final long fighterId) {
        return this.getFighterPositionThisTurn(fighterId);
    }
    
    public int getFighterPositionThisTurn(final long fighterId) {
        return this.m_nodes.getOrderThisTurn().indexOf(fighterId);
    }
    
    public int getFighterPositionNextTurn(final long fighterId) {
        return this.m_nodes.getOrderNextTurn().indexOf(fighterId);
    }
    
    public void sortInitially() {
        this.m_nodes.sortInitially();
    }
    
    public void updateDynamicOrder() {
        this.m_nodes.updateOrder();
    }
    
    public short getCurrentTableturn() {
        return this.m_tableTurn;
    }
    
    public void askForStartTurn() {
        this.askForStartTurn(false);
    }
    
    public void askForStartTurn(final boolean forceTurnStart) {
        if (!this.isRunning()) {
            return;
        }
        if (this.m_tableTurn == 0) {
            if (this.m_state != TimelineState.INITIAL) {
                return;
            }
            this.m_state = TimelineState.BETWEEN_FIGHTER_TURNS;
        }
        else {
            if (!forceTurnStart && !this.checkForEndOfTableTurn()) {
                return;
            }
            this.fireTimeEvent(TableTurnEndEvent.getInstance());
        }
        this.newTableTurn();
    }
    
    public void newTableTurn() {
        if (!this.isRunning()) {
            return;
        }
        ++this.m_tableTurn;
        this.m_nodes.newTableTurn();
        this.fireTimeEvent(TableTurnStartEvent.getInstance());
        if (!this.isRunning()) {
            return;
        }
        this.m_timelineEvents.processNewTableTurnEvents(this.m_tableTurn, this);
        if (!this.isRunning()) {
            return;
        }
        this.onNewTableTurn();
    }
    
    public int getTurnCounter() {
        return this.m_turnCounter;
    }
    
    public boolean askForFighterStartTurn(final long fighterId) {
        if (!this.checkStartFighterTurn(fighterId)) {
            return false;
        }
        this.clearCurrentFighterRemoved();
        this.m_state = TimelineState.BEGINNING_FIGHTER_TURN;
        this.m_nodes.nextFighter();
        this.onCurrentFighterChange();
        this.m_timelineEvents.processStartTurnEvents(fighterId, this.m_tableTurn, this);
        if (!this.isRunning()) {
            return false;
        }
        if (this.m_state != TimelineState.BEGINNING_FIGHTER_TURN) {
            return true;
        }
        this.m_state = TimelineState.ON_FIGHTER_TURN;
        ++this.m_turnCounter;
        this.notifyStartFighterTurn(fighterId);
        return true;
    }
    
    public boolean askForFighterEndTurn(final long fighterId) {
        if (this.m_state == TimelineState.ENDING_FIGHTER_TURN) {
            return true;
        }
        if (!this.m_wasCurrentFighterRemoved) {
            if (!this.checkEndFighterTurn(fighterId)) {
                return false;
            }
            this.m_state = TimelineState.ENDING_FIGHTER_TURN;
            this.m_timelineEvents.processEndTurnEvents(fighterId, this.m_tableTurn, this);
            if (!this.m_wasCurrentFighterRemoved) {
                this.m_nodes.endCurrentFighterTurn();
            }
        }
        else if (this.m_removedFighterId != fighterId) {
            return false;
        }
        if (!this.isRunning()) {
            return false;
        }
        if (this.m_state == TimelineState.BEGINNING_FIGHTER_TURN) {
            this.recoverToEndTurn(fighterId);
            return true;
        }
        this.m_state = TimelineState.BETWEEN_FIGHTER_TURNS;
        this.clearCurrentFighterRemoved();
        this.notifyEndFighterTurn(fighterId);
        return true;
    }
    
    public void recoverToEndTurn(final long fighterId) {
        this.m_state = TimelineState.BETWEEN_FIGHTER_TURNS;
        this.onTurnEnded(fighterId);
    }
    
    public TimelineEvents getTimelineEvents() {
        return this.m_timelineEvents;
    }
    
    @Override
    public TimeEventHandler getGlobalListener() {
        return this.m_timeEventHandler;
    }
    
    public RelativeFightTime addTimeEvent(final DelayableTimeEvent te, final RelativeFightTimeInterval timeToEvent) {
        if (this.fireTimeEventIfImmediate(te, timeToEvent)) {
            return RelativeFightTime.never();
        }
        final long fighterId = te.getFighterToAttachToById();
        return this.addTimeEventToFighter(te, timeToEvent, fighterId);
    }
    
    protected RelativeFightTime addTimeEventToFighter(final DelayableTimeEvent te, final RelativeFightTimeInterval timeToEvent, final long fighterId) {
        final boolean isTargetActive = this.isFighterActive(fighterId);
        final int fighterPositionThisTurn = this.getFighterPositionThisTurn(fighterId);
        final byte currentPosition = this.m_nodes.currentPosition();
        final boolean fighterHasNotPlayThisTurn = fighterPositionThisTurn < 0 || fighterPositionThisTurn > currentPosition;
        return this.m_timelineEvents.addTimeEvent(te, timeToEvent, isTargetActive, fighterHasNotPlayThisTurn, (short)Math.max(this.getCurrentTableturn(), 1));
    }
    
    public void clearTimeEvents() {
        this.m_timelineEvents.clearTimeEvents();
    }
    
    private boolean fireTimeEventIfImmediate(final DelayableTimeEvent te, final RelativeFightTimeInterval timeToEvent) {
        final boolean isImmediate = timeToEvent.isImmediate();
        if (isImmediate) {
            this.fireTimeEvent(te);
        }
        return isImmediate;
    }
    
    protected boolean isFighterActive(final long fighterId) {
        return this.m_nodes.isCurrentFighter(fighterId) && this.m_state == TimelineState.ON_FIGHTER_TURN;
    }
    
    public AbsoluteFightTime now() {
        return new AbsoluteFightTime(this.getCurrentFighterPosition(), this.getCurrentTableturn(), false);
    }
    
    public short howLongInTurnsUntil(final RelativeFightTime endTime) {
        if (!this.m_nodes.contains(endTime.getFighterId())) {
            return -1;
        }
        return this.m_nodes.howLongInTurnsUntil(endTime);
    }
    
    public int serializedSize() {
        return 11 + this.m_nodes.serializedSize();
    }
    
    @Override
    public byte[] serializeTimeline() {
        final ByteBuffer bb = ByteBuffer.allocate(this.serializedSize());
        this.serializeTo(bb);
        return bb.array();
    }
    
    protected void serializeTo(final ByteBuffer bb) {
        bb.putShort(this.m_tableTurn);
        bb.putInt(this.m_turnCounter);
        bb.put(this.m_state.getId());
        bb.putInt(this.m_currentTurnDurationInMilliseconds);
        this.m_nodes.serialize(bb);
    }
    
    @Override
    public void fromBuild(final EffectContext context, final byte[] data) {
        final ByteBuffer bb = ByteBuffer.wrap(data);
        this.prepareForUnserialize();
        final TimelineUnmarshallingContext ctx = this.newUnmarshallingContext(context);
        this.read(ctx, bb);
    }
    
    public TimelineUnmarshallingContext newUnmarshallingContext(final EffectContext context) {
        return new TimelineUnmarshallingContext(context, this.m_fighterSortingStrategy);
    }
    
    protected void read(final TimelineUnmarshallingContext ctx, final ByteBuffer bb) {
        this.m_tableTurn = bb.getShort();
        this.m_turnCounter = bb.getInt();
        final byte stateVal = bb.get();
        this.m_state = TimelineState.byId(stateVal);
        this.m_currentTurnDurationInMilliseconds = bb.getInt();
        this.m_nodes.clear();
        this.m_nodes.read(ctx, bb);
    }
    
    protected void prepareForUnserialize() {
        this.stop();
        this.m_nodes.clear();
    }
    
    public void fireTimeEvent(final TimeEvent te) {
        if (te == null) {
            TurnBasedTimeline.m_logger.error((Object)"On ne peut pas envoyer un timeEvent null");
            return;
        }
        if (this.m_timeEventHandler == null) {
            TurnBasedTimeline.m_logger.error((Object)"Pas de TimeEventHandler sur la timeline");
            return;
        }
        try {
            te.sendTo(this.m_timeEventHandler);
        }
        catch (Exception e) {
            TurnBasedTimeline.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    protected abstract void onTurnEnded(final long p0);
    
    protected abstract void onTurnStarted(final long p0);
    
    protected abstract void onNewTableTurn();
    
    protected abstract void onFighterAdded(final long p0);
    
    protected abstract void onFighterRemoved(final long p0);
    
    protected abstract void onCurrentFighterChange();
    
    private void processScheduledEvents(final Iterator<? extends DelayableTimeEvent> scheduledEvents) {
        while (this.isRunning() && scheduledEvents.hasNext()) {
            final DelayableTimeEvent event = (DelayableTimeEvent)scheduledEvents.next();
            scheduledEvents.remove();
            this.fireTimeEvent(event);
        }
    }
    
    private void notifyStartFighterTurn(final long fighterId) {
        final FighterTurnStartEvent te = FighterTurnStartEvent.checkOut(fighterId);
        try {
            this.fireTimeEvent(te);
        }
        finally {
            te.release();
        }
        if (this.m_nodes.isCurrentFighter(fighterId)) {
            this.onTurnStarted(fighterId);
        }
    }
    
    private void notifyEndFighterTurn(final long fighterId) {
        final FighterTurnEndEvent te = FighterTurnEndEvent.checkOut(fighterId);
        this.fireTimeEvent(te);
        te.release();
        this.onTurnEnded(fighterId);
    }
    
    private boolean checkForEndOfTableTurn() {
        if (this.m_state != TimelineState.BETWEEN_FIGHTER_TURNS) {
            TurnBasedTimeline.m_logger.error((Object)this.withFightIdAndState("Etat de la timeline incorrect : " + this.m_state + ", attendu: " + TimelineState.BETWEEN_FIGHTER_TURNS + " at " + ExceptionFormatter.currentStackTrace(5)));
            return false;
        }
        if (this.m_nodes.hasNextFighter()) {
            TurnBasedTimeline.m_logger.error((Object)this.withFightIdAndState("Assertion incorrecte sur la timeline (demande de fin de tour alors que joueur suivant = " + (this.m_nodes.hasNextFighter() ? this.m_nodes.peekAtNextFighter() : "null") + ')').append(ExceptionFormatter.currentStackTrace(16)));
            return false;
        }
        return true;
    }
    
    private boolean checkStartFighterTurn(final long fighterId) {
        if (this.m_state != TimelineState.BETWEEN_FIGHTER_TURNS) {
            TurnBasedTimeline.m_logger.error((Object)this.withFightIdAndState("Etat de la timeline incorrect : " + this.m_state + ", attendu: " + TimelineState.BETWEEN_FIGHTER_TURNS + " at " + ExceptionFormatter.currentStackTrace(5)));
            return false;
        }
        if (!this.m_nodes.canStartFighterTurn(fighterId)) {
            TurnBasedTimeline.m_logger.error((Object)this.withFightIdAndState("Assertion incorrecte sur la timeline (joueur suivant = " + (this.m_nodes.hasNextFighter() ? this.m_nodes.peekAtNextFighter() : "null") + ", attendu = " + fighterId + ')').append(ExceptionFormatter.currentStackTrace(16)));
            return false;
        }
        return true;
    }
    
    private boolean checkEndFighterTurn(final long fighterId) {
        if (this.m_state != TimelineState.ON_FIGHTER_TURN) {
            TurnBasedTimeline.m_logger.error((Object)this.withFightIdAndState("Etat de la timeline incorrect : " + this.m_state + ", attendu: " + TimelineState.ON_FIGHTER_TURN));
            return false;
        }
        if (!this.m_nodes.canEndFighterTurn(fighterId)) {
            final String id = this.hasCurrentFighter() ? String.valueOf(this.getCurrentFighterId()) : "NONE";
            TurnBasedTimeline.m_logger.error((Object)this.withFightIdAndState("Assertion incorrecte sur la timeline (joueur courant = " + id + ", attendu = " + fighterId + ')').append(ExceptionFormatter.currentStackTrace(16)));
            return false;
        }
        return true;
    }
    
    @Override
    public int getFightId() {
        return this.m_fightId;
    }
    
    @Override
    public void setFightId(final int fightId) {
        this.m_fightId = fightId;
    }
    
    protected StringBuilder withFightIdAndState(final String message) {
        return new StringBuilder().append("[_TL_] fightId=").append(this.m_fightId).append(" - ").append(message).append(" - ").append((CharSequence)this.stateRepr());
    }
    
    public StringBuilder stateRepr() {
        final StringBuilder rv = new StringBuilder(this.m_nodes.toString());
        if (this.m_wasCurrentFighterRemoved) {
            rv.append(" R:").append(this.m_removedFighterId);
        }
        return rv;
    }
    
    protected StringBuilder withFightIdAndState(final String message, final Throwable t) {
        return this.withFightIdAndState(message).append(' ').append(ExceptionFormatter.toString(t));
    }
    
    public void shelveFighter(final long id) {
    }
    
    public long getNextFighter() {
        if (this.m_nodes.hasNextFighter()) {
            return this.m_nodes.peekAtNextFighter();
        }
        final TLongArrayList orderNextTurn = this.m_nodes.getOrderNextTurn();
        if (!orderNextTurn.isEmpty()) {
            return orderNextTurn.get(0);
        }
        return 0L;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TurnBasedTimeline.class);
    }
}
