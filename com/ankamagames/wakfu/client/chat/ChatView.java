package com.ankamagames.wakfu.client.chat;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.graphics.chat.*;

public class ChatView extends AbstractChatView
{
    private static final Logger m_logger;
    private String m_name;
    private boolean m_defaultChatView;
    public static final String NAME_FIELD = "name";
    public static final String WINDOW_ID_FIELD = "windowId";
    public static final String ALL_FILTERS_LIST_FIELD = "allFiltersList";
    public static final String IS_DEFAULT_VIEW_FIELD = "isDefaultView";
    public static final String IS_PRIVATE_VIEW_FIELD = "isPrivateView";
    private ChatCommandsParametersInterface m_currentChannelCommand;
    private boolean m_privateView;
    private boolean m_nameDirty;
    public static final String CHAT_CHARACTER_NAME_TYPE_TAG = "characterName";
    public static final String[] LOCAL_ALL_FIELDS;
    public static final String[] LOCAL_FIELDS;
    private ReflowScrollNeededRunnable m_reflowScrollNeededRunnable;
    
    public ChatView(final int viewId, final String name, final ChatCommandsParameters commandsParametersInterface, final int[] opennedPipes, final boolean defaultView) {
        this(viewId, name, commandsParametersInterface, opennedPipes, defaultView, false);
    }
    
    public ChatView(final int viewId, final String name, final ChatCommandsParameters command, final int[] pipes, final boolean defaultView, final boolean privateView) {
        super(viewId, pipes);
        this.m_name = name;
        this.m_currentChannelCommand = command;
        this.m_defaultChatView = defaultView;
        this.m_privateView = privateView;
        if (privateView) {
            this.setCurrentPrefix(getPrivatePrefix(name));
        }
    }
    
    @Override
    public String[] getFields() {
        return ChatView.LOCAL_ALL_FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.isDefaultChatView() ? WakfuTranslator.getInstance().getString(this.m_name) : this.m_name;
        }
        if (fieldName.equals("windowId")) {
            return this.getWindowId();
        }
        if (fieldName.equals("allFiltersList")) {
            return this.getFiltersList();
        }
        if (fieldName.equals("isDefaultView")) {
            return this.m_defaultChatView;
        }
        if (fieldName.equals("isPrivateView")) {
            return this.isPrivateView();
        }
        return super.getFieldValue(fieldName);
    }
    
    private int getWindowId() {
        return ChatWindowManager.getInstance().getWindowIdFromView(this);
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return fieldName.equals("name") || super.isFieldSynchronisable(fieldName);
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
        if (fieldName.equals("name")) {
            this.setName(value.toString());
        }
        else {
            super.setFieldValue(fieldName, value);
        }
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
        super.prependFieldValue(fieldName, value);
        this.reflowScrollNeed();
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
        super.appendFieldValue(fieldName, value);
        this.reflowScrollNeed();
    }
    
    public void reflowScrollNeed() {
        final int windowId = this.getWindowId();
        final Window chatWindow = UIChatFrame.getInstance().getChatWindow(windowId);
        if (chatWindow == null) {
            return;
        }
        final ElementMap map = chatWindow.getElementMap();
        if (map == null) {
            return;
        }
        final ScrollContainer sc = (ScrollContainer)map.getElement("chatScrollContainer");
        if (this.m_reflowScrollNeededRunnable != null) {
            ProcessScheduler.getInstance().remove(this.m_reflowScrollNeededRunnable);
            this.m_reflowScrollNeededRunnable.setSc(sc);
        }
        else {
            this.m_reflowScrollNeededRunnable = new ReflowScrollNeededRunnable(sc);
        }
        ProcessScheduler.getInstance().schedule(this.m_reflowScrollNeededRunnable, 250L, 1);
    }
    
    @Override
    public void updateDisplayHistory() {
        super.updateDisplayHistory();
        this.reflowScrollNeed();
    }
    
    private void convert(final TextWidgetFormater formattedMessage, final String sourceId, final String sourceName, final String pipeName, final String color, final String messageContent) {
        if (color != null) {
            formattedMessage.openText();
            formattedMessage.addColor(color);
        }
        if (pipeName != null) {
            formattedMessage.append("[").append(pipeName).append("] ");
        }
        if (sourceName != null) {
            createLink(formattedMessage, sourceId, sourceName);
        }
        formattedMessage.append(WakfuTranslator.getInstance().getString("colon")).append(messageContent).append("\n");
        if (color != null) {
            formattedMessage.closeText();
        }
    }
    
    public static void createLink(final TextWidgetFormater formattedMessage, final String sourceId, final String sourceName) {
        formattedMessage.b().u();
        formattedMessage.addId(sourceId);
        formattedMessage.append(sourceName);
        formattedMessage._u()._b();
    }
    
    @Override
    protected String formatMessage(final ChatMessage message) {
        final TextWidgetFormater formattedMessage = new TextWidgetFormater();
        final String messageContent = message.getMessage();
        final float[] colorArray = ChatManager.getInstance().getChatPipe(message.getPipeDestination()).getColor();
        final String messageColor = message.getColor();
        final String color = TextWidgetFormater.containsTextTag(messageContent) ? null : ((messageColor == null) ? XulorUtil.convertToTextBuilderColor(colorArray) : messageColor);
        if (WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.CHAT_TIME)) {
            final GameDateConst date = message.getTime();
            final int hours = date.getHours();
            final int minutes = date.getMinutes();
            final int seconds = date.getSeconds();
            formattedMessage.openText();
            formattedMessage.addColor("808080");
            formattedMessage.append("[" + ((hours < 10) ? "0" : "") + hours + ":" + ((minutes < 10) ? "0" : "") + minutes + ":" + ((seconds < 10) ? "0" : "") + seconds + "] ");
            formattedMessage.closeText();
        }
        switch (message.getPipeDestination()) {
            case 4: {
                if (message.getSourceName() != null) {
                    formattedMessage.b().u().append(message.getSourceName())._u()._b().append(WakfuTranslator.getInstance().getString("colon"));
                }
                if (color != null) {
                    formattedMessage.openText().addColor(color).append(messageContent).closeText();
                }
                else {
                    formattedMessage.append(messageContent);
                }
                formattedMessage.append("\n");
                break;
            }
            case 5: {
                this.convert(formattedMessage, "characterName_" + message.getSourceId(), message.getSourceName(), WakfuTranslator.getInstance().getString("chat.pipeName.group"), color, messageContent);
                break;
            }
            case 9: {
                this.convert(formattedMessage, "characterName_" + message.getSourceId(), message.getSourceName(), WakfuTranslator.getInstance().getString("chat.pipeName.team"), color, messageContent);
                break;
            }
            case 10: {
                this.convert(formattedMessage, "characterName_" + message.getSourceId(), message.getSourceName(), WakfuTranslator.getInstance().getString("chat.pipeName.admin"), color, messageContent);
                break;
            }
            case 11: {
                formattedMessage.openText();
                formattedMessage.addColor(color);
                formattedMessage.append("(").append(WakfuTranslator.getInstance().getString("chat.pipeName.all")).append(") ");
                formattedMessage.append(messageContent);
                formattedMessage.append(">\n");
                formattedMessage.closeText();
                break;
            }
            case 6: {
                this.convert(formattedMessage, "characterName_" + message.getSourceId(), message.getSourceName(), WakfuTranslator.getInstance().getString("chat.pipeName.guild"), color, messageContent);
                break;
            }
            case 7: {
                this.convert(formattedMessage, "characterName_" + message.getSourceId(), message.getSourceName(), WakfuTranslator.getInstance().getString("chat.pipeName.trade"), color, messageContent);
                break;
            }
            case 8: {
                if (message.getSourceName() != null) {
                    this.convert(formattedMessage, "characterName_" + message.getSourceId(), message.getSourceName(), WakfuTranslator.getInstance().getString("chat.pipeName.politic"), color, messageContent);
                    break;
                }
                formattedMessage.openText().addColor(color).append(messageContent).closeText();
                formattedMessage.append("\n");
                break;
            }
            case 12: {
                this.convert(formattedMessage, "characterName_" + message.getSourceId(), message.getSourceName(), WakfuTranslator.getInstance().getString("chat.pipeName.recrute"), color, messageContent);
                break;
            }
            case 2: {
                if (message.getSourceId() != WakfuGameEntity.getInstance().getLocalPlayer().getId()) {
                    this.convert(formattedMessage, "characterName_" + message.getSourceId(), message.getSourceName(), null, color, messageContent);
                    break;
                }
                formattedMessage.openText();
                formattedMessage.addColor("7ad1d6");
                formattedMessage.append(WakfuTranslator.getInstance().getString("chat.to")).append(" ");
                formattedMessage.closeText();
                this.convert(formattedMessage, "characterName_" + message.getSourceName(), message.getSourceName(), null, "7ad1d6", messageContent);
                break;
            }
            case 3: {
                if (messageContent == null) {
                    if (color != null) {
                        formattedMessage.openText().addColor(color).append("error").closeText();
                    }
                    else {
                        formattedMessage.append("error");
                    }
                    formattedMessage.append("?\n");
                    break;
                }
                if (messageContent.length() > 0) {
                    if (color != null) {
                        formattedMessage.openText().addColor(color).append(messageContent).closeText();
                    }
                    else {
                        formattedMessage.append(messageContent);
                    }
                    formattedMessage.append("\n");
                    break;
                }
                if (color != null) {
                    formattedMessage.openText().addColor(color).append("error").closeText();
                }
                else {
                    formattedMessage.append("error");
                }
                formattedMessage.append("\n");
                break;
            }
            default: {
                this.convert(formattedMessage, "characterName_" + message.getSourceId(), message.getSourceName(), null, color, messageContent);
                break;
            }
        }
        return formattedMessage.finishAndToString();
    }
    
    @Override
    public void err(final String text) {
        final String errorMessage = WakfuTranslator.getInstance().getString("error.chat.malformedCommand");
        final ChatMessage message = new ChatMessage(errorMessage);
        message.setPipeDestination(3);
        ChatManager.getInstance().pushMessage(message);
    }
    
    @Override
    public void customTrace(final String text, final int color) {
    }
    
    @Override
    public void onMessage(final ChatMessage message) {
        if (!this.isPipeOpenned(message.getPipeDestination())) {
            return;
        }
        if (!this.isWindowValid(message)) {
            return;
        }
        super.onMessage(message);
        if (!ChatWindowManager.getInstance().isACurrentOpennedPipe(message.getPipeDestination())) {
            UIChatFrameHelper.blinkViewButton(this);
        }
    }
    
    private boolean isWindowValid(final ChatMessage message) {
        return message.getWindowId() == -1 || message.getWindowId() == this.getWindowId();
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public void setNameDirty(final boolean nameDirty) {
        this.m_nameDirty = nameDirty;
    }
    
    public boolean isDefaultChatView() {
        return this.m_defaultChatView;
    }
    
    public boolean isNameDirty() {
        return this.m_nameDirty;
    }
    
    public void setDefaultChatView(final boolean defaultChatView) {
        this.m_defaultChatView = defaultChatView;
    }
    
    public void setCurrentChannelCommand(final ChatCommandsParametersInterface currentChannelCommand) {
        this.m_currentChannelCommand = currentChannelCommand;
    }
    
    public ChatCommandsParametersInterface getCurrentChannelCommand() {
        return this.m_currentChannelCommand;
    }
    
    @Override
    public ChatPipeWrapper setCurrentChannel(final ChatCommandsParametersInterface chatCommandsParameters, boolean save) {
        final ChatPipeWrapper wrapper = super.setCurrentChannel(chatCommandsParameters, save);
        if (wrapper == null) {
            return null;
        }
        if (chatCommandsParameters == this.m_currentChannelCommand) {
            save = false;
        }
        this.m_currentChannelCommand = chatCommandsParameters;
        if (wrapper.getChatPipe().getInternalName().startsWith("subPipe")) {
            this.setCurrentPrefix(getPrivatePrefix(this.getName()));
        }
        else {
            this.setCurrentPrefix(wrapper.getCommand());
        }
        if (!save) {
            return wrapper;
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentChannel");
        try {
            ChatConfigurator.save();
        }
        catch (Exception e) {
            ChatView.m_logger.error((Object)"Erreur \u00e0 la sauvegarde du chat");
            ChatView.m_logger.error((Object)"Exception", (Throwable)e);
        }
        return wrapper;
    }
    
    public static String getPrivatePrefix(final String playerName) {
        final ChatChannelCommandData commandForChannelName = ChatConfigurator.getCommandForChannelName(ChatCommandsParameters.PRIVATE.getCommandName());
        return commandForChannelName.getCommand().concat(" \"" + playerName + "\"");
    }
    
    @Override
    public boolean isPrivateView() {
        return this.m_privateView;
    }
    
    @Override
    public String toString() {
        return "ChatView{m_name='" + this.m_name + '\'' + ", viewId=" + this.getViewIndex();
    }
    
    public void refreshCurrentChannel() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this.getCurrentChannel(), ChatPipeWrapper.FIELDS);
    }
    
    public void clean() {
        if (this.m_reflowScrollNeededRunnable != null) {
            ProcessScheduler.getInstance().remove(this.m_reflowScrollNeededRunnable);
            this.m_reflowScrollNeededRunnable = null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChatView.class);
        LOCAL_FIELDS = new String[] { "name", "windowId", "allFiltersList", "isPrivateView", "isDefaultView" };
        LOCAL_ALL_FIELDS = new String[ChatView.LOCAL_FIELDS.length + AbstractChatView.FIELDS.length];
        System.arraycopy(ChatView.LOCAL_FIELDS, 0, ChatView.LOCAL_ALL_FIELDS, 0, ChatView.LOCAL_FIELDS.length);
        System.arraycopy(AbstractChatView.FIELDS, 0, ChatView.LOCAL_ALL_FIELDS, ChatView.LOCAL_FIELDS.length, AbstractChatView.FIELDS.length);
    }
    
    private final class ReflowScrollNeededRunnable implements Runnable
    {
        private ScrollContainer m_sc;
        
        private ReflowScrollNeededRunnable(final ScrollContainer sc) {
            super();
            this.m_sc = sc;
        }
        
        public void setSc(final ScrollContainer sc) {
            this.m_sc = sc;
        }
        
        @Override
        public void run() {
            final float currentOffset = PropertiesProvider.getInstance().getFloatProperty("chat.scrollOffset", this.m_sc.getElementMap());
            final boolean needVerticalScroll = this.m_sc.needVerticalScroll();
            if (needVerticalScroll && currentOffset == -1.0f) {
                PropertiesProvider.getInstance().setLocalPropertyValue("chat.scrollOffset", 0.0f, this.m_sc.getElementMap());
            }
            else if (!needVerticalScroll) {
                PropertiesProvider.getInstance().setLocalPropertyValue("chat.scrollOffset", -1.0f, this.m_sc.getElementMap());
            }
            else if (currentOffset > 0.0f) {
                UIChatFrame.getInstance().highLightDownBundaryButton(ChatWindowManager.getInstance().getWindowFromView(ChatView.this).getWindowId());
            }
        }
    }
}
