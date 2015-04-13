package com.ankamagames.wakfu.common.game.effect.runningEffect.util.summonDouble.spellsFinder;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.summonDouble.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.spell.*;

public class HighestSpellsElementalOnlyFinder extends DoubleSpellsFinder
{
    public static final DoubleSpellsFinder INSTANCE;
    
    @Override
    public SpellInventory<AbstractSpellLevel> getSpells(final BasicCharacterInfo doubleModel, final SummonDoubleParams params, final short effectContainerLevel) {
        final SpellInventory<AbstractSpellLevel> modelSpellInventory = (SpellInventory<AbstractSpellLevel>)doubleModel.getSpellInventory();
        final LinkedList<AbstractSpellLevel> doubleSpells = new LinkedList<AbstractSpellLevel>();
        for (final AbstractSpellLevel spellLevel : modelSpellInventory) {
            final AbstractSpell spell = spellLevel.getSpell();
            Iterator<WakfuEffect> effectIterator;
            boolean isSummonSpell;
            WakfuEffect effect;
            for (effectIterator = spell.getEffectsForLevel(effectContainerLevel), isSummonSpell = false; effectIterator.hasNext() && !isSummonSpell; isSummonSpell = true) {
                effect = effectIterator.next();
                if (effect.getActionId() == RunningEffectConstants.SUMMON_SRAM_DOUBLE.getId() || effect.getActionId() == RunningEffectConstants.SUMMON.getId() || effect.getActionId() == RunningEffectConstants.SUMMON_FROM_SYMBIOT.getId()) {}
            }
            if (isSummonSpell) {
                continue;
            }
            final byte elementId = spell.getElementId();
            final Elements element = Elements.getElementFromId(elementId);
            if (element == null) {
                continue;
            }
            if (!element.isElemental()) {
                continue;
            }
            doubleSpells.add(spellLevel);
        }
        Collections.sort(doubleSpells, LevelableComparator.getInstance());
        final short maxSpellsCount = params.getMaxSpellsCount();
        final SpellInventory<AbstractSpellLevel> doubleSpellInventory = new SpellInventory<AbstractSpellLevel>(maxSpellsCount, (InventoryContentProvider<AbstractSpellLevel, RawSpellLevel>)modelSpellInventory.getContentProvider(), (InventoryContentChecker<AbstractSpellLevel>)modelSpellInventory.getContentChecker(), false, false, false);
        for (int i = 0; i < maxSpellsCount && i < doubleSpells.size(); ++i) {
            try {
                doubleSpellInventory.add((AbstractSpellLevel)doubleSpells.get(i));
            }
            catch (InventoryCapacityReachedException e) {
                HighestSpellsElementalOnlyFinder.m_logger.error((Object)"Exception", (Throwable)e);
            }
            catch (ContentAlreadyPresentException e2) {
                HighestSpellsElementalOnlyFinder.m_logger.error((Object)"Exception", (Throwable)e2);
            }
        }
        return doubleSpellInventory;
    }
    
    static {
        INSTANCE = new HighestSpellsElementalOnlyFinder();
    }
}
