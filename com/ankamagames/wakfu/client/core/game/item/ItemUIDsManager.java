package com.ankamagames.wakfu.client.core.game.item;

import com.ankamagames.framework.kernel.core.common.*;
import org.apache.log4j.*;
import java.util.*;

public class ItemUIDsManager implements LongUIDGenerator
{
    protected static final Logger m_logger;
    private static final ItemUIDsManager m_instance;
    private final ArrayList<Long> m_uids;
    
    public ItemUIDsManager() {
        super();
        this.m_uids = new ArrayList<Long>();
    }
    
    public static ItemUIDsManager getInstance() {
        return ItemUIDsManager.m_instance;
    }
    
    public void addUId(final long uid) {
        this.m_uids.add(uid);
    }
    
    @Override
    public long getNextUID() {
        if (!this.m_uids.isEmpty()) {
            return this.m_uids.remove(0);
        }
        ItemUIDsManager.m_logger.error((Object)" La list d'id unique est vide ");
        return -1L;
    }
    
    public void removeAll() {
        this.m_uids.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemUIDsManager.class);
        m_instance = new ItemUIDsManager();
    }
}
