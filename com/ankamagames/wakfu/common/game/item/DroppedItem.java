package com.ankamagames.wakfu.common.game.item;

public abstract class DroppedItem extends AbstractFloorItem
{
    protected short[] m_itemPhaseSpan;
    protected short m_itemSpan;
    private long m_remainingTicksInPhase;
    
    public long getRemainingTicksInPhase() {
        return this.m_remainingTicksInPhase;
    }
    
    public void decrementRemainingTicksInPhase() {
        --this.m_remainingTicksInPhase;
    }
    
    public void setRemainingTicksInPhase(final long remainingTicksInPhase) {
        this.m_remainingTicksInPhase = remainingTicksInPhase;
    }
    
    public void setItemPhaseSpan(final short[] itemPhaseSpan) {
        if (itemPhaseSpan == null || itemPhaseSpan.length >= 3) {
            this.m_itemPhaseSpan = itemPhaseSpan;
        }
        else {
            System.arraycopy(itemPhaseSpan, 0, this.m_itemPhaseSpan = new short[3], 0, itemPhaseSpan.length);
        }
    }
    
    public void setItemSpan(final short itemSpan) {
        this.m_itemSpan = itemSpan;
    }
    
    public short[] getItemPhaseSpan() {
        return this.m_itemPhaseSpan;
    }
    
    public short getItemSpan() {
        return this.m_itemSpan;
    }
}
