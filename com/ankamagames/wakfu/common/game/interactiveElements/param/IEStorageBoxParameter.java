package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.chaos.*;
import gnu.trove.*;

public class IEStorageBoxParameter extends IEParameter
{
    private final TIntObjectHashMap<Compartment> m_compartments;
    private final TIntArrayList m_order;
    
    public IEStorageBoxParameter(final int paramId, final int visualId, final ChaosInteractiveCategory chaosInteractiveCategory, final int chaosCollectorParamId) {
        super(paramId, visualId, chaosInteractiveCategory, chaosCollectorParamId);
        this.m_compartments = new TIntObjectHashMap<Compartment>();
        this.m_order = new TIntArrayList();
    }
    
    public void addCompartment(final Compartment compartment) {
        this.m_compartments.put(compartment.getId(), compartment);
        this.m_order.add(compartment.getId());
    }
    
    public TIntObjectIterator<Compartment> compartmentIterator() {
        return this.m_compartments.iterator();
    }
    
    public int size() {
        return this.m_compartments.size();
    }
    
    public Compartment getFromId(final int compartmentId) {
        return this.m_compartments.get(compartmentId);
    }
    
    public Compartment getFromIndex(final int index) {
        return this.m_compartments.get(this.m_order.get(index));
    }
    
    public int getCompartmentSize() {
        return this.m_compartments.size();
    }
    
    public static class Compartment
    {
        private final int m_id;
        private final byte m_capacity;
        private final int m_unlockRefItemId;
        
        public Compartment(final int id, final byte capacity, final int unlockRefItemId) {
            super();
            this.m_id = id;
            this.m_capacity = capacity;
            this.m_unlockRefItemId = unlockRefItemId;
        }
        
        public int getId() {
            return this.m_id;
        }
        
        public byte getCapacity() {
            return this.m_capacity;
        }
        
        public int getUnlockRefItemId() {
            return this.m_unlockRefItemId;
        }
    }
}
