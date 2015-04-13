package com.ankamagames.wakfu.client.core.game.equipableDummy;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class EquipableDummyView extends ImmutableFieldProvider implements InventoryObserver
{
    public static final String ANIM_NAME = "animName";
    public static final String ACTOR_DESCRIPTOR_LIBRARY = "actorDescriptorLibrary";
    public static final String EQUIPED_ITEM = "equipedItem";
    private EquipableDummy m_dummy;
    
    public EquipableDummyView(final EquipableDummy dummy) {
        super();
        (this.m_dummy = dummy).addInventoryObserver(this);
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("animName")) {
            return this.m_dummy.getAnimName();
        }
        if (fieldName.equals("actorDescriptorLibrary")) {
            return this.m_dummy.getView();
        }
        if (fieldName.equals("equipedItem")) {
            return this.m_dummy.getItem();
        }
        return null;
    }
    
    public EquipableDummy getDummy() {
        return this.m_dummy;
    }
    
    @Override
    public void onInventoryEvent(final InventoryEvent event) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "actorDescriptorLibrary", "animName", "equipedItem");
    }
}
