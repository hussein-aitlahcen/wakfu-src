package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.emote.*;
import com.ankamagames.wakfu.client.ui.protocol.message.shortcut.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.chat.*;
import com.ankamagames.xulor2.property.*;

@XulorActionsTag
public class EmotesInventoryDialogActions
{
    public static final String PACKAGE = "wakfu.emotesInventory";
    
    public static void onClick(final ItemEvent event) {
        if (event.getType() != Events.ITEM_CLICK || event.getButton() != 1) {
            return;
        }
        final EmoteSmileyFieldProvider emote = (EmoteSmileyFieldProvider)event.getItemValue();
        if (!emote.isKnown()) {
            return;
        }
        if (event.hasAlt()) {
            final UIShortcutMessage message = new UIShortcutMessage();
            message.setItem(event.getItemValue());
            message.setShorcutBarNumber(-1);
            message.setPosition(-1);
            message.setBooleanValue(true);
            message.setId(16700);
            Worker.getInstance().pushMessage(message);
        }
        else {
            if (!WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.EMOTE_ICONS_ACTIVATED)) {
                return;
            }
            final UIChatContentMessage msg = new UIChatContentMessage();
            msg.setMessage(emote.getCommandText());
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void overSmiley(final ItemEvent event) {
        PropertiesProvider.getInstance().setPropertyValue("overSmiley", event.getItemValue());
    }
}
