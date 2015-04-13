package com.ankamagames.wakfu.common.game.effect.runningEffect.util.summonDouble.spellsFinder;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.summonDouble.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;

public class HighestSpellsWithSupportFinder extends DoubleSpellsFinder
{
    public static final DoubleSpellsFinder INSTANCE;
    
    @Override
    public SpellInventory<AbstractSpellLevel> getSpells(final BasicCharacterInfo doubleModel, final SummonDoubleParams params, final short effectContainerLevel) {
        final SpellInventory<AbstractSpellLevel> casterSpellInventory = (SpellInventory<AbstractSpellLevel>)doubleModel.getSpellInventory();
        final List<AbstractSpellLevel> doubleSpells = new LinkedList<AbstractSpellLevel>();
        final AbstractList<AbstractSpellLevel> supportSpells = new LinkedList<AbstractSpellLevel>();
        for (final AbstractSpellLevel spellLevel : casterSpellInventory) {
            final AbstractSpell spell = spellLevel.getSpell();
            final byte elementId = spell.getElementId();
            final Elements element = Elements.getElementFromId(elementId);
            if (element == null) {
                continue;
            }
            if (element.isElemental()) {
                doubleSpells.add(spellLevel);
            }
            else {
                if (element != Elements.SUPPORT || spell.isPassive()) {
                    continue;
                }
                supportSpells.add(spellLevel);
            }
        }
        Collections.sort(doubleSpells, LevelableComparator.getInstance());
        Collections.sort(supportSpells, LevelableComparator.getInstance());
        final SpellInventory<AbstractSpellLevel> doubleSpellInventory = new SpellInventory<AbstractSpellLevel>(params.getMaxSpellsCount(), (InventoryContentProvider<AbstractSpellLevel, RawSpellLevel>)casterSpellInventory.getContentProvider(), (InventoryContentChecker<AbstractSpellLevel>)casterSpellInventory.getContentChecker(), false, false, false);
        final List<AbstractSpellLevel> supportSpellsSubList = (List<AbstractSpellLevel>)supportSpells.subList(0, Math.min(supportSpells.size(), params.getMaxSupportSpellsCount()));
        addToInventory(supportSpellsSubList, doubleSpellInventory, params.getMaxSpellsCount());
        addToInventory(doubleSpells, doubleSpellInventory, params.getMaxSpellsCount());
        return doubleSpellInventory;
    }
    
    private static void addToInventory(final List<AbstractSpellLevel> doubleSpells, final StackInventory doubleSpellInventory, final short maxSpellsCount) {
        for (int i = 0, n = doubleSpells.size(); i < n && doubleSpellInventory.size() < maxSpellsCount; ++i) {
            final AbstractSpellLevel abstractSpellLevel = doubleSpells.get(i);
            try {
                doubleSpellInventory.add(abstractSpellLevel);
            }
            catch (InventoryCapacityReachedException e) {
                HighestSpellsWithSupportFinder.m_logger.error((Object)"Exception", (Throwable)e);
            }
            catch (ContentAlreadyPresentException e2) {
                HighestSpellsWithSupportFinder.m_logger.error((Object)"Exception", (Throwable)e2);
            }
        }
    }
    
    static {
        INSTANCE = new HighestSpellsWithSupportFinder();
    }
}
