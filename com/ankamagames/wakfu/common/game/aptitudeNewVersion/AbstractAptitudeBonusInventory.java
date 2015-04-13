package com.ankamagames.wakfu.common.game.aptitudeNewVersion;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.framework.kernel.utils.*;
import gnu.trove.*;

public abstract class AbstractAptitudeBonusInventory
{
    private static final Logger m_logger;
    private final TIntShortHashMap m_levelsByBonusId;
    private final AptitudeCategoryPoints m_categoryPoints;
    private final Collection<AptitudeBonusInventoryListener> m_listeners;
    
    public AbstractAptitudeBonusInventory() {
        super();
        this.m_levelsByBonusId = new TIntShortHashMap();
        this.m_categoryPoints = new AptitudeCategoryPoints();
        this.m_listeners = new ArrayList<AptitudeBonusInventoryListener>();
    }
    
    public short getLevel(final int bonusId) {
        return this.m_levelsByBonusId.get(bonusId);
    }
    
    public void setLevel(final int bonusId, final short level) {
        this.m_levelsByBonusId.put(bonusId, level);
        this.fireLevelChanged(bonusId, level);
    }
    
    public void addLevel(final int bonusId, final short level) {
        final short newLevel = this.m_levelsByBonusId.adjustOrPutValue(bonusId, level, level);
        this.fireLevelChanged(bonusId, newLevel);
    }
    
    public boolean forEachBonus(final TIntShortProcedure procedure) {
        return this.m_levelsByBonusId.forEachEntry(procedure);
    }
    
    private void fireLevelChanged(final int bonusId, final short level) {
        for (final AptitudeBonusInventoryListener listener : this.m_listeners) {
            try {
                listener.onLevelChanged(bonusId, level);
            }
            catch (Exception e) {
                AbstractAptitudeBonusInventory.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    public void addListener(final AptitudeBonusInventoryListener listener) {
        if (this.m_listeners.contains(listener)) {
            return;
        }
        this.m_listeners.add(listener);
    }
    
    public void removeListener(final AptitudeBonusInventoryListener listener) {
        this.m_listeners.remove(listener);
    }
    
    public void toRaw(final RawAptitudeBonusInventory aptitudeInventory) {
        aptitudeInventory.clear();
        this.m_levelsByBonusId.forEachEntry(new TIntShortProcedure() {
            @Override
            public boolean execute(final int a, final short b) {
                final RawAptitudeBonusInventory.Content content = new RawAptitudeBonusInventory.Content();
                content.bonus.bonusId = a;
                content.bonus.level = b;
                aptitudeInventory.contents.add(content);
                return true;
            }
        });
        this.m_categoryPoints.forEachEntry(new TIntShortProcedure() {
            @Override
            public boolean execute(final int categoryId, final short availablePoints) {
                final RawAptitudeBonusInventory.AvailablePoints availablePointsList = new RawAptitudeBonusInventory.AvailablePoints();
                availablePointsList.categoryAvailablePoints.categoryId = categoryId;
                availablePointsList.categoryAvailablePoints.availablePoints = availablePoints;
                aptitudeInventory.availablePoints.add(availablePointsList);
                return true;
            }
        });
    }
    
    public void fromRaw(final RawAptitudeBonusInventory aptitudeInventory) {
        this.m_levelsByBonusId.clear();
        final ArrayList<RawAptitudeBonusInventory.Content> contents = aptitudeInventory.contents;
        for (final RawAptitudeBonusInventory.Content content : contents) {
            final int bonusId = content.bonus.bonusId;
            final short level = content.bonus.level;
            this.m_levelsByBonusId.put(bonusId, level);
        }
        this.m_categoryPoints.clear();
        final ArrayList<RawAptitudeBonusInventory.AvailablePoints> availablePointsList = aptitudeInventory.availablePoints;
        for (final RawAptitudeBonusInventory.AvailablePoints availablePoints : availablePointsList) {
            final int categoryId = availablePoints.categoryAvailablePoints.categoryId;
            final short points = availablePoints.categoryAvailablePoints.availablePoints;
            this.m_categoryPoints.addPoints(categoryId, points);
        }
    }
    
    public int getAvailablePointsForCategory(final int categoryId) {
        return this.m_categoryPoints.getPoints(categoryId);
    }
    
    public boolean hasPointsAvailableFor(final int bonusId, final int levelGain) {
        final int categoryId = AptitudeCategoryModelManager.INSTANCE.getBonusCategoryId(bonusId);
        final short points = this.m_categoryPoints.getPoints(categoryId);
        return points >= levelGain;
    }
    
    public void removePointsFor(final int bonusId, final short pointsToRemove) {
        final int categoryId = AptitudeCategoryModelManager.INSTANCE.getBonusCategoryId(bonusId);
        this.m_categoryPoints.removePoints(categoryId, pointsToRemove);
    }
    
    public void incPointFor(final int categoryId) {
        this.m_categoryPoints.addPoints(categoryId, (short)1);
    }
    
    void copyTo(final AbstractAptitudeBonusInventory inventory) {
        inventory.m_levelsByBonusId.clear();
        inventory.m_levelsByBonusId.putAll(this.m_levelsByBonusId);
        this.m_categoryPoints.copyTo(inventory.m_categoryPoints);
    }
    
    public short addPointsFor(final int categoryId, final short pointsToAdd) {
        return this.m_categoryPoints.addPoints(categoryId, pointsToAdd);
    }
    
    public int getTotalPoints() {
        final Accumulator acc = new Accumulator();
        this.m_categoryPoints.forEachEntry(new TIntShortProcedure() {
            @Override
            public boolean execute(final int categoryId, final short points) {
                acc.accumulate(points);
                return true;
            }
        });
        this.m_levelsByBonusId.forEachValue(new TShortProcedure() {
            @Override
            public boolean execute(final short level) {
                acc.accumulate(level);
                return true;
            }
        });
        return acc.getValue();
    }
    
    @Override
    public String toString() {
        return "AptitudeBonusInventory{m_levelsByBonusId=" + this.m_levelsByBonusId + ", m_categoryPoints=" + this.m_categoryPoints + '}';
    }
    
    public void clear() {
        this.m_categoryPoints.clear();
        this.m_levelsByBonusId.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractAptitudeBonusInventory.class);
    }
}
