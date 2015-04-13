package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class ClientEventBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_type;
    protected short m_dropRate;
    protected short m_maxCount;
    protected String m_criterion;
    protected String[] m_filters;
    protected boolean m_activeOnStart;
    protected EventActionGroup[] m_actionGroups;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getType() {
        return this.m_type;
    }
    
    public short getDropRate() {
        return this.m_dropRate;
    }
    
    public short getMaxCount() {
        return this.m_maxCount;
    }
    
    public String getCriterion() {
        return this.m_criterion;
    }
    
    public String[] getFilters() {
        return this.m_filters;
    }
    
    public boolean isActiveOnStart() {
        return this.m_activeOnStart;
    }
    
    public EventActionGroup[] getActionGroups() {
        return this.m_actionGroups;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_type = 0;
        this.m_dropRate = 0;
        this.m_maxCount = 0;
        this.m_criterion = null;
        this.m_filters = null;
        this.m_activeOnStart = false;
        this.m_actionGroups = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_type = buffer.getInt();
        this.m_dropRate = buffer.getShort();
        this.m_maxCount = buffer.getShort();
        this.m_criterion = buffer.readUTF8().intern();
        this.m_filters = buffer.readStringArray();
        this.m_activeOnStart = buffer.readBoolean();
        final int actionGroupCount = buffer.getInt();
        this.m_actionGroups = new EventActionGroup[actionGroupCount];
        for (int iActionGroup = 0; iActionGroup < actionGroupCount; ++iActionGroup) {
            (this.m_actionGroups[iActionGroup] = new EventActionGroup()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.CLIENT_EVENT.getId();
    }
    
    public static class EventAction
    {
        protected int m_id;
        protected int m_type;
        protected String[] m_params;
        
        public int getId() {
            return this.m_id;
        }
        
        public int getType() {
            return this.m_type;
        }
        
        public String[] getParams() {
            return this.m_params;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_type = buffer.getInt();
            this.m_params = buffer.readStringArray();
        }
    }
    
    public static class EventActionGroup
    {
        protected int m_id;
        protected short m_dropRate;
        protected String m_criterion;
        protected EventAction[] m_actions;
        
        public int getId() {
            return this.m_id;
        }
        
        public short getDropRate() {
            return this.m_dropRate;
        }
        
        public String getCriterion() {
            return this.m_criterion;
        }
        
        public EventAction[] getActions() {
            return this.m_actions;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_dropRate = buffer.getShort();
            this.m_criterion = buffer.readUTF8().intern();
            final int actionCount = buffer.getInt();
            this.m_actions = new EventAction[actionCount];
            for (int iAction = 0; iAction < actionCount; ++iAction) {
                (this.m_actions[iAction] = new EventAction()).read(buffer);
            }
        }
    }
}
