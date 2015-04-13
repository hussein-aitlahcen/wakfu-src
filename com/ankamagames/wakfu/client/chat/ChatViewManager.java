package com.ankamagames.wakfu.client.chat;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.contentInitializer.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.baseImpl.graphics.chat.*;
import java.util.*;
import gnu.trove.*;

public class ChatViewManager implements FieldProvider
{
    private static final Logger m_logger;
    public static final int MAX_CHAT_VIEW_NUMBER = 4;
    public static final String CURRENT_VIEW = "currentView";
    public static final String CHAT_VIEWS_FIELD = "list";
    public static final String WINDOW_ID_FIELD = "windowId";
    public static final String CAN_ADD_VIEW_FIELD = "canAddView";
    public static final String[] FIELDS;
    private int m_currentViewIndex;
    private final TIntObjectHashMap<ChatView> m_views;
    private int m_windowId;
    
    public ChatViewManager(final int windowId) {
        super();
        this.m_currentViewIndex = 0;
        this.m_views = new TIntObjectHashMap<ChatView>();
        this.m_windowId = -1;
        this.m_windowId = windowId;
    }
    
    public int getWindowId() {
        return this.m_windowId;
    }
    
    public ChatView createView() {
        final int count = ChatWindowManager.getInstance().getViewsCount() + 1;
        final int localSize = this.m_views.size();
        if (localSize >= 4) {
            return null;
        }
        if (localSize == 3) {
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.chatViewMaxReached"), 3);
        }
        final int index = this.getFreeViewIndex();
        final ChatView view = new ChatView(index, WakfuTranslator.getInstance().getString("chat.pipeName.personnal", count), ChatCommandsParameters.VICINITY, ChatConstants.DEFAULT_OPENNED_PIPES, false);
        ChatInitializer.registerPipesOnView(view);
        this.m_views.put(index, view);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "list", "canAddView");
        return view;
    }
    
    public ChatView createPrivateView(final int viewId, final String viewName) {
        final int id = (viewId == -1) ? this.getFreeViewIndex() : viewId;
        final int localSize = this.m_views.size();
        if (localSize >= 4) {
            return null;
        }
        if (localSize == 3) {
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.chatViewMaxReached"), 3);
        }
        final ChatView view = new ChatView(id, viewName, ChatCommandsParameters.PRIVATE, ChatConstants.PRIVATE_OPENNED_PIPES, false, true);
        this.m_views.put(id, view);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "list");
        return view;
    }
    
    private int getFreeViewIndex() {
        for (int i = 0; i < this.m_views.size(); ++i) {
            if (this.m_views.get(i) == null) {
                return i;
            }
        }
        return this.m_views.size();
    }
    
    public void removeView(final ChatView view, final boolean cleanPipes) {
        final int id = view.getViewIndex();
        this.removeView(id, cleanPipes);
    }
    
    public void removeView(final int index, final boolean cleanPipes) {
        if (!this.m_views.containsKey(index)) {
            return;
        }
        final ChatView chatView = this.m_views.remove(index);
        if (chatView != null) {
            if (cleanPipes) {
                chatView.cleanPipes();
            }
            this.removeViewIndex(index);
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "list", "canAddView");
        }
    }
    
    private void removeViewIndex(final int startIndex) {
        for (int i = startIndex; i < this.m_views.size(); ++i) {
            final ChatView chatView = this.m_views.remove(i + 1);
            if (chatView == null) {
                ChatViewManager.m_logger.error((Object)("Erreur de continuit\u00e9 dans les indices des vues \u00e0 l'indice : " + (i + 1)));
                return;
            }
            chatView.setViewIndex(i);
            this.m_views.put(i, chatView);
        }
    }
    
    public ChatView getView(final int index) {
        return this.m_views.get(index);
    }
    
    public TIntObjectIterator<ChatView> getViewsIterator() {
        return this.m_views.iterator();
    }
    
    public boolean containsView(final ChatView chatView) {
        return this.m_views.containsValue(chatView);
    }
    
    private ChatView getFirstView() {
        return (this.getViewNumber() > 0) ? ((ChatView)this.getViewsArray()[0]) : null;
    }
    
    public void setFirstViewCurrent() {
        this.setCurrentView(this.getFirstView());
    }
    
    public Object[] getViewsArray() {
        return this.m_views.getValues();
    }
    
    public int getViewNumber() {
        return this.getViewNumber(false);
    }
    
    public int getViewNumber(final boolean exceptDefault) {
        if (!exceptDefault) {
            return this.m_views.size();
        }
        int count = 0;
        final TIntObjectIterator<ChatView> it = this.m_views.iterator();
        while (it.hasNext()) {
            it.advance();
            if (it.value().isDefaultChatView()) {
                continue;
            }
            ++count;
        }
        return count;
    }
    
    public ChatView setCurrentView(final int index) {
        this.m_currentViewIndex = index;
        this.refreshCurrentView();
        return this.getCurrentView();
    }
    
    public ChatView getCurrentView() {
        return this.m_views.get(this.m_currentViewIndex);
    }
    
    public int getCurrentViewIndex() {
        return this.m_currentViewIndex;
    }
    
    public int getNumberOfViews() {
        return this.m_views.size();
    }
    
    public void setCurrentView(final AbstractChatView view) {
        this.setCurrentView(view.getViewIndex());
    }
    
    @Override
    public String[] getFields() {
        return ChatViewManager.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("list")) {
            final ArrayList<ChatView> views = new ArrayList<ChatView>();
            for (final Object o : this.getViewsArray()) {
                final ChatView chatView = (ChatView)o;
                views.add(chatView);
            }
            Collections.sort(views, new Comparator<ChatView>() {
                @Override
                public int compare(final ChatView o1, final ChatView o2) {
                    return o1.getViewIndex() - o2.getViewIndex();
                }
            });
            return views;
        }
        if (fieldName.equals("currentView")) {
            return this.getCurrentView();
        }
        if (fieldName.equals("windowId")) {
            return this.m_windowId;
        }
        if (fieldName.equals("canAddView")) {
            return this.m_views.size() < 4;
        }
        return null;
    }
    
    public void cleanViewsContent() {
        final TIntObjectIterator<ChatView> it = this.m_views.iterator();
        while (it.hasNext()) {
            it.advance();
            final ChatView chatView = it.value();
            chatView.clear();
            chatView.clean();
        }
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
    
    public void addView(final ChatView chatView) {
        final int localSize = this.m_views.size();
        if (localSize >= 4) {
            return;
        }
        if (this.m_views.containsKey(chatView.getViewIndex())) {
            chatView.setViewIndex(this.getFreeViewIndex());
        }
        this.m_views.put(chatView.getViewIndex(), chatView);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "list");
    }
    
    @Override
    public String toString() {
        return "wId=" + this.m_windowId + " curVId=" + this.m_currentViewIndex + " vieSize=" + this.getNumberOfViews();
    }
    
    public void cleanAllViews() {
        final TIntObjectIterator<ChatView> it = this.m_views.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().clean();
        }
    }
    
    public void cleanViewPipes() {
        final TIntObjectIterator<ChatView> it = this.getViewsIterator();
        while (it.hasNext()) {
            it.advance();
            it.value().cleanPipes();
        }
    }
    
    public void clean() {
        this.cleanViewPipes();
        this.m_views.clear();
    }
    
    public void refreshCurrentView() {
        final ChatView chatView = this.getCurrentView();
        if (chatView == null) {
            ChatViewManager.m_logger.error((Object)("On tente de rafra\u00eechir la vue courante du chat d'id=" + this.getWindowId() + " alors qu'elle n'existe pas !"));
            return;
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(chatView, ChatView.FIELDS);
        chatView.refreshCurrentChannel();
        chatView.reflowScrollNeed();
    }
    
    public void moveView(final ChatView chatView, final int viewIndex) {
        if (chatView == null) {
            return;
        }
        chatView.setViewIndex(viewIndex);
        final ChatView oldChatView = this.m_views.put(viewIndex, chatView);
        this.moveView(oldChatView, viewIndex + 1);
    }
    
    public void applyIndexDelta(final int delta) {
        for (final ChatView chatView : (ChatView[])this.m_views.getValues()) {
            chatView.setViewIndex(chatView.getViewIndex() + delta);
            this.m_views.put(chatView.getViewIndex(), chatView);
        }
    }
    
    public boolean hasFreePlace() {
        return this.m_views.size() < 4;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChatViewManager.class);
        FIELDS = new String[] { "currentView", "list", "canAddView", "windowId" };
    }
}
