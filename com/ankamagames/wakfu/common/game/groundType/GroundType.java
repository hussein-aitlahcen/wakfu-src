package com.ankamagames.wakfu.common.game.groundType;

import gnu.trove.*;

public class GroundType
{
    private final int m_id;
    private final TIntShortHashMap m_fertilityByResource;
    private final TShortShortHashMap m_fertilityByResourceType;
    
    public GroundType(final int id) {
        super();
        this.m_fertilityByResource = new TIntShortHashMap();
        this.m_fertilityByResourceType = new TShortShortHashMap();
        this.m_id = id;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public void setFertilityOnResources(final TIntShortHashMap resourceFertility) {
        this.m_fertilityByResource.clear();
        this.m_fertilityByResource.putAll(resourceFertility);
    }
    
    public void setFertilityOnResourceTypes(final TShortShortHashMap resourceTypeFertility) {
        this.m_fertilityByResourceType.clear();
        this.m_fertilityByResourceType.putAll(resourceTypeFertility);
    }
    
    public short getFertilityByReferenceResource(final int refId, final short resType) {
        final short value = this.m_fertilityByResource.get(refId);
        if (value != 0 || this.m_fertilityByResource.contains(refId)) {
            return value;
        }
        final short valueByType = this.m_fertilityByResourceType.get(resType);
        if (valueByType != 0 || this.m_fertilityByResourceType.contains(resType)) {
            return valueByType;
        }
        return 0;
    }
    
    public int[] getAllowedResources() {
        return this.m_fertilityByResource.keys();
    }
    
    public short[] getAllowedResourceFamilies() {
        return this.m_fertilityByResourceType.keys();
    }
}
