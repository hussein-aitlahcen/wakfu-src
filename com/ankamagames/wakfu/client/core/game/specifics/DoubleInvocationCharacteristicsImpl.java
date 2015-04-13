package com.ankamagames.wakfu.client.core.game.specifics;

import com.ankamagames.wakfu.common.datas.specific.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import java.util.*;

public class DoubleInvocationCharacteristicsImpl extends DoubleInvocationCharacteristics
{
    protected static final Logger m_logger;
    
    public DoubleInvocationCharacteristicsImpl() {
        super();
    }
    
    @Override
    public DoubleInvocationCharacteristics newInstance() {
        return new DoubleInvocationCharacteristicsImpl();
    }
    
    public DoubleInvocationCharacteristicsImpl(final short id, final String name, final int hp, final short level, final BasicCharacterInfo summoner, final int doublePower, final SpellInventory<AbstractSpellLevel> spellInventory) {
        super(id, name, hp, level, summoner, doublePower, spellInventory);
    }
    
    @Override
    public DoubleInvocationCharacteristicsImpl newInstance(final short id, final String name, final int hp, final short level, final BasicCharacterInfo model, final int doublePower, final SpellInventory<AbstractSpellLevel> spellInventory) {
        return new DoubleInvocationCharacteristicsImpl(id, name, hp, level, model, doublePower, spellInventory);
    }
    
    public DoubleInvocationCharacteristicsImpl(final short maximumSpellInventorySize, final InventoryContentProvider<AbstractSpellLevel, RawSpellLevel> contentProvider, final InventoryContentChecker<AbstractSpellLevel> contentChecker, final boolean ordered, final boolean stackable, final boolean serializeQuantity) {
        super(maximumSpellInventorySize, contentProvider, contentChecker, ordered, stackable, serializeQuantity);
    }
    
    @Override
    public DoubleInvocationCharacteristics newInstance(final short maximumSpellInventorySize, final InventoryContentProvider<AbstractSpellLevel, RawSpellLevel> contentProvider, final InventoryContentChecker<AbstractSpellLevel> contentChecker, final boolean ordered, final boolean stackable, final boolean serializeQuantity) {
        return new DoubleInvocationCharacteristicsImpl(maximumSpellInventorySize, contentProvider, contentChecker, ordered, stackable, serializeQuantity);
    }
    
    @Override
    public void initializeSummoning(final BasicCharacterInfo bciSummoning, final BasicCharacterInfo bciSummoner) {
        super.initializeSummoning(bciSummoning, bciSummoner);
        this.initializeAppearance((NonPlayerCharacter)bciSummoning, (CharacterInfo)bciSummoner);
    }
    
    public void initializeAppearance(final NonPlayerCharacter summonning, final CharacterInfo summoner) {
        AppearanceCopy.copySummonerAppearance(summonning, summoner, this.m_sex, this.getTypeId());
    }
    
    @Override
    protected void initializeDoubleSpells(final BasicCharacterInfo bciSummoning) {
        super.initializeDoubleSpells(bciSummoning);
        final NonPlayerCharacter summoning = (NonPlayerCharacter)bciSummoning;
        final NPCSpellInventoryManager spellInventoryManager = summoning.getSpellInventoryManager();
        if (spellInventoryManager == null) {
            return;
        }
        final double powerRatio = this.m_power / 100.0f;
        if (this.m_spellInventory == null) {
            return;
        }
        for (final AbstractSpellLevel spellLevel : this.m_spellInventory) {
            final SpellLevel summoningSpellLevel = (SpellLevel)spellLevel.getClone();
            summoningSpellLevel.setLevel((short)(spellLevel.getLevel() * powerRatio), true);
            spellInventoryManager.addSpellLevel(summoningSpellLevel);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)DoubleInvocationCharacteristicsImpl.class);
    }
}
