package com.ankamagames.baseImpl.graphics.chat;

import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.framework.graphics.image.*;
import java.util.*;
import com.ankamagames.framework.kernel.utils.*;
import gnu.trove.*;

public abstract class AbstractChatView implements ConsoleView, ChatPipeListener, FieldProvider
{
    public static final String FIELDEDPROPERTY_NAME = "chat.dialogView";
    public static final String INPUT_FIELD = "input";
    public static final String HISTORY_FIELD = "history";
    public static final String CHANNELS_LIST_FIELD = "channelsList";
    public static final String CURRENT_CHANNEL_FIELD = "currentChannel";
    public static final String PRIVATE_NAME_FIELD = "privateName";
    public static final String CURRENT_CHANNEL_NAME_FIELD = "currentChannelName";
    public static final String IS_PAUSED_FIELD = "isPaused";
    public static final String[] FIELDS;
    private final TIntArrayList m_defaultPipes;
    private int[] m_opennedPipes;
    private String m_input;
    private String m_history;
    private static final int MAX_LINE_NUMBER = 100;
    private final TIntObjectHashMap<ArrayList<ChatPipeWrapper>> m_wrappedPipes;
    private int m_viewIndex;
    private ChatPipeWrapper m_currentChannel;
    private String m_currentPrefix;
    private boolean m_paused;
    private String m_pausedHistory;
    
    protected AbstractChatView(final int viewIndex, final int[] pipes) {
        super();
        this.m_input = "";
        this.m_history = "";
        this.m_wrappedPipes = new TIntObjectHashMap<ArrayList<ChatPipeWrapper>>();
        this.m_currentChannel = null;
        this.m_currentPrefix = "";
        this.m_pausedHistory = "";
        this.m_viewIndex = viewIndex;
        this.m_defaultPipes = new TIntArrayList(pipes);
        this.m_opennedPipes = pipes;
    }
    
    public void updateDisplayHistory() {
        final ArrayList<ChatMessage> visibleMessages = new ArrayList<ChatMessage>();
        final TIntObjectIterator it = this.m_wrappedPipes.iterator();
        while (it.hasNext()) {
            it.advance();
            final ArrayList<ChatPipeWrapper> pipes = it.value();
            for (final ChatPipeWrapper pipe : pipes) {
                if (this.isPipeOpenned(pipe.getChatPipe().getId()) && pipe.getChannelMode() == ChannelMode.INPUT) {
                    this.addMessages(visibleMessages, pipe.getChatPipe());
                }
            }
        }
        Collections.sort(visibleMessages);
        final StringBuilder history = new StringBuilder();
        for (final ChatMessage message : visibleMessages) {
            history.append(this.formatMessage(message));
        }
        this.m_history = this.checkedHistoryString(history.toString());
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, "history");
    }
    
    public TIntObjectIterator<ArrayList<ChatPipeWrapper>> getWrappedPipes() {
        return this.m_wrappedPipes.iterator();
    }
    
    public ArrayList<ChatPipeWrapper> getWrappedPipesArray() {
        final ArrayList<ChatPipeWrapper> ws = new ArrayList<ChatPipeWrapper>();
        final TIntObjectIterator<ArrayList<ChatPipeWrapper>> it = this.getWrappedPipes();
        while (it.hasNext()) {
            it.advance();
            for (final ChatPipeWrapper wrapper : it.value()) {
                ws.add(wrapper);
            }
        }
        return ws;
    }
    
    public void addMessages(final ArrayList<ChatMessage> messages, final ChatPipe pipe) {
        if (pipe == null) {
            return;
        }
        if (pipe.getMessages() != null) {
            for (final ChatMessage message : pipe.getMessages()) {
                messages.add(message);
            }
        }
        if (pipe.getSubPipes() != null) {
            for (final ChatPipe subPipe : pipe.getSubPipes().values()) {
                this.addMessages(messages, subPipe);
            }
        }
    }
    
    protected abstract String formatMessage(final ChatMessage p0);
    
    public ArrayList<ChatFilterFieldProvider> getFiltersList() {
        final ArrayList<ChatFilterFieldProvider> filters = new ArrayList<ChatFilterFieldProvider>();
        for (final int pipeId : this.m_defaultPipes.toNativeArray()) {
            final ChatPipe chatPipe = ChatManager.getInstance().getChatPipe(pipeId);
            if (chatPipe.isFilterable()) {
                final float[] color = chatPipe.getColor();
                filters.add(new ChatFilterFieldProvider(chatPipe.getName(), this.isPipeOpenned(chatPipe.getId()), new Color(color[0], color[1], color[2], 1.0f), chatPipe.getId()));
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
    
    public boolean isPipeOpenned(final int id) {
        for (int i = 0, size = this.m_opennedPipes.length; i < size; ++i) {
            if (this.m_opennedPipes[i] == id) {
                return true;
            }
        }
        return false;
    }
    
    public void activateDisactivatePipe(final int chatPipeId, final boolean activate) {
        int[] pipes;
        if (activate) {
            if (this.isPipeOpenned(chatPipeId)) {
                return;
            }
            pipes = new int[this.m_opennedPipes.length + 1];
            System.arraycopy(this.m_opennedPipes, 0, pipes, 0, this.m_opennedPipes.length);
            pipes[pipes.length - 1] = chatPipeId;
            this.registerPipe(ChatManager.getInstance().getChatPipe(chatPipeId), ChannelMode.INPUT);
        }
        else {
            if (!this.isPipeOpenned(chatPipeId)) {
                return;
            }
            pipes = new int[this.m_opennedPipes.length - 1];
            int j = 0;
            for (int i = 0; i < this.m_opennedPipes.length; ++i) {
                final int opennedPipe = this.m_opennedPipes[i];
                if (opennedPipe != chatPipeId) {
                    pipes[j] = opennedPipe;
                    ++j;
                }
            }
            this.unregisterPipeForMode(chatPipeId);
        }
        this.m_opennedPipes = pipes;
    }
    
    public ChatPipeWrapper registerPipe(final ChatPipe pipe, final ChannelMode channelMode) {
        return this.registerPipe(pipe, channelMode, null);
    }
    
    public ChatPipeWrapper registerPipe(final ChatPipe pipe, final ChannelMode channelMode, final ChatChannelCommandData params) {
        if (channelMode == ChannelMode.INPUT) {
            pipe.addListener(this);
        }
        ArrayList<ChatPipeWrapper> wps = this.m_wrappedPipes.get(pipe.getId());
        if (wps == null) {
            wps = new ArrayList<ChatPipeWrapper>();
        }
        final ChatPipeWrapper wp = new ChatPipeWrapper(pipe, pipe.getName(), channelMode, params);
        if (wps.contains(wp)) {
            return null;
        }
        wp.setFilterable(pipe.isFilterable());
        wps.add(wp);
        this.m_wrappedPipes.put(pipe.getId(), wps);
        this.fireWrappedPipeAdded(wp);
        return wp;
    }
    
    public boolean hasChannel(final int chatPipeId) {
        final ArrayList<ChatPipeWrapper> pipeWrapperArrayList = this.m_wrappedPipes.get(chatPipeId);
        if (pipeWrapperArrayList == null) {
            return false;
        }
        for (int i = pipeWrapperArrayList.size() - 1; i >= 0; --i) {
            final ChatPipeWrapper chatPipeWrapper = pipeWrapperArrayList.get(i);
            if (chatPipeWrapper.getChannelMode() != ChannelMode.INPUT) {
                return true;
            }
        }
        return false;
    }
    
    public void unregisterChannel(final int chatPipeId) {
        final ArrayList<ChatPipeWrapper> pipeWrapperArrayList = this.m_wrappedPipes.get(chatPipeId);
        if (pipeWrapperArrayList == null) {
            return;
        }
        for (int i = pipeWrapperArrayList.size() - 1; i >= 0; --i) {
            final ChatPipeWrapper chatPipeWrapper = pipeWrapperArrayList.get(i);
            if (chatPipeWrapper.getChannelMode() != ChannelMode.INPUT) {
                pipeWrapperArrayList.remove(i);
            }
        }
    }
    
    protected void unregisterPipeForMode(final int chatPipeId) {
        final ArrayList<ChatPipeWrapper> pipeWrappers = this.m_wrappedPipes.get(chatPipeId);
        if (pipeWrappers == null) {
            return;
        }
        for (final ChatPipeWrapper pipeWrapper : pipeWrappers) {
            final ChatPipe chatPipe = pipeWrapper.getChatPipe();
            chatPipe.removeListener(this);
            for (final ChatPipe chatSubPipe : chatPipe.getSubPipes().values()) {
                chatSubPipe.removeListener(this);
            }
        }
    }
    
    public List<ChatPipeWrapper> getOutputWrappedPipes() {
        final List<ChatPipeWrapper> chatPipeWrappers = new ArrayList<ChatPipeWrapper>();
        final TIntObjectIterator it = this.m_wrappedPipes.iterator();
        while (it.hasNext()) {
            it.advance();
            final ArrayList<ChatPipeWrapper> pipes = it.value();
            for (final ChatPipeWrapper pipe : pipes) {
                if (pipe.getChannelMode() != ChannelMode.INPUT) {
                    chatPipeWrappers.add(pipe);
                }
            }
        }
        return chatPipeWrappers;
    }
    
    public ChatPipeWrapper setCurrentChannel(final ChatCommandsParametersInterface chatCommandsParameters, final boolean save) {
        final TIntObjectIterator<ArrayList<ChatPipeWrapper>> it = this.m_wrappedPipes.iterator();
        while (it.hasNext()) {
            it.advance();
            for (final ChatPipeWrapper wrapper : it.value()) {
                if (chatCommandsParameters == wrapper.getCommandView() && wrapper.getChannelMode() != ChannelMode.INPUT) {
                    this.setCurrentChannel(wrapper);
                    return wrapper;
                }
            }
        }
        return null;
    }
    
    private void setCurrentChannel(final ChatPipeWrapper wp) {
        if (wp != this.m_currentChannel && (wp == null || wp.getChannelMode() != ChannelMode.INPUT)) {
            this.m_currentChannel = wp;
            GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this.m_currentChannel, ChatPipeWrapper.FIELDS);
            GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, "currentChannel", "currentChannelName");
        }
    }
    
    public ChatPipeWrapper getCurrentChannel() {
        return this.m_currentChannel;
    }
    
    private void fireWrappedPipeAdded(final ChatPipeWrapper w) {
        if (w.getChannelMode() != ChannelMode.INPUT) {
            GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, "channelsList");
        }
    }
    
    @Override
    public void onMessage(final ChatMessage message) {
        final String formattedMessage = this.formatMessage(message);
        this.appendFieldValue("history", formattedMessage);
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, "history");
    }
    
    @Override
    public void onSubPipeCreated(final ChatPipe subPipe, final ChannelMode channelMode) {
        if (!this.isPrivateView()) {
            this.registerPipe(subPipe, channelMode);
        }
    }
    
    @Override
    public String[] getFields() {
        return AbstractChatView.FIELDS;
    }
    
    public int getViewIndex() {
        return this.m_viewIndex;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("input")) {
            return this.m_input;
        }
        if (fieldName.equals("history")) {
            return this.m_history;
        }
        if (fieldName.equals("channelsList")) {
            return this.getOutputWrappedPipes();
        }
        if (fieldName.equals("privateName")) {
            return this.getPrivateName();
        }
        if (fieldName.equals("isPaused")) {
            return this.m_paused;
        }
        if (fieldName.equals("currentChannel")) {
            return this.m_currentChannel;
        }
        if (fieldName.equals("currentChannelName")) {
            final String privateName = this.getPrivateName();
            return (privateName != null) ? privateName : ((this.m_currentChannel != null) ? this.m_currentChannel.getPipeName() : null);
        }
        return null;
    }
    
    public String getPrivateName() {
        if (this.m_currentPrefix == null) {
            return null;
        }
        final int index = this.m_currentPrefix.indexOf(32) + 1;
        if (index == 0 || index > this.m_currentPrefix.length() - 1) {
            return null;
        }
        return this.m_currentPrefix.substring(index).replaceAll("\"", "");
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
        if (fieldName.equals("input")) {
            this.m_input = (String)value;
        }
        else if (fieldName.equals("history")) {
            this.m_history = this.checkedHistoryString((String)value);
        }
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
        if (fieldName.equals("input")) {
            this.m_input = (String)((this.m_input == null) ? value : (value + this.m_input));
        }
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
        if (fieldName.equals("history")) {
            if (this.m_paused) {
                if (this.m_pausedHistory.length() == 0) {
                    this.m_pausedHistory = this.checkedHistoryString(this.m_history + value);
                }
                else {
                    this.m_pausedHistory = this.checkedHistoryString(this.m_pausedHistory + value);
                }
            }
            else if (this.m_pausedHistory.length() == 0) {
                this.m_history = this.checkedHistoryString(this.m_history + value);
            }
            else {
                this.m_history = this.m_pausedHistory;
                this.m_pausedHistory = "";
            }
        }
        else if (fieldName.equals("input")) {
            this.m_input = (String)((this.m_input == null) ? value : (this.m_input + value));
        }
    }
    
    private String checkedHistoryString(final String value) {
        final String[] lines = StringUtils.split(value, '\n');
        int offset = 0;
        final int lineCount = lines.length;
        if (lineCount > 100) {
            for (int i = 0; i < lineCount - 100; ++i) {
                offset += lines[i].length() + 1;
            }
        }
        return value.substring(offset);
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return fieldName.equals("input");
    }
    
    public void clear() {
        this.m_history = "";
        this.m_pausedHistory = "";
        this.m_input = "";
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, "history", "input");
    }
    
    @Override
    public void setPrompt(final String prompt) {
    }
    
    @Override
    public void err(final String text) {
    }
    
    @Override
    public void log(final String text) {
    }
    
    @Override
    public void customStyle(final String text) {
    }
    
    @Override
    public void trace(final String text) {
    }
    
    public String getCurrentPrefix() {
        return this.m_currentPrefix;
    }
    
    public void setCurrentPrefix(final String currentPrefix) {
        this.m_currentPrefix = currentPrefix;
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, "currentChannelName", "privateName");
    }
    
    public void setViewIndex(final int viewIndex) {
        this.m_viewIndex = viewIndex;
    }
    
    public void setPaused(final boolean paused) {
        if (!(this.m_paused = paused)) {
            this.appendFieldValue("history", this.m_pausedHistory);
        }
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, "history", "isPaused");
    }
    
    public boolean isPaused() {
        return this.m_paused;
    }
    
    public void cleanPipes() {
        final TIntObjectIterator it = this.m_wrappedPipes.iterator();
        while (it.hasNext()) {
            it.advance();
            final ArrayList<ChatPipeWrapper> pipes = it.value();
            for (final ChatPipeWrapper pipe : pipes) {
                final ChatPipe chatPipe = pipe.getChatPipe();
                chatPipe.removeListener(this);
                for (final ChatPipe subPipe : chatPipe.getSubPipes().values()) {
                    subPipe.removeListener(this);
                    if (subPipe.isListenersEmpty()) {
                        chatPipe.removeSubPipe(subPipe);
                    }
                }
            }
        }
        this.m_wrappedPipes.clear();
    }
    
    public boolean isPrivateView() {
        return false;
    }
    
    public int[] getOpennedPipes() {
        return this.m_opennedPipes;
    }
    
    public void setOpennedPipes(final int[] opennedPipes) {
        this.m_opennedPipes = opennedPipes;
    }
    
    static {
        FIELDS = new String[] { "history", "input", "channelsList", "currentChannel", "privateName", "currentChannelName", "isPaused" };
    }
}
