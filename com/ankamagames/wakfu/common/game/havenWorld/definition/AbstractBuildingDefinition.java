package com.ankamagames.wakfu.common.game.havenWorld.definition;

import gnu.trove.*;

public abstract class AbstractBuildingDefinition
{
    protected final short m_id;
    protected final short m_catalogEntryId;
    protected final int m_kamasCost;
    protected final byte m_neededWorkers;
    protected final int m_grantedWorkers;
    protected final int m_editorGroupId;
    protected final int m_resourcesCost;
    protected final boolean m_canBeDestroyed;
    
    protected AbstractBuildingDefinition(final short id, final short catalogEntryId, final int kamasCost, final byte neededWorkers, final int grantedWorkers, final int editorGroupId, final int resourcesCost, final boolean canBeDestroyed) {
        super();
        this.m_id = id;
        this.m_catalogEntryId = catalogEntryId;
        this.m_kamasCost = kamasCost;
        this.m_neededWorkers = neededWorkers;
        this.m_grantedWorkers = grantedWorkers;
        this.m_editorGroupId = editorGroupId;
        this.m_resourcesCost = resourcesCost;
        this.m_canBeDestroyed = canBeDestroyed;
    }
    
    public short getId() {
        return this.m_id;
    }
    
    public int getKamasCost() {
        return this.m_kamasCost;
    }
    
    public int getEditorGroupId() {
        return this.m_editorGroupId;
    }
    
    public abstract int getEditorGroupId(final int p0);
    
    public short getCatalogEntryId() {
        return this.m_catalogEntryId;
    }
    
    public byte getNeededWorkers() {
        return this.m_neededWorkers;
    }
    
    public int getGrantedWorkers() {
        return this.m_grantedWorkers;
    }
    
    public int getResourcesCost() {
        return this.m_resourcesCost;
    }
    
    public boolean canBeDestroyed() {
        return this.m_canBeDestroyed;
    }
    
    public abstract boolean isDecoOnly();
    
    public abstract boolean hasEffect();
    
    public abstract boolean forEachElement(final TObjectProcedure<BuildingIEDefinition> p0);
    
    public abstract boolean acceptItem(final int p0);
    
    public abstract int[] getEquipableItems();
}
