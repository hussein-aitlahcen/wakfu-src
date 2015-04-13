package com.ankamagames.wakfu.client.core.emote;

import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.chat.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import com.ankamagames.wakfu.client.core.*;

public class Emote extends EmoteSmileyFieldProvider
{
    private final boolean m_needTarget;
    private final boolean m_infiniteDuration;
    private final boolean m_moveToTarget;
    private final boolean m_isMusical;
    private final String[] m_scriptParams;
    
    Emote(final int id, final boolean infiniteDuration, final boolean needTarget, final boolean moveToTarget, final boolean isMusical, final String commandString, final String[] scriptParams) {
        super(id, commandString);
        this.m_needTarget = needTarget;
        this.m_infiniteDuration = infiniteDuration;
        this.m_moveToTarget = (needTarget && moveToTarget);
        this.m_isMusical = isMusical;
        this.m_scriptParams = scriptParams;
    }
    
    public void addEmotePatternToCommandDescriptor() {
        final CommandDescriptorSet set = ChatManager.getInstance().getConsole().getCommandDescriptorSet();
        if (set.getChildrenNamesList().contains(this.m_commandText)) {
            return;
        }
        final CommandDescriptor commandDescriptor = new CommandDescriptor(this.m_commandText, "", new EmoteCommand(this.m_id), false);
        commandDescriptor.setName(this.m_commandText);
        ChatManager.getInstance().getConsole().getCommandDescriptorSet().getRoot().addChild(commandDescriptor);
    }
    
    public boolean isInfiniteDuration() {
        return this.m_infiniteDuration;
    }
    
    public boolean isNeedTarget() {
        return this.m_needTarget;
    }
    
    public boolean isMoveToTarget() {
        return this.m_moveToTarget;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString(80, this.m_id, new Object[0]);
    }
    
    @Override
    public String getIconUrl() {
        return WakfuConfiguration.getInstance().getEmoteUrl(this.m_id);
    }
    
    public boolean isMusical() {
        return false;
    }
}
