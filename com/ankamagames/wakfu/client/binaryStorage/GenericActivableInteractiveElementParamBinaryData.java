package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class GenericActivableInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected Visual[] m_visuals;
    protected ChaosParamBinaryData m_chaosParams;
    
    public int getId() {
        return this.m_id;
    }
    
    public Visual[] getVisuals() {
        return this.m_visuals;
    }
    
    public ChaosParamBinaryData getChaosParams() {
        return this.m_chaosParams;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_visuals = null;
        this.m_chaosParams = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        final int visualCount = buffer.getInt();
        this.m_visuals = new Visual[visualCount];
        for (int iVisual = 0; iVisual < visualCount; ++iVisual) {
            (this.m_visuals[iVisual] = new Visual()).read(buffer);
        }
        if (buffer.get() != 0) {
            (this.m_chaosParams = new ChaosParamBinaryData()).read(buffer);
        }
        else {
            this.m_chaosParams = null;
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.GENERIC_ACTIVABLE_IE_PARAM.getId();
    }
    
    public static class Action
    {
        protected int m_actionId;
        protected int m_actionTypeId;
        protected String m_criteria;
        protected String[] m_actionParams;
        
        public int getActionId() {
            return this.m_actionId;
        }
        
        public int getActionTypeId() {
            return this.m_actionTypeId;
        }
        
        public String getCriteria() {
            return this.m_criteria;
        }
        
        public String[] getActionParams() {
            return this.m_actionParams;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_actionId = buffer.getInt();
            this.m_actionTypeId = buffer.getInt();
            this.m_criteria = buffer.readUTF8().intern();
            this.m_actionParams = buffer.readStringArray();
        }
    }
    
    public static class GroupAction
    {
        protected int m_id;
        protected String m_criteria;
        protected float m_weight;
        protected Action[] m_actions;
        
        public int getId() {
            return this.m_id;
        }
        
        public String getCriteria() {
            return this.m_criteria;
        }
        
        public float getWeight() {
            return this.m_weight;
        }
        
        public Action[] getActions() {
            return this.m_actions;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_criteria = buffer.readUTF8().intern();
            this.m_weight = buffer.getFloat();
            final int actionCount = buffer.getInt();
            this.m_actions = new Action[actionCount];
            for (int iAction = 0; iAction < actionCount; ++iAction) {
                (this.m_actions[iAction] = new Action()).read(buffer);
            }
        }
    }
    
    public static class Visual
    {
        protected int m_id;
        protected int m_visualId;
        protected int m_itemConsumed;
        protected int m_itemQuantity;
        protected boolean m_doConsumeItem;
        protected int m_kamaCost;
        protected int m_distributionDuration;
        protected GroupAction[] m_groupActions;
        
        public int getId() {
            return this.m_id;
        }
        
        public int getVisualId() {
            return this.m_visualId;
        }
        
        public int getItemConsumed() {
            return this.m_itemConsumed;
        }
        
        public int getItemQuantity() {
            return this.m_itemQuantity;
        }
        
        public boolean isDoConsumeItem() {
            return this.m_doConsumeItem;
        }
        
        public int getKamaCost() {
            return this.m_kamaCost;
        }
        
        public int getDistributionDuration() {
            return this.m_distributionDuration;
        }
        
        public GroupAction[] getGroupActions() {
            return this.m_groupActions;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_visualId = buffer.getInt();
            this.m_itemConsumed = buffer.getInt();
            this.m_itemQuantity = buffer.getInt();
            this.m_doConsumeItem = buffer.readBoolean();
            this.m_kamaCost = buffer.getInt();
            this.m_distributionDuration = buffer.getInt();
            final int groupActionCount = buffer.getInt();
            this.m_groupActions = new GroupAction[groupActionCount];
            for (int iGroupAction = 0; iGroupAction < groupActionCount; ++iGroupAction) {
                (this.m_groupActions[iGroupAction] = new GroupAction()).read(buffer);
            }
        }
    }
}
