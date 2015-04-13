package com.ankamagames.wakfu.client.chat;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.contentInitializer.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.chat.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import gnu.trove.*;

public class ChatWindowManager implements FieldProvider
{
    private static final Logger m_logger;
    private static ChatWindowManager m_instance;
    private int m_currentWindowIndex;
    private final TIntObjectHashMap<ChatViewManager> m_windows;
    public static final String CHAT_TEXT_STYLE_FIELD = "chatTextStyle";
    public static final String ALL_FILTERS_LIST_FIELD = "allFiltersList";
    public static final String[] FIELDS;
    private int m_moderatorWindowId;
    private int m_exchangeWindowId;
    
    public ChatWindowManager() {
        super();
        this.m_currentWindowIndex = 0;
        this.m_windows = new TIntObjectHashMap<ChatViewManager>();
        this.m_moderatorWindowId = -1;
        this.m_exchangeWindowId = -1;
    }
    
    public static ChatWindowManager getInstance() {
        return ChatWindowManager.m_instance;
    }
    
    @Override
    public String[] getFields() {
        return ChatWindowManager.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("chatTextStyle")) {
            return this.getChatTextStyle();
        }
        if (fieldName.equals("allFiltersList")) {
            return this.getAllEditableFiltersList();
        }
        return null;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public String getChatTextStyle() {
        final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
        final FontSize fontSize = FontSize.values()[wakfuGamePreferences.getIntValue(WakfuKeyPreferenceStoreEnum.CHAT_TEXT_FONT_SIZE_KEY)];
        return "white" + ((fontSize != null) ? fontSize.getFontSize() : FontSize.MEDIUM.getFontSize()) + "Bordered";
    }
    
    public ChatViewManager getWindowFromView(final ChatView chatView) {
        final TIntObjectIterator<ChatViewManager> it = this.m_windows.iterator();
        while (it.hasNext()) {
            it.advance();
            final ChatViewManager viewManager = it.value();
            if (viewManager.containsView(chatView)) {
                return viewManager;
            }
        }
        return null;
    }
    
    public int getWindowIdFromView(final ChatView chatView) {
        final ChatViewManager window = this.getWindowFromView(chatView);
        return (window == null) ? -1 : window.getWindowId();
    }
    
    public boolean isACurrentView(final ChatView chatView) {
        final TIntObjectIterator<ChatViewManager> it = this.m_windows.iterator();
        while (it.hasNext()) {
            it.advance();
            final ChatViewManager viewManager = it.value();
            if (viewManager.getCurrentView() == chatView) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isACurrentOpennedPipe(final int pipeDestination) {
        final TIntObjectIterator<ChatViewManager> it = this.m_windows.iterator();
        while (it.hasNext()) {
            it.advance();
            final ChatViewManager viewManager = it.value();
            final ChatView currentView = viewManager.getCurrentView();
            if (currentView == null) {
                ChatWindowManager.m_logger.error((Object)("Vue courante de la fen\u00eatre de chat d'id=" + viewManager.getWindowId() + " inexistante !!!"));
            }
            else {
                if (currentView.isPipeOpenned(pipeDestination)) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    public ChatViewManager getFirstWindowWithFreePlace() {
        final TIntObjectIterator<ChatViewManager> it = this.m_windows.iterator();
        while (it.hasNext()) {
            it.advance();
            final ChatViewManager viewManager = it.value();
            if (viewManager.hasFreePlace()) {
                return viewManager;
            }
        }
        return null;
    }
    
    public ChatViewManager createWindow() {
        final ChatViewManager manager = new ChatViewManager(this.getFreeWindowIndex());
        this.m_windows.put(manager.getWindowId(), manager);
        return manager;
    }
    
    private int getFreeWindowIndex() {
        for (int i = 0; i < this.m_windows.size(); ++i) {
            if (this.m_windows.get(i) == null) {
                return i;
            }
        }
        return this.m_windows.size();
    }
    
    public ChatViewManager createWindow(final int index) {
        if (this.m_windows.get(index) != null) {
            ChatWindowManager.m_logger.error((Object)("Window d\u00e9j\u00e0 pr\u00e9sente \u00e0 l'index : " + index));
            return null;
        }
        final ChatViewManager manager = new ChatViewManager(index);
        this.m_windows.put(manager.getWindowId(), manager);
        return manager;
    }
    
    public void removeChatWindow(final int id, int newCurrentId) {
        if (!this.m_windows.containsKey(id)) {
            ChatWindowManager.m_logger.error((Object)("on cherche \u00e0 supprimer une fen\u00eatre de chat inconnue d'id=" + id));
            return;
        }
        if (this.m_currentWindowIndex == id) {
            if (newCurrentId == -1) {
                newCurrentId = this.getNewCurrentWindowId(id);
            }
            if (newCurrentId == -1) {
                ChatWindowManager.m_logger.error((Object)"impossible d'atteindre une fen\u00eatre de chat ! On annule la suppression dans le manager");
                return;
            }
            this.m_currentWindowIndex = newCurrentId;
        }
        final ChatViewManager viewManager = this.m_windows.remove(id);
        viewManager.cleanViewPipes();
        viewManager.cleanAllViews();
    }
    
    private int getNewCurrentWindowId(final int fromId) {
        for (final int id : this.m_windows.keys()) {
            if (fromId != id) {
                return id;
            }
        }
        return -1;
    }
    
    public ChatViewManager getCurrentWindow() {
        return this.m_windows.get(this.m_currentWindowIndex);
    }
    
    public int getWindowNumber() {
        return this.m_windows.size();
    }
    
    public ChatViewManager getWindow(final int index) {
        return this.m_windows.get(index);
    }
    
    public void setCurrentWindow(final int index) {
        this.m_currentWindowIndex = index;
    }
    
    public void setCurrentWindow(final ChatViewManager manager) {
        if (manager != null) {
            this.m_currentWindowIndex = manager.getWindowId();
        }
        else {
            ChatWindowManager.m_logger.warn((Object)"On essaye de d\u00e9finir une vue pas enregistr\u00e9e dans le ChatWindowManager comme vue courante. (Ignor\u00e9)");
        }
    }
    
    public TIntObjectIterator<ChatViewManager> getWindowIterator() {
        return this.m_windows.iterator();
    }
    
    public ChatViewManager getDefaultWindow() {
        return this.m_windows.get(0);
    }
    
    public boolean isPrivateChatExisting(final String name) {
        final TIntObjectIterator<ChatViewManager> it = this.m_windows.iterator();
        while (it.hasNext()) {
            it.advance();
            final TIntObjectIterator<ChatView> it2 = it.value().getViewsIterator();
            while (it2.hasNext()) {
                it2.advance();
                if (it2.value().getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isExchangeChatWindow(final int windowId) {
        return this.m_exchangeWindowId == windowId;
    }
    
    public boolean isModeratorChatWindow(final int windowId) {
        return this.m_moderatorWindowId == windowId;
    }
    
    public void cleanPrivateChats() {
    }
    
    public void cleanCurrentViewContent() {
        final ChatViewManager currentWindow = this.getCurrentWindow();
        if (currentWindow == null) {
            return;
        }
        final ChatView chatView = currentWindow.getCurrentView();
        if (chatView == null) {
            return;
        }
        chatView.clean();
    }
    
    public void cleanWindowContent() {
        final TIntObjectIterator<ChatViewManager> it = this.m_windows.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().cleanViewsContent();
        }
    }
    
    public ChatViewManager transferChatView(final ChatView chatView, final int windowFrom, final int windowTo) {
        final ChatViewManager viewManagerFrom = this.getWindow(windowFrom);
        ChatViewManager viewManagerTo;
        if (windowTo == -1) {
            viewManagerTo = getInstance().createWindow();
        }
        else {
            viewManagerTo = this.getWindow(windowTo);
        }
        this.deleteView(viewManagerFrom, chatView, windowTo);
        viewManagerTo.addView(chatView);
        viewManagerTo.setCurrentView(chatView);
        UIChatFrame.getInstance().updateChatWindowProperties(viewManagerFrom);
        UIChatFrame.getInstance().updateChatWindowProperties(viewManagerTo);
        ChatInitializer.registerPipesOnWindow(viewManagerTo);
        return viewManagerTo;
    }
    
    public void deleteView(final ChatViewManager chatViewManager, final ChatView chatView, final int windowTo) {
        chatViewManager.removeView(chatView, windowTo == -1);
        if (chatViewManager.getNumberOfViews() == 0) {
            final int windowFrom = chatViewManager.getWindowId();
            this.removeChatWindow(windowFrom, windowTo);
            UIChatFrame.getInstance().removeChatWindow(windowFrom);
        }
        else {
            chatViewManager.setFirstViewCurrent();
        }
    }
    
    public int getViewsCount() {
        int count = 0;
        final TIntObjectIterator<ChatViewManager> it = this.m_windows.iterator();
        while (it.hasNext()) {
            it.advance();
            final ChatViewManager viewManager = it.value();
            count += viewManager.getViewNumber(true);
        }
        return count;
    }
    
    public ArrayList<ChatView> getAllViews() {
        final ArrayList<ChatView> chatViews = new ArrayList<ChatView>();
        final TIntObjectIterator<ChatViewManager> it = this.m_windows.iterator();
        while (it.hasNext()) {
            it.advance();
            final ChatViewManager viewManager = it.value();
            final TIntObjectIterator<ChatView> it2 = viewManager.getViewsIterator();
            while (it2.hasNext()) {
                it2.advance();
                chatViews.add(it2.value());
            }
        }
        Collections.sort(chatViews, new Comparator<ChatView>() {
            @Override
            public int compare(final ChatView o1, final ChatView o2) {
                final int i = ChatWindowManager.this.getWindowIdFromView(o1) - ChatWindowManager.this.getWindowIdFromView(o2);
                return (i == 0) ? o1.getName().compareTo(o2.getName()) : i;
            }
        });
        return chatViews;
    }
    
    private ArrayList<ChatFilterFieldProvider> getAllEditableFiltersList() {
        final ArrayList<ChatFilterFieldProvider> filters = new ArrayList<ChatFilterFieldProvider>();
        final TIntObjectIterator<ChatPipe> it = ChatManager.getInstance().getPipesIterator();
        while (it.hasNext()) {
            it.advance();
            final ChatPipe chatPipe = it.value();
            if (chatPipe.isColorEditable()) {
                final float[] color = chatPipe.getColor();
                filters.add(new ChatFilterFieldProvider(chatPipe.getName(), false, new Color(color[0], color[1], color[2], 1.0f), chatPipe.getId()));
            }
        }
        Collections.sort(filters, new Comparator<ChatFilterFieldProvider>() {
            @Override
            public int compare(final ChatFilterFieldProvider o1, final ChatFilterFieldProvider o2) {
                return o1.getId() - o2.getId();
            }
        });
        return filters;
    }
    
    private ArrayList<ChatFilterFieldProvider> getChannelsList() {
        final ArrayList<ChatFilterFieldProvider> filters = new ArrayList<ChatFilterFieldProvider>();
        final TIntObjectIterator<ChatPipe> it = ChatManager.getInstance().getPipesIterator();
        while (it.hasNext()) {
            it.advance();
            final ChatPipe chatPipe = it.value();
            if (chatPipe.isFilterable()) {
                final float[] color = chatPipe.getColor();
                filters.add(new ChatFilterFieldProvider(chatPipe.getName(), false, new Color(color[0], color[1], color[2], 1.0f), chatPipe.getId()));
            }
        }
        Collections.sort(filters, new Comparator<ChatFilterFieldProvider>() {
            @Override
            public int compare(final ChatFilterFieldProvider o1, final ChatFilterFieldProvider o2) {
                return o1.getId() - o2.getId();
            }
        });
        return filters;
    }
    
    public void saveChatConfiguration() {
        try {
            ChatConfigurator.save();
        }
        catch (Exception e) {
            ChatWindowManager.m_logger.error((Object)"Erreur \u00e0 la sauvegarde du chat :");
            ChatWindowManager.m_logger.error((Object)"Exception", (Throwable)e);
        }
    }
    
    public void clean() {
        final TIntObjectIterator<ChatViewManager> it = this.m_windows.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().clean();
        }
        this.m_windows.clear();
    }
    
    public void cleanAndDeletePreferences() {
        this.clean();
        try {
            ChatConfigurator.deleteUserFile();
        }
        catch (Exception e) {
            ChatWindowManager.m_logger.error((Object)"Erreur \u00e0 la suppression du fichier utilisateur du chat");
            ChatWindowManager.m_logger.error((Object)"Exception", (Throwable)e);
        }
    }
    
    public void refreshAllViews() {
        final TIntObjectIterator<ChatViewManager> it = this.m_windows.iterator();
        while (it.hasNext()) {
            it.advance();
            final ChatViewManager window = it.value();
            PropertiesProvider.getInstance().firePropertyValueChanged(window, window.getFields());
            window.refreshCurrentView();
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "allFiltersList");
    }
    
    public int getExchangeWindowId() {
        return this.m_exchangeWindowId;
    }
    
    public void setExchangeWindowId(final int exchangeWindowId) {
        this.m_exchangeWindowId = exchangeWindowId;
    }
    
    public int getModeratorWindowId() {
        return this.m_moderatorWindowId;
    }
    
    public void setModeratorWindowId(final int moderatorWindowId) {
        this.m_moderatorWindowId = moderatorWindowId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChatWindowManager.class);
        ChatWindowManager.m_instance = new ChatWindowManager();
        FIELDS = new String[] { "chatTextStyle", "allFiltersList" };
    }
    
    public enum FontSize implements FieldProvider
    {
        LITTLE("little.f", 14), 
        MEDIUM("medium.f", 16), 
        HIGH("high.f", 18);
        
        public static final String INDEX_FIELD = "index";
        public static final String NAME_FIELD = "name";
        public static final String LIST_FIELD = "list";
        public static final String[] FIELDS;
        private final String m_translatorKey;
        private final int m_fontSize;
        
        private FontSize(final String translatorKey, final int fontSize) {
            this.m_translatorKey = translatorKey;
            this.m_fontSize = fontSize;
        }
        
        @Override
        public String[] getFields() {
            return FontSize.FIELDS;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("index")) {
                return this.ordinal();
            }
            if (fieldName.equals("name")) {
                return this.getName();
            }
            if (fieldName.equals("list")) {
                final ArrayList<String> list = new ArrayList<String>();
                for (final FontSize fontSize : values()) {
                    list.add(fontSize.getName());
                }
                return list;
            }
            return null;
        }
        
        @Override
        public void setFieldValue(final String fieldName, final Object value) {
        }
        
        @Override
        public void prependFieldValue(final String fieldName, final Object value) {
        }
        
        @Override
        public void appendFieldValue(final String fieldName, final Object value) {
        }
        
        @Override
        public boolean isFieldSynchronisable(final String fieldName) {
            return false;
        }
        
        public int getFontSize() {
            return this.m_fontSize;
        }
        
        public String getName() {
            return WakfuTranslator.getInstance().getString(this.m_translatorKey);
        }
        
        public static FontSize getFontSizeByName(final String name) {
            for (final FontSize fontSize : values()) {
                if (fontSize.getName().equals(name)) {
                    return fontSize;
                }
            }
            return null;
        }
        
        static {
            FIELDS = new String[] { "index", "name", "list" };
        }
    }
}
