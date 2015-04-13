package com.ankamagames.wakfu.client.core.game.restat;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class GlobalSpellRestatComputer extends AbstractSpellRestatComputer
{
    public static final Logger m_logger;
    protected final long m_playerCurrentXp;
    protected final long m_maxTheoricalXp;
    protected final long m_unpenalizedXpLimitPerElement;
    
    public GlobalSpellRestatComputer() {
        super(getConcernedElements());
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        this.m_playerCurrentXp = localPlayer.getCurrentXp();
        this.m_maxTheoricalXp = SpellXpComputer.getMaxSpellXpFromPlayerXp(this.m_playerCurrentXp);
        this.m_unpenalizedXpLimitPerElement = this.m_maxTheoricalXp / 3L;
    }
    
    public void setToCurrentValues() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null || localPlayer.getSpellInventory() == null) {
            return;
        }
        final ArrayList<SpellLevel> spells = new ArrayList<SpellLevel>();
        for (final SpellLevel spellLevel : localPlayer.getSpellInventory()) {
            spells.add(spellLevel);
        }
        Collections.sort(spells, new Comparator<SpellLevel>() {
            @Override
            public int compare(final SpellLevel spellLevel1, final SpellLevel spellLevel2) {
                return spellLevel1.getSpell().getUiPosition() - spellLevel2.getSpell().getUiPosition();
            }
        });
        for (final SpellLevel spellLevel : spells) {
            if (this.getRemainingXpToDistribute() <= 0L) {
                return;
            }
            if (spellLevel.getSpell().isPassive()) {
                continue;
            }
            final Elements element = spellLevel.getElement();
            final int referenceId = spellLevel.getReferenceId();
            final short levelWithoutGain = spellLevel.getLevelWithoutGain();
            for (int i = 0; i < levelWithoutGain; ++i) {
                this.incrementSpellLevel(element, referenceId);
            }
        }
        Collections.sort(spells, new Comparator<SpellLevel>() {
            @Override
            public int compare(final SpellLevel spellLevel1, final SpellLevel spellLevel2) {
                final int spellLevelsDiff = spellLevel2.getLevelWithoutGain() - spellLevel1.getLevelWithoutGain();
                if (spellLevelsDiff == 0) {
                    return (int)(spellLevel2.getXpDoneInLevel() - spellLevel1.getXpDoneInLevel());
                }
                return spellLevelsDiff;
            }
        });
        for (final SpellLevel spellLevel : spells) {
            if (this.getRemainingXpToDistribute() > 0L) {
                this.incrementSpellLevel(spellLevel.getElement(), spellLevel.getReferenceId());
            }
        }
    }
    
    private static List<Elements> getConcernedElements() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final AvatarBreed breed = localPlayer.getBreed();
        final List<Elements> elements = new ArrayList<Elements>();
        breed.foreachElement(new TObjectProcedure<Elements>() {
            @Override
            public boolean execute(final Elements element) {
                if (element.isElemental()) {
                    elements.add(element);
                }
                return true;
            }
        });
        return elements;
    }
    
    @Override
    public SpellRestatComputer.RestatType getRestatType() {
        return SpellRestatComputer.RestatType.GLOBAL;
    }
    
    @Override
    public long getTotalXpToDistribute() {
        return this.m_maxTheoricalXp;
    }
    
    @Override
    public long getTotalXpToDistribute(@Nullable final Elements element) {
        return this.m_maxTheoricalXp;
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
        final long totalXpDistributed = this.getAlreadyDistributedXp(element);
        final long virtualXpDifference = SpellXpComputer.realXpToVirtualXpDiff(totalXpDistributed, realXpNeeded, this.m_unpenalizedXpLimitPerElement);
        return virtualXpDifference;
    }
    
    @Override
    protected boolean canIncrementSpellLevel(@NotNull final Elements element, @NotNull final SpellLevel spellLevel) {
        if (!super.canIncrementSpellLevel(element, spellLevel)) {
            return false;
        }
        final long remainingXpToDistribute = this.getRemainingXpToDistribute();
        return remainingXpToDistribute >= 1L && (remainingXpToDistribute > 1L || this.getAlreadyDistributedXp(element) < this.m_unpenalizedXpLimitPerElement);
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
        final long virtualXpNeeded = this.getXpNeededToIncrementSpellLevel(element, spellLevel);
        if (virtualXpNeeded <= 0L) {
            return false;
        }
        final long virtualXpWonBeforeRound = Math.min(virtualXpNeeded, this.getRemainingXpToDistribute());
        if (virtualXpWonBeforeRound <= 0L) {
            return false;
        }
        final long alreadyDistributedXpInElement = this.getAlreadyDistributedXp(element);
        final double realXpWithDecimal = SpellXpComputer.virtualXpToRealXpDiff(alreadyDistributedXpInElement, virtualXpWonBeforeRound, this.m_unpenalizedXpLimitPerElement);
        final long realXp = (long)Math.floor(realXpWithDecimal);
        long virtualXpWon;
        if (MathHelper.isEqual(realXp, realXpWithDecimal, 1.0E-5)) {
            virtualXpWon = virtualXpWonBeforeRound;
        }
        else {
            virtualXpWon = SpellXpComputer.realXpToVirtualXpDiff(alreadyDistributedXpInElement, realXp, this.m_unpenalizedXpLimitPerElement);
        }
        this.registerSpellXpModification(element, spellLevel, realXp, virtualXpWon);
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
        long realXpNeeded;
        if (xpDoneInLevel != 0L) {
            realXpNeeded = xpDoneInLevel;
        }
        else {
            if (spellLevel.getLevel() == xpTable.getMinLevel()) {
                return -1L;
            }
            realXpNeeded = xpTable.getLevelExtent((short)(spellLevel.getLevel() - 1));
        }
        final long totalXpDistributed = this.getAlreadyDistributedXp(element);
        final long virtualXpDifference = -SpellXpComputer.realXpToVirtualXpDiff(totalXpDistributed, -realXpNeeded, this.m_unpenalizedXpLimitPerElement);
        return virtualXpDifference;
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
        final long virtualXpGainedBeforeRound = this.getXpGainedByDecrementingSpellLevel(element, spellLevel);
        if (virtualXpGainedBeforeRound <= 0L) {
            return false;
        }
        final long alreadyDistributedXpInElement = this.getAlreadyDistributedXp(element);
        final double realXpWithDecimal = -SpellXpComputer.virtualXpToRealXpDiff(alreadyDistributedXpInElement, -virtualXpGainedBeforeRound, this.m_unpenalizedXpLimitPerElement);
        final long realXp = (long)Math.floor(realXpWithDecimal);
        long virtualXpGained;
        if (MathHelper.isEqual(realXp, realXpWithDecimal, 1.0E-5)) {
            virtualXpGained = virtualXpGainedBeforeRound;
        }
        else {
            virtualXpGained = -SpellXpComputer.realXpToVirtualXpDiff(alreadyDistributedXpInElement, -realXp, this.m_unpenalizedXpLimitPerElement);
        }
        this.registerSpellXpModification(element, spellLevel, -realXp, -virtualXpGained);
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GlobalSpellRestatComputer.class);
    }
}
