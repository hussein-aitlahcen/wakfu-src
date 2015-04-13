package com.ankamagames.wakfu.client.core.game.travel;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;

public class BoatIslandFieldProvider extends ImmutableFieldProvider
{
    public static final String ISLAND_FIELD = "island";
    public static final String LINK_LIST_FIELD = "links";
    private final int m_instanceId;
    private final ArrayList<BoatInfoFieldProvider> m_boatList;
    
    public BoatIslandFieldProvider(final int instanceId) {
        super();
        this.m_boatList = new ArrayList<BoatInfoFieldProvider>();
        this.m_instanceId = instanceId;
    }
    
    public void addBoat(final BoatInfoFieldProvider boatInfo) {
        this.m_boatList.add(boatInfo);
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("island")) {
            return this.getIslandName();
        }
        if (fieldName.equals("links")) {
            return this.m_boatList;
        }
        return null;
    }
    
    public String getIslandName() {
        return WakfuTranslator.getInstance().getString(77, this.m_instanceId, new Object[0]);
    }
    
    public ArrayList<BoatInfoFieldProvider> getBoatList() {
        return this.m_boatList;
    }
}
