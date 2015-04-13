package com.ankamagames.wakfu.client.chat;

import java.util.regex.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;

public class ChatHelper
{
    private static final Pattern SPLIT_PATTERN;
    
    public static void sendPrivateMessage(final String textMessage, final String sourceName, final long sourceId, final String destName) {
        final String validSentence = WakfuWordsModerator.makeValidSentence(textMessage);
        if (validSentence.length() == 0) {
            pushErrorMessage("error.chat.operationNotPermited", new Object[0]);
            return;
        }
        final UserPrivateContentMessage privateMessage = new UserPrivateContentMessage();
        privateMessage.setUserName(destName);
        privateMessage.setMessageContent(validSentence);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(privateMessage);
    }
    
    public static String checkForCommandTyped(final String commandLine, final String commandPattern) {
        if (commandLine.length() <= 0 || commandLine.charAt(0) != '/') {
            return commandLine;
        }
        if (commandLine.startsWith(commandPattern)) {
            return commandLine.substring(commandPattern.length());
        }
        final String[] command = ChatHelper.SPLIT_PATTERN.split(commandLine, 2);
        pushErrorMessage("error.chat.commandNotFound", command[0]);
        return commandLine;
    }
    
    public static boolean controlAction(final Action action) {
        switch (SmsAndFloodController.getInstance().pushAction(action)) {
            case BLOCK_FLOOD: {
                pushErrorMessage("error.chat.flood", new Object[0]);
                return false;
            }
        }
        return true;
    }
    
    public static String controlSmsAndFlood(final String commandLine) {
        String result = commandLine;
        switch (SmsAndFloodController.getInstance().pushMessage(commandLine)) {
            case BLOCK_FLOOD: {
                pushErrorMessage("error.chat.flood", new Object[0]);
                return null;
            }
            case BLOCK_SMS_LIGHT: {
                pushErrorMessage("error.chat.sms.light", new Object[0]);
                final StringBuilder sb = new StringBuilder();
                sb.append("*bwork*");
                int indexOfSpace = commandLine.indexOf(32);
                int lastIndexOfSpace = 0;
                do {
                    sb.append(' ');
                    final int wordLength = indexOfSpace - lastIndexOfSpace;
                    WordModeratorHelper.appendObscenity(sb, wordLength);
                    lastIndexOfSpace = indexOfSpace + 1;
                    indexOfSpace = commandLine.indexOf(32, lastIndexOfSpace);
                } while (indexOfSpace != -1);
                WordModeratorHelper.appendObscenity(sb, commandLine.length() - lastIndexOfSpace);
                result = sb.toString();
                break;
            }
            case BLOCK_SMS_HARDCORE: {
                pushErrorMessage("error.chat.sms", new Object[0]);
                return null;
            }
        }
        return result;
    }
    
    public static void pushErrorMessage(final String errorTranslatorKey, final Object... args) {
        final String errorMessage = WakfuTranslator.getInstance().getString(errorTranslatorKey, args);
        final ChatMessage message = new ChatMessage(errorMessage);
        message.setPipeDestination(3);
        ChatManager.getInstance().pushMessage(message);
    }
    
    public static void pushInformationMessage(final String key, final Object... args) {
        final String errorMessage = WakfuTranslator.getInstance().getString(key, args);
        final ChatMessage message = new ChatMessage(errorMessage);
        message.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(message);
    }
    
    static {
        SPLIT_PATTERN = Pattern.compile(" ");
    }
}
