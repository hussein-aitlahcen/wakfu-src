package com.ankamagames.wakfu.client.core.game.specifics;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.wakfu.common.datas.specific.*;

public final class BellaphoneDoubleCharacteristicsImpl extends BellaphoneDoubleCharacteristics
{
    private static final Logger m_logger;
    
    public BellaphoneDoubleCharacteristicsImpl() {
        super();
    }
    
    @Override
    public BellaphoneDoubleCharacteristicsImpl newInstance() {
        return new BellaphoneDoubleCharacteristicsImpl();
    }
    
    public BellaphoneDoubleCharacteristicsImpl(final short id, final String name, final int hp, final short level, final BasicCharacterInfo model, final int doublePower, final SpellInventory<AbstractSpellLevel> spellInventory) {
        super(id, name, hp, level, model, doublePower, spellInventory);
    }
    
    @Override
    public BellaphoneDoubleCharacteristicsImpl newInstance(final short id, final String name, final int hp, final short level, final BasicCharacterInfo model, final int doublePower, final SpellInventory<AbstractSpellLevel> spellInventory) {
        return new BellaphoneDoubleCharacteristicsImpl(id, name, hp, level, model, doublePower, spellInventory);
    }
    
    public BellaphoneDoubleCharacteristicsImpl(final short maximumSpellInventorySize, final InventoryContentProvider<AbstractSpellLevel, RawSpellLevel> contentProvider, final InventoryContentChecker<AbstractSpellLevel> contentChecker, final boolean ordered, final boolean stackable, final boolean serializeQuantity) {
        super(maximumSpellInventorySize, contentProvider, contentChecker, ordered, stackable, serializeQuantity);
    }
    
    @Override
    public BellaphoneDoubleCharacteristicsImpl newInstance(final short maximumSpellInventorySize, final InventoryContentProvider<AbstractSpellLevel, RawSpellLevel> contentProvider, final InventoryContentChecker<AbstractSpellLevel> contentChecker, final boolean ordered, final boolean stackable, final boolean serializeQuantity) {
        return new BellaphoneDoubleCharacteristicsImpl(maximumSpellInventorySize, contentProvider, contentChecker, ordered, stackable, serializeQuantity);
    }
    
    @Override
    public void initializeSummoning(final BasicCharacterInfo summoning, final BasicCharacterInfo summoner) {
        super.initializeSummoning(summoning, summoner);
        final NonPlayerCharacter summon = (NonPlayerCharacter)summoning;
        summon.setGfxId(this.getDoubleGfx());
        summon.setForcedGfxId(this.getDoubleGfx());
        final MonsterSpecialGfx customGfx = new MonsterSpecialGfx();
        customGfx.addEquipement(new MonsterSpecialGfx.Equipment(BreedColorsManager.getInstance().getDressStyle(this.getTypeId(), this.getSex() == 0, this.m_clothIndex), AnmPartHelper.getParts("VETEMENTCUSTOM")));
        summon.setSpecificSpecialGfx(customGfx);
        summon.forceEquipmentAppearance(this.m_equipmentAppearance);
        summon.refreshDisplayEquipment();
    }
    
    private short getDoubleGfx() {
        final int baseID = this.getTypeId() * 10;
        if (this.m_sex == 0) {
            return (short)(baseID + 4);
        }
        return (short)(baseID + 5);
    }
    
    static {
        m_logger = Logger.getLogger((Class)BellaphoneDoubleCharacteristicsImpl.class);
    }
}
