package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class ItemBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_metaId;
    protected short m_itemSetId;
    protected int m_gfxId;
    protected int m_femaleGfxId;
    protected int m_floorGfxId;
    protected short m_level;
    protected String[] m_criteria;
    protected int m_itemTypeId;
    protected short m_maxStackHeight;
    protected byte m_useCostAP;
    protected byte m_useCostMP;
    protected byte m_useCostFP;
    protected int m_useRangeMin;
    protected int m_useRangeMax;
    protected boolean m_useTestFreeCell;
    protected boolean m_useTestNotBorderCell;
    protected boolean m_useTestLos;
    protected boolean m_useTestOnlyLine;
    protected short m_itemRarity;
    protected byte m_itemBindType;
    protected String m_generationType;
    protected int[] m_itemProperties;
    protected byte m_itemActionVisual;
    protected byte m_worldUsageTarget;
    protected byte m_gemElementType;
    protected byte m_gemNum;
    protected int[] m_effectIds;
    protected ItemAction[] m_actions;
    protected int[] m_subMetaIds;
    protected byte[] m_gemSlots;
    protected byte[] m_gemSlotType;
    
    public boolean isMetaItem() {
        return this.m_metaId == this.m_id;
    }
    
    public boolean isSubMetaItem() {
        return this.m_metaId > 0 && this.m_metaId != this.m_id;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public int getMetaId() {
        return this.m_metaId;
    }
    
    public short getItemSetId() {
        return this.m_itemSetId;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public int getFemaleGfxId() {
        return this.m_femaleGfxId;
    }
    
    public int getFloorGfxId() {
        return this.m_floorGfxId;
    }
    
    public short getLevel() {
        return this.m_level;
    }
    
    public String[] getCriteria() {
        return this.m_criteria;
    }
    
    public int getItemTypeId() {
        return this.m_itemTypeId;
    }
    
    public short getMaxStackHeight() {
        return this.m_maxStackHeight;
    }
    
    public byte getUseCostAP() {
        return this.m_useCostAP;
    }
    
    public byte getUseCostMP() {
        return this.m_useCostMP;
    }
    
    public byte getUseCostFP() {
        return this.m_useCostFP;
    }
    
    public int getUseRangeMin() {
        return this.m_useRangeMin;
    }
    
    public int getUseRangeMax() {
        return this.m_useRangeMax;
    }
    
    public boolean isUseTestFreeCell() {
        return this.m_useTestFreeCell;
    }
    
    public boolean isUseTestNotBorderCell() {
        return this.m_useTestNotBorderCell;
    }
    
    public boolean isUseTestLos() {
        return this.m_useTestLos;
    }
    
    public boolean isUseTestOnlyLine() {
        return this.m_useTestOnlyLine;
    }
    
    public short getItemRarity() {
        return this.m_itemRarity;
    }
    
    public byte getItemBindType() {
        return this.m_itemBindType;
    }
    
    public String getGenerationType() {
        return this.m_generationType;
    }
    
    public int[] getItemProperties() {
        return this.m_itemProperties;
    }
    
    public byte getItemActionVisual() {
        return this.m_itemActionVisual;
    }
    
    public byte getWorldUsageTarget() {
        return this.m_worldUsageTarget;
    }
    
    public byte getGemElementType() {
        return this.m_gemElementType;
    }
    
    public byte getGemNum() {
        return this.m_gemNum;
    }
    
    public int[] getEffectIds() {
        return this.m_effectIds;
    }
    
    public ItemAction[] getActions() {
        return this.m_actions;
    }
    
    public int[] getSubMetaIds() {
        return this.m_subMetaIds;
    }
    
    public byte[] getGemSlots() {
        return this.m_gemSlots;
    }
    
    public byte[] getGemSlotType() {
        return this.m_gemSlotType;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_metaId = 0;
        this.m_itemSetId = 0;
        this.m_gfxId = 0;
        this.m_femaleGfxId = 0;
        this.m_floorGfxId = 0;
        this.m_level = 0;
        this.m_criteria = null;
        this.m_itemTypeId = 0;
        this.m_maxStackHeight = 0;
        this.m_useCostAP = 0;
        this.m_useCostMP = 0;
        this.m_useCostFP = 0;
        this.m_useRangeMin = 0;
        this.m_useRangeMax = 0;
        this.m_useTestFreeCell = false;
        this.m_useTestNotBorderCell = false;
        this.m_useTestLos = false;
        this.m_useTestOnlyLine = false;
        this.m_itemRarity = 0;
        this.m_itemBindType = 0;
        this.m_generationType = null;
        this.m_itemProperties = null;
        this.m_itemActionVisual = 0;
        this.m_worldUsageTarget = 0;
        this.m_gemElementType = 0;
        this.m_gemNum = 0;
        this.m_effectIds = null;
        this.m_actions = null;
        this.m_subMetaIds = null;
        this.m_gemSlots = null;
        this.m_gemSlotType = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_metaId = buffer.getInt();
        this.m_itemSetId = buffer.getShort();
        this.m_gfxId = buffer.getInt();
        this.m_femaleGfxId = buffer.getInt();
        this.m_floorGfxId = buffer.getInt();
        this.m_level = buffer.getShort();
        this.m_criteria = buffer.readStringArray();
        this.m_itemTypeId = buffer.getInt();
        this.m_maxStackHeight = buffer.getShort();
        this.m_useCostAP = buffer.get();
        this.m_useCostMP = buffer.get();
        this.m_useCostFP = buffer.get();
        this.m_useRangeMin = buffer.getInt();
        this.m_useRangeMax = buffer.getInt();
        this.m_useTestFreeCell = buffer.readBoolean();
        this.m_useTestNotBorderCell = buffer.readBoolean();
        this.m_useTestLos = buffer.readBoolean();
        this.m_useTestOnlyLine = buffer.readBoolean();
        this.m_itemRarity = buffer.getShort();
        this.m_itemBindType = buffer.get();
        this.m_generationType = buffer.readUTF8().intern();
        this.m_itemProperties = buffer.readIntArray();
        this.m_itemActionVisual = buffer.get();
        this.m_worldUsageTarget = buffer.get();
        this.m_gemElementType = buffer.get();
        this.m_gemNum = buffer.get();
        this.m_effectIds = buffer.readIntArray();
        final int actionCount = buffer.getInt();
        this.m_actions = new ItemAction[actionCount];
        for (int iAction = 0; iAction < actionCount; ++iAction) {
            (this.m_actions[iAction] = new ItemAction()).read(buffer);
        }
        this.m_subMetaIds = buffer.readIntArray();
        this.m_gemSlots = buffer.readByteArray();
        this.m_gemSlotType = buffer.readByteArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.ITEM.getId();
    }
    
    public static class ItemAction
    {
        protected int m_actionId;
        protected int m_actionTypeId;
        protected boolean m_consumeItemOnAction;
        protected boolean m_clientOnly;
        protected boolean m_stopMovement;
        protected boolean m_hasScript;
        protected String m_criteria;
        protected String[] m_actionParams;
        protected String[] m_actionScriptParams;
        
        public int getActionId() {
            return this.m_actionId;
        }
        
        public int getActionTypeId() {
            return this.m_actionTypeId;
        }
        
        public boolean isConsumeItemOnAction() {
            return this.m_consumeItemOnAction;
        }
        
        public boolean isClientOnly() {
            return this.m_clientOnly;
        }
        
        public boolean isStopMovement() {
            return this.m_stopMovement;
        }
        
        public boolean hasScript() {
            return this.m_hasScript;
        }
        
        public String getCriteria() {
            return this.m_criteria;
        }
        
        public String[] getActionParams() {
            return this.m_actionParams;
        }
        
        public String[] getActionScriptParams() {
            return this.m_actionScriptParams;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_actionId = buffer.getInt();
            this.m_actionTypeId = buffer.getInt();
            this.m_consumeItemOnAction = buffer.readBoolean();
            this.m_clientOnly = buffer.readBoolean();
            this.m_stopMovement = buffer.readBoolean();
            this.m_hasScript = buffer.readBoolean();
            this.m_criteria = buffer.readUTF8().intern();
            this.m_actionParams = buffer.readStringArray();
            this.m_actionScriptParams = buffer.readStringArray();
        }
    }
}
