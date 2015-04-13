package com.ankamagames.wakfu.client.core.game.dimensionalBag;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.text.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.rights.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.item.*;

public class DimBagRightsView extends ImmutableFieldProvider
{
    public static final String ROOM_TYPE_PERMISSION_LIST_FIELD = "roomTypePermList";
    public static final String INDIVIDUALS_LIST_FIELD = "individualList";
    public static final String INDIVIDUALS_MAX_SIZE_TEXT_FIELD = "individualMaxSizeText";
    public static final String INDIVIDUALS_MAX_SIZE_REACHED_FIELD = "individualMaxSizeReached";
    private final DimBagRights m_bag;
    private final RoomView[] m_roomViews;
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("roomTypePermList")) {
            return this.m_roomViews;
        }
        if (fieldName.equals("individualList")) {
            return this.getIndividualsList();
        }
        if (fieldName.equals("individualMaxSizeReached")) {
            final int size = this.m_bag.getIndividualsList().size();
            return size >= 25;
        }
        if (fieldName.equals("individualMaxSizeText")) {
            final TextWidgetFormater twf = new TextWidgetFormater();
            final int size2 = this.m_bag.getIndividualsList().size();
            twf.append(WakfuTranslator.getInstance().getString("dimBag.individualCappedSize", size2, 25));
            return twf.finishAndToString();
        }
        return null;
    }
    
    private Object getIndividualsList() {
        final ArrayList<Individual> individuals = new ArrayList<Individual>();
        for (final DimBagIndividualRight dimBagIndividualRight : this.m_bag.getIndividualsList()) {
            individuals.add(new Individual(dimBagIndividualRight.getId(), dimBagIndividualRight.getName()));
        }
        return individuals;
    }
    
    public void addGroupRight(final DimBagGroupRight groupRight) {
        this.m_bag.addGroupRight(groupRight);
    }
    
    public void addIndividualRight(final DimBagIndividualRight dimBagIndividualRight) {
        final int size = this.m_bag.getIndividualsList().size();
        if (size >= 25) {
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("desc.individualRightsMaxSizeReached"), 3);
            return;
        }
        this.m_bag.addIndividualRight(dimBagIndividualRight);
        this.computeIndividualViews();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "individualList", "roomTypePermList", "individualMaxSizeText", "individualMaxSizeReached");
    }
    
    public void removeIndividualRight(final long id) {
        this.m_bag.removeIndividualRight(id);
        this.computeIndividualViews();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "individualList", "roomTypePermList", "individualMaxSizeText", "individualMaxSizeReached");
    }
    
    private void computeIndividualViews() {
        for (final RoomView roomView : this.m_roomViews) {
            roomView.computeIndividualViews();
        }
    }
    
    public DimBagRightsView(final DimBagRights bag) {
        super();
        this.m_bag = bag;
        this.m_roomViews = this.initialiseGraphicalFields();
    }
    
    public RoomView[] initialiseGraphicalFields() {
        final int[] availableRooms = this.enumAvailableRooms();
        final RoomView[] roomsViews = new RoomView[availableRooms.length];
        for (int i = 0; i < availableRooms.length; ++i) {
            roomsViews[i] = new RoomView(availableRooms[i], this.m_bag);
        }
        return roomsViews;
    }
    
    public int[] enumAvailableRooms() {
        final ArrayList<Integer> types = new ArrayList<Integer>();
        final DimensionalBagView visitingDimentionalBag = WakfuGameEntity.getInstance().getLocalPlayer().getVisitingDimentionalBag();
        for (byte i = 0; i < 9; ++i) {
            final Item gem = visitingDimentionalBag.getGem(i, true);
            if (gem != null) {
                final int gemTypeId = gem.getReferenceId();
                if (!types.contains(gemTypeId)) {
                    types.add(gemTypeId);
                }
            }
        }
        final int count = types.size();
        final int[] data = new int[count];
        for (int j = 0; j < count; ++j) {
            data[j] = types.get(j);
        }
        return data;
    }
    
    public DimBagRights getBag() {
        return this.m_bag;
    }
    
    public class Individual extends ImmutableFieldProvider
    {
        public static final String ID_FIELD = "id";
        public static final String NAME_FIELD = "name";
        private final long m_id;
        private final String m_name;
        
        public Individual(final long id, final String name) {
            super();
            this.m_id = id;
            this.m_name = name;
        }
        
        public long getId() {
            return this.m_id;
        }
        
        public String getName() {
            return this.m_name;
        }
        
        @Override
        public String[] getFields() {
            return null;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("id")) {
                return this.m_id;
            }
            if (fieldName.equals("name")) {
                return this.m_name;
            }
            return null;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Individual that = (Individual)o;
            return this.m_id == that.m_id;
        }
        
        @Override
        public int hashCode() {
            return (int)(this.m_id ^ this.m_id >>> 32);
        }
    }
}
