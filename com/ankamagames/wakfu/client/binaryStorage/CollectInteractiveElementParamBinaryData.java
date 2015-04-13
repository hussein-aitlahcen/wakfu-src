package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class CollectInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_visualId;
    protected short m_capacity;
    protected boolean m_locked;
    protected int m_cashQty;
    protected CollectItem[] m_items;
    protected CollectAction[] m_actions;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getVisualId() {
        return this.m_visualId;
    }
    
    public short getCapacity() {
        return this.m_capacity;
    }
    
    public boolean isLocked() {
        return this.m_locked;
    }
    
    public int getCashQty() {
        return this.m_cashQty;
    }
    
    public CollectItem[] getItems() {
        return this.m_items;
    }
    
    public CollectAction[] getActions() {
        return this.m_actions;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_visualId = 0;
        this.m_capacity = 0;
        this.m_locked = false;
        this.m_cashQty = 0;
        this.m_items = null;
        this.m_actions = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_visualId = buffer.getInt();
        this.m_capacity = buffer.getShort();
        this.m_locked = buffer.readBoolean();
        this.m_cashQty = buffer.getInt();
        final int itemCount = buffer.getInt();
        this.m_items = new CollectItem[itemCount];
        for (int iItem = 0; iItem < itemCount; ++iItem) {
            (this.m_items[iItem] = new CollectItem()).read(buffer);
        }
        final int actionCount = buffer.getInt();
        this.m_actions = new CollectAction[actionCount];
        for (int iAction = 0; iAction < actionCount; ++iAction) {
            (this.m_actions[iAction] = new CollectAction()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.COLLECT_IE_PARAM.getId();
    }
    
    public static class CollectItem
    {
        protected int m_uid;
        protected int m_itemId;
        protected int m_qty;
        
        public int getUid() {
            return this.m_uid;
        }
        
        public int getItemId() {
            return this.m_itemId;
        }
        
        public int getQty() {
            return this.m_qty;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_uid = buffer.getInt();
            this.m_itemId = buffer.getInt();
            this.m_qty = buffer.getInt();
        }
    }
    
    public static class CollectAction
    {
        protected int m_actionId;
        protected int m_actionType;
        protected String[] m_params;
        protected String m_criteria;
        
        public int getActionId() {
            return this.m_actionId;
        }
        
        public int getActionType() {
            return this.m_actionType;
        }
        
        public String[] getParams() {
            return this.m_params;
        }
        
        public String getCriteria() {
            return this.m_criteria;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_actionId = buffer.getInt();
            this.m_actionType = buffer.getInt();
            this.m_params = buffer.readStringArray();
            this.m_criteria = buffer.readUTF8().intern();
        }
    }
}
