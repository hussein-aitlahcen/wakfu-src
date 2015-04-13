package com.ankamagames.wakfu.common.game.havenWorld;

import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import gnu.trove.*;

class BuildingModel implements Building
{
    private final AbstractBuildingDefinition m_definition;
    private final long m_uid;
    private final long m_creationDate;
    private short m_x;
    private short m_y;
    private int m_equippedItemId;
    private final TLongObjectHashMap<BuildingElement> m_elements;
    
    BuildingModel(final AbstractBuildingDefinition definition, final long uid, final int itemId, final long creationDate, final short x, final short y) {
        super();
        this.m_definition = definition;
        this.m_uid = uid;
        this.m_equippedItemId = itemId;
        this.m_creationDate = creationDate;
        this.m_x = x;
        this.m_y = y;
        if (!this.m_definition.isDecoOnly()) {
            this.m_elements = new TLongObjectHashMap<BuildingElement>();
        }
        else {
            this.m_elements = new TLongObjectHashMap<BuildingElement>(0);
        }
    }
    
    @Override
    public AbstractBuildingDefinition getDefinition() {
        return this.m_definition;
    }
    
    @Override
    public long getUid() {
        return this.m_uid;
    }
    
    @Override
    public long getCreationDate() {
        return this.m_creationDate;
    }
    
    @Override
    public short getX() {
        return this.m_x;
    }
    
    @Override
    public short getY() {
        return this.m_y;
    }
    
    @Override
    public void setX(final short x) {
        this.m_x = x;
    }
    
    @Override
    public void setY(final short y) {
        this.m_y = y;
    }
    
    @Override
    public boolean hasElement() {
        return !this.m_elements.isEmpty();
    }
    
    @Override
    public BuildingElement getElement(final long uid) {
        return this.m_elements.get(uid);
    }
    
    @Override
    public boolean forEachElement(final TObjectProcedure<BuildingElement> procedure) {
        return this.m_elements.forEachValue(procedure);
    }
    
    void addElement(final BuildingElement element) {
        this.m_elements.put(element.getUid(), element);
    }
    
    void removeElement(final long elementUid) {
        this.m_elements.remove(elementUid);
    }
    
    @Override
    public int getEquippedItemId() {
        return this.m_equippedItemId;
    }
    
    public void setEquippedItemId(final int equippedItemId) {
        this.m_equippedItemId = equippedItemId;
    }
    
    @Override
    public String toString() {
        return "BuildingModel{m_definition=" + this.m_definition + ", m_uid=" + this.m_uid + ", m_creationDate=" + this.m_creationDate + ", m_x=" + this.m_x + ", m_y=" + this.m_y + ", m_equippedItemId=" + this.m_equippedItemId + ", m_elements=" + this.m_elements.size() + '}';
    }
}
