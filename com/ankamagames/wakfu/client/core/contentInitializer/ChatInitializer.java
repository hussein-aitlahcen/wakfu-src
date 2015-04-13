package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.pipe.*;
import com.ankamagames.wakfu.client.chat.console.command.*;
import com.ankamagames.wakfu.client.chat.bubble.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.bubble.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.baseImpl.graphics.chat.*;
import java.util.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import gnu.trove.*;

public class ChatInitializer implements ContentInitializer
{
    private static ChatInitializer m_instance;
    private static final Logger m_logger;
    
    public static ChatInitializer getInstance() {
        return ChatInitializer.m_instance;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.chat");
    }
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        ChatConfigurator.loadChatCommandFile();
        final ChatPipe vicinityPipe = new ChatBubblePipe(1, "vicinityPipe", ChatConstants.CHAT_DEFAULT_COLOR_VALUE, WakfuTranslator.getInstance().getString("chat.pipeName.vicinity"), true);
        final ChatPipe privatePipe = new ChatPrivatePipe(2, "privatePipe", ChatConstants.CHAT_PRIVATE_COLOR_VALUE, WakfuTranslator.getInstance().getString("chat.pipeName.private"), true);
        final ChatPipe gameInformationPipe = new ChatSimplePipe(4, "gameInformationPipe", ChatConstants.CHAT_GAME_INFORMATION_COLOR_VALUE, WakfuTranslator.getInstance().getString("chat.pipeName.gameInformation"), true);
        final ChatPipe groupPipe = new ChatBubblePipe(5, "groupPipe", ChatConstants.CHAT_GROUP_COLOR_VALUE, WakfuTranslator.getInstance().getString("chat.pipeName.group"), true);
        final ChatPipe guildPipe = new ChatBubblePipe(6, "guildPipe", ChatConstants.CHAT_GUILD_COLOR_VALUE, WakfuTranslator.getInstance().getString("chat.pipeName.guild"), true);
        final ChatPipe tradePipe = new ChatBubblePipe(7, "tradePipe", ChatConstants.CHAT_TRADE_COLOR_VALUE, WakfuTranslator.getInstance().getString("chat.pipeName.trade"), true);
        final ChatPipe politicPipe = new ChatBubblePipe(8, "politicPipe", ChatConstants.CHAT_POLITIC_COLOR_VALUE, WakfuTranslator.getInstance().getString("chat.pipeName.politic"), true);
        final ChatPipe teamPipe = new ChatBubblePipe(9, "teamPipe", ChatConstants.CHAT_TEAM_COLOR_VALUE, WakfuTranslator.getInstance().getString("chat.pipeName.team"), true);
        final ChatPipe recrutePipe = new ChatBubblePipe(12, "lobbyPipe", ChatConstants.CHAT_RECRUTE_COLOR_VALUE, WakfuTranslator.getInstance().getString("chat.pipeName.recrute"), true);
        final ChatPipe gameErrorPipe = new ChatSimplePipe(3, "gameErrorPipe", ChatConstants.CHAT_GAME_ERROR_COLOR_VALUE, WakfuTranslator.getInstance().getString("chat.pipeName.gameError"), false);
        final ChatPipe adminPipe = new ChatBubblePipe(10, "admin_channel", ChatConstants.CHAT_ADMIN_COLOR_VALUE, WakfuTranslator.getInstance().getString("chat.pipeName.admin"), false);
        final ChatPipe allPipe = new ChatSimplePipe(11, "all_channel", ChatConstants.CHAT_ALL_COLOR_VALUE, WakfuTranslator.getInstance().getString("chat.pipeName.all"), false);
        final ChatManager chatManager = ChatManager.getInstance();
        chatManager.addChatPipe(1, vicinityPipe);
        chatManager.addChatPipe(2, privatePipe);
        chatManager.addChatPipe(3, gameErrorPipe);
        chatManager.addChatPipe(4, gameInformationPipe);
        chatManager.addChatPipe(5, groupPipe);
        chatManager.addChatPipe(6, guildPipe);
        chatManager.addChatPipe(7, tradePipe);
        chatManager.addChatPipe(8, politicPipe);
        chatManager.addChatPipe(9, teamPipe);
        chatManager.addChatPipe(10, adminPipe);
        chatManager.addChatPipe(11, allPipe);
        chatManager.addChatPipe(12, recrutePipe);
        chatManager.initialise(new VicinityContentCommand(), ChatConfigurator.getChatCommandDescriptorSetPath(), new ChatBubbleManager());
        clientInstance.fireContentInitializerDone(this);
    }
    
    public static void initializeChatFromPreferences() throws Exception {
        final String chatFile = ChatConfigurator.getChatFilePath();
        final String defaultChatFile = WakfuConfiguration.getContentPath("defaultChatFile");
        ChatInitializer.m_logger.info((Object)"Loading chat file.");
        try {
            ChatWindowManager.getInstance().clean();
            if (FileHelper.isExistingFile(ChatConfigurator.getChatFileUrl())) {
                ChatConfigurator.loadFromXMLFile(chatFile, true);
            }
            else {
                ChatConfigurator.loadFromXMLFile(defaultChatFile, false);
            }
        }
        catch (Exception e) {
            ChatInitializer.m_logger.error((Object)"Exception \u00e0 la lecture des param\u00e8tres de chat : ", (Throwable)e);
            ChatWindowManager.getInstance().cleanAndDeletePreferences();
            ChatConfigurator.loadFromXMLFile(defaultChatFile, false);
        }
        final TIntObjectIterator<ChatViewManager> it = ChatWindowManager.getInstance().getWindowIterator();
        while (it.hasNext()) {
            it.advance();
            final ChatViewManager manager = it.value();
            if (manager != null) {
                registerPipesOnWindow(manager);
            }
        }
        PropertiesProvider.getInstance().setPropertyValue("chatManager", ChatWindowManager.getInstance());
        ChatConfigurator.setChatLoaded(true);
    }
    
    public static void registerPipesOnView(final ChatView view) {
        registerFiltersOnView(view);
        final ChatManager chatManager = ChatManager.getInstance();
        registerPipeOnView(view, ChatCommandsParameters.VICINITY, 1);
        registerPipeOnView(view, ChatCommandsParameters.PRIVATE, 2);
        registerPipeOnView(view, ChatCommandsParameters.GROUP, 5);
        registerPipeOnView(view, ChatCommandsParameters.GUILD, 6);
        registerPipeOnView(view, ChatCommandsParameters.TRADE, 7);
        registerPipeOnView(view, ChatCommandsParameters.TEAM, 9);
        registerPipeOnView(view, ChatCommandsParameters.RECRUTE, 12);
        if (AdminRightHelper.checkRight(WakfuGameEntity.getInstance().getLocalAccount().getAdminRights(), AdminRightsEnum.ADMIN_CHAT)) {
            registerPipeOnView(view, ChatCommandsParameters.ADMIN, 10);
        }
        view.setCurrentChannel(view.getCurrentChannelCommand(), false);
    }
    
    private static void registerPipeOnView(final AbstractChatView view, final ChatCommandsParametersInterface commandParameter, final int pipeId) {
        final ChatChannelCommandData command = ChatConfigurator.getCommandForChannelName(commandParameter.getCommandName());
        if (command != null) {
            view.registerPipe(ChatManager.getInstance().getChatPipe(pipeId), ChannelMode.NONE, command);
        }
    }
    
    public static void registerFiltersOnView(final ChatView view) {
        final ChatManager chatManager = ChatManager.getInstance();
        for (final int opennedPipe : view.getOpennedPipes()) {
            final ChatPipe chatPipe = chatManager.getChatPipe(opennedPipe);
            if (chatPipe == null) {
                ChatInitializer.m_logger.error((Object)("on essai d'enregistrer le cannal courant inconnu au bataillon d'id=" + opennedPipe));
                return;
            }
            for (final ChatPipe subPipe : chatPipe.getSubPipes().values()) {
                view.registerPipe(subPipe, ChannelMode.INPUT, null);
            }
            view.registerPipe(chatPipe, ChannelMode.INPUT, null);
        }
    }
    
    public static void registerPipesOnWindow(final ChatViewManager window) {
        window.cleanViewPipes();
        final TIntObjectIterator<ChatView> it = window.getViewsIterator();
        while (it.hasNext()) {
            it.advance();
            final ChatView view = it.value();
            if (view.isPrivateView()) {
                registerPipeOnPrivateView(view);
            }
            else {
                registerPipesOnView(view);
            }
        }
    }
    
    public static ChatViewManager initPrivateSubPipe(final ChatViewManager chatViewManager, final String subPipeName) {
        return initPrivateSubPipe(-1, chatViewManager, subPipeName);
    }
    
    public static ChatViewManager initPrivateSubPipe(final int viewId, ChatViewManager chatViewManager, final String subPipeName) {
        if (chatViewManager == null) {
            chatViewManager = ChatWindowManager.getInstance().createWindow();
        }
        final ChatView defaultView = chatViewManager.createPrivateView(viewId, subPipeName);
        registerPipeOnPrivateView(defaultView);
        chatViewManager.setCurrentView(defaultView);
        return chatViewManager;
    }
    
    private static void registerPipeOnPrivateView(final ChatView chatView) {
        ChatPipe chatPipe = ChatManager.getInstance().getChatPipe(2);
        if (chatPipe == null) {
            chatPipe = new ChatPrivatePipe(2, "privatePipe", ChatConstants.CHAT_PRIVATE_COLOR_VALUE, WakfuTranslator.getInstance().getString("chat.pipeName.private"), true);
            final ChatManager chatManager = ChatManager.getInstance();
            chatManager.addChatPipe(2, chatPipe);
        }
        final String subPipeName = chatView.getName();
        ChatBubblePipe bubblePipe = (ChatBubblePipe)chatPipe.getSubPipe(subPipeName);
        if (bubblePipe == null) {
            bubblePipe = new ChatBubblePipe(-1, "subPipe".concat(subPipeName), ChatConstants.CHAT_PRIVATE_COLOR_VALUE, WakfuTranslator.getInstance().getString("chat.pipeName.private"), true);
        }
        final ChatChannelCommandData commandsParameters = ChatConfigurator.getCommandForChannelName(ChatCommandsParameters.PRIVATE.getCommandName());
        chatView.registerPipe(chatPipe, ChannelMode.INPUT, commandsParameters);
        chatView.registerPipe(bubblePipe, ChannelMode.INPUT, commandsParameters);
        chatView.registerPipe(chatPipe, ChannelMode.NONE, commandsParameters);
        chatView.registerPipe(bubblePipe, ChannelMode.NONE, commandsParameters);
        chatView.registerPipe(ChatManager.getInstance().getChatPipe(3), ChannelMode.INPUT, null);
        chatPipe.addSubPipe(subPipeName, bubblePipe);
        chatView.setCurrentChannel(commandsParameters.getChatCommandsParametersInterface(), false);
    }
    
    public static void registerChannel(final int pipe, final ChatCommandsParameters command) {
        final ChatChannelCommandData commandForChannelName = ChatConfigurator.getCommandForChannelName(command.getCommandName());
        final ChatManager chatManager = ChatManager.getInstance();
        final TIntObjectIterator<ChatViewManager> it = ChatWindowManager.getInstance().getWindowIterator();
        while (it.hasNext()) {
            it.advance();
            final ChatViewManager manager = it.value();
            if (manager != null) {
                final TIntObjectIterator<ChatView> it2 = manager.getViewsIterator();
                while (it2.hasNext()) {
                    it2.advance();
                    final ChatView chatView = it2.value();
                    chatView.registerPipe(chatManager.getChatPipe(pipe), ChannelMode.NONE, commandForChannelName);
                }
            }
        }
    }
    
    public static void unregisterChannel(final int pipe) {
        final TIntObjectIterator<ChatViewManager> it = ChatWindowManager.getInstance().getWindowIterator();
        while (it.hasNext()) {
            it.advance();
            final ChatViewManager manager = it.value();
            if (manager != null) {
                final TIntObjectIterator<ChatView> it2 = manager.getViewsIterator();
                while (it2.hasNext()) {
                    it2.advance();
                    final ChatView chatView = it2.value();
                    chatView.unregisterChannel(pipe);
                    PropertiesProvider.getInstance().firePropertyValueChanged(chatView, "channelsList");
                }
            }
        }
    }
    
    public void refreshPoliticPipes() {
        final ChatManager chatManager = ChatManager.getInstance();
        final TIntObjectIterator<ChatViewManager> it = ChatWindowManager.getInstance().getWindowIterator();
        while (it.hasNext()) {
            it.advance();
            final ChatViewManager window = it.value();
            if (window != null) {
                final TIntObjectIterator<ChatView> it2 = window.getViewsIterator();
                while (it2.hasNext()) {
                    it2.advance();
                    final ChatView view = it2.value();
                    if (!view.isPrivateView()) {
                        final NationRank rank = WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getRank();
                        if (rank != null) {
                            if (!view.hasChannel(8)) {
                                view.registerPipe(chatManager.getChatPipe(8), ChannelMode.NONE, ChatConfigurator.getCommandForChannelName(ChatCommandsParameters.FACTION.getCommandName()));
                                view.registerPipe(chatManager.getChatPipe(8), ChannelMode.NONE, ChatConfigurator.getCommandForChannelName(ChatCommandsParameters.PUBLIC_FACTION.getCommandName()));
                                view.setCurrentChannel(view.getCurrentChannelCommand(), false);
                            }
                        }
                        else {
                            view.unregisterChannel(8);
                            if (view.getCurrentChannelCommand().isPolitic()) {
                                view.setCurrentChannel(ChatCommandsParameters.VICINITY, true);
                            }
                        }
                        PropertiesProvider.getInstance().firePropertyValueChanged(view, "channelsList");
                    }
                }
            }
        }
    }
    
    static {
        ChatInitializer.m_instance = new ChatInitializer();
        m_logger = Logger.getLogger((Class)ChatInitializer.class);
    }
}
