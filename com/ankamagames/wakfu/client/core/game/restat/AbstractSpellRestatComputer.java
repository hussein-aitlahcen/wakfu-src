package com.ankamagames.wakfu.client.core.game.restat;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.restat.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;
import java.util.*;
import gnu.trove.*;

abstract class AbstractSpellRestatComputer implements SpellRestatComputer
{
    public static final Logger m_logger;
    protected final short m_playerCurrentLevel;
    private final HashMap<Elements, List<SpellLevel>> m_spellsPerElement;
    private final TObjectLongHashMap<Elements> m_distributedXpPerElement;
    
    protected AbstractSpellRestatComputer(final List<Elements> elements) {
        super();
        this.m_spellsPerElement = new HashMap<Elements, List<SpellLevel>>();
        this.m_distributedXpPerElement = new TObjectLongHashMap<Elements>();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        this.m_playerCurrentLevel = localPlayer.getLevel();
        for (final Elements element : elements) {
            this.m_spellsPerElement.put(element, this.createSpellLevels(localPlayer.getBreed(), element));
            this.m_distributedXpPerElement.put(element, 0L);
        }
    }
    
    private List<SpellLevel> createSpellLevels(@NotNull final Breed breed, @NotNull final Elements element) {
        final AvatarBreedInfo breedInfo = AvatarBreedInfoManager.getInstance().getBreedInfo(breed.getBreedId());
        if (breedInfo == null) {
            AbstractSpellRestatComputer.m_logger.error((Object)("Unable to find a valid breedInfo for breed " + breed));
            return Collections.emptyList();
        }
        final SpellElement[] spellsByElementId = breedInfo.getSpellsElement(element);
        final List<SpellLevel> list = new ArrayList<SpellLevel>(spellsByElementId.length);
        for (final SpellElement sp : spellsByElementId) {
            list.add(new SpellLevel(sp.getSpell(), (short)0, 0L));
        }
        Collections.sort(list, new Comparator<SpellLevel>() {
            @Override
            public int compare(final SpellLevel level1, final SpellLevel level2) {
                return level1.getSpell().getUiPosition() - level2.getSpell().getUiPosition();
            }
        });
        return list;
    }
    
    @Override
    public boolean isElementConcerned(@NotNull final Elements element) {
        return this.m_spellsPerElement.containsKey(element);
    }
    
    @Override
    public void reset() {
        this.m_distributedXpPerElement.transformValues(new TLongFunction() {
            @Override
            public long execute(final long value) {
                return 0L;
            }
        });
        for (final Elements element : this.m_spellsPerElement.keySet()) {
            final List<SpellLevel> spellLevels = this.m_spellsPerElement.get(element);
            for (final SpellLevel spellLevel : spellLevels) {
                spellLevel.setLevelAndXp((short)0, 0L);
            }
        }
    }
    
    @Override
    public abstract long getTotalXpToDistribute();
    
    @Override
    public abstract long getTotalXpToDistribute(@Nullable final Elements p0);
    
    @Override
    public long getTotalAlreadyDistributeXp() {
        long xp = 0L;
        final TObjectLongIterator<Elements> elementsIterator = this.m_distributedXpPerElement.iterator();
        while (elementsIterator.hasNext()) {
            elementsIterator.advance();
            xp += elementsIterator.value();
        }
        return xp;
    }
    
    @Override
    public long getAlreadyDistributedXp(@Nullable final Elements element) {
        if (element == null) {
            return this.getTotalAlreadyDistributeXp();
        }
        return this.m_distributedXpPerElement.get(element);
    }
    
    @Override
    public long getRemainingXpToDistribute() {
        return this.getTotalXpToDistribute() - this.getTotalAlreadyDistributeXp();
    }
    
    @Override
    public long getRemainingXpToDistribute(@Nullable final Elements element) {
        return this.getTotalXpToDistribute(element) - this.getAlreadyDistributedXp(element);
    }
    
    @Override
    public boolean isSpellActivated(@NotNull final Elements element, final int spellId) {
        final SpellLevel spellLevel = this.getSpellLevel(element, spellId);
        return spellLevel != null && this.isSpellActivated(element, spellLevel);
    }
    
    protected boolean isSpellActivated(@NotNull final Elements element, @NotNull final SpellLevel spellLevel) {
        return isSpellActivated(spellLevel, this.m_spellsPerElement.get(element));
    }
    
    private static boolean isSpellActivated(@NotNull final SpellLevel spellLevel, @NotNull final Iterable levelsInventory) {
        final SimpleCriterion criterions = spellLevel.getSpell().getLearningCriterions();
        if (criterions == null) {
            return true;
        }
        final int validity = criterions.getValidity(levelsInventory, null, null, null);
        return validity == 0;
    }
    
    @Override
    public boolean canIncrementSpellLevel(@NotNull final Elements element, final int spellId) {
        final SpellLevel spellLevel = this.getSpellLevel(element, spellId);
        return spellLevel != null && this.canIncrementSpellLevel(element, spellLevel);
    }
    
    protected boolean canIncrementSpellLevel(@NotNull final Elements element, @NotNull final SpellLevel spellLevel) {
        if (this.getRemainingXpToDistribute(element) <= 0L) {
            return false;
        }
        final short levelValue = spellLevel.getLevel();
        return levelValue < this.m_playerCurrentLevel && levelValue < spellLevel.getXpTable().getMaxLevel() && this.isSpellActivated(element, spellLevel);
    }
    
    @Override
    public boolean canDecrementSpellLevel(@NotNull final Elements element, final int spellId) {
        final SpellLevel spellLevel = this.getSpellLevel(element, spellId);
        return spellLevel != null && this.canDecrementSpellLevel(element, spellLevel);
    }
    
    public boolean canDecrementSpellLevel(@NotNull final Elements element, @NotNull final SpellLevel spellLevel) {
        final short minLevel = spellLevel.getXpTable().getMinLevel();
        if (spellLevel.getLevel() < minLevel) {
            return false;
        }
        if (spellLevel.getLevel() == minLevel && spellLevel.getXp() == 0L) {
            return false;
        }
        if (!this.isSpellActivated(element, spellLevel)) {
            return false;
        }
        if (MathHelper.isZero(spellLevel.getCurrentLevelPercentage())) {
            final List<SpellLevel> spellLevels = this.m_spellsPerElement.get(element);
            final int initialSpellPosition = spellLevel.getSpell().getUiPosition();
            final boolean[] levelCanBeProblem = new boolean[5];
            final short[] savedLevels = new short[5];
            final long[] savedXp = new long[5];
            for (int i = 0; i < spellLevels.size(); ++i) {
                final SpellLevel sl = spellLevels.get(i);
                savedLevels[i] = sl.getLevel();
                savedXp[i] = sl.getXp();
                levelCanBeProblem[i] = (sl.getSpell().getUiPosition() > initialSpellPosition && sl.getXp() > 0L);
                if (sl == spellLevel) {
                    spellLevel.setLevel((short)(spellLevel.getLevel() - 1), false);
                }
            }
            boolean valid = true;
            for (int j = 4; j > 0; --j) {
                spellLevels.get(j).setLevel((short)0, false);
                if (levelCanBeProblem[j]) {
                    if (!isSpellActivated(spellLevels.get(j), spellLevels)) {
                        valid = false;
                        break;
                    }
                }
            }
            for (int j = 0; j < spellLevels.size(); ++j) {
                final SpellLevel sl2 = spellLevels.get(j);
                sl2.setLevelAndXp(savedLevels[j], savedXp[j]);
            }
            if (!valid) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean canValidateRestat() {
        return this.getRemainingXpToDistribute() <= 1L;
    }
    
    @Override
    public boolean validateRestat() {
        if (!this.canValidateRestat()) {
            return false;
        }
        final SpellsRestatRequestMessage msg = new SpellsRestatRequestMessage();
        for (final Elements element : this.m_spellsPerElement.keySet()) {
            final List<SpellLevel> spellLevels = this.m_spellsPerElement.get(element);
            for (int i = 0; i < spellLevels.size(); ++i) {
                final SpellLevel spellLevel = spellLevels.get(i);
                if (spellLevel.getXp() != 0L) {
                    msg.addXpToSpell(spellLevel.getReferenceId(), spellLevel.getXp());
                }
            }
        }
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
        return true;
    }
    
    @Nullable
    @Override
    public SpellLevel getSpellLevel(@NotNull final Elements element, final int spellId) {
        final List<SpellLevel> spellLevels = this.m_spellsPerElement.get(element);
        if (spellLevels == null) {
            AbstractSpellRestatComputer.m_logger.error((Object)("Unable to find spell " + spellId + " with element " + element + " : element is unknown"));
            return null;
        }
        for (int i = spellLevels.size() - 1; i >= 0; --i) {
            if (spellLevels.get(i).getReferenceId() == spellId) {
                return spellLevels.get(i);
            }
        }
        AbstractSpellRestatComputer.m_logger.error((Object)("Unable to find spell " + spellId + " in element " + element + " : spell is unknown"));
        return null;
    }
    
    protected void registerSpellXpModification(@NotNull final Elements element, @NotNull final SpellLevel spellLevel, final long realXpToAdd, final long virtualXpToAdd) {
        final long newRealXp = spellLevel.getXp() + realXpToAdd;
        final short newLevel = SpellXpTable.getInstance().getLevelByXp(newRealXp);
        final XpModification xpModification = spellLevel.setLevelAndXp(newLevel, newRealXp);
        if (xpModification.getXpDifference() != realXpToAdd) {
            AbstractSpellRestatComputer.m_logger.error((Object)("Unconsistant result. Tried to add " + realXpToAdd + ", but " + xpModification.getXpDifference() + " added"));
        }
        this.m_distributedXpPerElement.adjustValue(element, virtualXpToAdd);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractSpellRestatComputer.class);
    }
}
