package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.*;
import org.jetbrains.annotations.*;
import org.apache.commons.lang3.*;
import java.util.regex.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.contentInitializer.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.wakfu.client.console.command.commonIn.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.protocol.message.chat.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.baseImpl.graphics.chat.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.xulor2.component.*;
import gnu.trove.*;

public class UIChatFrame extends AbstractEmoteIconManager implements MessageFrame, EventListener
{
    private static final Logger m_logger;
    private static final UIChatFrame m_instance;
    private String m_lastPrivateChatDialogId;
    private boolean m_lockChat;
    private boolean m_chatFaded;
    private Window m_mouseInsideChat;
    private TIntObjectHashMap<ChatWindow> m_chatWindows;
    private static final int REMOVE_COMPASS_SUB_ID = 2;
    private static final TIntObjectHashMap<FadeChatRunnable> m_fadeInRequests;
    private static final TIntObjectHashMap<FadeChatRunnable> m_fadeOutRequests;
    public static final int CHAT_WINDOW_FADE_DELAY = 5000;
    private static final int DESCRIBED_ITEMS_IN_CHAT_MAX_COUNT = 100;
    private LinkedList<Long> m_compassIds;
    private TIntObjectHashMap<Item> m_describedItems;
    private LinkedList<Integer> m_decribedItemsMapIds;
    private Pattern m_itemPattern;
    public static final String CHAT_SELECTABLE_ITEM_TYPE_TAG = "item";
    
    public UIChatFrame() {
        super();
        this.m_lockChat = true;
        this.m_chatWindows = new TIntObjectHashMap<ChatWindow>();
        this.m_compassIds = new LinkedList<Long>();
        this.m_describedItems = new TIntObjectHashMap<Item>();
        this.m_decribedItemsMapIds = new LinkedList<Integer>();
        this.m_itemPattern = Pattern.compile("(\\[[^\\[]+\\])");
    }
    
    public boolean isOnChatWindow() {
        return this.m_mouseInsideChat != null;
    }
    
    public static UIChatFrame getInstance() {
        return UIChatFrame.m_instance;
    }
    
    public static String getItemFormatedForChatLinkString(@NotNull final Item item) {
        return "<u id=\"item_" + item.getChatLogRepresentation() + "\">" + item.getName() + "</u>";
    }
    
    public String formatMessageWithItemInfos(String message) {
        final Matcher matcher = this.m_itemPattern.matcher(message);
        while (matcher.find()) {
            final String group = matcher.group();
            final String group2 = group.substring(1, group.length() - 1);
            final Item item = this.m_describedItems.get(group2.hashCode());
            if (item == null) {
                UIChatFrame.m_logger.error((Object)("Erreur lors de la tentative de r\u00e9cup\u00e9ration d'un item \u00e0 d\u00e9crire dans le chat : " + group2));
                return message;
            }
            if (!item.getName().equals(group2)) {
                UIChatFrame.m_logger.error((Object)("Le nom de l'objet en t\u00eate de pile : " + item.getName() + " ne correspond pas \u00e0 " + group2 + " , on le passe..."));
            }
            else {
                message = StringUtils.replace(message, group, getItemFormatedForChatLinkString(item));
                this.addItemDescription(item);
            }
        }
        return message;
    }
    
    public void addItemDescription(final Item item) {
        final int nameHashCode = item.getName().hashCode();
        if (this.m_decribedItemsMapIds.contains(nameHashCode)) {
            return;
        }
        if (this.m_decribedItemsMapIds.size() > 100) {
            final Integer hash = this.m_decribedItemsMapIds.removeFirst();
            this.m_describedItems.remove(hash);
        }
        this.m_decribedItemsMapIds.addLast(nameHashCode);
        this.m_describedItems.put(nameHashCode, item);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (message instanceof ClockMessage) {
            final ClockMessage msg = (ClockMessage)message;
            switch (msg.getSubId()) {
                case 2: {
                    if (this.m_compassIds.isEmpty()) {
                        break;
                    }
                    final long compassId = this.m_compassIds.removeFirst();
                    MapManager.getInstance().removeCompassPointAndPositionMarker();
                    break;
                }
            }
            return false;
        }
        switch (message.getId()) {
            case 19000: {
                final UIChatContentMessage msg2 = (UIChatContentMessage)message;
                AbstractChatView view = msg2.getView();
                if (view == null) {
                    final ChatViewManager manager = ChatWindowManager.getInstance().getCurrentWindow();
                    if (manager != null) {
                        view = manager.getCurrentView();
                    }
                }
                if (view != null) {
                    ChatManager.getInstance().getConsole().parseInput(msg2.getMessage());
                }
                return false;
            }
            case 19011: {
                final UIChatChannelSelectionMessage msg3 = (UIChatChannelSelectionMessage)message;
                final AbstractChatView view = msg3.getView();
                final ChatPipeWrapper wrapper = msg3.getPipeWrapper();
                view.setCurrentChannel(wrapper.getCommandView(), true);
                return false;
            }
            case 19002: {
                if (Xulor.getInstance().isLoaded("contactListDialog")) {
                    WakfuGameEntity.getInstance().removeFrame(UIContactListFrame.getInstance());
                }
                else {
                    WakfuGameEntity.getInstance().pushFrame(UIContactListFrame.getInstance());
                }
                return false;
            }
            case 19013: {
                final UIMessage msg4 = (UIMessage)message;
                ChatWindowManager.getInstance().setCurrentWindow(msg4.getIntValue());
                return false;
            }
            case 19014: {
                ChatWindowManager.getInstance().getCurrentWindow().createView();
                ChatWindowManager.getInstance().saveChatConfiguration();
                return false;
            }
            case 19015: {
                final UIChatRemoveViewMessage msg5 = (UIChatRemoveViewMessage)message;
                ChatWindowManager.getInstance().getCurrentWindow().removeView(msg5.getView(), true);
                ChatWindowManager.getInstance().saveChatConfiguration();
                return false;
            }
            case 19061: {
                final UIMessage msg4 = (UIMessage)message;
                final String characterName = msg4.getStringValue();
                final String prefix = ChatView.getPrivatePrefix(characterName);
                if (ChatWindowManager.getInstance().isPrivateChatExisting(characterName)) {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.chat.privateChatAlreadyExist"), 3);
                    return false;
                }
                ChatViewManager manager2 = ChatWindowManager.getInstance().getFirstWindowWithFreePlace();
                final boolean newWindow = manager2 == null;
                manager2 = ChatInitializer.initPrivateSubPipe(manager2, characterName);
                final ChatView privateView = manager2.getCurrentView();
                privateView.setCurrentPrefix(prefix);
                final int windowId = manager2.getWindowId();
                final String dialogId = UIChatFrameHelper.getDialogIdFromWindowId(windowId);
                Window window;
                if (newWindow) {
                    window = (Window)this.loadChatWindow(manager2, this.m_lastPrivateChatDialogId);
                }
                else {
                    window = this.getChatWindow(manager2.getWindowId());
                }
                Xulor.getInstance().addDialogUnloadListener(new DialogUnloadListener() {
                    @Override
                    public void dialogUnloaded(final String id) {
                        if (id != null && id.equals(dialogId)) {
                            UIChatFrame.this.closePrivateChatWindow(windowId);
                            Xulor.getInstance().removeDialogUnloadListener(this);
                        }
                    }
                });
                final StaticLayoutData staticLayoutData = (StaticLayoutData)window.getLayoutData();
                staticLayoutData.setAlign(Alignment17.CENTER);
                this.m_lastPrivateChatDialogId = dialogId;
                this.m_chatWindows.put(windowId, new ChatWindow(window, manager2));
                ChatWindowManager.getInstance().setCurrentWindow(manager2.getWindowId());
                window.addWindowPostProcessedListener(new WindowPostProcessedListener() {
                    @Override
                    public void windowPostProcessed() {
                        ChatCommand.focusCurrentChatWindow();
                        window.removeWindowPostProcessedListener(this);
                    }
                });
                PropertiesProvider.getInstance().setLocalPropertyValue("chat", manager2, dialogId);
                PropertiesProvider.getInstance().firePropertyValueChanged(manager2, ChatViewManager.FIELDS);
                PropertiesProvider.getInstance().setLocalPropertyValue("chat.channels.list.displayed", false, dialogId);
                return false;
            }
            case 19065: {
                final UIMessage msg4 = (UIMessage)message;
                final String characterName = msg4.getStringValue();
                final String prefix = ChatView.getPrivatePrefix(characterName);
                final ChatViewManager manager2 = ChatInitializer.initPrivateSubPipe(null, characterName);
                final int windowId2 = manager2.getWindowId();
                final ChatView view2 = manager2.getView(0);
                view2.setCurrentPrefix(prefix);
                ChatWindowManager.getInstance().setExchangeWindowId(windowId2);
                PropertiesProvider.getInstance().setLocalPropertyValue("chat", manager2, "exchangeDialog");
                return false;
            }
            case 19066: {
                UIChatFrameHelper.closeExchangeChatWindow();
                return false;
            }
            case 19067: {
                final UIChatCreationRequestMessage msg6 = (UIChatCreationRequestMessage)message;
                final String characterName = msg6.getCharacterName();
                final String prefix = ChatView.getPrivatePrefix(characterName);
                final ChatViewManager manager2 = ChatInitializer.initPrivateSubPipe(null, characterName);
                final int windowId2 = manager2.getWindowId();
                final ChatView view2 = manager2.getView(0);
                view2.setCurrentPrefix(prefix);
                ChatWindowManager.getInstance().setModeratorWindowId(windowId2);
                PropertiesProvider.getInstance().setLocalPropertyValue("chat", manager2, "moderatorChatDialog");
                if (msg6.getListener() != null) {
                    msg6.getListener().onCreation(manager2);
                }
                return false;
            }
            case 19068: {
                UIChatFrameHelper.closeExchangeChatWindow();
                return false;
            }
            case 19062: {
                final UIMessage msg4 = (UIMessage)message;
                final int chatWindowId = msg4.getIntValue();
                this.closePrivateChatWindow(chatWindowId);
                return false;
            }
            case 19063: {
                final UIMessage msg4 = (UIMessage)message;
                final CharacterInfo characterInfo = CharacterInfoManager.getInstance().getCharacterByName(msg4.getStringValue());
                if (characterInfo == null || !characterInfo.getActor().isVisible()) {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.group.memberTooFar"), 3);
                    return false;
                }
                final Point3 memberPosition = characterInfo.getPosition();
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                final long characterId = characterInfo.getId();
                if (!this.m_compassIds.contains(characterId)) {
                    MapManager.getInstance().addCompassPointAndPositionMarker(memberPosition.getX(), memberPosition.getY(), memberPosition.getZ(), characterInfo.getInstanceId(), characterInfo, false);
                    MessageScheduler.getInstance().addClock(this, 5000L, 2, 1);
                    this.m_compassIds.add(characterId);
                }
                return false;
            }
            case 19025: {
                final UIMessage msg4 = (UIMessage)message;
                final boolean force = msg4.getBooleanValue();
                if (!force && this.m_lockChat) {
                    return false;
                }
                final long value = msg4.getLongValue();
                final int windowId2 = msg4.getIntValue();
                FadeChatRunnable fadeChatRunnable = UIChatFrame.m_fadeOutRequests.get(windowId2);
                if (fadeChatRunnable != null && force) {
                    ProcessScheduler.getInstance().remove(fadeChatRunnable);
                }
                fadeChatRunnable = UIChatFrame.m_fadeInRequests.get(windowId2);
                if (fadeChatRunnable != null) {
                    ProcessScheduler.getInstance().remove(fadeChatRunnable);
                }
                if (value == -1L) {
                    return false;
                }
                if (fadeChatRunnable == null) {
                    fadeChatRunnable = new FadeChatRunnable(windowId2, true);
                }
                UIChatFrame.m_fadeInRequests.put(windowId2, fadeChatRunnable);
                ProcessScheduler.getInstance().schedule(fadeChatRunnable, value, 1);
                return false;
            }
            case 19026: {
                final UIMessage msg4 = (UIMessage)message;
                final boolean force = msg4.getBooleanValue();
                if (!force && this.m_lockChat) {
                    return false;
                }
                final long value = msg4.getLongValue();
                final int windowId2 = msg4.getIntValue();
                FadeChatRunnable fadeChatRunnable = UIChatFrame.m_fadeInRequests.get(windowId2);
                if (fadeChatRunnable != null && force) {
                    ProcessScheduler.getInstance().remove(fadeChatRunnable);
                }
                fadeChatRunnable = UIChatFrame.m_fadeOutRequests.get(windowId2);
                if (fadeChatRunnable != null) {
                    ProcessScheduler.getInstance().remove(fadeChatRunnable);
                }
                if (fadeChatRunnable == null) {
                    fadeChatRunnable = new FadeChatRunnable(windowId2, false);
                }
                UIChatFrame.m_fadeOutRequests.put(windowId2, fadeChatRunnable);
                ProcessScheduler.getInstance().schedule(fadeChatRunnable, value, 1);
                return false;
            }
            case 19027: {
                this.m_lockChat = ((UIMessage)message).getBooleanValue();
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void closePrivateChatWindow(final int windowId) {
        final String dialogId = UIChatFrameHelper.getDialogIdFromWindowId(windowId);
        ChatWindowManager.getInstance().removeChatWindow(windowId, -1);
        this.removeChatWindow(windowId);
        if (dialogId != null && dialogId.equals(this.m_lastPrivateChatDialogId)) {
            this.m_lastPrivateChatDialogId = null;
        }
    }
    
    public Window getCurrentChatWindow() {
        final ChatViewManager currentWindow = ChatWindowManager.getInstance().getCurrentWindow();
        return (currentWindow != null) ? this.m_chatWindows.get(currentWindow.getWindowId()).getWindow() : null;
    }
    
    @Override
    public long getId() {
        return 10L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    private void fadeChat(final boolean fadeIn, final int id) {
        final ChatWindow chatWindow = this.m_chatWindows.get(id);
        if (chatWindow == null) {
            return;
        }
        UIChatFrameHelper.fadeChatWindow(id, chatWindow, fadeIn);
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_lockChat = !WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.CHAT_FADE_KEY);
            PropertiesProvider.getInstance().setPropertyValue("chat.isMaximize", false);
            PropertiesProvider.getInstance().setPropertyValue("chatManager", ChatWindowManager.getInstance());
            PropertiesProvider.getInstance().setPropertyValue("chat.editedView", null);
            final TIntObjectIterator<ChatViewManager> it = ChatWindowManager.getInstance().getWindowIterator();
            final boolean first = true;
            String lastDialogId = null;
            final boolean locked = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.BARS_LOCKED_MODE_KEY);
            while (it.hasNext()) {
                it.advance();
                final ChatViewManager manager = it.value();
                final EventDispatcher eventDispatcher = this.loadChatWindow(manager, null);
                if (locked) {
                    lastDialogId = eventDispatcher.getElementMap().getId();
                }
                if (!this.m_lockChat) {
                    final UIMessage message = new UIMessage();
                    message.setId(19026);
                    message.setLongValue(5000L);
                    message.setIntValue(it.key());
                    Worker.getInstance().pushMessage(message);
                }
            }
            Xulor.getInstance().putActionClass("wakfu.chat", ChatDialogActions.class);
            MasterRootContainer.getInstance().addEventListener(Events.MOUSE_MOVED, this, false);
            MasterRootContainer.getInstance().addEventListener(Events.MOUSE_WHEELED, this, false);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            final TIntObjectIterator<ChatWindow> it = this.m_chatWindows.iterator();
            while (it.hasNext()) {
                it.advance();
                it.value().removeListeners();
            }
            MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_MOVED, this, false);
            MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_WHEELED, this, false);
            UIChatFrame.m_fadeInRequests.clear();
            UIChatFrame.m_fadeOutRequests.clear();
            this.m_chatWindows.clear();
            this.m_mouseInsideChat = null;
            this.m_compassIds.clear();
            this.m_decribedItemsMapIds.clear();
            this.m_describedItems.clear();
            MessageScheduler.getInstance().removeAllClocks(this);
            PropertiesProvider.getInstance().removeProperty("chatManager");
            PropertiesProvider.getInstance().removeProperty("chat.editedView");
            try {
                final TIntObjectIterator<ChatViewManager> it2 = ChatWindowManager.getInstance().getWindowIterator();
                while (it2.hasNext()) {
                    it2.advance();
                    final ChatViewManager manager = it2.value();
                    manager.cleanAllViews();
                    Xulor.getInstance().unload(UIChatFrameHelper.getDialogIdFromWindowId(manager.getWindowId()));
                }
            }
            catch (Exception e) {
                UIChatFrame.m_logger.error((Object)("Erreur \u00e0 la fermeture des chats " + e));
            }
            Xulor.getInstance().removeActionClass("wakfu.chat");
        }
    }
    
    public EventDispatcher loadChatWindow(final ChatViewManager chatViewManager, final String lastChatDialogid) {
        final EventDispatcher ed = UIChatFrameHelper.loadChatWindow(chatViewManager, lastChatDialogid);
        this.m_chatWindows.put(chatViewManager.getWindowId(), new ChatWindow((Window)ed, chatViewManager));
        return ed;
    }
    
    public ChatViewManager getFirstChatOnPosition(final MouseEvent e) {
        final TIntObjectIterator<ChatWindow> it = this.m_chatWindows.iterator();
        while (it.hasNext()) {
            it.advance();
            final Window w = it.value().getWindow();
            if (w.getAppearance().insideInsets(e.getX(w), e.getY(w))) {
                return ChatWindowManager.getInstance().getWindow(it.key());
            }
        }
        return null;
    }
    
    public Window getFirstChatWindowOnPosition(final MouseEvent e) {
        final TIntObjectIterator<ChatWindow> it = this.m_chatWindows.iterator();
        while (it.hasNext()) {
            it.advance();
            final Window w = it.value().getWindow();
            if (w.getAppearance().insideInsets(e.getX(w), e.getY(w))) {
                return w;
            }
        }
        return null;
    }
    
    public ChatView getFirstChatViewOnPosition(final MouseEvent e) {
        final TIntObjectIterator<ChatWindow> it = this.m_chatWindows.iterator();
        while (it.hasNext()) {
            it.advance();
            final Window w = it.value().getWindow();
            if (w.getAppearance().insideInsets(e.getX(w), e.getY(w))) {
                final List list = (List)w.getElementMap().getElement("viewsList");
                final Widget widget = list.getWidget(e.getX(list), e.getY(list));
                if (widget == null) {
                    return null;
                }
                return (ChatView)widget.getRenderableParent().getItemValue();
            }
        }
        return null;
    }
    
    @Override
    public boolean run(final Event event) {
        final MouseEvent e = (MouseEvent)event;
        switch (event.getType()) {
            case MOUSE_MOVED: {
                final TIntObjectIterator<ChatWindow> it = this.m_chatWindows.iterator();
                while (it.hasNext()) {
                    it.advance();
                    final Window window = it.value().getWindow();
                    if (window.isUnloading()) {
                        continue;
                    }
                    final ElementMap map = window.getElementMap();
                    final Widget widget = (Widget)map.getElement("chatWindow");
                    final boolean isInside = widget.getAppearance().insideInsets(e.getX(window), e.getY(window));
                    this.m_mouseInsideChat = (isInside ? window : null);
                    if (isInside) {
                        break;
                    }
                }
                break;
            }
            case MOUSE_WHEELED: {
                if (this.m_mouseInsideChat == null) {
                    break;
                }
                final ScrollContainer scrollContainer = (ScrollContainer)this.m_mouseInsideChat.getElementMap().getElement("chatScrollContainer");
                if (e.getRotations() > 0) {
                    ChatDialogActions.goDownText(e, scrollContainer, false);
                    break;
                }
                ChatDialogActions.goUpText(e, scrollContainer, false);
                break;
            }
        }
        return false;
    }
    
    public void removeChatWindow(final int windowId) {
        final ChatWindow chatWindow = this.m_chatWindows.remove(windowId);
        if (chatWindow == null) {
            return;
        }
        final Window window = chatWindow.getWindow();
        if (window == null) {
            return;
        }
        final String mapId = window.getElementMap().getId();
        final String dialogId = window.getId();
        final WidgetUserDefinedManager definedManager = (WidgetUserDefinedManager)window.getUserDefinedManager();
        Xulor.getInstance().unload(UIChatFrameHelper.getDialogIdFromWindowId(windowId));
        definedManager.removeKeys(mapId, dialogId);
    }
    
    public Window getChatWindow(final int windowId) {
        final ChatWindow chatWindow = this.m_chatWindows.get(windowId);
        return (chatWindow == null) ? null : chatWindow.getWindow();
    }
    
    public void updateChatWindowProperties(final ChatViewManager viewManagerTo) {
        PropertiesProvider.getInstance().firePropertyValueChanged(viewManagerTo, viewManagerTo.getFields());
    }
    
    public boolean displayEmoteIcon(final int id, final long sourceId) {
        return this.displayEmoteIcon(id, sourceId, -1);
    }
    
    public boolean displayEmoteIcon(final int id, final long sourceId, final int familyId) {
        if (!WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.EMOTE_ICONS_ACTIVATED)) {
            return true;
        }
        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(sourceId);
        if (character == null) {
            return EmoteIconHelper.displayEmoteIconOnMobile(id, sourceId, familyId);
        }
        final Breed characterBreed = character.getBreed();
        if (characterBreed instanceof MonsterBreed) {
            return EmoteIconHelper.displayEmoteIconOnMonster(id, sourceId, familyId, character);
        }
        return EmoteIconHelper.prepareSmileyWidget(id, character);
    }
    
    public void selectChatOptions(final boolean select) {
        if (this.m_chatWindows == null) {
            return;
        }
        final TIntObjectIterator<ChatWindow> it = this.m_chatWindows.iterator();
        while (it.hasNext()) {
            it.advance();
            final ElementMap map = it.value().getWindow().getElementMap();
            if (map == null) {
                return;
            }
            final EventDispatcher eventDispatcher = map.getElement("chatOptionsButton");
            if (eventDispatcher == null) {
                return;
            }
            ((ToggleButton)eventDispatcher).setSelected(select);
        }
    }
    
    public void highLightDownBundaryButton(final int windowId) {
        final Window chatWindow = this.getChatWindow(windowId);
        UIChatFrameHelper.highlightDownBundaryButton(chatWindow);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIChatFrame.class);
        m_instance = new UIChatFrame();
        m_fadeInRequests = new TIntObjectHashMap<FadeChatRunnable>();
        m_fadeOutRequests = new TIntObjectHashMap<FadeChatRunnable>();
    }
    
    private class FadeChatRunnable implements Runnable
    {
        private final int m_windowId;
        private final boolean m_fadeIn;
        
        private FadeChatRunnable(final int windowId, final boolean fadeIn) {
            super();
            this.m_windowId = windowId;
            this.m_fadeIn = fadeIn;
        }
        
        @Override
        public void run() {
            UIChatFrame.this.fadeChat(this.m_fadeIn, this.m_windowId);
            if (this.m_fadeIn) {
                UIChatFrame.m_fadeInRequests.put(this.m_windowId, null);
            }
            else {
                UIChatFrame.m_fadeOutRequests.put(this.m_windowId, null);
            }
        }
    }
}
