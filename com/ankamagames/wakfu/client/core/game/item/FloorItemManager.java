package com.ankamagames.wakfu.client.core.game.item;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.item.*;

public class FloorItemManager extends DroppedItemManager<FloorItem>
{
    protected static final Logger m_logger;
    private static final FloorItemManager m_instance;
    private final TLongObjectHashMap<FloorItem> m_floorItems;
    
    public FloorItemManager() {
        super();
        this.m_floorItems = new TLongObjectHashMap<FloorItem>();
    }
    
    public static FloorItemManager getInstance() {
        return FloorItemManager.m_instance;
    }
    
    @Override
    public void addFloorItem(final FloorItem floorItem) {
        this.m_floorItems.put(floorItem.getId(), floorItem);
        super.addFloorItem(floorItem);
    }
    
    @Override
    public FloorItem getFloorItem(final long itemId) {
        return this.m_floorItems.get(itemId);
    }
    
    @Override
    public void foreachFloorItem(final TObjectProcedure<FloorItem> procedure) {
        this.m_floorItems.forEachValue(procedure);
    }
    
    @Override
    public void silentUnspawnItem(final long itemId) {
        final FloorItem item = this.m_floorItems.remove(itemId);
        if (item != null) {
            FloorItemManager.m_logger.info((Object)("FloorItem despawn : " + item.getId() + " phase = " + item.getPhase() + " lock = " + item.getLock()));
            item.unspawn();
        }
    }
    
    @Override
    public void unspawnItem(final long itemId) {
        this.silentUnspawnItem(itemId);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        return super.onMessage(message);
    }
    
    public void initialize() {
        this.setId(5L);
        this.createClock();
    }
    
    static {
        m_logger = Logger.getLogger((Class)FloorItemManager.class);
        m_instance = new FloorItemManager();
    }
}
