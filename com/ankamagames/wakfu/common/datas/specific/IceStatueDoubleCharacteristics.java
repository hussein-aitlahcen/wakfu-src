package com.ankamagames.wakfu.common.datas.specific;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class IceStatueDoubleCharacteristics extends DoubleInvocationCharacteristics
{
    private static IceStatueDoubleCharacteristics m_instance;
    
    public IceStatueDoubleCharacteristics() {
        super();
    }
    
    public IceStatueDoubleCharacteristics(final short id, final String name, final int hp, final short level, final BasicCharacterInfo model, final int doublePower, final SpellInventory<AbstractSpellLevel> spellInventory) {
        super(id, name, hp, level, model, doublePower, spellInventory);
    }
    
    public IceStatueDoubleCharacteristics(final short maximumSpellInventorySize, final InventoryContentProvider<AbstractSpellLevel, RawSpellLevel> contentProvider, final InventoryContentChecker<AbstractSpellLevel> contentChecker, final boolean ordered, final boolean stackable, final boolean serializeQuantity) {
        super(maximumSpellInventorySize, contentProvider, contentChecker, ordered, stackable, serializeQuantity);
    }
    
    public static IceStatueDoubleCharacteristics getDefaultInstance() {
        return IceStatueDoubleCharacteristics.m_instance;
    }
    
    public static void setDefaultInstance(final IceStatueDoubleCharacteristics instance) {
        IceStatueDoubleCharacteristics.m_instance = instance;
    }
    
    @Override
    protected byte getDoubleType() {
        return 3;
    }
}
