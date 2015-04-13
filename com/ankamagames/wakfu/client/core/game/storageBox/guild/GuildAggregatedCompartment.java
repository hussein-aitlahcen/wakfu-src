package com.ankamagames.wakfu.client.core.game.storageBox.guild;

import com.ankamagames.wakfu.common.game.inventory.action.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.guild.storage.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import org.jetbrains.annotations.*;

public class GuildAggregatedCompartment implements ItemInventoryHandler
{
    private final ArrayList<GuildStorageCompartment> m_inventories;
    private short m_maximumSize;
    
    public GuildAggregatedCompartment() {
        super();
        this.m_inventories = new ArrayList<GuildStorageCompartment>();
    }
    
    public void addInventoryHandler(final GuildStorageCompartment handler) {
        this.m_inventories.add(handler);
        this.m_maximumSize += handler.getMaximumSize();
    }
    
    public void clearInventories() {
        this.m_inventories.clear();
        this.m_maximumSize = 0;
    }
    
    public ArrayList<GuildStorageCompartment> getInventories() {
        return this.m_inventories;
    }
    
    public boolean isEnabled() {
        for (int i = 0, size = this.m_inventories.size(); i < size; ++i) {
            final GuildStorageCompartment compartment = this.m_inventories.get(i);
            if (compartment.isEnabled()) {
                return true;
            }
        }
        return false;
    }
    
    public short getMaximumSize() {
        return this.m_maximumSize;
    }
    
    @Override
    public boolean isRemote() {
        return true;
    }
    
    GuildStorageCompartment getCompartmentFromIndex(final byte index) {
        int totalSize = 0;
        for (int i = 0, size = this.m_inventories.size(); i < size; ++i) {
            final GuildStorageCompartment compartment = this.m_inventories.get(i);
            totalSize += compartment.getMaximumSize();
            if (index < totalSize) {
                return compartment;
            }
        }
        return null;
    }
    
    byte getCompartmentIndexFromIndex(final byte index) {
        int totalSize = 0;
        for (int i = 0, size = this.m_inventories.size(); i < size; ++i) {
            final GuildStorageCompartment compartment = this.m_inventories.get(i);
            if (index < totalSize + compartment.getMaximumSize()) {
                return MathHelper.ensureByte(index - totalSize);
            }
            totalSize += compartment.getMaximumSize();
        }
        return -1;
    }
    
    byte getFirstAvailableIndex() {
        byte totalOffset = 0;
        for (int i = 0, size = this.m_inventories.size(); i < size; ++i) {
            final GuildStorageCompartment compartment = this.m_inventories.get(i);
            for (byte j = 0; j < compartment.getMaximumSize(); ++j) {
                if (compartment.getItemFromPosition(j) == null) {
                    return MathHelper.ensureByte(j + totalOffset);
                }
            }
            totalOffset += (byte)compartment.getMaximumSize();
        }
        return -1;
    }
    
    byte getFirstAvailableIndexFor(final Item item, final short quantity) {
        byte totalOffset = 0;
        byte candidate = -1;
        for (int i = 0, size = this.m_inventories.size(); i < size; ++i) {
            final GuildStorageCompartment compartment = this.m_inventories.get(i);
            for (byte j = 0; j < compartment.getMaximumSize(); ++j) {
                final Item itemAtPos = compartment.getItemFromPosition(j);
                final byte currentIndex = MathHelper.ensureByte(j + totalOffset);
                if (itemAtPos != null) {
                    if (itemAtPos.canStackWith(item)) {
                        if (itemAtPos.getStackMaximumHeight() != itemAtPos.getQuantity()) {
                            if (itemAtPos.getQuantity() + quantity <= itemAtPos.getStackMaximumHeight()) {
                                return currentIndex;
                            }
                            candidate = currentIndex;
                        }
                    }
                }
                else if (candidate == -1) {
                    candidate = currentIndex;
                }
            }
            totalOffset += (byte)compartment.getMaximumSize();
        }
        return candidate;
    }
    
    @Override
    public boolean add(final Item item) {
        for (int i = 0, size = this.m_inventories.size(); i < size; ++i) {
            if (this.m_inventories.get(i).add(item)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean canAdd(final Item item) {
        for (int i = 0, size = this.m_inventories.size(); i < size; ++i) {
            if (this.m_inventories.get(i).canAdd(item)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean canAdd(final Item item, final byte position) {
        final GuildStorageCompartment compartment = this.getCompartmentFromIndex(position);
        final byte index = this.getCompartmentIndexFromIndex(position);
        return compartment.canAdd(item, index);
    }
    
    @Override
    public boolean add(final Item item, final byte position) {
        final GuildStorageCompartment compartment = this.getCompartmentFromIndex(position);
        final byte index = this.getCompartmentIndexFromIndex(position);
        return compartment.add(item, index);
    }
    
    @Nullable
    @Override
    public Item getItem(final long uniqueId) {
        for (int i = 0, size = this.m_inventories.size(); i < size; ++i) {
            final Item item = this.m_inventories.get(i).getItem(uniqueId);
            if (item != null) {
                return item;
            }
        }
        return null;
    }
    
    @Nullable
    @Override
    public Item getItemFromPosition(final byte position) {
        final GuildStorageCompartment compartment = this.getCompartmentFromIndex(position);
        final byte index = this.getCompartmentIndexFromIndex(position);
        return compartment.getItemFromPosition(index);
    }
    
    @Override
    public byte getPosition(final long uniqueId) {
        byte totalPosition = 0;
        for (int i = 0, size = this.m_inventories.size(); i < size; ++i) {
            final GuildStorageCompartment inventory = this.m_inventories.get(i);
            final byte position = inventory.getPosition(uniqueId);
            if (position != -1) {
                return MathHelper.ensureByte(totalPosition + position);
            }
            totalPosition += (byte)inventory.getMaximumSize();
        }
        return -1;
    }
    
    @Override
    public boolean canRemove(final long itemUid, final short qty) {
        for (int i = 0, size = this.m_inventories.size(); i < size; ++i) {
            if (this.m_inventories.get(i).canRemove(itemUid, qty)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean canRemove(final Item item) {
        for (int i = 0, size = this.m_inventories.size(); i < size; ++i) {
            if (this.m_inventories.get(i).canRemove(item)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean remove(final long itemUid, final short qty) {
        for (int i = 0, size = this.m_inventories.size(); i < size; ++i) {
            if (this.m_inventories.get(i).remove(itemUid, qty)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean remove(final Item item) {
        for (int i = 0, size = this.m_inventories.size(); i < size; ++i) {
            if (this.m_inventories.get(i).remove(item)) {
                return true;
            }
        }
        return false;
    }
}
