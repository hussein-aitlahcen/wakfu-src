package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

abstract class SpellsNeededForIAEffect extends WakfuRunningEffect
{
    private static final short MAX_SPELLS_TO_USE = 8;
    private static final short MAX_SUPPORT_SPELLS_TO_USE = 2;
    private SpellInventory<AbstractSpellLevel> m_spellInventoryForNewControllerUse;
    public BinarSerialPart m_targetSpellInventory;
    
    SpellsNeededForIAEffect() {
        super();
        this.m_targetSpellInventory = new TargetSpellInventory();
    }
    
    protected void createSpellInventoryForController() {
        final BasicCharacterInfo target = (BasicCharacterInfo)this.m_target;
        final SpellInventory<AbstractSpellLevel> targetSpellInventory = (SpellInventory<AbstractSpellLevel>)target.getSpellInventory();
        final List<AbstractSpellLevel> availableSpells = new ArrayList<AbstractSpellLevel>();
        final List<AbstractSpellLevel> availableSupportSpells = new ArrayList<AbstractSpellLevel>();
        this.fillControllerSpellsWithControlledOnes(targetSpellInventory, availableSpells, true, false);
        this.fillControllerSpellsWithControlledOnes(targetSpellInventory, availableSupportSpells, false, true);
        Collections.sort(availableSpells, LevelableComparator.getInstance());
        Collections.sort(availableSupportSpells, LevelableComparator.getInstance());
        List<AbstractSpellLevel> spellLevels;
        if (availableSupportSpells.size() > 0) {
            spellLevels = (List<AbstractSpellLevel>)availableSupportSpells.subList(0, Math.min(availableSupportSpells.size(), 2));
        }
        else {
            spellLevels = new LinkedList<AbstractSpellLevel>();
        }
        spellLevels.addAll(availableSpells.subList(0, Math.min(availableSpells.size(), 8 - spellLevels.size())));
        this.m_spellInventoryForNewControllerUse = new SpellInventory<AbstractSpellLevel>((short)8, (InventoryContentProvider<AbstractSpellLevel, RawSpellLevel>)targetSpellInventory.getContentProvider(), (InventoryContentChecker<AbstractSpellLevel>)targetSpellInventory.getContentChecker(), false, false, false);
        for (final AbstractSpellLevel abstractSpellLevel : spellLevels) {
            try {
                if (this.m_spellInventoryForNewControllerUse.contains(abstractSpellLevel)) {
                    SpellsNeededForIAEffect.m_logger.warn((Object)("Le sort est deja contenu dans l'inventaire, il faut v\u00e9rifier si son \u00e9l\u00e9ment n'est pas PHYSICAL id : " + abstractSpellLevel.getReferenceId()));
                }
                else {
                    this.m_spellInventoryForNewControllerUse.add(abstractSpellLevel);
                }
            }
            catch (InventoryCapacityReachedException e) {
                SpellsNeededForIAEffect.m_logger.error((Object)"Exception", (Throwable)e);
            }
            catch (ContentAlreadyPresentException e2) {
                SpellsNeededForIAEffect.m_logger.error((Object)"Exception", (Throwable)e2);
            }
        }
    }
    
    private void fillControllerSpellsWithControlledOnes(final SpellInventory<AbstractSpellLevel> targetSpellInventory, final List<AbstractSpellLevel> doubleSpells, final boolean includeElementalSpells, final boolean includeSupportSpells) {
        for (final AbstractSpellLevel spellLevel : targetSpellInventory) {
            final AbstractSpell spell = spellLevel.getSpell();
            final Iterator<WakfuEffect> effectIterator = spell.getEffectsForLevel(this.getContainerLevel());
            final byte elementId = spell.getElementId();
            final Elements element = Elements.getElementFromId(elementId);
            if (element == null) {
                continue;
            }
            if (!includeElementalSpells && element.isElemental()) {
                continue;
            }
            if (!includeSupportSpells && element == Elements.SUPPORT) {
                continue;
            }
            if (spell.isPassive()) {
                continue;
            }
            boolean isSummonSpell = false;
            while (effectIterator.hasNext()) {
                final WakfuEffect effect = effectIterator.next();
                if (effect.getActionId() == RunningEffectConstants.SUMMON_SRAM_DOUBLE.getId() || effect.getActionId() == RunningEffectConstants.SUMMON.getId() || effect.getActionId() == RunningEffectConstants.SUMMON_IMAGE.getId() || effect.getActionId() == RunningEffectConstants.SUMMON_FROM_SYMBIOT.getId()) {
                    isSummonSpell = true;
                    break;
                }
            }
            if (isSummonSpell) {
                continue;
            }
            doubleSpells.add(spellLevel);
        }
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.m_targetSpellInventory;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_targetSpellInventory = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_targetSpellInventory = new TargetSpellInventory();
    }
    
    protected class TargetSpellInventory extends BinarSerialPart
    {
        private RawSpellLevelInventory m_rawSpellLevelInventory;
        
        @Override
        public void serialize(final ByteBuffer buffer) {
            if (this.m_rawSpellLevelInventory == null) {
                buffer.put((byte)0);
                return;
            }
            SpellsNeededForIAEffect.this.m_spellInventoryForNewControllerUse.toRaw(this.m_rawSpellLevelInventory);
            this.m_rawSpellLevelInventory.serialize(buffer);
        }
        
        @Override
        public void unserialize(final ByteBuffer buffer, final int version) {
            final BasicCharacterInfo target = (BasicCharacterInfo)SpellsNeededForIAEffect.this.getTarget();
            if (target.getId() < 0L) {
                buffer.get();
                return;
            }
            (this.m_rawSpellLevelInventory = new RawSpellLevelInventory()).unserialize(buffer);
            target.createSpellInventoryFromRaw(this.m_rawSpellLevelInventory);
        }
        
        @Override
        public int expectedSize() {
            if (SpellsNeededForIAEffect.this.m_target.getId() < 0L) {
                return 1;
            }
            this.m_rawSpellLevelInventory = new RawSpellLevelInventory();
            SpellsNeededForIAEffect.this.m_spellInventoryForNewControllerUse.toRaw(this.m_rawSpellLevelInventory);
            return this.m_rawSpellLevelInventory.serializedSize();
        }
    }
}
