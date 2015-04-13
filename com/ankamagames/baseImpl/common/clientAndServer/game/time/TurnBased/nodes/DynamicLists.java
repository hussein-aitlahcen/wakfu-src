package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.nodes;

import gnu.trove.*;
import java.nio.*;

public class DynamicLists
{
    private final TLongArrayList m_dynamicOrder;
    private final TLongArrayList m_turnOrder;
    final FighterSortingStrategy m_fighterSortingStrategy;
    
    public DynamicLists(final FighterSortingStrategy fighterSortingStrategy) {
        super();
        this.m_dynamicOrder = new TLongArrayList();
        this.m_turnOrder = new TLongArrayList();
        this.m_fighterSortingStrategy = fighterSortingStrategy;
    }
    
    void addToDynamicList(final long fighterId) {
        this.m_fighterSortingStrategy.addToDynamicList(this, fighterId);
    }
    
    void insertInTurnOrder(final long fighterId, final int index) {
        this.m_fighterSortingStrategy.insertIntoTurnOrderList(this, fighterId, Math.min(index, this.m_turnOrder.size()));
    }
    
    void remove(final long fighterId) {
        int index;
        while ((index = this.m_turnOrder.indexOf(fighterId)) != -1) {
            this.m_turnOrder.remove(index);
        }
        this.m_fighterSortingStrategy.removeFromDynamicList(this, fighterId);
    }
    
    void sortInitially() {
        this.m_fighterSortingStrategy.sortInitially(this);
    }
    
    void sortForNewTurn() {
        this.m_fighterSortingStrategy.sortForOneTurn(this);
    }
    
    void updateDynamicOrder() {
        this.m_fighterSortingStrategy.sortDynamically(this);
    }
    
    public TLongArrayList getTurnOrder() {
        return this.m_turnOrder;
    }
    
    public TLongArrayList getDynamicOrder() {
        return this.m_dynamicOrder;
    }
    
    public int serializedSize() {
        return 1 + 8 * this.m_turnOrder.size() + 1 + 8 * this.m_dynamicOrder.size();
    }
    
    public void serialize(final ByteBuffer buffer) {
        buffer.put((byte)this.m_turnOrder.size());
        for (int i = 0; i < this.m_turnOrder.size(); ++i) {
            buffer.putLong(this.m_turnOrder.get(i));
        }
        buffer.put((byte)this.m_dynamicOrder.size());
        for (int i = 0; i < this.m_dynamicOrder.size(); ++i) {
            buffer.putLong(this.m_dynamicOrder.get(i));
        }
    }
    
    public void read(final ByteBuffer buffer) {
        final byte turnOrderSize = buffer.get();
        for (int i = 0; i < turnOrderSize; ++i) {
            final long fighterId = buffer.getLong();
            this.m_turnOrder.add(fighterId);
        }
        final byte dynamicListSize = buffer.get();
        for (int j = 0; j < dynamicListSize; ++j) {
            final long fighterId2 = buffer.getLong();
            this.m_dynamicOrder.add(fighterId2);
        }
    }
    
    public void clear() {
        this.m_turnOrder.clear();
        this.m_dynamicOrder.clear();
    }
}
