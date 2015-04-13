package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.message.guild.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.group.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;

@XulorActionsTag
public class GuildManagementDialogActions
{
    public static final String PACKAGE = "wakfu.guildManagement";
    private static int m_currentPosition;
    private static GuildRankView m_sourceValue;
    private static int m_startPosition;
    
    public static void closeDialog(final Event e) {
        WakfuGameEntity.getInstance().removeFrame(UIGuildManagementFrame.getInstance());
    }
    
    public static void onRankDropped(final DropEvent dropEvent) {
        final EventDispatcher target = dropEvent.getTarget();
        if (target == null || target.getRenderableParent() == null) {
            return;
        }
        final UIMessage uiMessage = new UIMessage();
        uiMessage.setId(18214);
        uiMessage.setShortValue((short)GuildManagementDialogActions.m_currentPosition);
        uiMessage.setLongValue(GuildManagementDialogActions.m_sourceValue.getId());
        Worker.getInstance().pushMessage(uiMessage);
        GuildManagementDialogActions.m_sourceValue = null;
        GuildManagementDialogActions.m_currentPosition = -1;
        GuildManagementDialogActions.m_startPosition = -1;
        PropertiesProvider.getInstance().setPropertyValue("draggedGuildRank", null);
    }
    
    public static void onRankDragged(final DragEvent dragEvent) {
        final EventDispatcher target = dragEvent.getTarget();
        final List l = (List)target.getElementMap().getElement("rankList");
        GuildManagementDialogActions.m_sourceValue = (GuildRankView)dragEvent.getSourceValue();
        GuildManagementDialogActions.m_startPosition = l.getOffsetByRenderable(target.getRenderableParent());
        PropertiesProvider.getInstance().setPropertyValue("draggedGuildRank", GuildManagementDialogActions.m_sourceValue);
    }
    
    public static void onRankDropOut(final DropOutEvent dropOutEvent) {
        final EventDispatcher target = dropOutEvent.getDragNDropable();
        if (target == null || target.getRenderableParent() == null) {
            return;
        }
        final List l = (List)target.getElementMap().getElement("rankList");
        l.moveValueToIndex(GuildManagementDialogActions.m_sourceValue, GuildManagementDialogActions.m_startPosition);
        GuildManagementDialogActions.m_sourceValue = null;
        GuildManagementDialogActions.m_currentPosition = -1;
        GuildManagementDialogActions.m_startPosition = -1;
        PropertiesProvider.getInstance().setPropertyValue("draggedGuildRank", null);
        WakfuGuildView.getInstance().updateRanksField(true);
    }
    
    public static void onRankDraggedOver(final DragOverEvent event) {
        final EventDispatcher target = event.getOver();
        if (GuildManagementDialogActions.m_sourceValue == null || target == null || target.getRenderableParent() == null) {
            return;
        }
        final List l = (List)target.getElementMap().getElement("rankList");
        if (l == null) {
            return;
        }
        final int index = l.getOffsetByRenderable(target.getRenderableParent());
        final GuildRankView guildRankView = (GuildRankView)l.getValue(index);
        if (!guildRankView.canBeDeletedByLocalPlayer()) {
            return;
        }
        GuildManagementDialogActions.m_currentPosition = index;
        l.moveValueToIndex(GuildManagementDialogActions.m_sourceValue, GuildManagementDialogActions.m_currentPosition);
    }
    
    public static void onMouseEnter(final Event event, final Container container, final GuildRankView guildRankView) {
        final GuildRankView draggedGuildRankView = (GuildRankView)PropertiesProvider.getInstance().getObjectProperty("draggedGuildRank");
        if (draggedGuildRankView != null) {
            return;
        }
        if (!guildRankView.canBeDeletedByLocalPlayer()) {
            return;
        }
        PropertiesProvider.getInstance().setPropertyValue("overGuildRank", guildRankView);
        WakfuGuildView.getInstance().updateRanksField(true);
    }
    
    public static void onMouseExit(final Event event, final Container container, final GuildRankView guildRankView) {
        final GuildRankView draggedGuildRankView = (GuildRankView)PropertiesProvider.getInstance().getObjectProperty("draggedGuildRank");
        if (draggedGuildRankView != null) {
            return;
        }
        PropertiesProvider.getInstance().setPropertyValue("overGuildRank", null);
        WakfuGuildView.getInstance().updateRanksField(true);
    }
    
    public static void switchRank(final ListSelectionChangedEvent event, final Long characterId) {
        if (event.getSelected()) {
            final UIGuildChangeRankMessage msg = new UIGuildChangeRankMessage(characterId, ((GuildRankView)event.getValue()).getId());
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void addToGuild(final Event e, final TextEditor ed) {
        if (e.getType() == Events.KEY_PRESSED && ((KeyEvent)e).getKeyCode() != 10) {
            return;
        }
        final String value = ed.getText();
        if (value != null && value.length() > 0) {
            final UIMessage msg = new UIMessage();
            msg.setId(18206);
            msg.setStringValue(value);
            Worker.getInstance().pushMessage(msg);
            ed.setText("");
            FocusManager.getInstance().setFocused(null);
        }
    }
    
    public static void excludeCharacter(final Event e, final Long id) {
        final UIMessage msg = new UIMessage();
        msg.setId(18207);
        msg.setLongValue(id);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void leaveGuild(final Event e) {
        UIMessage.send((short)(WakfuGuildView.getInstance().isLocalPlayerLeader() ? 18210 : 18208));
    }
    
    public static void createRank(final Event e) {
        UIMessage.send((short)18211);
    }
    
    public static void deleteRank(final Event e, final GuildRankView rank) {
        final UIMessage msg = new UIMessage();
        msg.setLongValue(rank.getId());
        msg.setId(18212);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void applyRankModifications(final Event e) {
        UIMessage.send((short)18213);
    }
    
    public static void displayDisconnectedMembers(final SelectionChangedEvent e) {
        final UIMessage msg = new UIMessage();
        msg.setId(18209);
        msg.setBooleanValue(e.isSelected());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void addToParty(final Event event, final Long id) {
        final WakfuGuildMemberView guildMember = WakfuGuildView.getInstance().getMemberView(id);
        if (guildMember == null) {
            return;
        }
        final UIGroupSendInvitationMessage msg = new UIGroupSendInvitationMessage();
        msg.setGroupType(GroupType.PARTY);
        msg.setName(guildMember.getName());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void addToFriendlist(final Event event, final Long id) {
        final WakfuGuildMemberView guildMember = WakfuGuildView.getInstance().getMemberView(id);
        if (guildMember == null) {
            return;
        }
        final AddFriendMessage addFriendMessage = new AddFriendMessage();
        addFriendMessage.setFriendName(guildMember.getName());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(addFriendMessage);
        final Widget widget = event.getTarget();
        widget.setEnabled(false);
    }
    
    public static void addPrivatePipe(final Event e, final Long id) {
        final WakfuGuildMemberView guildMember = WakfuGuildView.getInstance().getMemberView(id);
        if (guildMember == null) {
            return;
        }
        ChatDialogActions.addPrivatePipe(e, guildMember.getName());
    }
    
    public static void editPersonalNote(final Event event) {
    }
    
    public static void changeRankName(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 10 && checkRankModificationDirty()) {
            applyRankModifications(keyEvent);
            return;
        }
        final ModifiedGuildRankView selectedRank = (ModifiedGuildRankView)PropertiesProvider.getInstance().getObjectProperty("selectedGuildRank");
        if (selectedRank == null) {
            return;
        }
        final TextEditor te = (TextEditor)keyEvent.getCurrentTarget().getElementMap().getElement("guildName");
        selectedRank.setName(te.getText());
        checkRankModificationDirty();
    }
    
    public static void checkAuthorisation(final MouseEvent e, final GuildAuthorisationView guildAuthorisationView) {
        final ToggleButton tb = e.getCurrentTarget();
        guildAuthorisationView.setChecked(tb.getSelected());
        checkRankModificationDirty();
    }
    
    public static boolean checkRankModificationDirty() {
        final GuildRankView selectedRank = (GuildRankView)PropertiesProvider.getInstance().getObjectProperty("selectedGuildRank");
        if (selectedRank == null) {
            return false;
        }
        final boolean dirty = !selectedRank.equals(WakfuGuildView.getInstance().getRankView(selectedRank.getId()));
        PropertiesProvider.getInstance().setPropertyValue("rankModificationDirty", dirty);
        return dirty;
    }
    
    public static void selectRank(final ItemEvent itemEvent) {
        final GuildRankView guildRankView = (GuildRankView)itemEvent.getItemValue();
        final TextEditor te = (TextEditor)itemEvent.getTarget().getElementMap().getElement("guildName");
        te.setText(guildRankView.getName());
        PropertiesProvider.getInstance().setPropertyValue("selectedGuildRank", guildRankView.getCopy());
        WakfuGuildView.getInstance().updateRanksField(true);
    }
    
    public static void displayPage(final SelectionChangedEvent e) {
        if (e.isSelected()) {
            final RadioButton radioButton = e.getTarget();
            final int index = Integer.valueOf(radioButton.getValue());
            PropertiesProvider.getInstance().setLocalPropertyValue("currentPage", index, radioButton.getElementMap());
        }
    }
    
    public static void openMessagesEditionDialog(final Event e) {
        PropertiesProvider.getInstance().setPropertyValue("isEditingGuildDescs", true);
        Xulor.getInstance().load("guildTextEditorDialog", Dialogs.getDialogPath("guildTextEditorDialog"), 257L, (short)10000);
    }
    
    public static void onKeyEvent(final Event event, final Button b) {
        if (event.getType() == Events.KEY_PRESSED) {
            final TextEditor textEditor = event.getTarget();
            final KeyEvent keyPressedEvent = (KeyEvent)event;
            if (!NationDialogActions.checkText(textEditor, keyPressedEvent.getKeyChar() == '\n')) {}
            final String text = textEditor.getText();
            if (text != null && text.length() > 0) {
                b.setEnabled(true);
            }
        }
    }
    
    private static void msgBox(final String msg, final TextEditor te) {
        final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(msg, WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 102, 1);
        messageBoxControler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                FocusManager.getInstance().setFocused(te);
            }
        });
    }
    
    public static void applyGuildDescription(final Event e, final TextEditor te) {
        final String text = te.getText();
        final String censoredSentence = WakfuWordsModerator.makeValidSentence(text);
        te.setText(censoredSentence);
        if (censoredSentence.length() == 0 && text.length() != 0) {
            msgBox(WakfuTranslator.getInstance().getString("error.censoredSentence"), te);
            return;
        }
        final UIMessage msg = new UIMessage();
        msg.setId(18216);
        msg.setStringValue(censoredSentence);
        Worker.getInstance().pushMessage(msg);
        e.getTarget().setEnabled(false);
    }
    
    public static void applyGuildMessage(final Event e, final TextEditor te) {
        final String text = te.getText();
        final String censoredSentence = WakfuWordsModerator.makeValidSentence(text);
        te.setText(censoredSentence);
        if (censoredSentence.length() == 0 && text.length() != 0) {
            msgBox(WakfuTranslator.getInstance().getString("error.censoredSentence"), te);
            return;
        }
        final UIMessage msg = new UIMessage();
        msg.setId(18217);
        msg.setStringValue(censoredSentence);
        Worker.getInstance().pushMessage(msg);
        e.getTarget().setEnabled(false);
    }
    
    public static void applyGuildPersonalDescription(final Event e, final TextEditor te) {
        final UIMessage msg = new UIMessage();
        msg.setId(18218);
        msg.setStringValue(te.getText());
        Worker.getInstance().pushMessage(msg);
        e.getTarget().setEnabled(false);
    }
    
    public static void chooseSmiley(final ListSelectionChangedEvent event) {
        if (event.getSelected()) {
            final UIMessage uiMessage = new UIMessage();
            uiMessage.setByteValue((byte)((AvatarSmileyView)event.getValue()).getSmileyEnumId());
            uiMessage.setId(18219);
            Worker.getInstance().pushMessage(uiMessage);
        }
    }
    
    public static void acceptQuest(final Event e, final AchievementView view) {
        final UIMessage msg = new UIMessage();
        msg.setId(16140);
        msg.setIntValue(view.getId());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void switchContainerVisibility(final Event e, final Container c, final Button b) {
        final boolean visible = !c.isVisible();
        c.setVisible(visible);
        b.setStyle(visible ? "YellowUpArrow" : "YellowDownArrow");
    }
    
    public static void selectUpgradeSubCategory(final Event event, final GuildBonusSubCategoryView guildBonusSubCategory) {
        guildBonusSubCategory.toggleOpen();
    }
    
    public static void onNationSelected(final SelectionChangedEvent e, final RadioGroup group) {
        if (e.isSelected()) {
            group.setValue(e.getTarget().getValue());
        }
    }
    
    public static void changeNation(final Event e, final RadioGroup group) {
        final AbstractUIMessage message = new UIMessage((short)17014);
        message.setIntValue(PrimitiveConverter.getInteger(group.getValue()));
        Worker.getInstance().pushMessage(message);
    }
}
