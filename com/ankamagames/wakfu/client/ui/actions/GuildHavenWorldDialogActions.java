package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;

@XulorActionsTag
public class GuildHavenWorldDialogActions
{
    public static final String PACKAGE = "wakfu.guildHavenWorld";
    private static final String HAVEN_WORLD_ELEMENT_CONTAINER = "havenWorldElementContainer";
    
    public static void openLinkedBook(final Event e) {
        final AbstractReferenceItem item = ReferenceItemManager.getInstance().getReferenceItem(17426);
        ((OpenBackgroundDisplayItemAction)item.getItemAction()).display();
    }
    
    public static void selectTab(final SelectionChangedEvent e) {
        if (!e.isSelected()) {
            return;
        }
        final RadioButton radioButton = e.getTarget();
        final String value = radioButton.getValue();
        final ElementMap elementMap = radioButton.getElementMap();
        hideAllContainers(elementMap);
        setVisibility(elementMap, "havenWorldElementContainer" + value, true);
    }
    
    private static void hideAllContainers(final ElementMap map) {
        setVisibility(map, "havenWorldElementContainer1", false);
        setVisibility(map, "havenWorldElementContainer2", false);
        setVisibility(map, "havenWorldElementContainer3", false);
        setVisibility(map, "havenWorldElementContainer4", false);
    }
    
    private static void setVisibility(final ElementMap map, final String id, final boolean visible) {
        final Widget element = (Widget)map.getElement(id);
        if (element != null) {
            element.setVisible(visible);
        }
    }
}
