package com.ankamagames.wakfu.client.core.preferences.checker;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.*;
import java.io.*;

public class ChatResetAction implements CheckerAction
{
    private static final Logger m_logger;
    
    @Override
    public void load(final DocumentEntry node) {
    }
    
    @Override
    public CheckerAction newInstance() {
        return new ChatResetAction();
    }
    
    @Override
    public void execute(final GamePreferences preferences) {
        processCharacter(new File(WakfuClientConfigurationManager.getInstance().getCharacterDirectory()));
    }
    
    private static void processCharacter(final File characterDirectory) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(characterDirectory.getCanonicalPath());
            sb.append(File.separatorChar).append("chat.xml");
            final String chatFilePath = sb.toString();
            final File chatFile = new File(chatFilePath);
            if (chatFile.exists()) {
                final boolean success = chatFile.delete();
                if (!success) {
                    ChatResetAction.m_logger.warn((Object)("Impossible de supprimer " + chatFilePath));
                }
            }
        }
        catch (IOException e) {
            ChatResetAction.m_logger.warn((Object)"Probl\u00e8me \u00e0 la suppression d'un fichier de chat", (Throwable)e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChatResetAction.class);
    }
}
