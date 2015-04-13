package com.ankamagames.wakfu.common.game.item;

import org.apache.log4j.*;
import gnu.trove.*;

public class BagStoringManager
{
    private static final Logger m_logger;
    public static final BagStoringManager INSTANCE;
    private final TIntObjectHashMap<int[]> m_bagsItemValidity;
    private final TIntShortHashMap m_bagCapacity;
    
    private BagStoringManager() {
        super();
        this.m_bagsItemValidity = new TIntObjectHashMap<int[]>();
        this.m_bagCapacity = new TIntShortHashMap();
    }
    
    public void put(final int bagReferenceId, final int[] itemTypeValidity, final short capacity) {
        final int[] validities = (int[])((itemTypeValidity == null || itemTypeValidity.length == 0) ? null : itemTypeValidity);
        this.m_bagsItemValidity.put(bagReferenceId, validities);
        this.m_bagCapacity.put(bagReferenceId, capacity);
    }
    
    public int[] getValidTypes(final int bagReferenceId) {
        return this.m_bagsItemValidity.get(bagReferenceId);
    }
    
    public boolean isTypedBag(final int bagReferenceId) {
        return this.getValidTypes(bagReferenceId) != null;
    }
    
    public short getCapacity(final int bagReferenceId) {
        return this.m_bagCapacity.get(bagReferenceId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)BagStoringManager.class);
        INSTANCE = new BagStoringManager();
    }
}
