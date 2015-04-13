package com.ankamagames.wakfu.client.core.game.specifics;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.wakfu.common.datas.specific.*;

public final class IceStatueDoubleCharacteristicsImpl extends IceStatueDoubleCharacteristics
{
    public IceStatueDoubleCharacteristicsImpl() {
        super();
    }
    
    @Override
    public IceStatueDoubleCharacteristicsImpl newInstance() {
        return new IceStatueDoubleCharacteristicsImpl();
    }
    
    public IceStatueDoubleCharacteristicsImpl(final short id, final String name, final int hp, final short level, final BasicCharacterInfo model, final int doublePower, final SpellInventory<AbstractSpellLevel> spellInventory) {
        super(id, name, hp, level, model, doublePower, spellInventory);
    }
    
    @Override
    public IceStatueDoubleCharacteristicsImpl newInstance(final short id, final String name, final int hp, final short level, final BasicCharacterInfo model, final int doublePower, final SpellInventory<AbstractSpellLevel> spellInventory) {
        return new IceStatueDoubleCharacteristicsImpl(id, name, hp, level, model, doublePower, spellInventory);
    }
    
    public IceStatueDoubleCharacteristicsImpl(final short maximumSpellInventorySize, final InventoryContentProvider<AbstractSpellLevel, RawSpellLevel> contentProvider, final InventoryContentChecker<AbstractSpellLevel> contentChecker, final boolean ordered, final boolean stackable, final boolean serializeQuantity) {
        super(maximumSpellInventorySize, contentProvider, contentChecker, ordered, stackable, serializeQuantity);
    }
    
    @Override
    public IceStatueDoubleCharacteristicsImpl newInstance(final short maximumSpellInventorySize, final InventoryContentProvider<AbstractSpellLevel, RawSpellLevel> contentProvider, final InventoryContentChecker<AbstractSpellLevel> contentChecker, final boolean ordered, final boolean stackable, final boolean serializeQuantity) {
        return new IceStatueDoubleCharacteristicsImpl(maximumSpellInventorySize, contentProvider, contentChecker, ordered, stackable, serializeQuantity);
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
    
    private int getDoubleGfx() {
        int baseId = 1;
        baseId *= 1000;
        baseId += this.getTypeId() * 10 + this.m_sex;
        baseId *= 100;
        return ++baseId;
    }
}
