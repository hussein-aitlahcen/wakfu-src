package com.ankamagames.wakfu.common.game.guild.storage;

import com.ankamagames.wakfu.common.game.inventory.action.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.common.rawData.*;

public final class GuildStorageCompartment implements ItemInventoryHandler
{
    private static final Logger m_logger;
    private final GuildStorageCompartmentType m_type;
    private final ArrayInventory<Item, RawInventoryItem> m_inventory;
    private int m_id;
    private boolean m_enabled;
    private long m_buildingUid;
    
    public GuildStorageCompartment(final GuildStorageCompartmentType type) {
        super();
        this.m_type = type;
        this.m_inventory = new ArrayInventory<Item, RawInventoryItem>(GuildStorageBoxContentProvider.INSTANCE, GuildStorageBoxContentChecker.INSTANCE, type.m_size, true);
    }
    
    @Override
    public boolean canAdd(final Item item) {
        return this.m_inventory.canAdd(item);
    }
    
    @Override
    public boolean add(final Item item) {
        try {
            return this.canAdd(item) && this.m_inventory.add(item);
        }
        catch (InventoryException e) {
            GuildStorageCompartment.m_logger.error((Object)"[GUILD_STORAGE] Impossible d'ajouter un item \u00e0 l'inventaire alors qu'on \u00e0 pourtant test\u00e9 le canAdd", (Throwable)e);
            return false;
        }
    }
    
    @Override
    public boolean canAdd(final Item item, final byte position) {
        return this.m_inventory.canAdd(item, position);
    }
    
    @Override
    public boolean add(final Item item, final byte position) {
        try {
            return this.canAdd(item, position) && this.m_inventory.addAt(item, position);
        }
        catch (InventoryException e) {
            GuildStorageCompartment.m_logger.error((Object)"[GUILD_STORAGE] Impossible d'ajouter un item \u00e0 l'inventaire alors qu'on \u00e0 pourtant test\u00e9 le canAdd", (Throwable)e);
            return false;
        }
    }
    
    @Override
    public boolean canRemove(final Item item) {
        return this.m_inventory.canRemove(item);
    }
    
    @Override
    public boolean remove(final Item item) {
        return this.canRemove(item) && this.m_inventory.remove(item);
    }
    
    @Override
    public boolean canRemove(final long itemUid, final short qty) {
        return this.m_inventory.canRemove(itemUid, qty);
    }
    
    @Override
    public boolean remove(final long itemUid, final short qty) {
        return this.canRemove(itemUid, qty) && this.m_inventory.updateQuantity(itemUid, (short)(-qty));
    }
    
    @Override
    public boolean isRemote() {
        return true;
    }
    
    @Override
    public Item getItem(final long uniqueId) {
        return this.m_inventory.getWithUniqueId(uniqueId);
    }
    
    @Override
    public Item getItemFromPosition(final byte position) {
        return this.m_inventory.getFromPosition(position);
    }
    
    public int size() {
        return this.m_inventory.size();
    }
    
    public short getMaximumSize() {
        return this.m_inventory.getMaximumSize();
    }
    
    @Override
    public byte getPosition(final long uniqueId) {
        return (byte)this.m_inventory.getPosition(uniqueId);
    }
    
    public Iterator<Item> iterator() {
        return this.m_inventory.iterator(false);
    }
    
    public Iterator<Item> iteratorWithNulls() {
        return this.m_inventory.iterator(true);
    }
    
    public void addObserver(final InventoryObserver observer) {
        this.m_inventory.addObserver(observer);
    }
    
    public void removeObserver(final InventoryObserver observer) {
        this.m_inventory.removeObserver(observer);
    }
    
    public boolean isEnabled() {
        return this.m_enabled;
    }
    
    public long getBuildingUid() {
        return this.m_buildingUid;
    }
    
    public void toRaw(final RawGuildStorageCompartment rawCompartment) {
        rawCompartment.id = this.m_id;
        rawCompartment.enabled = this.m_enabled;
        for (final Item item : this) {
            final RawGuildStorageCompartment.Item rawItem = new RawGuildStorageCompartment.Item();
            rawItem.position = this.getPosition(item.getUniqueId());
            item.toRaw(rawItem.item);
            rawCompartment.items.add(rawItem);
        }
        if (this.m_buildingUid >= 0L) {
            rawCompartment.building = new RawGuildStorageCompartment.Building();
            rawCompartment.building.buildingUid = this.m_buildingUid;
        }
        else {
            rawCompartment.building = null;
        }
    }
    
    public void fromRaw(final RawGuildStorageCompartment rawCompartment) {
        this.m_id = rawCompartment.id;
        this.m_enabled = rawCompartment.enabled;
        this.m_inventory.cleanup();
        final GuildStorageBoxContentProvider provider = (GuildStorageBoxContentProvider)this.m_inventory.getContentProvider();
        for (int i = 0, size = rawCompartment.items.size(); i < size; ++i) {
            final RawGuildStorageCompartment.Item rawItem = rawCompartment.items.get(i);
            try {
                this.m_inventory.addAt(provider.unSerializeContent(rawItem.item), rawItem.position);
            }
            catch (InventoryException e) {
                GuildStorageCompartment.m_logger.error((Object)"[GUILD_STORAGE] Probl\u00e8me de r\u00e9cup\u00e9ration d'un item de compartiment", (Throwable)e);
            }
        }
        this.m_buildingUid = ((rawCompartment.building == null) ? -1L : rawCompartment.building.buildingUid);
    }
    
    public GuildStorageCompartmentType getType() {
        return this.m_type;
    }
    
    @Override
    public String toString() {
        return "GuildStorageCompartment{m_inventory=" + this.m_inventory + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildStorageCompartment.class);
    }
}
