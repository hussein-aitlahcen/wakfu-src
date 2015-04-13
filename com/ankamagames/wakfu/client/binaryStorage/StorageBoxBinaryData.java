package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class StorageBoxBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_visualId;
    protected ChaosParamBinaryData m_chaosParams;
    protected Compartment[] m_compartments;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getVisualId() {
        return this.m_visualId;
    }
    
    public ChaosParamBinaryData getChaosParams() {
        return this.m_chaosParams;
    }
    
    public Compartment[] getCompartments() {
        return this.m_compartments;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_visualId = 0;
        this.m_chaosParams = null;
        this.m_compartments = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_visualId = buffer.getInt();
        if (buffer.get() != 0) {
            (this.m_chaosParams = new ChaosParamBinaryData()).read(buffer);
        }
        else {
            this.m_chaosParams = null;
        }
        final int compartmentCount = buffer.getInt();
        this.m_compartments = new Compartment[compartmentCount];
        for (int iCompartment = 0; iCompartment < compartmentCount; ++iCompartment) {
            (this.m_compartments[iCompartment] = new Compartment()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.STORAGE_BOX.getId();
    }
    
    public static class Compartment
    {
        protected int m_uid;
        protected int m_boxId;
        protected int m_unlockItemId;
        protected byte m_capacity;
        protected int m_compartmentOrder;
        
        public int getUid() {
            return this.m_uid;
        }
        
        public int getBoxId() {
            return this.m_boxId;
        }
        
        public int getUnlockItemId() {
            return this.m_unlockItemId;
        }
        
        public byte getCapacity() {
            return this.m_capacity;
        }
        
        public int getCompartmentOrder() {
            return this.m_compartmentOrder;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_uid = buffer.getInt();
            this.m_boxId = buffer.getInt();
            this.m_unlockItemId = buffer.getInt();
            this.m_capacity = buffer.get();
            this.m_compartmentOrder = buffer.getInt();
        }
    }
}
