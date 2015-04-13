package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.game.group.partySearch.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.property.*;

@XulorActionsTag
public class PartySearchDialogActions
{
    public static final String PACKAGE = "wakfu.partySearch";
    
    public static void selectOccupation(final SelectionChangedEvent e, final PartyOccupationView<PartyOccupation> occupation) {
        sendUIMessage((short)(e.isSelected() ? 19441 : 19442), occupation);
    }
    
    public static void selectBreed(final ListSelectionChangedEvent e) {
        if (e.getSelected()) {
            sendUIMessage((short)19451, e.getValue());
        }
    }
    
    public static void selectMood(final ListSelectionChangedEvent e) {
        if (e.getSelected()) {
            sendUIMessage((short)19443, e.getValue());
        }
    }
    
    public static void selectRole(final ListSelectionChangedEvent e) {
        if (e.getSelected()) {
            sendUIMessage((short)19444, e.getValue());
        }
    }
    
    public static void filterOccupations(final MouseEvent e, final TextWidget family, final TextWidget minLevel, final TextWidget maxLevel) {
        filter(family, minLevel, maxLevel, (short)19447);
    }
    
    private static void filter(final TextWidget family, final TextWidget minLevel, final TextWidget maxLevel, final short protocol) {
        final short level = WakfuGameEntity.getInstance().getLocalPlayer().getLevel();
        final short typedMaxLevel = PrimitiveConverter.getShort(maxLevel.getText());
        short typedMinLevel = PrimitiveConverter.getShort(minLevel.getText());
        if (typedMinLevel > typedMaxLevel) {
            typedMinLevel = (short)((level > 20) ? (level - 20) : 0);
            minLevel.setText(typedMinLevel);
        }
        final PartySearchFilter filter = new PartySearchFilter(typedMinLevel, typedMaxLevel, family.getText());
        sendUIMessage(protocol, filter.serialize());
    }
    
    public static void filterResult(final MouseEvent e, final TextWidget minLevel, final TextWidget maxLevel) {
        final short level = WakfuGameEntity.getInstance().getLocalPlayer().getLevel();
        final short typedMaxLevel = PrimitiveConverter.getShort(maxLevel.getText());
        short typedMinLevel = PrimitiveConverter.getShort(minLevel.getText());
        if (typedMinLevel > typedMaxLevel) {
            typedMinLevel = (short)((level > 20) ? (level - 20) : 0);
            minLevel.setText(typedMinLevel);
        }
        final PartySearchFilter filter = new PartySearchFilter(typedMinLevel, typedMaxLevel, "");
        sendUIMessage((short)19450, filter.serialize());
    }
    
    public static void talk(final MouseEvent e, final PartyRequesterView view) {
        sendUIMessage((short)19453, view.getLeaderName());
    }
    
    public static void group(final MouseEvent e, final PartyRequesterView view) {
        sendUIMessage((short)19452, view.getId());
    }
    
    public static void resetSearch(final Event e) {
        sendUIMessage((short)19449, null);
    }
    
    public static void selectRegistrationOccupation(final SelectionChangedEvent e, final PartyOccupationView<PartyOccupation> occupation) {
        if (e.isSelected() == occupation.isSelected()) {
            return;
        }
        sendUIMessage((short)(e.isSelected() ? 19436 : 19437), occupation);
    }
    
    public static void selectRegistrationMood(final ListSelectionChangedEvent e) {
        if (e.getSelected()) {
            sendUIMessage((short)19438, e.getValue());
        }
    }
    
    public static void selectRegistrationRole(final ListSelectionChangedEvent e, final PartyPlayerDefinitionView partyPlayerDefinitionView) {
        if (e.getSelected()) {
            final AbstractUIMessage msg = new UIMessage((short)19439);
            msg.setObjectValue(e.getValue());
            msg.setLongValue(partyPlayerDefinitionView.getId());
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void keyPressRegistrationOccupations(final KeyEvent e, final TextWidget family, final TextWidget minLevel, final TextWidget maxLevel) {
        if (e.getKeyChar() == '\n') {
            filter(family, minLevel, maxLevel, (short)19446);
        }
    }
    
    public static void keyPressSearchOccupations(final KeyEvent e, final TextWidget family, final TextWidget minLevel, final TextWidget maxLevel) {
        if (e.getKeyChar() == '\n') {
            filter(family, minLevel, maxLevel, (short)19447);
        }
    }
    
    public static void filterRegistrationOccupations(final MouseEvent e, final TextWidget family, final TextWidget minLevel, final TextWidget maxLevel) {
        filter(family, minLevel, maxLevel, (short)19446);
    }
    
    public static void resetRegistrationSearch(final Event e) {
        sendUIMessage((short)19448, null);
    }
    
    public static void register(final Event e, final TextWidget ed) {
        sendUIMessage((short)19440, ed.getText());
    }
    
    public static void unregister(final Event e) {
        sendUIMessage((short)19455, null);
    }
    
    private static void sendUIMessage(final short id, final Object o) {
        final AbstractUIMessage msg = new UIMessage(id);
        msg.setObjectValue(o);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void displayPage(final Event e, final String pageString) {
        final int page = PrimitiveConverter.getInteger(pageString);
        PropertiesProvider.getInstance().setLocalPropertyValue("currentPage", page, "partySearchDialog");
        final AbstractUIMessage msg = new UIMessage((short)19457);
        msg.setBooleanValue(page == 1);
        Worker.getInstance().pushMessage(msg);
    }
}
