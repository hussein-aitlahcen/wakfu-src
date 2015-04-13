package com.ankamagames.wakfu.client.core.havenWorld;

import java.util.*;

public class HavenWorldBuildingVisualDefinition
{
    private final int m_buildingId;
    private final ArrayList<HavenWorldBuildingVisualElement> m_elements;
    
    public HavenWorldBuildingVisualDefinition(final int buildingId) {
        super();
        this.m_buildingId = buildingId;
        this.m_elements = new ArrayList<HavenWorldBuildingVisualElement>();
    }
    
    public void addElement(final HavenWorldBuildingVisualElement element) {
        this.m_elements.add(element);
    }
    
    public ArrayList<HavenWorldBuildingVisualElement> getElements() {
        return this.m_elements;
    }
    
    public int getBuildingId() {
        return this.m_buildingId;
    }
}
