package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.nodes;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;
import java.io.*;

public class TimelineNodesImpl implements TimelineNodes
{
    protected static final Logger m_logger;
    TLongObjectHashMap<FighterNode> m_nodesMap;
    DynamicLists m_dynamicLists;
    byte m_currentPosition;
    
    public TimelineNodesImpl() {
        super();
        this.m_nodesMap = new TLongObjectHashMap<FighterNode>();
        this.m_currentPosition = -1;
    }
    
    @Override
    public void setDynamicLists(final DynamicLists dynamicLists) {
        this.m_dynamicLists = dynamicLists;
    }
    
    public TimelineNodesImpl(final FighterSortingStrategy sortingStrategy) {
        super();
        this.m_nodesMap = new TLongObjectHashMap<FighterNode>();
        this.m_currentPosition = -1;
        this.m_dynamicLists = new DynamicLists(sortingStrategy);
    }
    
    @Override
    public void addFromThisTurn(final long fighterId, final short turn) {
        this.addNode(fighterId, turn);
        this.m_dynamicLists.insertInTurnOrder(fighterId, this.m_currentPosition + 1);
        this.m_dynamicLists.updateDynamicOrder();
    }
    
    @Override
    public void addFromNextTurn(final long fighterId, final short turn) {
        this.addNode(fighterId, (short)(turn + 1));
    }
    
    @Override
    public void addThisTurnOnly(final long fighterId, final short turn) {
        this.m_dynamicLists.insertInTurnOrder(fighterId, this.m_currentPosition + 1);
        this.m_dynamicLists.updateDynamicOrder();
    }
    
    @Override
    public boolean contains(final long fighterId) {
        return this.m_nodesMap.containsKey(fighterId);
    }
    
    private void addNode(final long fighterId, final short turn) {
        final FighterNode node = new FighterNode(fighterId, turn);
        this.m_nodesMap.put(fighterId, node);
        this.m_dynamicLists.addToDynamicList(fighterId);
    }
    
    @Override
    public void remove(final long fighterId) {
        if (!this.contains(fighterId)) {
            TimelineNodesImpl.m_logger.error((Object)("On tente de retirer un fighter absent de la timeline (" + fighterId + ')'));
            return;
        }
        final TLongArrayList turnOrder = this.getOrderThisTurn();
        for (int boundToCheckForRemovedFighter = Math.min(this.m_currentPosition + 1, turnOrder.size()), i = 0; i < boundToCheckForRemovedFighter; ++i) {
            if (turnOrder.getQuick(i) == fighterId) {
                --this.m_currentPosition;
            }
        }
        this.m_dynamicLists.remove(fighterId);
        this.m_nodesMap.remove(fighterId);
    }
    
    @Override
    public void sortInitially() {
        this.m_dynamicLists.sortInitially();
    }
    
    @Override
    public void newTableTurn() {
        this.sortForOneTurn();
        this.resetTurnSequence();
    }
    
    private void sortForOneTurn() {
        this.m_dynamicLists.sortForNewTurn();
    }
    
    @Override
    public void updateOrder() {
        this.m_dynamicLists.updateDynamicOrder();
    }
    
    private void resetTurnSequence() {
        this.m_currentPosition = -1;
    }
    
    @Override
    public TLongArrayList getOrderThisTurn() {
        return this.m_dynamicLists.getTurnOrder();
    }
    
    @Override
    public TLongArrayList getOrderNextTurn() {
        return this.m_dynamicLists.getDynamicOrder();
    }
    
    @Override
    public boolean hasCurrentFighter() {
        return this.m_currentPosition >= 0 && this.m_currentPosition < this.getOrderThisTurn().size();
    }
    
    @Override
    public long currentFighter() {
        if (!this.hasCurrentFighter()) {
            this.dumpState();
            throw new IllegalStateException("currentFighter() sans hasCurrentFighter()");
        }
        return this.getOrderThisTurn().get(this.m_currentPosition);
    }
    
    @Override
    public boolean isCurrentFighter(final long fighterId) {
        return this.hasCurrentFighter() && this.currentFighter() == fighterId;
    }
    
    FighterNode currentNode() {
        if (!this.hasCurrentFighter()) {
            this.dumpState();
            throw new IllegalStateException("currentNode() sans hasCurrentFighter()");
        }
        return this.m_nodesMap.get(this.currentFighter());
    }
    
    @Override
    public byte currentPosition() {
        return this.m_currentPosition;
    }
    
    @Override
    public boolean hasNextFighter() {
        return this.m_currentPosition + 1 < this.getOrderThisTurn().size();
    }
    
    @Override
    public void nextFighter() {
        if (!this.hasNextFighter()) {
            return;
        }
        ++this.m_currentPosition;
    }
    
    @Override
    public long peekAtNextFighter() {
        if (!this.hasNextFighter()) {
            throw new IllegalStateException("peekAtNextFighter() sans hasNextFighter()");
        }
        return this.getOrderThisTurn().get(this.m_currentPosition + 1);
    }
    
    @Override
    public void endCurrentFighterTurn() {
        if (this.hasCurrentFighter()) {
            this.currentNode().endTurn();
        }
    }
    
    void dumpState() {
        final String stringValue = this.toString();
        TimelineNodesImpl.m_logger.error((Object)(stringValue + ExceptionFormatter.currentStackTrace(1, 5)));
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[Timeline] ");
        sb.append("P:").append(this.m_currentPosition).append(' ');
        sb.append("T:[");
        final TLongArrayList turnOrder = this.getOrderThisTurn();
        final int nTurn = turnOrder.size();
        for (int i = 0; i < nTurn; ++i) {
            sb.append(turnOrder.get(i)).append(',');
        }
        if (nTurn > 0) {
            sb.setLength(sb.length() - 1);
        }
        sb.append("] D:[");
        final TLongArrayList dynamicOrder = this.m_dynamicLists.getDynamicOrder();
        final int nDyn = dynamicOrder.size();
        for (int j = 0; j < nDyn; ++j) {
            sb.append(dynamicOrder.get(j)).append(',');
        }
        if (nDyn > 0) {
            sb.setLength(sb.length() - 1);
        }
        sb.append(']');
        final long[] nodes = this.m_nodesMap.keys();
        final int nMap = nodes.length;
        sb.append(" N:[");
        for (final long key : nodes) {
            if (this.m_nodesMap.get(key) == null) {
                sb.append('!');
            }
            sb.append(key).append(',');
        }
        if (nMap > 0) {
            sb.setLength(sb.length() - 1);
        }
        sb.append(']');
        return sb.toString();
    }
    
    @Override
    public boolean isOutdated(final RelativeFightTime relativeFightTime, final short currentTableTurn) {
        final long fighterId = relativeFightTime.getFighterId();
        final FighterNode nodeForTargetFighter = this.m_nodesMap.get(fighterId);
        if (nodeForTargetFighter == null) {
            return relativeFightTime.getTableTurn() < currentTableTurn;
        }
        int timeToEvent = relativeFightTime.getTableTurn() - nodeForTargetFighter.getNextTurnToPlay();
        if (!relativeFightTime.isAtEndOfTurn() && this.isCurrentFighter(fighterId)) {
            --timeToEvent;
        }
        return timeToEvent < 0;
    }
    
    @Override
    public short howLongInTurnsUntil(final RelativeFightTime endTime) {
        final long fighterId = endTime.getFighterId();
        final FighterNode nodeForTargetFighter = this.m_nodesMap.get(fighterId);
        if (nodeForTargetFighter == null) {
            return -1;
        }
        int result = endTime.getTableTurn() - nodeForTargetFighter.getNextTurnToPlay();
        if (endTime.isAtEndOfTurn() && !this.isCurrentFighter(fighterId)) {
            ++result;
        }
        return (short)result;
    }
    
    @Override
    public short getLastTurnPlayed(final long fighterId) {
        if (!this.contains(fighterId)) {
            return -1;
        }
        return (short)(this.m_nodesMap.get(fighterId).getNextTurnToPlay() - 1);
    }
    
    @Override
    public boolean canStartFighterTurn(final long fighterId) {
        return this.hasNextFighter() && this.peekAtNextFighter() == fighterId;
    }
    
    @Override
    public boolean canEndFighterTurn(final long fighterId) {
        return this.hasCurrentFighter() && this.currentFighter() == fighterId;
    }
    
    @Override
    public int serializedSize() {
        int size = 1 + this.m_dynamicLists.serializedSize() + 1 + 8 * this.m_nodesMap.size();
        for (final long fighterId : this.m_nodesMap.keys()) {
            size += this.m_nodesMap.get(fighterId).serializedSize();
        }
        return size;
    }
    
    @Override
    public void serialize(final ByteBuffer buffer) {
        this.m_dynamicLists.serialize(buffer);
        buffer.put((byte)this.m_nodesMap.size());
        for (int i = 0; i < this.m_nodesMap.keys().length; ++i) {
            final long fighterId = this.m_nodesMap.keys()[i];
            buffer.putLong(fighterId);
            this.m_nodesMap.get(fighterId).serialize(buffer);
        }
        buffer.put(this.m_currentPosition);
    }
    
    @Override
    public void read(final TimelineUnmarshallingContext ctx, final ByteBuffer buffer) {
        this.m_dynamicLists.read(buffer);
        final byte nodesSize = buffer.get();
        for (int i = 0; i < nodesSize; ++i) {
            final long fighterId = buffer.getLong();
            final FighterNode node = FighterNode.deserialize(ctx, buffer);
            node.setFighterId(fighterId);
            this.m_nodesMap.put(fighterId, node);
        }
        this.m_currentPosition = buffer.get();
    }
    
    public static TimelineNodesImpl deserialize(final TimelineUnmarshallingContext ctx, final ByteBuffer buffer) {
        final TimelineNodesImpl timelineNodes = new TimelineNodesImpl(ctx.getFighterSortingStrategy());
        timelineNodes.read(ctx, buffer);
        return timelineNodes;
    }
    
    @Override
    public void clear() {
        this.m_nodesMap.clear();
        if (this.m_dynamicLists != null) {
            this.m_dynamicLists.clear();
        }
        this.resetTurnSequence();
    }
    
    static {
        m_logger = Logger.getLogger((Class)TimelineNodesImpl.class);
    }
}
