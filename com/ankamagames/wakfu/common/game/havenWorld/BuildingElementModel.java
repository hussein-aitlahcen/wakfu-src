package com.ankamagames.wakfu.common.game.havenWorld;

class BuildingElementModel implements BuildingElement
{
    private final long m_uid;
    private final long m_elementId;
    
    BuildingElementModel(final long uid, final long elementId) {
        super();
        this.m_uid = uid;
        this.m_elementId = elementId;
    }
    
    @Override
    public long getElementId() {
        return this.m_elementId;
    }
    
    @Override
    public long getUid() {
        return this.m_uid;
    }
    
    @Override
    public String toString() {
        return "BuildingElementModel{m_uid=" + this.m_uid + '}';
    }
}
