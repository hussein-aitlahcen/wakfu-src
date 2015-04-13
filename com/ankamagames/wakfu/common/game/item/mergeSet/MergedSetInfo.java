package com.ankamagames.wakfu.common.game.item.mergeSet;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;
import java.nio.*;

public final class MergedSetInfo
{
    private final List<Item> m_items;
    
    public MergedSetInfo() {
        super();
        this.m_items = new ArrayList<Item>();
    }
    
    public MergedSetInfo(final MergedSetInfo mergedSetItems) {
        super();
        (this.m_items = new ArrayList<Item>()).addAll(mergedSetItems.m_items);
    }
    
    public void add(final Item item) {
        if (this.m_items.contains(item)) {
            return;
        }
        this.m_items.add(item);
    }
    
    public int size() {
        return this.m_items.size();
    }
    
    public List<Item> getItems() {
        return Collections.unmodifiableList((List<? extends Item>)this.m_items);
    }
    
    public void toRaw(final RawMergedItems rawMergedItems) {
        rawMergedItems.version = Version.SERIALIZATION_VERSION;
        final ByteArray ba = new ByteArray();
        ba.put((byte)this.m_items.size());
        for (final Item item : this.m_items) {
            final byte[] serialize = item.serialize();
            if (serialize == null) {
                continue;
            }
            ba.putInt(serialize.length);
            ba.put(serialize);
        }
        rawMergedItems.items = ba.toArray();
    }
    
    @Override
    public String toString() {
        return "MergedSetInfo{m_items=" + this.m_items + '}';
    }
    
    public static MergedSetInfo fromRaw(final RawMergedItems mergedItems) {
        final MergedSetInfo res = new MergedSetInfo();
        final int version = mergedItems.version;
        final byte[] items = mergedItems.items;
        final ByteBuffer bb = ByteBuffer.wrap(items);
        final byte itemCount = bb.get();
        for (int i = 0; i < itemCount; ++i) {
            final int serializedSize = bb.getInt();
            final byte[] serializedItem = new byte[serializedSize];
            bb.get(serializedItem);
            final Item item = new Item();
            item.unserialize(version, ByteBuffer.wrap(serializedItem));
            res.m_items.add(item);
        }
        return res;
    }
}
