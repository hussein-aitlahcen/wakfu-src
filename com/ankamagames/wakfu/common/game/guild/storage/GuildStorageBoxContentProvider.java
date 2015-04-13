package com.ankamagames.wakfu.common.game.guild.storage;

import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class GuildStorageBoxContentProvider implements InventoryContentProvider<Item, RawInventoryItem>
{
    public static final GuildStorageBoxContentProvider INSTANCE;
    private final ItemProvider m_itemProvider;
    private final RawInventoryItemProvider m_rawItemProvider;
    
    private GuildStorageBoxContentProvider() {
        super();
        this.m_itemProvider = ReferenceItemManager.getInstance();
        this.m_rawItemProvider = ReferenceItemManager.getInstance();
    }
    
    GuildStorageBoxContentProvider(final ItemProvider itemProvider, final RawInventoryItemProvider rawItemProvider) {
        super();
        this.m_itemProvider = itemProvider;
        this.m_rawItemProvider = rawItemProvider;
    }
    
    @Nullable
    public RawInventoryItem serializeContent(final RawConvertible<RawInventoryItem> item) {
        final RawInventoryItem raw = this.createRawItem();
        return item.toRaw(raw) ? raw : null;
    }
    
    @Nullable
    @Override
    public Item unSerializeContent(final RawInventoryItem rawItem) {
        final Item item = this.createItem();
        return item.fromRaw(rawItem) ? item : null;
    }
    
    public Item createItem() {
        return this.m_itemProvider.createItem();
    }
    
    public RawInventoryItem createRawItem() {
        return this.m_rawItemProvider.createRawInventoryItem();
    }
    
    @Override
    public String toString() {
        return "GuildStorageBoxContentProvider{m_itemProvider=" + this.m_itemProvider.getClass().getName() + ", m_rawItemProvider=" + this.m_rawItemProvider.getClass().getName() + '}';
    }
    
    static {
        INSTANCE = new GuildStorageBoxContentProvider();
    }
}
