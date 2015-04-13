package com.ankamagames.baseImpl.common.clientAndServer.game.inventory;

import org.apache.log4j.*;
import gnu.trove.*;

public class ItemExchangerManager
{
    protected static final Logger m_logger;
    private static final ItemExchangerManager m_uniqueInstance;
    private final TLongObjectHashMap<ItemExchanger> m_exchangers;
    private final TLongHashSet m_users;
    
    public static ItemExchangerManager getInstance() {
        return ItemExchangerManager.m_uniqueInstance;
    }
    
    private ItemExchangerManager() {
        super();
        this.m_exchangers = new TLongObjectHashMap<ItemExchanger>();
        this.m_users = new TLongHashSet();
    }
    
    public ItemExchanger getItemExchanger(final long exchangeId) {
        return this.m_exchangers.get(exchangeId);
    }
    
    public boolean addExchanger(final ItemExchanger exchanger) {
        if (this.m_exchangers.containsKey(exchanger.getId())) {
            ItemExchangerManager.m_logger.info((Object)("Impossible d'ajouter l'\u00e9change " + exchanger.getClass().getName() + " : un \u00e9change avec le m\u00eame ID (" + exchanger.getId() + ") existe d\u00e9j\u00e0."));
            return false;
        }
        if (this.m_users.contains(exchanger.getRequesterId())) {
            ItemExchangerManager.m_logger.info((Object)("Impossible d'ajouter l'\u00e9change " + exchanger.getId() + " : un des participants (" + exchanger.getRequesterId() + ") a d\u00e9j\u00e0 un \u00e9change en cours."));
            return false;
        }
        if (this.m_users.contains(exchanger.getTargetId())) {
            ItemExchangerManager.m_logger.info((Object)("Impossible d'ajouter l'\u00e9change " + exchanger.getId() + " : un des participants (" + exchanger.getTargetId() + ") a d\u00e9j\u00e0 un \u00e9change en cours."));
            return false;
        }
        this.m_exchangers.put(exchanger.getId(), exchanger);
        this.m_users.add(exchanger.getRequesterId());
        this.m_users.add(exchanger.getTargetId());
        return true;
    }
    
    public void removeExchanger(final ItemExchanger exchanger) {
        this.m_exchangers.remove(exchanger.getId());
        this.m_users.remove(exchanger.getRequesterId());
        this.m_users.remove(exchanger.getTargetId());
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemExchangerManager.class);
        m_uniqueInstance = new ItemExchangerManager();
    }
}
