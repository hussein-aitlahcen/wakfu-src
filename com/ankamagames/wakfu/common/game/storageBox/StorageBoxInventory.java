package com.ankamagames.wakfu.common.game.storageBox;

import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.rawData.*;
import gnu.trove.*;

public class StorageBoxInventory
{
    private final TIntObjectHashMap<StorageBoxCompartment> m_compartments;
    
    public StorageBoxInventory(final int numCompartment) {
        super();
        this.m_compartments = new TIntObjectHashMap<StorageBoxCompartment>(numCompartment);
    }
    
    public StorageBoxCompartment get(final int compartmentId) {
        return this.m_compartments.get(compartmentId);
    }
    
    public int size() {
        return this.m_compartments.size();
    }
    
    public TIntObjectIterator<StorageBoxCompartment> compartmentIterator() {
        return this.m_compartments.iterator();
    }
    
    public StorageBoxCompartment createCompartment(final IEStorageBoxParameter.Compartment def) {
        final StorageBoxCompartment storageBoxCompartment = new StorageBoxCompartment(def.getId(), def.getCapacity());
        this.m_compartments.put(def.getId(), storageBoxCompartment);
        return storageBoxCompartment;
    }
    
    public void toRaw(final RawStorageBox rawBox) {
        final TIntObjectIterator<StorageBoxCompartment> it = this.m_compartments.iterator();
        while (it.hasNext()) {
            it.advance();
            final StorageBoxCompartment compartment = it.value();
            final RawStorageBox.Compartment innerCompartment = new RawStorageBox.Compartment();
            compartment.toRaw(innerCompartment.compartment);
            rawBox.compartments.add(innerCompartment);
        }
    }
    
    public void fromRaw(final IEStorageBoxParameter info, final RawStorageBox rawBox) {
        this.m_compartments.clear();
        for (int i = 0, size = rawBox.compartments.size(); i < size; ++i) {
            final RawStorageBox.Compartment innerComp = rawBox.compartments.get(i);
            StorageBoxCompartment compartment = this.m_compartments.get(innerComp.compartment.id);
            if (compartment == null) {
                compartment = this.createCompartment(info.getFromId(innerComp.compartment.id));
            }
            compartment.fromRaw(innerComp.compartment);
        }
    }
    
    @Override
    public String toString() {
        return "StorageBoxInventory{m_compartments=" + this.m_compartments.size() + '}';
    }
}
