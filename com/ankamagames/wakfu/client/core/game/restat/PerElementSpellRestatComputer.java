package com.ankamagames.wakfu.client.core.game.restat;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effect.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.xp.*;
import java.util.*;
import gnu.trove.*;

public class PerElementSpellRestatComputer extends AbstractSpellRestatComputer
{
    public static final Logger m_logger;
    private TObjectLongHashMap<Elements> m_totalXpToDistributePerElement;
    
    public PerElementSpellRestatComputer(@NotNull final List<Elements> concernedElements) {
        super(concernedElements);
        this.m_totalXpToDistributePerElement = new TObjectLongHashMap<Elements>();
    }
    
    @Override
    public SpellRestatComputer.RestatType getRestatType() {
        return SpellRestatComputer.RestatType.PER_ELEMENT;
    }
    
    public void setTotalXpForElement(@NotNull final Elements element, final long xp) {
        this.m_totalXpToDistributePerElement.put(element, xp);
    }
    
    @Override
    public long getTotalXpToDistribute() {
        long total = 0L;
        final TObjectLongIterator<Elements> iterator = this.m_totalXpToDistributePerElement.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            total += iterator.value();
        }
        return total;
    }
    
    @Override
    public long getTotalXpToDistribute(@Nullable final Elements element) {
        return this.m_totalXpToDistributePerElement.get(element);
    }
    
    @Override
    public long getXpNeededToIncrementSpellLevel(@NotNull final Elements element, final int spellId) {
        final SpellLevel spellLevel = this.getSpellLevel(element, spellId);
        if (spellLevel == null) {
            return -1L;
        }
        return this.getXpNeededToIncrementSpellLevel(element, spellLevel);
    }
    
    protected long getXpNeededToIncrementSpellLevel(@NotNull final Elements element, @NotNull final SpellLevel spellLevel) {
        final XpTable xpTable = spellLevel.getXpTable();
        if (spellLevel.getLevel() >= xpTable.getMaxLevel()) {
            return -1L;
        }
        final long xpDoneInLevel = xpTable.getXpInLevel(spellLevel.getXp());
        final long totalXpInLevel = xpTable.getLevelExtent(spellLevel.getLevel());
        final long realXpNeeded = totalXpInLevel - xpDoneInLevel;
        return realXpNeeded;
    }
    
    @Override
    protected boolean canIncrementSpellLevel(@NotNull final Elements element, @NotNull final SpellLevel spellLevel) {
        if (!super.canIncrementSpellLevel(element, spellLevel)) {
            return false;
        }
        final long remainingXpToDistribute = this.getRemainingXpToDistribute();
        return remainingXpToDistribute > 0L;
    }
    
    @Override
    public boolean incrementSpellLevel(@NotNull final Elements element, final int spellId) {
        final SpellLevel spellLevel = this.getSpellLevel(element, spellId);
        if (spellLevel == null) {
            return false;
        }
        if (!this.canIncrementSpellLevel(element, spellLevel)) {
            return false;
        }
        final long xpNeeded = Math.min(this.getRemainingXpToDistribute(element), this.getXpNeededToIncrementSpellLevel(element, spellLevel));
        if (xpNeeded <= 0L) {
            return false;
        }
        this.registerSpellXpModification(element, spellLevel, xpNeeded, xpNeeded);
        return true;
    }
    
    @Override
    public long getXpGainedByDecrementingSpellLevel(@NotNull final Elements element, final int spellId) {
        final SpellLevel spellLevel = this.getSpellLevel(element, spellId);
        if (spellLevel == null) {
            return -1L;
        }
        return this.getXpGainedByDecrementingSpellLevel(element, spellLevel);
    }
    
    protected long getXpGainedByDecrementingSpellLevel(@NotNull final Elements element, @NotNull final SpellLevel spellLevel) {
        final XpTable xpTable = spellLevel.getXpTable();
        if (spellLevel.getLevel() < xpTable.getMinLevel()) {
            return -1L;
        }
        final long xpDoneInLevel = xpTable.getXpInLevel(spellLevel.getXp());
        long xpNeeded;
        if (xpDoneInLevel != 0L) {
            xpNeeded = xpDoneInLevel;
        }
        else {
            if (spellLevel.getLevel() == xpTable.getMinLevel()) {
                return -1L;
            }
            xpNeeded = xpTable.getLevelExtent((short)(spellLevel.getLevel() - 1));
        }
        return xpNeeded;
    }
    
    @Override
    public boolean decrementSpellLevel(@NotNull final Elements element, final int spellId) {
        final SpellLevel spellLevel = this.getSpellLevel(element, spellId);
        if (spellLevel == null) {
            return false;
        }
        if (!this.canDecrementSpellLevel(element, spellLevel)) {
            return false;
        }
        final long xpToRemove = this.getXpGainedByDecrementingSpellLevel(element, spellLevel);
        if (xpToRemove <= 0L) {
            return false;
        }
        this.registerSpellXpModification(element, spellLevel, -xpToRemove, -xpToRemove);
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PerElementSpellRestatComputer.class);
    }
}
