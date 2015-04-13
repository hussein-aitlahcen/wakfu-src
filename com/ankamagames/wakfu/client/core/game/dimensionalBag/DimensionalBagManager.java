package com.ankamagames.wakfu.client.core.game.dimensionalBag;

import java.util.*;

public class DimensionalBagManager
{
    private static final DimensionalBagManager m_instance;
    private final HashMap<Long, DimensionalBagView> m_bags;
    
    public DimensionalBagManager() {
        super();
        this.m_bags = new HashMap<Long, DimensionalBagView>();
    }
    
    public static DimensionalBagManager getInstance() {
        return DimensionalBagManager.m_instance;
    }
    
    public DimensionalBagView get(final long characterId) {
        return this.m_bags.get(characterId);
    }
    
    public void remove(final long characterId) {
        this.m_bags.remove(characterId);
    }
    
    public void add(final DimensionalBagView dimensionalBag) {
        this.m_bags.put(dimensionalBag.getOwnerId(), dimensionalBag);
    }
    
    static {
        m_instance = new DimensionalBagManager();
    }
}
