package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.group.party.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.game.hero.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.baseImpl.graphics.game.worldPositionManager.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.component.*;

@XulorActionsTag
public class PartyListDialogActions
{
    public static final String PACKAGE = "wakfu.partyList";
    private static final Logger m_logger;
    public static boolean m_maximized;
    
    public static void deleteMember(final Event e, final PartyDisplayer.PartyMemberDisplayer member) {
        final UIMessage msg = new UIMessage();
        msg.setId(19030);
        msg.setLongValue(member.getId());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void setFollowedMember(final SelectionChangedEvent event, final PartyDisplayer.PartyMemberDisplayer member) {
        final UIMessage msg = new UIMessage();
        msg.setId(19031);
        msg.setLongValue(member.getId());
        msg.setBooleanValue(event.getCurrentTarget().getSelected());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void addMember(final Event event, final TextEditor textEdior) {
        if (event.getType() == Events.MOUSE_CLICKED || (event.getType() == Events.KEY_PRESSED && ((KeyEvent)event).getKeyCode() == 10)) {
            final String text = textEdior.getText();
            if (text != null) {
                final UIMessage msg = new UIMessage();
                msg.setId(19032);
                msg.setStringValue(textEdior.getText());
                Worker.getInstance().pushMessage(msg);
                textEdior.setText("");
            }
        }
    }
    
    public static void onPlayerClick(final MouseEvent e, final PartyDisplayer.PartyMemberDisplayer member) {
        if (e.getButton() != 3) {
            return;
        }
        final LocalPlayerCharacter lpc = WakfuGameEntity.getInstance().getLocalPlayer();
        if (member.getId() == lpc.getId()) {
            return;
        }
        final PopupMenu popupMenu = Xulor.getInstance().popupMenu();
        popupMenu.setHotSpotPosition(Alignment9.WEST);
        final PartyMemberInterface pm = member.getPartyMember();
        popupMenu.addLabel(pm.getName(), null);
        final boolean isCompanion = member.getPartyMember().isCompanion();
        if (lpc.getPartyComportment().getParty().getLeaderId() == lpc.getId() || (isCompanion && member.getPartyMember().getClientId() == WakfuGameEntity.getInstance().getLocalAccount().getAccountId())) {
            popupMenu.addButton(WakfuTranslator.getInstance().getString("party.deleteMember"), null, new MouseClickedListener() {
                @Override
                public boolean run(final Event event) {
                    final UIMessage msg = new UIMessage();
                    msg.setId(19030);
                    msg.setLongValue(member.getId());
                    Worker.getInstance().pushMessage(msg);
                    return false;
                }
            }, !pm.isInFight());
        }
        final boolean isFriend = WakfuUserGroupManager.getInstance().getFriendGroup().getUserById(pm.getClientId()) != null;
        final LocalPlayerCharacter leader = ClientHeroUtils.getLeaderCharacter();
        final long leaderId = (leader != null) ? leader.getId() : 0L;
        final boolean isHero = member.getId() != leaderId && member.getClientId() == WakfuGameEntity.getInstance().getLocalPlayer().getClientId();
        if (!isFriend && !isCompanion && !isHero) {
            popupMenu.addButton(WakfuTranslator.getInstance().getString("chat.addToFriendList"), null, new MouseClickedListener() {
                @Override
                public boolean run(final Event event) {
                    final AddFriendMessage addFriendMessage = new AddFriendMessage();
                    addFriendMessage.setFriendName(pm.getName());
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(addFriendMessage);
                    return false;
                }
            }, true);
        }
        if (!isCompanion && !isHero) {
            final boolean isFollowed = WorldPositionMarkerManager.getInstance().hasPoint(0, pm.getCharacterId());
            if (isFollowed) {
                popupMenu.addButton(WakfuTranslator.getInstance().getString("group.party.unFollowMember"), null, new MouseClickedListener() {
                    @Override
                    public boolean run(final Event event) {
                        WorldPositionMarkerManager.getInstance().removePoint(0, pm.getCharacterId());
                        return false;
                    }
                }, pm.getInstanceId() == lpc.getInstanceId());
            }
            else {
                popupMenu.addButton(WakfuTranslator.getInstance().getString("group.party.followMember"), null, new MouseClickedListener() {
                    @Override
                    public boolean run(final Event event) {
                        final Point3 position = pm.getPosition();
                        WorldPositionMarkerManager.getInstance().setPoint(0, pm.getCharacterId(), position.getX(), position.getY(), position.getZ(), pm, null, false);
                        return false;
                    }
                }, pm.getInstanceId() == lpc.getInstanceId());
            }
        }
        Xulor.getInstance().showPopupMenu(popupMenu, MouseManager.getInstance().getX() + 20, MouseManager.getInstance().getY());
    }
    
    public static void openInviteDialog(final Event e) {
        Xulor.getInstance().load("partyListInviteDialog", Dialogs.getDialogPath("partyListInviteDialog"), 257L, (short)10000);
    }
    
    public static void invite(final Event e, final TextEditor ed) {
        final String name = ed.getText();
        if (name != null) {
            WakfuGameEntity.getInstance().getLocalPlayer().getPartyComportment().inviteSomeone(name);
        }
        Xulor.getInstance().unload("partyListInviteDialog");
    }
    
    public static void leaveParty(final Event e) {
        final UIMessage msg = new UIMessage();
        msg.setId(19030);
        msg.setLongValue(WakfuGameEntity.getInstance().getLocalPlayer().getId());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void minimizeMaximizeDialog(final Event e, final Container c, final Image b) {
        c.setVisible(PartyListDialogActions.m_maximized = !PartyListDialogActions.m_maximized);
        b.setStyle(PartyListDialogActions.m_maximized ? "yellowLeftArrow" : "yellowRightArrow");
    }
    
    static {
        m_logger = Logger.getLogger((Class)PartyListDialogActions.class);
        PartyListDialogActions.m_maximized = true;
    }
}
