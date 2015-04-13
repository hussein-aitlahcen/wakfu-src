package com.ankamagames.baseImpl.graphics.chat;

public class ChatChannelCommandData
{
    private ChatCommandsParametersInterface m_chatCommandsParametersInterface;
    private String m_command;
    
    public ChatChannelCommandData(final ChatCommandsParametersInterface chatCommandsParametersInterface, final String command) {
        super();
        this.m_chatCommandsParametersInterface = chatCommandsParametersInterface;
        this.m_command = command;
    }
    
    public ChatCommandsParametersInterface getChatCommandsParametersInterface() {
        return this.m_chatCommandsParametersInterface;
    }
    
    public String getCommand() {
        return this.m_command;
    }
}
