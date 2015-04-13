package com.ankamagames.wakfu.client.ui.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.decorator.*;
import gnu.trove.*;
import com.ankamagames.xulor2.core.form.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.console.command.commonIn.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.chat.*;
import com.ankamagames.baseImpl.graphics.chat.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.xulor2.component.text.builder.content.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.xulor2.component.text.document.part.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.client.ui.protocol.message.fight.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.moderationNew.panel.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.group.party.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.xulor2.component.*;

@XulorActionsTag
public class ChatDialogActions
{
    private static final Logger m_logger;
    public static final String PACKAGE = "wakfu.chat";
    private static FieldProvider m_describedChatLink;
    private static PlainBackground m_overPlainBackground;
    private static final String PRIVATE_PATTERN = "/w (&quot;.*&quot;|[^&quot;][\\p{L}'-]*[^&quot;])\\s+(.*)(^(.*)$)*";
    private static TIntByteHashMap m_chatActive;
    private static ScrollRunnable m_scrollRunnable;
    private static final EventListener m_mouseReleasedListener;
    
    public static boolean processInputKeyEvent(final KeyEvent keyPressedEvent, final Form form, final AbstractChatView view) {
        form.synchronizeProperties();
        final Property fieldedProperty = form.getProperty("chat").getChildProperty("currentView");
        fieldedProperty.synchronizeWithLastClient();
        final String currentCommandPrefix = view.getCurrentPrefix();
        final boolean ctrlPressed = keyPressedEvent.hasCtrl();
        switch (keyPressedEvent.getKeyCode()) {
            case 10: {
                form.synchronizeProperties();
                String input = fieldedProperty.getFieldStringValue("input");
                if (input.length() > 0) {
                    if (!input.startsWith("/") && currentCommandPrefix != null && currentCommandPrefix.length() > 0) {
                        input = currentCommandPrefix + " " + input;
                    }
                    if (!input.matches(".*\".*\".*")) {
                        final ChatChannelCommandData commandForChannelName = ChatConfigurator.getCommandForChannelName(ChatCommandsParameters.PRIVATE.getCommandName());
                        final String privateCommandPattern = commandForChannelName.getCommand();
                        String name = null;
                        if (input.matches("/w (&quot;.*&quot;|[^&quot;][\\p{L}'-]*[^&quot;])\\s+(.*)(^(.*)$)*")) {
                            name = input.replaceFirst(privateCommandPattern + ' ', "");
                            name = name.substring(0, name.indexOf(32));
                        }
                        if (name != null && name.length() > 2) {
                            input = input.replaceFirst(name, '\"' + name + '\"');
                        }
                    }
                    final UIChatContentMessage contentMessage = new UIChatContentMessage();
                    contentMessage.setMessage(input);
                    contentMessage.setView(view);
                    Worker.getInstance().pushMessage(contentMessage);
                    fieldedProperty.setFieldValue("input", "");
                    if (WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.CHAT_AUTOMATIC_DISACTIVATION)) {
                        FocusManager.getInstance().setFocused(null);
                    }
                    return true;
                }
                FocusManager.getInstance().setFocused(null);
                return true;
            }
            case 27: {
                FocusManager.getInstance().setFocused(null);
                keyPressedEvent.getTarget().setText("");
                return true;
            }
            case 38: {
                if (ctrlPressed) {
                    GetPrivateContactCommand.execute(false);
                    return true;
                }
                String pastSentence = ChatManager.getInstance().getConsole().getHistoryUp();
                pastSentence = transformPastSentence(currentCommandPrefix, pastSentence);
                fieldedProperty.setFieldValue("input", pastSentence);
                return true;
            }
            case 40: {
                if (ctrlPressed) {
                    GetPrivateContactCommand.execute(false);
                    return true;
                }
                String pastSentence = ChatManager.getInstance().getConsole().getHistoryDown();
                pastSentence = transformPastSentence(currentCommandPrefix, pastSentence);
                fieldedProperty.setFieldValue("input", pastSentence);
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    private static String transformPastSentence(final String currentCommandPrefix, final String pastSentence) {
        if (!currentCommandPrefix.isEmpty()) {
            if (pastSentence.matches("/w (&quot;.*&quot;|[^&quot;][\\p{L}'-]*[^&quot;])\\s+(.*)(^(.*)$)*") && currentCommandPrefix.matches("/w (&quot;.*&quot;|[^&quot;][\\p{L}'-]*[^&quot;])\\s+(.*)(^(.*)$)*")) {
                return pastSentence.replaceFirst("(/w \"[a-zA-Z_0-9'-]+\"\\s)", "");
            }
            if (!pastSentence.startsWith("/") || pastSentence.startsWith(currentCommandPrefix)) {
                return pastSentence.replaceFirst(currentCommandPrefix + " ", "");
            }
        }
        return pastSentence;
    }
    
    public static void chooseChannel(final ListSelectionChangedEvent event, final AbstractChatView view) {
        final ChatViewManager chatViewManager = ChatWindowManager.getInstance().getWindowFromView((ChatView)view);
        if (chatViewManager != null) {
            final UIMessage msg = new UIMessage();
            msg.setId(19025);
            msg.setIntValue(chatViewManager.getWindowId());
            Worker.getInstance().pushMessage(msg);
        }
        if (event.getValue() != null) {
            final UIChatChannelSelectionMessage message = new UIChatChannelSelectionMessage();
            message.setPipeWrapper((ChatPipeWrapper)event.getValue());
            message.setView(view);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    public static void openCloseContactList(final Event event) {
        UIMessage.send((short)19002);
    }
    
    public static void openCloseEmoteBar(final Event event) {
        UIMessage.send((short)19008);
    }
    
    public static void filterButtonActivation(final MouseEvent event, final AbstractChatView view, final PopupElement popup) {
        if (event.getType() == Events.MOUSE_CLICKED) {
            PropertiesProvider.getInstance().setLocalPropertyValue("chat.popupCurrentView", view, event.getTarget().getElementMap());
            XulorActions.openClosePopup(event, popup);
        }
    }
    
    public static void scrollBarDown(final Event event, final ScrollContainer scrollContainer) {
        scrollContainer.scrollVerticalScrollBar(0.0f);
    }
    
    public static void notifyFocusChange(final FocusChangedEvent fEvent, final ChatViewManager chatViewManager) {
        final TextEditor textEditor = fEvent.getTarget();
        final ElementMap map = textEditor.getRenderableParent().getElementMap();
        final Widget focused = FocusManager.getInstance().getFocused();
        if (focused != null && map == focused.getElementMap() && focused.getId() == textEditor.getId()) {
            return;
        }
        final boolean active = !fEvent.getFocused();
        final int windowId = chatViewManager.getWindowId();
        ChatDialogActions.m_chatActive.put(windowId, (byte)(active ? 1 : 0));
        if (active) {
            textEditor.setStyle("dark");
            final UIMessage msg = new UIMessage();
            msg.setId(19026);
            msg.setIntValue(windowId);
            msg.setLongValue(5000L);
            Worker.getInstance().pushMessage(msg);
        }
        else {
            textEditor.setStyle("light");
            final UIMessage msg = new UIMessage();
            msg.setId(19025);
            msg.setIntValue(windowId);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void windowFocusChange(final FocusChangedEvent event, final ChatViewManager manager) {
        if (event.getFocused() && manager != null) {
            final UIMessage msg = new UIMessage();
            msg.setId(19013);
            msg.setIntValue(manager.getWindowId());
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void addPrivatePipe(final Event event, final WakfuUser user) {
        addPrivatePipe(event, user.getCharacterName());
    }
    
    public static void addPrivatePipe(final Event event, final String userName) {
        final UIMessage msg = new UIMessage();
        msg.setId(19061);
        msg.setStringValue(userName);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void closeChat(final Event event, final ChatViewManager chatViewManager) {
        Xulor.getInstance().unload(UIChatFrameHelper.getDialogIdFromWindowId(chatViewManager.getWindowId()));
        final UIMessage msg = new UIMessage();
        msg.setId(19062);
        msg.setIntValue(chatViewManager.getWindowId());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void addItem(final Event event, final Item item, AbstractChatView view) {
        EquipmentDialogActions.onDropItem();
        final long ownerId = WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId();
        if (HeroUtils.getHeroWithItemUidFromBagsOrEquipment(ownerId, item.getUniqueId()) == null && CompanionManager.INSTANCE.getCompanionHoldingItem(ownerId, item.getUniqueId()) == null) {
            return;
        }
        final ChatViewManager currentWindow = ChatWindowManager.getInstance().getCurrentWindow();
        if (view == null) {
            view = currentWindow.getCurrentView();
        }
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(UIChatFrameHelper.getDialogIdFromWindowId(currentWindow.getWindowId()));
        if (map == null) {
            return;
        }
        final TextEditor chatTextEditor = (TextEditor)map.getElement("chatInput");
        if (chatTextEditor == null) {
            return;
        }
        final String textToAdd = " [" + item.getName() + "] ";
        UIChatFrame.getInstance().addItemDescription(item);
        view.appendFieldValue("input", textToAdd);
        PropertiesProvider.getInstance().firePropertyValueChanged(view, "input");
        if (!chatTextEditor.equals(FocusManager.getInstance().getFocused())) {
            FocusManager.getInstance().setFocused(chatTextEditor);
        }
    }
    
    public static void addItem(final DropEvent dropEvent, final AbstractChatView view) {
        if (dropEvent.getValue() instanceof Item) {
            addItem(dropEvent, (Item)dropEvent.getValue(), view);
        }
    }
    
    public static void processLinkAction(final Event event) {
        processLinkAction(event, null);
    }
    
    public static void processLinkAction(final Event event, final AbstractChatView view) {
        final TextView textView = event.getTarget();
        final AbstractContentBlock block = textView.getBlockUnderMouse();
        if (block != null && block.getType() == AbstractContentBlock.BlockType.TEXT) {
            final AbstractDocumentPart part = block.getDocumentPart();
            if (part.getType() == DocumentPartType.TEXT) {
                final String partId = ((TextDocumentPart)part).getId();
                if (partId != null && partId.length() > 0) {
                    final String[] parts = partId.split("_");
                    if (parts.length == 2) {
                        final String type = parts[0];
                        final String id = parts[1];
                        if (type == null || type.length() == 0) {
                            return;
                        }
                        if (event.getType() == Events.MOUSE_CLICKED) {
                            onClickSelectableText((MouseEvent)event, view, type, id, part.getData());
                        }
                        else if (event.getType() == Events.MOUSE_ENTERED) {
                            onMouseEnterSelectableText(event, view, type, id, part.getData());
                        }
                    }
                }
            }
        }
    }
    
    private static void onClickSelectableText(final MouseEvent event, final AbstractChatView view, final String type, final String id, final String data) {
        if (type.equals("item")) {
            final Item item = fromLogRepresentation(id);
            if (event.getButton() == 3) {
                Actions.sendOpenCloseItemDetailMessage(null, item);
            }
        }
        else if (type.equals("state")) {
            final int stateData = Integer.parseInt(id);
            final short stateBaseId = MathHelper.getFirstShortFromInt(stateData);
            final short stateLevel = MathHelper.getSecondShortFromInt(stateData);
            StateClient state = (StateClient)StateManager.getInstance().getState(stateBaseId);
            if (state.getLevel() != stateLevel) {
                state = state.instanceAnother(stateLevel);
            }
            final UIStateMessage uiStateMessage = new UIStateMessage();
            uiStateMessage.setState(state);
            uiStateMessage.setStringValue(event.getTarget().getElementMap().getId());
            Worker.getInstance().pushMessage(uiStateMessage);
        }
        else if (type.equals("characterName")) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            boolean isPnj = false;
            long sourceId = -1L;
            try {
                sourceId = Long.parseLong(id);
                if (MobileManager.getInstance().getMobile(sourceId) == null) {
                    final CharacterInfo characterInfo = CharacterInfoManager.getInstance().getCharacterByName(data);
                }
                else {
                    final CharacterInfo characterInfo = CharacterInfoManager.getInstance().getCharacter(sourceId);
                }
                isPnj = (sourceId < 0L);
            }
            catch (Exception ex) {}
            if (data == null || data.equals(localPlayer.getName())) {
                return;
            }
            if (event.getButton() == 3) {
                final PopupMenu popupMenu = Xulor.getInstance().popupMenu();
                popupMenu.addLabel(data, null);
                if (!isPnj) {
                    popupMenu.addButton(WakfuTranslator.getInstance().getString("desc.mru.createPrivateChat", data), null, new MouseClickedListener() {
                        @Override
                        public boolean run(final Event event) {
                            final UIMessage msg = new UIMessage();
                            msg.setId(19061);
                            msg.setStringValue(data);
                            Worker.getInstance().pushMessage(msg);
                            return false;
                        }
                    }, true);
                }
                if (!isPnj && WakfuUserGroupManager.getInstance().getFriendGroup().getUserById(sourceId) == null) {
                    popupMenu.addButton(WakfuTranslator.getInstance().getString("chat.addToFriendList"), null, new MouseClickedListener() {
                        @Override
                        public boolean run(final Event event) {
                            final AddFriendMessage addFriendMessage = new AddFriendMessage();
                            addFriendMessage.setFriendName(data);
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(addFriendMessage);
                            return false;
                        }
                    }, true);
                }
                if (!isPnj && WakfuUserGroupManager.getInstance().getIgnoreGroup().getUserById(sourceId) == null) {
                    popupMenu.addButton(WakfuTranslator.getInstance().getString("chat.addToIgnoreList"), null, new MouseClickedListener() {
                        @Override
                        public boolean run(final Event event) {
                            final AddIgnoreMessage privateMessage = new AddIgnoreMessage();
                            privateMessage.setIgnoreName(data);
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(privateMessage);
                            return false;
                        }
                    }, true);
                }
                if (!isPnj) {
                    boolean addToGroup = true;
                    if (localPlayer.getPartyComportment().isInParty()) {
                        final PartyModelInterface party = localPlayer.getPartyComportment().getParty();
                        if (party.contains(data)) {
                            addToGroup = false;
                        }
                    }
                    if (addToGroup) {
                        popupMenu.addButton(WakfuTranslator.getInstance().getString("desc.mru.invitToJoinGroup", data), null, new MouseClickedListener() {
                            @Override
                            public boolean run(final Event event) {
                                localPlayer.getPartyComportment().inviteSomeone(data);
                                return false;
                            }
                        }, true);
                    }
                }
                final CharacterInfo characterInfo2 = CharacterInfoManager.getInstance().getCharacterByName(data);
                if (characterInfo2 != null) {
                    popupMenu.addButton(WakfuTranslator.getInstance().getString("desc.show"), null, new MouseClickedListener() {
                        @Override
                        public boolean run(final Event event) {
                            final UIMessage msg = new UIMessage();
                            msg.setId(19063);
                            msg.setStringValue(data);
                            Worker.getInstance().pushMessage(msg);
                            return false;
                        }
                    }, true);
                }
                if (!isPnj && !AdminRightHelper.checkRights(WakfuGameEntity.getInstance().getLocalAccount().getAdminRights(), AdminRightHelper.NO_RIGHT)) {
                    popupMenu.addButton(WakfuTranslator.getInstance().getString("desc.mru.openModerationPanel"), null, new MouseClickedListener() {
                        @Override
                        public boolean run(final Event event) {
                            UIModerationPanelFrame.INSTANCE.getModerationPanelView().setCurrentPlayer(new ModeratedPlayerView(data));
                            UIModerationPanelFrame.INSTANCE.getModerationPanelView().setCurrentPage(ModerationPanelPage.PLAYER);
                            if (!WakfuGameEntity.getInstance().hasFrame(UIModerationPanelFrame.INSTANCE)) {
                                WakfuGameEntity.getInstance().pushFrame(UIModerationPanelFrame.INSTANCE);
                            }
                            return false;
                        }
                    }, true);
                }
                popupMenu.show(MouseManager.getInstance().getX() + 50, MouseManager.getInstance().getY() + 10);
            }
            else if (event.getButton() == 1) {
                if (event.hasShift()) {
                    final UIMessage msg = new UIMessage();
                    msg.setId(19061);
                    msg.setStringValue(data);
                    Worker.getInstance().pushMessage(msg);
                    return;
                }
                view.setFieldValue("input", "/w \"" + data + "\" ");
                PropertiesProvider.getInstance().firePropertyValueChanged(view, "input");
                final TextEditor chatTextEditor = (TextEditor)UIChatFrame.getInstance().getCurrentChatWindow().getElementMap().getElement("textEditorRenderableContainer.chatInput");
                if (chatTextEditor == null) {
                    return;
                }
                if (!chatTextEditor.equals(FocusManager.getInstance().getFocused())) {
                    FocusManager.getInstance().setFocused(chatTextEditor);
                }
            }
        }
    }
    
    private static void onMouseEnterSelectableText(final Event event, final AbstractChatView view, final String type, final String id, final String data) {
        if (type.equals("item")) {
            PopupInfosActions.showPopup(ChatDialogActions.m_describedChatLink = fromLogRepresentation(id), 500);
        }
    }
    
    public static void onMouseExitSelectableText(final Event event) {
        if (ChatDialogActions.m_describedChatLink != null) {
            PopupInfosActions.hidePopup(event, ChatDialogActions.m_describedChatLink);
            ChatDialogActions.m_describedChatLink = null;
        }
    }
    
    public static void setPrivateName(final Event event, final ChatView chatView) {
        if (event instanceof FocusChangedEvent && chatView.getPrivateName() != null) {
            final FocusChangedEvent focusEvent = (FocusChangedEvent)event;
            final TextEditor textEditor = event.getTarget();
            if (focusEvent.getTarget() == focusEvent.getCurrentTarget() && !focusEvent.getFocused()) {
                chatView.setCurrentPrefix(ChatView.getPrivatePrefix(textEditor.getText()));
            }
            else if (focusEvent.getFocused() && textEditor.getText().contains("<")) {
                chatView.setCurrentPrefix(ChatView.getPrivatePrefix(""));
            }
        }
    }
    
    public static void selectView(final Event event, final ChatViewManager chatViewManager, final ChatView chatView, final TextEditor te) {
        if (chatViewManager != null) {
            final UIMessage msg = new UIMessage();
            msg.setId(19025);
            msg.setIntValue(chatViewManager.getWindowId());
            Worker.getInstance().pushMessage(msg);
        }
        if (((MouseEvent)event).getButton() == 3) {
            if (chatView.isDefaultChatView() || chatView.isPrivateView()) {
                return;
            }
            PropertiesProvider.getInstance().setPropertyValue("chat.editedView", chatView);
            FocusManager.getInstance().setFocused(te);
        }
        else {
            chatViewManager.setCurrentView(chatView);
            PropertiesProvider.getInstance().firePropertyValueChanged("chat", "currentView", event.getTarget().getElementMap());
        }
    }
    
    public static void dropView(final DropEvent event, final ChatViewManager chatViewManager, final ChatView chatViewTo) {
    }
    
    public static void dragView(final DragEvent event, final ChatViewManager chatViewManager) {
        final UIMessage msg = new UIMessage();
        msg.setId(19025);
        msg.setIntValue(chatViewManager.getWindowId());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void dropView(final DropOutEvent event, final ChatViewManager chatViewManager) {
        dropView((ChatView)event.getValue(), event, chatViewManager);
    }
    
    public static void dropView(final DropEvent event) {
        if (!(event.getSourceValue() instanceof ChatView)) {
            return;
        }
        final ChatView chatView = (ChatView)event.getSourceValue();
        dropView(chatView, event, ChatWindowManager.getInstance().getWindowFromView(chatView));
    }
    
    public static void dropView(final ChatView chatView, final MouseEvent event, final ChatViewManager chatViewManager) {
        final int screenX = event.getScreenX();
        final int screenY = event.getScreenY();
        final ChatViewManager overChat = UIChatFrame.getInstance().getFirstChatOnPosition(event);
        if (overChat != null && ChatDialogActions.m_overPlainBackground != null) {
            ChatDialogActions.m_overPlainBackground.setColor(Color.WHITE_ALPHA);
        }
        final int windowFromId = chatViewManager.getWindowId();
        if (overChat == chatViewManager) {
            final ChatView chatViewTo = UIChatFrame.getInstance().getFirstChatViewOnPosition(event);
            if (chatView != chatViewTo && chatViewTo != null) {
                chatViewManager.removeView(chatView, false);
                chatViewManager.moveView(chatView, chatViewTo.getViewIndex() + 1);
                chatViewManager.setCurrentView(chatView);
                ChatWindowManager.getInstance().refreshAllViews();
                ChatWindowManager.getInstance().saveChatConfiguration();
                return;
            }
        }
        else if (overChat != null && overChat.getNumberOfViews() < 4) {
            ChatWindowManager.getInstance().transferChatView(chatView, windowFromId, overChat.getWindowId());
            final Window chatWindow = UIChatFrame.getInstance().getChatWindow(overChat.getWindowId());
            chatWindow.addWindowPostProcessedListener(new WindowPostProcessedListener() {
                @Override
                public void windowPostProcessed() {
                    chatWindow.removeWindowPostProcessedListener(this);
                    overChat.refreshCurrentView();
                }
            });
            ChatWindowManager.getInstance().saveChatConfiguration();
            UIMessage msg = new UIMessage();
            msg.setId(19025);
            msg.setIntValue(overChat.getWindowId());
            Worker.getInstance().pushMessage(msg);
            msg = new UIMessage();
            msg.setId(19026);
            msg.setIntValue(overChat.getWindowId());
            msg.setLongValue(5000L);
            Worker.getInstance().pushMessage(msg);
            msg = new UIMessage();
            msg.setId(19026);
            msg.setIntValue(windowFromId);
            msg.setLongValue(5000L);
            Worker.getInstance().pushMessage(msg);
            return;
        }
        if (chatViewManager.getNumberOfViews() == 1) {
            final Window chatWindow = UIChatFrame.getInstance().getChatWindow(chatViewManager.getWindowId());
            chatWindow.setPosition(screenX - 50, screenY + 10 - chatWindow.getHeight());
            final UIMessage msg = new UIMessage();
            msg.setId(19026);
            msg.setIntValue(windowFromId);
            msg.setLongValue(5000L);
            Worker.getInstance().pushMessage(msg);
            return;
        }
        if (overChat == chatViewManager) {
            return;
        }
        final ChatViewManager newChatViewManager = ChatWindowManager.getInstance().transferChatView(chatView, windowFromId, -1);
        final Window widget = (Window)UIChatFrame.getInstance().loadChatWindow(newChatViewManager, null);
        widget.addWindowPostProcessedListener(new WindowPostProcessedListener() {
            @Override
            public void windowPostProcessed() {
                widget.setPosition(screenX - 50, screenY + 10 - widget.getHeight());
                widget.removeWindowPostProcessedListener(this);
                newChatViewManager.refreshCurrentView();
            }
        });
        final Window windowFrom = UIChatFrame.getInstance().getChatWindow(windowFromId);
        windowFrom.addWindowPostProcessedListener(new WindowPostProcessedListener() {
            @Override
            public void windowPostProcessed() {
                windowFrom.removeWindowPostProcessedListener(this);
                chatViewManager.refreshCurrentView();
            }
        });
        ChatWindowManager.getInstance().saveChatConfiguration();
        UIMessage msg2 = new UIMessage();
        msg2.setId(19026);
        msg2.setIntValue(windowFromId);
        msg2.setLongValue(5000L);
        Worker.getInstance().pushMessage(msg2);
        msg2 = new UIMessage();
        msg2.setId(19026);
        msg2.setIntValue(newChatViewManager.getWindowId());
        msg2.setLongValue(5000L);
        Worker.getInstance().pushMessage(msg2);
    }
    
    public static void createView(final Event event, final ChatViewManager chatViewManager) {
        chatViewManager.createView();
        ChatWindowManager.getInstance().saveChatConfiguration();
    }
    
    public static void openChatOptions(final Event event) {
        WakfuGameEntity.getInstance().pushFrame(UIChatOptionsFrame.getInstance());
    }
    
    public static void deleteView(final ChatView chatView) {
        final ChatViewManager viewManager = ChatWindowManager.getInstance().getWindow(ChatWindowManager.getInstance().getWindowIdFromView(chatView));
        ChatWindowManager.getInstance().deleteView(viewManager, chatView, -1);
        PropertiesProvider.getInstance().firePropertyValueChanged(ChatWindowManager.getInstance(), ChatWindowManager.FIELDS);
        PropertiesProvider.getInstance().firePropertyValueChanged(ChatWindowManager.getInstance(), ChatWindowManager.FIELDS);
        PropertiesProvider.getInstance().firePropertyValueChanged(viewManager, ChatViewManager.FIELDS);
        ChatWindowManager.getInstance().saveChatConfiguration();
        PropertiesProvider.getInstance().setPropertyValue("chat.editedView", null);
    }
    
    public static void setViewName(final Event e, final TextEditor textEditor, final ChatViewManager chatViewManager) {
        final ChatView chatView = (ChatView)PropertiesProvider.getInstance().getObjectProperty("chat.editedView");
        if (chatView == null) {
            return;
        }
        chatView.setName(textEditor.getText());
        chatView.setNameDirty(true);
        PropertiesProvider.getInstance().firePropertyValueChanged(chatView, ChatView.FIELDS);
        PropertiesProvider.getInstance().firePropertyValueChanged(chatViewManager, ChatViewManager.FIELDS);
        ChatWindowManager.getInstance().saveChatConfiguration();
        if ((e instanceof FocusChangedEvent && !((FocusChangedEvent)e).getFocused()) || (e instanceof KeyEvent && ((KeyEvent)e).getKeyCode() == 10)) {
            PropertiesProvider.getInstance().setPropertyValue("chat.editedView", null);
            FocusManager.getInstance().setFocused(null);
        }
    }
    
    public static void activateDisactivateFilter(final Event e, final ChatView chatView, final String filterName) {
        final ChatViewManager chatViewManager = ChatWindowManager.getInstance().getWindowFromView(chatView);
        if (chatViewManager != null) {
            final UIMessage msg = new UIMessage();
            msg.setId(19026);
            msg.setIntValue(chatViewManager.getWindowId());
            msg.setLongValue(5000L);
            Worker.getInstance().pushMessage(msg);
        }
        final ToggleButton toggleButton = e.getCurrentTarget();
        chatView.activateDisactivatePipe(ChatManager.getInstance().getChatPipe(filterName).getId(), toggleButton.getSelected());
        chatView.updateDisplayHistory();
        ChatWindowManager.getInstance().saveChatConfiguration();
    }
    
    public static void deleteView(final Event event, final ChatView chatView) {
        deleteView(chatView);
    }
    
    public static void onMouseEnterChat(final Event event) {
    }
    
    public static void onMouseExitChat(final Event event) {
    }
    
    public static void onMouseEnterIntersection(final Event event) {
    }
    
    public static void onMouseExitIntersection(final Event event) {
    }
    
    public static void setOverIntersection(final EventDispatcher target, final boolean over) {
    }
    
    public static void goDownText(final Event e, final ScrollContainer scrollContainer) {
        goDownText(e, scrollContainer, true);
    }
    
    public static void goDownText(final Event e, final ScrollContainer scrollContainer, final boolean schedule) {
        if (schedule && ChatDialogActions.m_scrollRunnable.isRunning()) {
            return;
        }
        ChatDialogActions.m_scrollRunnable.setScrollContainer(scrollContainer);
        ChatDialogActions.m_scrollRunnable.setUp(false);
        ChatDialogActions.m_scrollRunnable.setRunning(true);
        ChatDialogActions.m_scrollRunnable.run();
        if (schedule) {
            ProcessScheduler.getInstance().schedule(ChatDialogActions.m_scrollRunnable, 100L);
            MasterRootContainer.getInstance().addEventListener(Events.MOUSE_RELEASED, ChatDialogActions.m_mouseReleasedListener, true);
        }
    }
    
    public static void goUpText(final Event e, final ScrollContainer scrollContainer) {
        goUpText(e, scrollContainer, true);
    }
    
    public static void goUpText(final Event e, final ScrollContainer scrollContainer, final boolean schedule) {
        if (schedule && ChatDialogActions.m_scrollRunnable.isRunning()) {
            return;
        }
        ChatDialogActions.m_scrollRunnable.setScrollContainer(scrollContainer);
        ChatDialogActions.m_scrollRunnable.setUp(true);
        ChatDialogActions.m_scrollRunnable.setRunning(true);
        ChatDialogActions.m_scrollRunnable.run();
        if (schedule) {
            ProcessScheduler.getInstance().schedule(ChatDialogActions.m_scrollRunnable, 100L);
            MasterRootContainer.getInstance().addEventListener(Events.MOUSE_RELEASED, ChatDialogActions.m_mouseReleasedListener, true);
        }
    }
    
    public static void goDownBundaryText(final Event e, final ScrollContainer scrollContainer) {
        final float scrollValue = 0.0f;
        scrollContainer.setVerticalScrollBarPosition(0.0f);
        PropertiesProvider.getInstance().setLocalPropertyValue("chat.scrollOffset", scrollContainer.getVerticalScrollBar().getValue(), e.getCurrentTarget().getElementMap().getId());
    }
    
    private static final void cleanScrollRunnable() {
        ProcessScheduler.getInstance().remove(ChatDialogActions.m_scrollRunnable);
        ChatDialogActions.m_scrollRunnable.setRunning(false);
        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, ChatDialogActions.m_mouseReleasedListener, true);
    }
    
    public static Item fromLogRepresentation(final String logRepresentation) {
        return Item.LogRepresentation.toItem(Item.LogRepresentation.parse(logRepresentation));
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChatDialogActions.class);
        ChatDialogActions.m_chatActive = new TIntByteHashMap();
        ChatDialogActions.m_scrollRunnable = new ScrollRunnable();
        m_mouseReleasedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                cleanScrollRunnable();
                return false;
            }
        };
    }
    
    private static class ScrollRunnable implements Runnable
    {
        private ScrollContainer m_scrollContainer;
        private Boolean m_up;
        private boolean m_running;
        
        public void setScrollContainer(final ScrollContainer scrollContainer) {
            this.m_scrollContainer = scrollContainer;
        }
        
        public void setUp(final Boolean up) {
            this.m_up = up;
        }
        
        @Override
        public void run() {
            final ScrollBar bar = this.m_scrollContainer.getVerticalScrollBar();
            float scrollValue = bar.getValue();
            final float buttonJump = bar.getButtonJump();
            if (this.m_up) {
                if (scrollValue == 1.0f) {
                    cleanScrollRunnable();
                    return;
                }
                scrollValue += buttonJump;
                if (scrollValue > 1.0f) {
                    scrollValue = 1.0f;
                }
            }
            else {
                if (scrollValue == 0.0f) {
                    cleanScrollRunnable();
                    return;
                }
                scrollValue -= buttonJump;
                if (scrollValue < 0.0f) {
                    scrollValue = 0.0f;
                }
            }
            this.m_scrollContainer.setVerticalScrollBarPosition(scrollValue);
            PropertiesProvider.getInstance().setLocalPropertyValue("chat.scrollOffset", bar.getValue(), this.m_scrollContainer.getElementMap().getId());
        }
        
        public boolean isRunning() {
            return this.m_running;
        }
        
        public void setRunning(final boolean running) {
            this.m_running = running;
        }
    }
}
