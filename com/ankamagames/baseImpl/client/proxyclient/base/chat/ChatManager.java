package com.ankamagames.baseImpl.client.proxyclient.base.chat;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.bubble.*;
import java.util.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import java.net.*;
import java.util.regex.*;
import gnu.trove.*;

public class ChatManager
{
    private static final Pattern CLEAN_MSG_PATTERN;
    private static final Logger m_logger;
    private static final Logger m_chatLogger;
    private static final ChatManager m_instance;
    private ConsoleManager m_console;
    private AbstractChatBubbleManager m_chatBubbleManager;
    private TIntObjectHashMap<ChatPipe> m_chatPipes;
    private final boolean m_revertPrivateContactDirectionForward = true;
    private final boolean m_privateContactListDirty = false;
    private final LinkedList<String> m_lastPrivateContacts;
    private final LinkedList<String> m_staticLastPrivateContacts;
    
    public ChatManager() {
        super();
        this.m_lastPrivateContacts = new LinkedList<String>();
        this.m_staticLastPrivateContacts = new LinkedList<String>();
    }
    
    public static ChatManager getInstance() {
        return ChatManager.m_instance;
    }
    
    public void initialise(final Command garbageCommand, final URL chatCommandPath, final AbstractChatBubbleManager abstractChatBubbleManager) {
        (this.m_console = new ConsoleManager()).setUsePath(false);
        this.m_console.setUseMultiCommands(false);
        this.m_console.setGarbageCommand(garbageCommand);
        if (chatCommandPath != null) {
            this.m_console.addCommandListFromXmlFile(chatCommandPath);
        }
        else {
            ChatManager.m_logger.error((Object)"Impossible de charger les commandes de chat !");
        }
        this.m_chatBubbleManager = abstractChatBubbleManager;
    }
    
    public ConsoleManager getConsole() {
        return this.m_console;
    }
    
    public void addChatPipe(final int key, final ChatPipe pipe) {
        if (this.m_chatPipes == null) {
            this.m_chatPipes = new TIntObjectHashMap<ChatPipe>();
        }
        this.m_chatPipes.put(key, pipe);
    }
    
    public void removeChatPipe(final int key) {
        if (this.m_chatPipes != null) {
            this.m_chatPipes.remove(key);
        }
    }
    
    public ChatPipe getChatPipe(final int key) {
        return this.m_chatPipes.get(key);
    }
    
    public int getChatPipeIndex(final String internalName) {
        final TIntObjectIterator<ChatPipe> it = this.m_chatPipes.iterator();
        while (it.hasNext()) {
            it.advance();
            if (it.value().getInternalName().equals(internalName)) {
                return it.key();
            }
        }
        return -1;
    }
    
    public ChatPipe getChatPipe(final String name) {
        final TIntObjectIterator<ChatPipe> it = this.m_chatPipes.iterator();
        while (it.hasNext()) {
            it.advance();
            if (it.value().getName().equals(name)) {
                return it.value();
            }
        }
        return null;
    }
    
    public void pushMessage(final String message, final int canal) {
        final ChatMessage chatMsg = new ChatMessage(message);
        chatMsg.setPipeDestination(canal);
        this.logMessage(chatMsg);
        final ChatPipe pipe = this.m_chatPipes.get(chatMsg.getPipeDestination());
        pipe.pushMessage(chatMsg);
    }
    
    public void pushMessage(final ChatMessage message, final String subPipeKey) {
        this.logMessage(message);
        final ChatPipe pipe = this.m_chatPipes.get(message.getPipeDestination());
        pipe.pushMessage(message, subPipeKey);
    }
    
    public void pushMessage(final ChatMessage message) {
        this.logMessage(message);
        final ChatPipe pipe = this.m_chatPipes.get(message.getPipeDestination());
        pipe.pushMessage(message);
    }
    
    private void logMessage(final ChatMessage message) {
        final String pipeName = this.m_chatPipes.get(message.getPipeDestination()).getName();
        final String sourceName = message.getSourceName();
        final String text = message.getMessage();
        ChatManager.m_chatLogger.info((Object)("[" + pipeName + "] " + ((sourceName == null) ? "" : (sourceName + " : ")) + cleanMessageText(text)));
    }
    
    private static String cleanMessageText(final String text) {
        final StringBuilder sb = new StringBuilder();
        final String trimmedSentence = text.trim();
        final Matcher matcher = ChatManager.CLEAN_MSG_PATTERN.matcher(trimmedSentence);
        int currentIndex = 0;
        while (matcher.find()) {
            sb.append(trimmedSentence, currentIndex, matcher.start());
            currentIndex = matcher.end();
        }
        sb.append(trimmedSentence, currentIndex, trimmedSentence.length());
        return sb.toString();
    }
    
    public String getLastPrivateContact(final boolean forward) {
        if (this.m_lastPrivateContacts.size() == 0) {
            return null;
        }
        String contact;
        if (forward) {
            contact = this.m_lastPrivateContacts.removeFirst();
            this.m_lastPrivateContacts.addLast(contact);
        }
        else {
            contact = this.m_lastPrivateContacts.removeLast();
            this.m_lastPrivateContacts.addFirst(contact);
        }
        return contact;
    }
    
    public void addLastPrivateContact(final String lastPrivateContact) {
        if (!this.m_lastPrivateContacts.contains(lastPrivateContact)) {
            this.m_lastPrivateContacts.add(lastPrivateContact);
        }
        else {
            this.m_lastPrivateContacts.remove(lastPrivateContact);
            this.m_lastPrivateContacts.addLast(lastPrivateContact);
        }
        if (!this.m_staticLastPrivateContacts.contains(lastPrivateContact)) {
            this.m_staticLastPrivateContacts.add(lastPrivateContact);
        }
        if (this.m_staticLastPrivateContacts.size() > 10 || this.m_lastPrivateContacts.size() > 10) {
            final String contactToRemove = this.m_staticLastPrivateContacts.removeFirst();
            this.m_lastPrivateContacts.remove(contactToRemove);
        }
    }
    
    public void clean() {
        this.m_lastPrivateContacts.clear();
        this.m_staticLastPrivateContacts.clear();
        final TIntObjectIterator<ChatPipe> it = this.m_chatPipes.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().clean();
        }
    }
    
    public TIntObjectIterator<ChatPipe> getPipesIterator() {
        return this.m_chatPipes.iterator();
    }
    
    public AbstractChatBubbleManager getChatBubbleManager() {
        return this.m_chatBubbleManager;
    }
    
    static {
        CLEAN_MSG_PATTERN = Pattern.compile("<[^<>]*>");
        m_logger = Logger.getLogger((Class)ChatManager.class);
        m_chatLogger = Logger.getLogger("chat");
        m_instance = new ChatManager();
    }
}
