package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.emote.*;

public class EmoteCommand implements Command
{
    protected static final Logger m_logger;
    private final int m_emoteId;
    
    public EmoteCommand(final int emoteId) {
        super();
        this.m_emoteId = emoteId;
    }
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final boolean ok = WakfuGameEntity.getInstance().getLocalPlayer().getEmoteHandler().useEmote(this.m_emoteId, EmoteRunnableHandler.EMOTE_MESSAGE_RUNNABLE_HANDLER);
        if (!ok) {
            EmoteCommand.m_logger.error((Object)("Impossible d'utiliser l'Emote " + this.m_emoteId));
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)EmoteCommand.class);
    }
}
