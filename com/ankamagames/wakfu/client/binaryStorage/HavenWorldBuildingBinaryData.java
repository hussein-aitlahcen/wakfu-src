package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class HavenWorldBuildingBinaryData implements BinaryData
{
    protected int m_id;
    protected short m_catalogEntryId;
    protected int m_kamaCost;
    protected int m_ressourceCost;
    protected byte m_workersGranted;
    protected byte m_workers;
    protected int m_editorGroupId;
    protected boolean m_canBeDestroyed;
    protected Interactive[] m_interactives;
    protected Skin[] m_skins;
    protected int[] m_effectIds;
    protected WorldEffect[] m_worldEffects;
    
    public int getId() {
        return this.m_id;
    }
    
    public short getCatalogEntryId() {
        return this.m_catalogEntryId;
    }
    
    public int getKamaCost() {
        return this.m_kamaCost;
    }
    
    public int getRessourceCost() {
        return this.m_ressourceCost;
    }
    
    public byte getWorkersGranted() {
        return this.m_workersGranted;
    }
    
    public byte getWorkers() {
        return this.m_workers;
    }
    
    public int getEditorGroupId() {
        return this.m_editorGroupId;
    }
    
    public boolean isCanBeDestroyed() {
        return this.m_canBeDestroyed;
    }
    
    public Interactive[] getInteractives() {
        return this.m_interactives;
    }
    
    public Skin[] getSkins() {
        return this.m_skins;
    }
    
    public int[] getEffectIds() {
        return this.m_effectIds;
    }
    
    public WorldEffect[] getWorldEffects() {
        return this.m_worldEffects;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_catalogEntryId = 0;
        this.m_kamaCost = 0;
        this.m_ressourceCost = 0;
        this.m_workersGranted = 0;
        this.m_workers = 0;
        this.m_editorGroupId = 0;
        this.m_canBeDestroyed = false;
        this.m_interactives = null;
        this.m_skins = null;
        this.m_effectIds = null;
        this.m_worldEffects = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_catalogEntryId = buffer.getShort();
        this.m_kamaCost = buffer.getInt();
        this.m_ressourceCost = buffer.getInt();
        this.m_workersGranted = buffer.get();
        this.m_workers = buffer.get();
        this.m_editorGroupId = buffer.getInt();
        this.m_canBeDestroyed = buffer.readBoolean();
        final int interactiveCount = buffer.getInt();
        this.m_interactives = new Interactive[interactiveCount];
        for (int iInteractive = 0; iInteractive < interactiveCount; ++iInteractive) {
            (this.m_interactives[iInteractive] = new Interactive()).read(buffer);
        }
        final int skinCount = buffer.getInt();
        this.m_skins = new Skin[skinCount];
        for (int iSkin = 0; iSkin < skinCount; ++iSkin) {
            (this.m_skins[iSkin] = new Skin()).read(buffer);
        }
        this.m_effectIds = buffer.readIntArray();
        final int worldEffectCount = buffer.getInt();
        this.m_worldEffects = new WorldEffect[worldEffectCount];
        for (int iWorldEffect = 0; iWorldEffect < worldEffectCount; ++iWorldEffect) {
            (this.m_worldEffects[iWorldEffect] = new WorldEffect()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.HAVEN_WORLD_BUILDING_DEFINITION.getId();
    }
    
    public static class Interactive
    {
        protected int m_uid;
        protected int m_templateId;
        protected byte m_x;
        protected byte m_y;
        protected byte m_z;
        
        public int getUid() {
            return this.m_uid;
        }
        
        public int getTemplateId() {
            return this.m_templateId;
        }
        
        public byte getX() {
            return this.m_x;
        }
        
        public byte getY() {
            return this.m_y;
        }
        
        public byte getZ() {
            return this.m_z;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_uid = buffer.getInt();
            this.m_templateId = buffer.getInt();
            this.m_x = buffer.get();
            this.m_y = buffer.get();
            this.m_z = buffer.get();
        }
    }
    
    public static class Skin
    {
        protected int m_itemId;
        protected int m_editorGroupId;
        
        public int getItemId() {
            return this.m_itemId;
        }
        
        public int getEditorGroupId() {
            return this.m_editorGroupId;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_itemId = buffer.getInt();
            this.m_editorGroupId = buffer.getInt();
        }
    }
    
    public static class WorldEffect
    {
        protected int m_buffId;
        
        public int getBuffId() {
            return this.m_buffId;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_buffId = buffer.getInt();
        }
    }
}
