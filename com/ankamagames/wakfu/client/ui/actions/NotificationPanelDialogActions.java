package com.ankamagames.wakfu.client.ui.actions;

import java.util.regex.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.component.text.builder.content.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.component.text.document.part.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.event.*;

@XulorActionsTag
public class NotificationPanelDialogActions
{
    public static final String PACKAGE = "wakfu.notificationPanel";
    private static final Pattern LINK_PATTERN;
    
    public static String createLink(final String text, final NotificationMessageType notificationMessageType) {
        return createLink(text, notificationMessageType, null);
    }
    
    public static String createLink(String text, final NotificationMessageType notificationMessageType, final String id) {
        final Matcher spellMatcher = NotificationPanelDialogActions.LINK_PATTERN.matcher(text);
        if (spellMatcher.find()) {
            final String value = spellMatcher.group(1);
            final TextWidgetFormater sb = new TextWidgetFormater();
            sb.append(text.substring(0, spellMatcher.start()));
            sb.openText().u().addId(notificationMessageType.name() + ((id == null) ? "" : id));
            sb.append(value)._u().closeText();
            sb.append(text.substring(spellMatcher.end()));
            text = sb.finishAndToString();
        }
        return text;
    }
    
    public static void forceCycle(final Event e) {
        if (((MouseEvent)e).getButton() != 1) {
            return;
        }
        UINotificationPanelFrame.getInstance().forceCycle();
    }
    
    public static void gotoNotificationLinkTooltip(final Event event, final PopupElement popupElement) {
        final TextView textView = event.getTarget();
        final AbstractContentBlock block = textView.getBlockUnderMouse();
        if (block != null && block.getType() == AbstractContentBlock.BlockType.TEXT) {
            final AbstractDocumentPart part = block.getDocumentPart();
            if (part == null) {
                return;
            }
            if (part.getType() == DocumentPartType.TEXT && ((TextDocumentPart)part).isUnderline()) {
                XulorActions.popup(popupElement, textView);
            }
        }
    }
    
    public static void gotoNotificationLink(final Event event, final NotificationMessageView notificationMessageView) {
        if (((MouseEvent)event).getButton() != 3) {
            forceCycle(event);
            return;
        }
        String id = null;
        final TextView textView = event.getTarget();
        final AbstractContentBlock block = textView.getBlockUnderMouse();
        final NotificationMessageType type = notificationMessageView.getNotificationMessageType();
        if (block != null && block.getType() == AbstractContentBlock.BlockType.TEXT) {
            final AbstractDocumentPart part = block.getDocumentPart();
            if (part == null) {
                return;
            }
            if (part.getType() == DocumentPartType.TEXT) {
                final String partId = ((TextDocumentPart)part).getId();
                if (partId != null && partId.length() > 0) {
                    id = partId.replaceAll(type.name(), "");
                }
            }
        }
        switch (type) {
            case CHARACTER: {
                if (!WakfuGameEntity.getInstance().hasFrame(UICharacterSheetFrame.getInstance())) {
                    WakfuGameEntity.getInstance().pushFrame(UICharacterSheetFrame.getInstance());
                    break;
                }
                break;
            }
            case CITIZEN: {
                if (!WakfuGameEntity.getInstance().hasFrame(UINationFrame.getInstance())) {
                    PropertiesProvider.getInstance().setPropertyValue("nationCurrentPageIndex", (byte)2);
                    WakfuGameEntity.getInstance().pushFrame(UINationFrame.getInstance());
                    break;
                }
                break;
            }
            case OUTLAW: {
                if (id == null) {
                    return;
                }
                final int nationId = Integer.parseInt(id);
                if (!WakfuGameEntity.getInstance().hasFrame(UINationFrame.getInstance())) {
                    final boolean localPLayerNation = nationId == WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getNationId();
                    PropertiesProvider.getInstance().setPropertyValue("nationCurrentPageIndex", (byte)(localPLayerNation ? 2 : 3));
                    WakfuGameEntity.getInstance().pushFrame(UINationFrame.getInstance());
                    break;
                }
                break;
            }
            case CRAFT: {
                if (id == null) {
                    return;
                }
                final int craftId = Integer.parseInt(id);
                WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.LAST_CRAFT_SEEN, craftId);
                if (!WakfuGameEntity.getInstance().hasFrame(UICraftFrame.getInstance())) {
                    WakfuGameEntity.getInstance().pushFrame(UICraftFrame.getInstance());
                    break;
                }
                UICraftFrame.getInstance().setCurrentCtaftFromPrefs();
                break;
            }
            case NATION: {
                if (id == null) {
                    return;
                }
                if (id.length() > 0) {
                    final byte pageIndex = Byte.parseByte(id);
                    PropertiesProvider.getInstance().setPropertyValue("nationCurrentPageIndex", pageIndex);
                }
                if (!WakfuGameEntity.getInstance().hasFrame(UINationFrame.getInstance())) {
                    WakfuGameEntity.getInstance().pushFrame(UINationFrame.getInstance());
                    break;
                }
                break;
            }
            case PROTECTOR_WEATHER: {}
            case SOCIAL: {
                if (!WakfuGameEntity.getInstance().hasFrame(UIGuildManagementFrame.getInstance())) {
                    WakfuGameEntity.getInstance().pushFrame(UIGuildManagementFrame.getInstance());
                    break;
                }
                break;
            }
        }
    }
    
    public static void selectMessage(final ItemEvent itemEvent) {
        UINotificationPanelFrame.getInstance().setSelectedMessage((NotificationMessageView)itemEvent.getItemValue());
    }
    
    public static void overMessage(final Event event) {
        UINotificationPanelFrame.getInstance().setOverLocked(true);
    }
    
    public static void outMessage(final Event event) {
        UINotificationPanelFrame.getInstance().setOverLocked(false);
    }
    
    static {
        LINK_PATTERN = Pattern.compile("\\#\\#(.*)\\#\\#");
    }
}
