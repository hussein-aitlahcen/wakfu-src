package com.ankamagames.wakfu.common.game.inventory.action;

import com.ankamagames.framework.kernel.core.common.*;
import java.nio.*;

public class InventoryRemoveMoneyAction implements InventoryAction
{
    public static final SimpleObjectFactory<InventoryAction> FACTORY;
    private int m_kamas;
    
    public InventoryRemoveMoneyAction(final int kamas) {
        super();
        this.m_kamas = kamas;
    }
    
    private InventoryRemoveMoneyAction() {
        super();
    }
    
    public int getKamas() {
        return this.m_kamas;
    }
    
    @Override
    public int serializedSize() {
        return 4;
    }
    
    @Override
    public void serializeIn(final ByteBuffer buffer) {
        buffer.putInt(this.m_kamas);
    }
    
    @Override
    public void unSerializeFrom(final ByteBuffer buffer) {
        this.m_kamas = buffer.getInt();
    }
    
    @Override
    public InventoryActionType getType() {
        return InventoryActionType.REMOVE_MONEY;
    }
    
    @Override
    public String toString() {
        return "InventoryRemoveMoneyAction{m_kamas=" + this.m_kamas + '}';
    }
    
    static {
        FACTORY = new SimpleObjectFactory<InventoryAction>() {
            @Override
            public InventoryAction createNew() {
                return new InventoryRemoveMoneyAction(null);
            }
        };
    }
}
