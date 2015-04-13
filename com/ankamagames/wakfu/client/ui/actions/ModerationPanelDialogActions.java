package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.moderationNew.panel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.graphics.image.*;

@XulorActionsTag
public class ModerationPanelDialogActions
{
    public static final String PACKAGE = "wakfu.moderationPanel";
    
    public static void backToMainPage(final Event e) {
        UIModerationPanelFrame.INSTANCE.request((short)211, new Object[0]);
        UIModerationPanelFrame.INSTANCE.request((short)210, new Object[0]);
        UIModerationPanelFrame.INSTANCE.getModerationPanelView().setCurrentPage(ModerationPanelPage.MAIN);
    }
    
    public static void selectPlayer(final Event e, final ModeratedPlayerView player) {
        UIModerationPanelFrame.INSTANCE.getModerationPanelView().setCurrentPage(ModerationPanelPage.PLAYER);
        UIModerationPanelFrame.INSTANCE.getModerationPanelView().setCurrentPlayer(player);
    }
    
    public static void removePlayerTab(final Event e, final ModeratedPlayerView player) {
        UIModerationPanelFrame.INSTANCE.getModerationPanelView().removeModeratedPlayer(player);
        UIModerationPanelFrame.INSTANCE.getModerationPanelView().setCurrentPage(ModerationPanelPage.MAIN);
    }
    
    public static void addTabPlayer(final Event e, final String name) {
        UIModerationPanelFrame.INSTANCE.getModerationPanelView().addPlayerTab(name);
    }
    
    public static void searchPlayer(final Event e, final TextEditor viewEditorName) {
        if (e instanceof KeyEvent && ((KeyEvent)e).getKeyCode() != 10) {
            return;
        }
        final String pseudo = viewEditorName.getText();
        UIModerationPanelFrame.INSTANCE.request((short)1, (byte)1, pseudo);
    }
    
    public static void eTargetable(final Event e, final String mode) {
        UIModerationPanelFrame.INSTANCE.requestWithServer((short)198, (byte)3, (int)("1".equals(mode) ? 1 : 0));
    }
    
    public static void dnd(final Event e, final String mode) {
        UIModerationPanelFrame.INSTANCE.request((short)184, (byte)("1".equals(mode) ? 1 : 0));
    }
    
    public static void setVisibility(final Event e, final String mode) {
        UIModerationPanelFrame.INSTANCE.requestWithServer((short)183, (byte)3, (byte)("1".equals(mode) ? 1 : 0));
    }
    
    public static void sanction(final Event e, final ModerationSanctionView sanction) {
        UIModerationPanelFrame.INSTANCE.request((short)206, sanction.getFieldValue("id"), UIModerationPanelFrame.INSTANCE.getModerationPanelView().getCurrentPlayer().getAccountId());
    }
    
    public static void redMessage(final Event e) {
        final MessageBoxData preMessage = new MessageBoxData(102, 0, "Type a red message to send :", 65542L);
        final MessageBoxControler preMessageControler = Xulor.getInstance().msgBox(preMessage);
        preMessageControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 2 && !userEntry.isEmpty()) {
                    UIModerationPanelFrame.INSTANCE.requestWithServer((short)196, (byte)3, userEntry);
                }
            }
        });
    }
    
    public static void redMessageToPlayer(final Event e) {
        final String characterName = UIModerationPanelFrame.INSTANCE.getModerationPanelView().getCurrentPlayer().getName();
        final MessageBoxData preMessage = new MessageBoxData(102, 0, "Type a red message to send to player " + characterName + " :", 65542L);
        final MessageBoxControler preMessageControler = Xulor.getInstance().msgBox(preMessage);
        preMessageControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 2 && !userEntry.isEmpty()) {
                    UIModerationPanelFrame.INSTANCE.request((short)197, characterName, userEntry);
                }
            }
        });
    }
    
    public static void havenBagKick(final Event e) {
        final String pseudo = UIModerationPanelFrame.INSTANCE.getModerationPanelView().getCurrentPlayer().getName();
        final long characterId = UIModerationPanelFrame.INSTANCE.getModerationPanelView().getCurrentPlayer().getCharacterId();
        UIModerationPanelFrame.INSTANCE.request((short)212, (short)199, pseudo, characterId);
    }
    
    public static void pum(final Event e) {
        final MessageBoxData preMessage = new MessageBoxData(102, 0, "Type a message for the PopUp (0 < size < 255) :", 65542L);
        final MessageBoxControler preMessageControler = Xulor.getInstance().msgBox(preMessage);
        preMessageControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 2) {
                    if (userEntry.length() > 255 || userEntry.isEmpty()) {
                        final GameDateConst tmp = BaseGameDateProvider.INSTANCE.getDate();
                        final String time = "[" + tmp.getHours() + ':' + tmp.getMinutes() + "] ";
                        UIModerationPanelFrame.INSTANCE.getModerationPanelView().addToLogs(time + "Incorrect message size, try again.");
                        return;
                    }
                    UIModerationPanelFrame.INSTANCE.request((short)195, userEntry, UIModerationPanelFrame.INSTANCE.getModerationPanelView().getCurrentPlayer().getName());
                }
            }
        });
    }
    
    public static void tpCoords(final Event e) {
        final MessageBoxData preMessage = new MessageBoxData(102, 0, "Type coords with format \"x y <instance>\" (instance facultative) :", 65542L);
        final MessageBoxControler preMessageControler = Xulor.getInstance().msgBox(preMessage);
        preMessageControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 2 && !userEntry.isEmpty()) {
                    final String[] coordsString = userEntry.split(" ");
                    final int x = Integer.parseInt(coordsString[0]);
                    final int y = Integer.parseInt(coordsString[1]);
                    if (coordsString.length == 3) {
                        final short instanceId = Short.parseShort(coordsString[2]);
                        UIModerationPanelFrame.INSTANCE.requestWithServer((short)22, (byte)3, x, y, instanceId);
                    }
                    else if (coordsString.length == 2) {
                        UIModerationPanelFrame.INSTANCE.requestWithServer((short)6, (byte)3, x, y);
                    }
                }
            }
        });
    }
    
    public static void tpToPlayer(final Event e) {
        UIModerationPanelFrame.INSTANCE.request((short)212, (short)5, UIModerationPanelFrame.INSTANCE.getModerationPanelView().getCurrentPlayer().getName());
    }
    
    public static void tpToMe(final Event e) {
        UIModerationPanelFrame.INSTANCE.request((short)212, (short)36, UIModerationPanelFrame.INSTANCE.getModerationPanelView().getCurrentPlayer().getName());
    }
    
    public static void tpToJail(final Event e) {
        UIModerationPanelFrame.INSTANCE.request((short)212, (short)200, UIModerationPanelFrame.INSTANCE.getModerationPanelView().getCurrentPlayer().getName());
    }
    
    public static void freedom(final Event e) {
        UIModerationPanelFrame.INSTANCE.request((short)212, (short)203, UIModerationPanelFrame.INSTANCE.getModerationPanelView().getCurrentPlayer().getName());
    }
    
    public static void switchContainerVisibility(final Event e, final Button switchButton, final Container container) {
        final boolean visible = !container.isVisible();
        container.setVisible(visible);
        switchButton.setStyle(visible ? "YellowUpArrow" : "YellowDownArrow");
    }
    
    public static void changeBackgroundColor(final MouseEvent mouseEvent, final PlainBackground plainBackground) {
        plainBackground.setColor((mouseEvent.getType() == Events.MOUSE_ENTERED) ? new Color(0.5f, 0.5f, 0.5f, 0.1f) : new Color(0.5f, 0.5f, 0.5f, 0.0f));
    }
}
