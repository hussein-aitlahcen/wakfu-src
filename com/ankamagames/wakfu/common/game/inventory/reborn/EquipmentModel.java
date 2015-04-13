package com.ankamagames.wakfu.common.game.inventory.reborn;

import com.ankamagames.wakfu.common.game.inventory.reborn.definition.equipment.*;
import java.util.*;
import gnu.trove.*;

final class EquipmentModel implements EquipmentInventory
{
    private final List<EquipmentListener> m_listeners;
    private final EnumMap<EquipmentPosition, EquipmentItem> m_items;
    
    EquipmentModel() {
        super();
        this.m_listeners = new ArrayList<EquipmentListener>();
        this.m_items = new EnumMap<EquipmentPosition, EquipmentItem>(EquipmentPosition.class);
    }
    
    @Override
    public boolean forEach(final TObjectProcedure<EquipmentItem> procedure) {
        final EquipmentPosition[] positions = EquipmentPosition.values();
        for (int i = 0, length = positions.length; i < length; ++i) {
            final EquipmentItem item = this.m_items.get(positions[i]);
            if (item != null) {
                if (!procedure.execute(item)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public EquipmentItem getItem(final EquipmentPosition position) {
        return this.m_items.get(position);
    }
    
    void add(final EquipmentPosition position, final EquipmentItem item) {
        this.m_items.put(position, item);
        this.fireItemAdded(item);
    }
    
    void remove(final EquipmentPosition position) {
        final EquipmentItem item = this.m_items.remove(position);
        this.fireItemRemoved(item);
    }
    
    void clear() {
        this.m_items.clear();
    }
    
    private void fireItemAdded(final EquipmentItem item) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).itemAdded(item);
        }
    }
    
    private void fireItemRemoved(final EquipmentItem item) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).itemRemoved(item);
        }
    }
    
    @Override
    public boolean addListener(final EquipmentListener listener) {
        return !this.m_listeners.contains(listener) && this.m_listeners.add(listener);
    }
    
    @Override
    public boolean removeListener(final EquipmentListener listener) {
        return this.m_listeners.remove(listener);
    }
    
    @Override
    public String toString() {
        return "EquipmentModel{m_items=" + this.m_items.size() + '}';
    }
}
