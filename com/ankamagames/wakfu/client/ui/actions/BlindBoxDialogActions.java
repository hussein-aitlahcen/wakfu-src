package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;

@XulorActionsTag
public class BlindBoxDialogActions
{
    public static final String PACKAGE = "wakfu.blindBox";
    
    public static void showItemDetails(final ItemEvent event, final Container cont) {
        final AbstractReferenceItem referenceItem = (ReferenceItem)event.getItemValue();
        final PopupElement popup = (PopupElement)cont.getElementMap().getElement("itemDetailPopup");
        if (event.getType() == Events.ITEM_OVER && !MasterRootContainer.getInstance().isDragging()) {
            PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", referenceItem);
            PropertiesProvider.getInstance().setPropertyValue("isFromShortcut", false);
            XulorActions.popup(popup, event.getTarget());
        }
        else if (event.getType() == Events.ITEM_OUT) {
            PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", null);
            PropertiesProvider.getInstance().setPropertyValue("isFromShortcut", false);
            PropertiesProvider.getInstance().setPropertyValue("isFromEquipedShortcut", false);
            XulorActions.closePopup(event, popup);
        }
    }
}
