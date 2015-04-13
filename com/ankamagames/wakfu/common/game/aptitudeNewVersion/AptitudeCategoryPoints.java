package com.ankamagames.wakfu.common.game.aptitudeNewVersion;

import gnu.trove.*;

public final class AptitudeCategoryPoints
{
    private final TIntShortHashMap m_pointsByCategory;
    
    public AptitudeCategoryPoints() {
        super();
        this.m_pointsByCategory = new TIntShortHashMap();
    }
    
    public short addPoints(final int categoryId, final short pointsToAdd) {
        return this.m_pointsByCategory.adjustOrPutValue(categoryId, pointsToAdd, pointsToAdd);
    }
    
    public short removePoints(final int categoryId, final short pointsToRemove) {
        final short currentPoints = this.m_pointsByCategory.get(categoryId);
        if (currentPoints - pointsToRemove <= 0) {
            this.m_pointsByCategory.remove(categoryId);
        }
        return this.m_pointsByCategory.adjustOrPutValue(categoryId, (short)(-pointsToRemove), (short)0);
    }
    
    public short getPoints(final int categoryId) {
        return this.m_pointsByCategory.get(categoryId);
    }
    
    public boolean forEachEntry(final TIntShortProcedure procedure) {
        return this.m_pointsByCategory.forEachEntry(procedure);
    }
    
    public void clear() {
        this.m_pointsByCategory.clear();
    }
    
    void copyTo(final AptitudeCategoryPoints points) {
        points.m_pointsByCategory.clear();
        points.m_pointsByCategory.putAll(this.m_pointsByCategory);
    }
    
    @Override
    public String toString() {
        return "AptitudeCategoryPoints{m_pointsByCategory=" + this.m_pointsByCategory + '}';
    }
}
