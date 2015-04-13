package com.ankamagames.wakfu.common.game.effect.runningEffect.util.summonDouble.spellsFinder;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.summonDouble.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import java.util.*;

public class AllSpellsFinder extends DoubleSpellsFinder
{
    public static final DoubleSpellsFinder INSTANCE;
    
    private DoubleSpellsFinder getFallbackFinder() {
        return HighestSpellsWithSupportFinder.INSTANCE;
    }
    
    private SpellInventory<AbstractSpellLevel> getFallbackSpells(final BasicCharacterInfo doubleModel, final SummonDoubleParams params, final short effectContainerLevel) {
        return this.getFallbackFinder().getSpells(doubleModel, params, effectContainerLevel);
    }
    
    @Override
    public SpellInventory<AbstractSpellLevel> getSpells(final BasicCharacterInfo doubleModel, final SummonDoubleParams params, final short effectContainerLevel) {
        final ShortcutInventory<? extends AbstractShortCutItem> spellsInventory = (ShortcutInventory<? extends AbstractShortCutItem>)doubleModel.getShortcutInventory(ShortCutBarType.FIGHT, (byte)0);
        if (spellsInventory == null) {
            return this.getFallbackSpells(doubleModel, params, effectContainerLevel);
        }
        final SpellInventory<AbstractSpellLevel> casterSpellInventory = (SpellInventory<AbstractSpellLevel>)doubleModel.getSpellInventory();
        final short doubleSpellsMaxCount = params.getMaxSpellsCount();
        final SpellInventory<AbstractSpellLevel> doubleSpellInventory = new SpellInventory<AbstractSpellLevel>(doubleSpellsMaxCount, (InventoryContentProvider<AbstractSpellLevel, RawSpellLevel>)casterSpellInventory.getContentProvider(), (InventoryContentChecker<AbstractSpellLevel>)casterSpellInventory.getContentChecker(), true, false, false);
        for (final AbstractSpellLevel spellLevel : casterSpellInventory) {
            if (spellLevel == null) {
                continue;
            }
            if (spellLevel.getSpell() == null) {
                continue;
            }
            if (spellLevel.getSpell().isPassive()) {
                continue;
            }
            try {
                doubleSpellInventory.add(spellLevel);
            }
            catch (Exception e) {
                AllSpellsFinder.m_logger.error((Object)("Unable to add spellLevel " + spellLevel + "to double"), (Throwable)e);
            }
        }
        if (doubleSpellInventory.isEmpty()) {
            return this.getFallbackSpells(doubleModel, params, effectContainerLevel);
        }
        return doubleSpellInventory;
    }
    
    static {
        INSTANCE = new AllSpellsFinder();
    }
}
