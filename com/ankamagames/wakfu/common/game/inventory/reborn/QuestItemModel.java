package com.ankamagames.wakfu.common.game.inventory.reborn;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import java.util.*;

final class QuestItemModel implements QuestItem
{
    private final List<QuestItemListener> m_listeners;
    private final BasicReferenceItem m_refItem;
    private short m_quantity;
    
    QuestItemModel(final BasicReferenceItem refItem) {
        super();
        this.m_listeners = new ArrayList<QuestItemListener>();
        this.m_refItem = refItem;
        this.m_quantity = 1;
    }
    
    @Override
    public int getRefId() {
        return this.m_refItem.getId();
    }
    
    @Override
    public short getQuantity() {
        return this.m_quantity;
    }
    
    @Override
    public short getStackMaximumHeight() {
        return this.m_refItem.getStackMaximumHeight();
    }
    
    void setQuantity(final short quantity) {
        final int delta = quantity - this.m_quantity;
        this.m_quantity = quantity;
        this.fireQuantityChanged(delta);
    }
    
    private void fireQuantityChanged(final int delta) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).quantityChanged(this, delta);
        }
    }
    
    @Override
    public boolean addListener(final QuestItemListener listener) {
        return !this.m_listeners.contains(listener) && this.m_listeners.add(listener);
    }
    
    @Override
    public boolean removeListener(final QuestItemListener listener) {
        return this.m_listeners.remove(listener);
    }
    
    @Override
    public String toString() {
        return "QuestItem{m_refId=" + this.m_refItem + ", m_quantity=" + this.m_quantity + '}';
    }
}
