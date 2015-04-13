package com.ankamagames.wakfu.common.game.item;

import java.util.*;

public abstract class AbstractFloorItem
{
    protected int m_currentFightId;
    private byte m_phase;
    private final LinkedList<ArrayList<Long>> m_locked_for;
    
    public AbstractFloorItem() {
        super();
        this.m_currentFightId = -1;
        this.m_locked_for = new LinkedList<ArrayList<Long>>();
    }
    
    public byte getPhase() {
        return this.m_phase;
    }
    
    public void setPhase(final byte phase) {
        this.m_phase = phase;
    }
    
    public boolean isLocked() {
        return this.m_locked_for.size() > 0;
    }
    
    public void addLock(final ArrayList<Long> toLockFor) {
        this.m_locked_for.addLast(toLockFor);
    }
    
    public void removeLock() {
        this.m_locked_for.clear();
    }
    
    public void nextLock() {
        if (this.isLocked()) {
            this.m_locked_for.removeFirst();
        }
    }
    
    public ArrayList<Long> getLock() {
        if (this.m_locked_for != null && !this.m_locked_for.isEmpty()) {
            return this.m_locked_for.getFirst();
        }
        return null;
    }
    
    public void nextPhase() {
        if (this.m_phase != -1) {
            this.nextLock();
            if (this.isLocked() && this.m_phase + 1 < 3) {
                ++this.m_phase;
            }
            else {
                this.m_phase = -1;
            }
        }
    }
    
    protected List<ArrayList<Long>> getLocks() {
        return this.m_locked_for;
    }
    
    public abstract long getId();
    
    public abstract void setId(final long p0);
    
    public abstract Item getItem();
    
    public int getCurrentFightId() {
        return this.m_currentFightId;
    }
    
    public void setCurrentFightId(final int currentFightId) {
        this.m_currentFightId = currentFightId;
    }
}
