package com.ankamagames.wakfu.client.chat;

import org.apache.log4j.*;
import java.net.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.kernel.core.translator.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.baseImpl.graphics.chat.*;
import java.util.regex.*;
import com.ankamagames.wakfu.client.core.contentInitializer.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.fileFormat.xml.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.io.*;
import gnu.trove.*;

public class ChatConfigurator
{
    private static final Logger m_logger;
    protected static final String ROOT_TAG = "chat";
    protected static final String SELECTED_CHAT_TAG = "selectedChat";
    protected static final String WINDOW_TAG = "window";
    protected static final String COMMAND_TAG = "command";
    protected static final String VIEW_TAG = "view";
    protected static final String SELECTED_VIEW_TAG = "selectedView";
    protected static final String NAME_TAG = "name";
    protected static final String IS_DEFAULT_TAG = "isDefault";
    protected static final String ID_TAG = "id";
    protected static final String CHANNEL_NAME_TAG = "channelName";
    protected static final String OPENNED_PIPES_TAG = "opennedPipes";
    protected static final String VALUE_TAG = "value";
    protected static final String IS_PRIVATE_TAG = "isPrivate";
    protected static final String IS_NAME_DIRTY_TAG = "isNameDirty";
    protected static final String COMMAND_PATTERN_TAG = "cmdPattern";
    protected static final String PIPE_COLOR_SEPARATOR = ",";
    protected static final String PIPE_ID_AND_COLOR_SEPARATOR = ":";
    public static final String CHAT_FILENAME = "chat.xml";
    private static boolean m_chatLoaded;
    private static final ArrayList<ChatMessage> m_pendingChatMessages;
    private static final HashMap<String, ChatChannelCommandData> m_commandsByChannelName;
    private static final Pattern m_chatCommandPattern;
    
    public static boolean isChatLoaded() {
        return ChatConfigurator.m_chatLoaded;
    }
    
    public static URL getChatCommandDescriptorSetPath() {
        final Language language = WakfuTranslator.getInstance().getLanguage();
        final String path = String.format("/com/ankamagames/wakfu/client/chat/console/ChatCommandDescriptorSet_%s.xml".toString(), language.getCode());
        final URL chatCommandDescriptorSetPath = WakfuClientConstants.class.getResource(path);
        if (chatCommandDescriptorSetPath == null) {
            ChatConfigurator.m_logger.error((Object)("Impossible de r\u00e9cup\u00e9rer le descripteur de commande de chat pour la langue de code " + language.getCode() + " au path =" + path + " on prend le fichier par d\u00e9faut"));
            return WakfuClientConstants.class.getResource("/com/ankamagames/wakfu/client/chat/console/ChatCommandDescriptorSet.xml".toString());
        }
        return chatCommandDescriptorSetPath;
    }
    
    public static void addPendingChatMessage(final ChatMessage chatMessage) {
        if (!ChatConfigurator.m_pendingChatMessages.contains(chatMessage)) {
            ChatConfigurator.m_pendingChatMessages.add(chatMessage);
        }
    }
    
    public static void setChatLoaded(final boolean chatLoaded) {
        ChatConfigurator.m_chatLoaded = chatLoaded;
        if (ChatConfigurator.m_chatLoaded && ChatConfigurator.m_pendingChatMessages.size() > 0) {
            for (final ChatMessage chatMessage : ChatConfigurator.m_pendingChatMessages) {
                ChatManager.getInstance().pushMessage(chatMessage);
            }
            ChatConfigurator.m_pendingChatMessages.clear();
        }
    }
    
    public static void loadFromXMLFile(final String XMLFileName, final boolean userFile) throws Exception {
        final DocumentAccessor accessor = XMLDocumentAccessor.getInstance();
        final DocumentContainer document = accessor.getNewDocumentContainer();
        accessor.open(XMLFileName);
        accessor.read(document, new DocumentEntryParser[0]);
        accessor.close();
        if (userFile) {
            loadChatFromUserFile(document);
        }
        else {
            loadChatFromDefaultFile(document);
        }
    }
    
    public static void loadChatFromDefaultFile(final DocumentContainer modelsDocument) {
        final ArrayList<DocumentEntry> windowEntries = modelsDocument.getEntriesByName("window");
        boolean firstWindow = true;
        final ChatWindowManager windowManager = ChatWindowManager.getInstance();
        for (final DocumentEntry windowDoc : windowEntries) {
            if (windowDoc.getParameterByName("id") == null) {
                ChatConfigurator.m_logger.error((Object)"Id null sur au chargement d'une fen\u00eatre de chat");
            }
            else {
                final int id = windowDoc.getParameterByName("id").getIntValue();
                final int currentView = windowDoc.getParameterByName("selectedView").getIntValue();
                ChatViewManager viewManager = windowManager.getWindow(id);
                if (viewManager == null) {
                    viewManager = windowManager.createWindow(id);
                }
                viewManager.setCurrentView(currentView);
                final ArrayList<DocumentEntry> views = windowDoc.getChildrenByName("view");
                if (views == null) {
                    continue;
                }
                for (final DocumentEntry d : views) {
                    if (d.getParameterByName("id") == null) {
                        continue;
                    }
                    final int viewId = d.getParameterByName("id").getIntValue();
                    final String name = d.getParameterByName("name").getStringValue();
                    final String opennedPipes = d.getParameterByName("opennedPipes").getStringValue();
                    final String channelName = d.getParameterByName("channelName").getStringValue();
                    final ChatCommandsParameters commandsParameters = ChatCommandsParameters.getChatCommandsParameterByCommandName(channelName);
                    if (commandsParameters == null) {
                        ChatConfigurator.m_logger.error((Object)("Commande inconnue enregistr\u00e9e sur la vue " + name));
                    }
                    else {
                        final ChatView chatView = new ChatView(viewId, name, commandsParameters, ChatConstants.DEFAULT_OPENNED_PIPES, true);
                        chatView.setOpennedPipes(getFilters(opennedPipes));
                        viewManager.addView(chatView);
                    }
                }
                if (firstWindow) {
                    windowManager.setCurrentWindow(viewManager);
                }
                firstWindow = false;
            }
        }
    }
    
    private static void initializePipesColors(final String pipesColorsString) {
        final String[] arr$;
        final String[] pipesColors = arr$ = pipesColorsString.split(",");
        for (final String pipeColor : arr$) {
            final String[] idColor = pipeColor.split(":");
            final int pipeId = Integer.parseInt(idColor[0]);
            final Color color = new Color(Integer.parseInt(idColor[1]));
            ChatManager.getInstance().getChatPipe(pipeId).setColor(color.getRed(), color.getGreen(), color.getBlue());
        }
    }
    
    public static ChatChannelCommandData getCommandForChannelName(final String channelName) {
        return ChatConfigurator.m_commandsByChannelName.get(channelName);
    }
    
    public static void loadChatCommandFile() throws Exception {
        final DocumentAccessor accessor = XMLDocumentAccessor.getInstance();
        final DocumentContainer document = accessor.getNewDocumentContainer();
        accessor.open(getChatCommandDescriptorSetPath().toString());
        accessor.read(document, new DocumentEntryParser[0]);
        accessor.close();
        final ArrayList<DocumentEntry> commandEntries = document.getEntriesByName("command");
        for (final DocumentEntry commandEntry : commandEntries) {
            final DocumentEntry channelParameter = commandEntry.getParameterByName("channelName");
            if (channelParameter == null) {
                continue;
            }
            final DocumentEntry commandParameter = commandEntry.getParameterByName("cmdPattern");
            if (commandParameter == null) {
                continue;
            }
            final String channelName = channelParameter.getStringValue();
            final String command = commandParameter.getStringValue();
            final Matcher matcher = ChatConfigurator.m_chatCommandPattern.matcher(command);
            if (!matcher.find()) {
                continue;
            }
            final String commandString = matcher.group(1);
            final ChatCommandsParameters chatCommandsParameterByCommandName = ChatCommandsParameters.getChatCommandsParameterByCommandName(channelName);
            if (chatCommandsParameterByCommandName == null) {
                continue;
            }
            ChatConfigurator.m_commandsByChannelName.put(channelName, new ChatChannelCommandData(chatCommandsParameterByCommandName, commandString));
        }
    }
    
    public static void loadChatFromUserFile(final DocumentContainer modelsDocument) throws Exception {
        final ArrayList<DocumentEntry> windowEntries = modelsDocument.getEntriesByName("window");
        boolean defaultView = false;
        for (final DocumentEntry windowDoc : windowEntries) {
            if (windowDoc.getParameterByName("id") == null) {
                ChatConfigurator.m_logger.error((Object)"Id null sur au chargement d'une fen\u00eatre de chat");
            }
            else {
                final int id = windowDoc.getParameterByName("id").getIntValue();
                final int currentView = windowDoc.getParameterByName("selectedView").getIntValue();
                ChatViewManager viewManager = ChatWindowManager.getInstance().getWindow(id);
                if (viewManager == null) {
                    viewManager = ChatWindowManager.getInstance().createWindow(id);
                }
                final ArrayList<DocumentEntry> views = windowDoc.getChildrenByName("view");
                if (views == null) {
                    continue;
                }
                for (final DocumentEntry d : views) {
                    if (d.getParameterByName("id") == null) {
                        continue;
                    }
                    final int viewId = d.getParameterByName("id").getIntValue();
                    final boolean isDefault = d.getParameterByName("isDefault").getBooleanValue();
                    final boolean nameDirty = d.getParameterByName("isNameDirty").getBooleanValue();
                    defaultView |= isDefault;
                    String name;
                    if (!nameDirty && !isDefault) {
                        name = d.getParameterByName("name").getStringValue();
                        final Pattern pattern = Pattern.compile("[0-9]+");
                        final Matcher matcher = pattern.matcher(name);
                        if (matcher.find()) {
                            final String s = matcher.group();
                            final Integer num = Integer.valueOf(s);
                            name = WakfuTranslator.getInstance().getString("chat.pipeName.personnal", num);
                        }
                    }
                    else {
                        name = d.getParameterByName("name").getStringValue();
                    }
                    final String opennedPipes = d.getParameterByName("opennedPipes").getStringValue();
                    final String channelName = d.getParameterByName("channelName").getStringValue();
                    final ChatCommandsParameters commandsParameters = ChatCommandsParameters.getChatCommandsParameterByCommandName(channelName);
                    if (commandsParameters == null) {
                        ChatConfigurator.m_logger.error((Object)("Commande inconnue enregistr\u00e9e sur la vue " + name));
                    }
                    else {
                        final boolean isPrivate = d.getParameterByName("isPrivate") != null && d.getParameterByName("isPrivate").getBooleanValue();
                        final int[] filters = getFilters(opennedPipes);
                        ChatView chatView = viewManager.getView(viewId);
                        if (chatView == null) {
                            if (isPrivate) {
                                ChatInitializer.initPrivateSubPipe(viewId, viewManager, name);
                                continue;
                            }
                            chatView = new ChatView(viewId, name, commandsParameters, ChatConstants.DEFAULT_OPENNED_PIPES, isDefault);
                            chatView.setOpennedPipes(filters);
                            chatView.setNameDirty(nameDirty);
                        }
                        else {
                            chatView.setName(name);
                            chatView.setNameDirty(nameDirty);
                            chatView.setCurrentChannelCommand(commandsParameters);
                            chatView.setOpennedPipes(filters);
                            chatView.setDefaultChatView(isDefault);
                        }
                        viewManager.addView(chatView);
                    }
                }
                final ChatView chatView2 = viewManager.setCurrentView(currentView);
                if (chatView2 == null) {
                    throw new Exception("Impossible de retrouver la vue de la fen\u00eatre de chat d'id=" + viewManager.getWindowId() + " id de la vue enregistr\u00e9e=" + currentView + " reset config...");
                }
                continue;
            }
        }
        if (!defaultView) {
            throw new Exception("Acucune vue par d\u00e9faut dans la configuration du chat ! On reset la config en attendant de savoir ce qui s'est pass\u00e9");
        }
        final ArrayList<DocumentEntry> currentWindowEntries = modelsDocument.getEntriesByName("selectedChat");
        if (currentWindowEntries != null) {
            if (currentWindowEntries.size() > 1) {
                ChatConfigurator.m_logger.warn((Object)"La sauvegarde poss\u00e8de plusieurs r\u00e9f\u00e9rences \u00e0 un chat par d\u00e9faut, \u00e9trange...");
            }
            final DocumentEntry documentEntry = currentWindowEntries.get(0);
            final int currentWindowId = documentEntry.getParameterByName("value").getIntValue();
            ChatWindowManager.getInstance().setCurrentWindow(currentWindowId);
        }
    }
    
    private static int[] getFilters(final String opennedPipes) {
        final String[] filtersStrings = StringUtils.split(opennedPipes, ',');
        final int[] filters = new int[filtersStrings.length];
        for (int i = 0; i < filters.length; ++i) {
            try {
                filters[i] = Integer.parseInt(filtersStrings[i]);
            }
            catch (ClassCastException e) {
                ChatConfigurator.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
        return filters;
    }
    
    public static void save() throws Exception {
        ChatConfigurator.m_logger.error((Object)"save");
        final XMLDocumentAccessor accessor = XMLDocumentAccessor.getInstance();
        final XMLDocumentContainer document = accessor.getNewDocumentContainer();
        final String filePath = getChatFileUrl();
        accessor.create(filePath);
        document.setRootNode(new XMLDocumentNode("chat", null));
        accessor.writeWithHeader(document, "");
        accessor.close();
        accessor.open(filePath);
        accessor.read(document, new DocumentEntryParser[0]);
        accessor.close();
        final ChatWindowManager chatWindowManager = ChatWindowManager.getInstance();
        final TIntObjectIterator<ChatViewManager> it = chatWindowManager.getWindowIterator();
        while (it.hasNext()) {
            it.advance();
            final ChatViewManager window = it.value();
            final int windowId = window.getWindowId();
            if (chatWindowManager.isExchangeChatWindow(windowId)) {
                continue;
            }
            final XMLDocumentNode windowDoc = new XMLDocumentNode("window", null);
            windowDoc.addParameter(new XMLNodeAttribute("id", String.valueOf(windowId)));
            windowDoc.addParameter(new XMLNodeAttribute("selectedView", String.valueOf(window.getCurrentViewIndex())));
            int count = 0;
            final TIntObjectIterator<ChatView> it2 = window.getViewsIterator();
            while (it2.hasNext()) {
                it2.advance();
                final ChatView chatView = it2.value();
                try {
                    addViewToWindowDoc(chatView, windowDoc);
                }
                catch (Exception e) {
                    ChatConfigurator.m_logger.error((Object)"Impossible de sauvegarder une vue");
                    ChatConfigurator.m_logger.error((Object)"Exception", (Throwable)e);
                }
                finally {
                    ++count;
                }
            }
            if (count == 0) {
                continue;
            }
            document.getRootNode().addChild(windowDoc);
        }
        final ChatViewManager currentWindow = ChatWindowManager.getInstance().getCurrentWindow();
        if (currentWindow == null) {
            ChatConfigurator.m_logger.error((Object)"Impossible de r\u00e9cup\u00e9rer le chat par d\u00e9faut, il y a un probl\u00e8me.");
        }
        else {
            final XMLDocumentNode selectedWindowDoc = new XMLDocumentNode("selectedChat", null);
            selectedWindowDoc.addParameter(new XMLNodeAttribute("value", String.valueOf(currentWindow.getWindowId())));
            document.getRootNode().addChild(selectedWindowDoc);
        }
        accessor.create(filePath);
        accessor.write(document);
        accessor.close();
    }
    
    private static void addViewToWindowDoc(final ChatView chatView, final XMLDocumentNode windowDoc) throws Exception {
        final XMLDocumentNode viewEntry = new XMLDocumentNode("view", null);
        viewEntry.addParameter(new XMLNodeAttribute("id", String.valueOf(chatView.getViewIndex())));
        viewEntry.addParameter(new XMLNodeAttribute("name", chatView.getName()));
        viewEntry.addParameter(new XMLNodeAttribute("isDefault", String.valueOf(chatView.isDefaultChatView())));
        viewEntry.addParameter(new XMLNodeAttribute("isNameDirty", String.valueOf(chatView.isNameDirty())));
        viewEntry.addParameter(new XMLNodeAttribute("channelName", chatView.getCurrentChannel().getCommandView().getCommandName()));
        viewEntry.addParameter(new XMLNodeAttribute("isPrivate", String.valueOf(chatView.isPrivateView())));
        String filtersString = "";
        final int[] opennedPipes = chatView.getOpennedPipes();
        for (int i = 0; i < opennedPipes.length; ++i) {
            filtersString += opennedPipes[i];
            if (i < opennedPipes.length - 1) {
                filtersString += ",";
            }
        }
        viewEntry.addParameter(new XMLNodeAttribute("opennedPipes", filtersString));
        windowDoc.addChild(viewEntry);
    }
    
    public static final String getChatFilePath() {
        return "file:" + WakfuClientConfigurationManager.getInstance().getCharacterDirectory() + '/' + "chat.xml";
    }
    
    public static final String getChatFileUrl() throws Exception {
        return ContentFileHelper.getURL(getChatFilePath()).getFile();
    }
    
    private static final String getDefaultChatFileUrl() throws Exception {
        return ContentFileHelper.getURL(WakfuConfiguration.getContentPath("defaultChatFile")).getFile();
    }
    
    public static void deleteUserFile() throws Exception {
        FileHelper.deleteFile(getChatFileUrl());
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChatConfigurator.class);
        ChatConfigurator.m_chatLoaded = false;
        m_pendingChatMessages = new ArrayList<ChatMessage>();
        m_commandsByChannelName = new HashMap<String, ChatChannelCommandData>();
        m_chatCommandPattern = Pattern.compile(".*\\Q(?:\\E(/[^|^)]+)[|)].*");
    }
}
