package com.ankamagames.wakfu.common.game.interactiveElements.action;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public final class ChangeEquipableDummyItem implements InteractiveElementParametrizedAction
{
    private long m_itemToEquipOnDummy;
    public static final int COSTUME_TYPE_ID = 647;
    public static final int BADGE_TYPE_ID = 646;
    
    ChangeEquipableDummyItem() {
        super();
    }
    
    public ChangeEquipableDummyItem(final long itemToEquipOnDummy) {
        super();
        this.m_itemToEquipOnDummy = itemToEquipOnDummy;
    }
    
    @Override
    public byte[] serialize() {
        final ByteArray ba = new ByteArray();
        ba.putLong(this.m_itemToEquipOnDummy);
        return ba.toArray();
    }
    
    @Override
    public void unserialize(final ByteBuffer bb) {
        this.m_itemToEquipOnDummy = bb.getLong();
    }
    
    @Override
    public short getId() {
        return 2;
    }
    
    public long getItemToEquipOnDummy() {
        return this.m_itemToEquipOnDummy;
    }
}
