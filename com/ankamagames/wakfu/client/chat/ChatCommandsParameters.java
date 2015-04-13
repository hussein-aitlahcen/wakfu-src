package com.ankamagames.wakfu.client.chat;

import com.ankamagames.baseImpl.graphics.chat.*;
import com.ankamagames.wakfu.client.core.*;

public enum ChatCommandsParameters implements ChatCommandsParametersInterface
{
    VICINITY("local"), 
    PRIVATE("whisp"), 
    GROUP("party"), 
    GUILD("guild"), 
    TRADE("trade"), 
    TEAM("team"), 
    RECRUTE("recrute"), 
    FACTION("politic"), 
    PUBLIC_FACTION("public politic", "(" + WakfuTranslator.getInstance().getString("publicChannel") + ")"), 
    ADMIN("admin");
    
    private final String m_addedText;
    private final String m_commandName;
    
    private ChatCommandsParameters(final String commandName) {
        this(commandName, null);
    }
    
    private ChatCommandsParameters(final String commandName, final String addedText) {
        this.m_commandName = commandName;
        this.m_addedText = addedText;
    }
    
    @Override
    public String getAddedText() {
        return this.m_addedText;
    }
    
    @Override
    public String getCommandName() {
        return this.m_commandName;
    }
    
    @Override
    public boolean isPolitic() {
        return this == ChatCommandsParameters.FACTION || this == ChatCommandsParameters.PUBLIC_FACTION;
    }
    
    public static ChatCommandsParameters getChatCommandsParameterByCommandName(final String commandName) {
        for (final ChatCommandsParameters chatCommandsParameters : values()) {
            if (chatCommandsParameters.getCommandName().equals(commandName)) {
                return chatCommandsParameters;
            }
        }
        return null;
    }
}
