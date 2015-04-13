package com.ankamagames.framework.ai.targetfinder.aoe;

import com.ankamagames.framework.external.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public abstract class AreaOfEffect implements Parameterized
{
    private AOEPattern m_aoePattern;
    private short m_orderingMethod;
    
    public AreaOfEffect() {
        super();
        this.m_orderingMethod = 0;
    }
    
    public abstract void initialize(final int[] p0) throws IllegalArgumentException;
    
    public <T extends Target> Iterable<T> getTargets(final int cellCenterX, final int cellCenterY, final short cellCenterAltitude, final int cellSourceX, final int cellSourceY, final short cellSourceAltitude, final Iterator<T> possibleTargets) {
        return this.getTargets(cellCenterX, cellCenterY, cellCenterAltitude, cellSourceX, cellSourceY, cellSourceAltitude, Direction8.NORTH_EAST, possibleTargets);
    }
    
    public <T extends Target> Iterable<T> getTargets(final int cellCenterX, final int cellCenterY, final short cellCenterAltitude, final int cellSourceX, final int cellSourceY, final short cellSourceAltitude, final Direction8 applicantDirection, final Iterator<T> possibleTargets) {
        final List<T> targets = new ArrayList<T>();
        while (possibleTargets.hasNext()) {
            final T target = possibleTargets.next();
            if (this.isPointInside(cellCenterX, cellCenterY, cellCenterAltitude, cellSourceX, cellSourceY, cellSourceAltitude, applicantDirection, target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude())) {
                targets.add(target);
            }
        }
        return targets;
    }
    
    public short getOrderingMethod() {
        return this.m_orderingMethod;
    }
    
    public void setOrderingMethod(final short orderingMethod) {
        this.m_orderingMethod = orderingMethod;
    }
    
    public boolean isPointInside(final int cellCenterX, final int cellCenterY, final short cellCenterAltitude, final int cellSourceX, final int cellSourceY, final short cellSourceAltitude, final Direction8 effectSourceDirection, final int cellQueriedX, final int cellQueriedY, final short cellQueriedAltitude) {
        return this.getAOEPattern().isPointInside(cellCenterX, cellCenterY, cellCenterAltitude, cellSourceX, cellSourceY, cellSourceAltitude, effectSourceDirection, cellQueriedX, cellQueriedY, cellQueriedAltitude);
    }
    
    public boolean intersects(final int cellCenterX, final int cellCenterY, final short cellCenterAltitude, final int cellSourceX, final int cellSourceY, final short cellSourceAltitude, final Direction8 effectSourceDirection, final int areaQueriedX, final int areaQueriedY, final short areaQueriedAltitude, final byte areaQueriedRadius) {
        return this.getAOEPattern().isAreaInside(cellCenterX, cellCenterY, cellCenterAltitude, cellSourceX, cellSourceY, cellSourceAltitude, effectSourceDirection, areaQueriedX, areaQueriedY, areaQueriedAltitude, areaQueriedRadius);
    }
    
    public boolean isPointInside(final int cellCenterX, final int cellCenterY, final short cellCenterAltitude, final int cellSourceX, final int cellSourceY, final short cellSourceAltitude, final int cellQueriedX, final int cellQueriedY, final short cellQueriedAltitude) {
        return this.isPointInside(cellCenterX, cellCenterY, cellCenterAltitude, cellSourceX, cellSourceY, cellSourceAltitude, Direction8.NORTH_EAST, cellQueriedX, cellQueriedY, cellQueriedAltitude);
    }
    
    protected AOEPattern getAOEPattern() {
        if (this.m_aoePattern == null) {
            this.m_aoePattern = new AOEPattern(this.getPattern(), this.isInvariantByRotation());
        }
        return this.m_aoePattern;
    }
    
    public Iterable<int[]> getCells(final int cellCenterX, final int cellCenterY, final short cellCenterAltitude, final int cellSourceX, final int cellSourceY, final short cellSourceAltitude, final Direction8 sourceOrientation) {
        return this.getAOEPattern().apply(cellCenterX, cellCenterY, cellCenterAltitude, cellSourceX, cellSourceY, cellSourceAltitude, sourceOrientation);
    }
    
    public Iterable<int[]> getCells(final int cellCenterX, final int cellCenterY, final short cellCenterAltitude, final int cellSourceX, final int cellSourceY, final short cellSourceAltitude) {
        return this.getCells(cellCenterX, cellCenterY, cellCenterAltitude, cellSourceX, cellSourceY, cellSourceAltitude, Direction8.NORTH_EAST);
    }
    
    protected abstract boolean isInvariantByRotation();
    
    public abstract AreaOfEffectEnum getType();
    
    public abstract List<int[]> getPattern();
    
    public abstract String toShortDescription();
    
    public abstract int getVisualRange();
    
    public abstract ArrayList<AreaOfEffect> getSubAOEs();
    
    public abstract AreaOfEffectShape getShape();
}
