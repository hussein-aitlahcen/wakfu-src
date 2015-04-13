package com.ankamagames.baseImpl.graphics.chat;

import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.framework.reflect.*;

public class ChatPipeWrapper implements FieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String PIPE_INTERNAL_NAME_FIELD = "pipeInternalName";
    public static final String DESC_SENTENCE_FIELD = "descSentence";
    public static final String DISPLAYABLE_FIELD = "displayable";
    public static final String COLOR_STRING_FIELD = "colorString";
    public static final String COMMAND_FIELD = "command";
    public static final String[] FIELDS;
    private final ChatPipe m_chatPipe;
    private final ChatCommandsParametersInterface m_commandView;
    private String m_pipeName;
    private ChannelMode m_channelMode;
    private boolean m_isFilterable;
    private String m_command;
    
    public ChatPipeWrapper(final ChatPipe pipe, final String pipeName, final ChannelMode channelMode, final ChatChannelCommandData chatChannelCommandData) {
        super();
        this.m_isFilterable = true;
        this.m_chatPipe = pipe;
        this.m_pipeName = pipeName;
        if (chatChannelCommandData != null) {
            this.m_commandView = chatChannelCommandData.getChatCommandsParametersInterface();
            if (this.m_commandView.getAddedText() != null) {
                this.m_pipeName = this.m_pipeName + " " + this.m_commandView.getAddedText();
            }
            this.m_command = chatChannelCommandData.getCommand();
        }
        else {
            this.m_commandView = null;
        }
        this.m_channelMode = channelMode;
    }
    
    public ChatCommandsParametersInterface getCommandView() {
        return this.m_commandView;
    }
    
    public ChatPipe getChatPipe() {
        return this.m_chatPipe;
    }
    
    public String getPipeName() {
        return this.m_pipeName;
    }
    
    public ChannelMode getChannelMode() {
        return this.m_channelMode;
    }
    
    public void setChannelMode(final ChannelMode channelMode) {
        this.m_channelMode = channelMode;
    }
    
    public String getCommand() {
        return this.m_command;
    }
    
    @Override
    public String[] getFields() {
        return ChatPipeWrapper.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_pipeName;
        }
        if (fieldName.equals("pipeInternalName")) {
            return this.getChatPipe().getInternalName();
        }
        if (fieldName.equals("descSentence")) {
            return this.m_pipeName;
        }
        if (fieldName.equals("displayable")) {
            return true;
        }
        if (fieldName.equals("colorString")) {
            final float[] color = this.m_chatPipe.getColor();
            return color[0] + "," + color[1] + "," + color[2];
        }
        if (fieldName.equals("command")) {
            return this.m_command;
        }
        return null;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public boolean isFilterable() {
        return this.m_isFilterable;
    }
    
    public ChatPipeWrapper setFilterable(final boolean isFilterable) {
        this.m_isFilterable = isFilterable;
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, ChatPipeWrapper.FIELDS);
        return this;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof ChatPipeWrapper)) {
            return false;
        }
        final ChatPipeWrapper chatPipeWrapper = (ChatPipeWrapper)obj;
        return this.m_channelMode == chatPipeWrapper.getChannelMode() && (this.m_command == null || this.m_command.equals(chatPipeWrapper.getCommand()));
    }
    
    static {
        FIELDS = new String[] { "name", "pipeInternalName", "descSentence", "displayable", "colorString", "command" };
    }
}
